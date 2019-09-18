/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.camera;

import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.stage.CameraSurface;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.utils.FlashUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CameraManager implements Camera.PreviewCallback {

	public class CameraInformation {

		protected int cameraId;
		protected boolean flashAvailable = false;

		protected CameraInformation(int cameraId, boolean flashAvailable) {
			this.cameraId = cameraId;
			this.flashAvailable = flashAvailable;
		}
	}

	public static final int TEXTURE_NAME = 1;
	private static final String TAG = CameraManager.class.getSimpleName();
	private static CameraManager instance;
	private Camera currentCamera;
	private SurfaceTexture texture;
	private List<JpgPreviewCallback> callbacks = new ArrayList<>();
	private int previewFormat;
	private int previewWidth;
	private int previewHeight;

	private CameraInformation defaultCameraInformation = null;
	private CameraInformation currentCameraInformation = null;
	private CameraInformation frontCameraInformation = null;
	private CameraInformation backCameraInformation = null;

	private int cameraCount = 0;

	StageActivity stageActivity = null;
	CameraSurface cameraSurface = null;

	public final Object cameraChangeLock = new Object();
	private final Object cameraBaseLock = new Object();
	private boolean wasRunning = false;

	//Mode used for CameraPreview
	public enum CameraState {
		notUsed,
		prepare,
		previewRunning,
		previewPaused,
		stopped
	}

	private CameraState state = CameraState.notUsed;

	public static CameraManager getInstance() {
		return instance;
	}

	@VisibleForTesting(otherwise = VisibleForTesting.NONE)
	public static void setInstance(CameraManager cameraManager) {
		instance = cameraManager;
	}

	public static void makeInstance() {
		instance = new CameraManager();
	}

	private CameraManager() {
		cameraCount = Camera.getNumberOfCameras();
		for (int id = 0; id < cameraCount; id++) {
			Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
			Camera.getCameraInfo(id, cameraInfo);

			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
				backCameraInformation = new CameraInformation(id, hasCameraFlash(id));
				currentCameraInformation = backCameraInformation;
			}
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				frontCameraInformation = new CameraInformation(id, hasCameraFlash(id));
				currentCameraInformation = frontCameraInformation;
			}
		}
		defaultCameraInformation = currentCameraInformation;
		createTexture();
	}

	public boolean hasBackCamera() {
		return backCameraInformation != null;
	}

	public boolean hasFrontCamera() {
		return frontCameraInformation != null;
	}

	private boolean hasCameraFlash(int cameraId) {
		try {
			Camera camera;
			camera = Camera.open(cameraId);

			if (camera == null) {
				return false;
			}

			Camera.Parameters parameters = camera.getParameters();

			if (parameters.getFlashMode() == null) {
				camera.release();
				return false;
			}

			List<String> supportedFlashModes = parameters.getSupportedFlashModes();
			if (supportedFlashModes == null || supportedFlashModes.isEmpty()
					|| (supportedFlashModes.size() == 1 && supportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_OFF))) {
				camera.release();
				return false;
			}

			camera.release();
			return true;
		} catch (Exception exception) {
			Log.e(TAG, "failed checking for flash", exception);
			return false;
		}
	}

	public boolean isCurrentCameraFacingBack() {
		return currentCameraInformation == backCameraInformation;
	}

	public boolean isCameraActive() {
		return state == CameraState.previewRunning;
	}

	public void setToDefaultCamera() {
		updateCamera(defaultCameraInformation);
	}

	public boolean setToBackCamera() {
		if (!hasBackCamera()) {
			return false;
		}
		updateCamera(backCameraInformation);
		return true;
	}

	public boolean setToFrontCamera() {
		if (!hasFrontCamera()) {
			return false;
		}
		updateCamera(frontCameraInformation);
		return true;
	}

	private void updateCamera(CameraInformation cameraInformation) {

		synchronized (cameraChangeLock) {
			CameraState currentState = state;

			if (cameraInformation == currentCameraInformation) {
				return;
			}

			currentCameraInformation = cameraInformation;

			if (FlashUtil.isOn() && !currentCameraInformation.flashAvailable) {
				Log.w(TAG, "destroy Stage because flash isOn while changing camera");
				CameraManager.getInstance().destroyStage();
				return;
			}

			FlashUtil.pauseFlash();
			FaceDetectionHandler.pauseFaceDetection();
			releaseCamera();

			startCamera();
			FaceDetectionHandler.resumeFaceDetection();
			FlashUtil.resumeFlash();

			if (currentState == CameraState.prepare
					|| currentState == CameraState.previewRunning) {
				changeCameraAsync();
			}
		}
	}

	public boolean startCamera() {
		synchronized (cameraBaseLock) {
			if (currentCamera == null) {
				boolean success = createCamera();
				if (!success) {
					return false;
				}
			}
			Camera.Parameters cameraParameters = currentCamera.getParameters();
			previewFormat = cameraParameters.getPreviewFormat();
			previewWidth = cameraParameters.getPreviewSize().width;
			previewHeight = cameraParameters.getPreviewSize().height;
			try {
				currentCamera.startPreview();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
				return false;
			}
			return true;
		}
	}

	public void releaseCamera() {
		synchronized (cameraBaseLock) {
			if (currentCamera == null) {
				return;
			}
			currentCamera.setPreviewCallback(null);
			currentCamera.stopPreview();
			currentCamera.release();
			currentCamera = null;
		}
	}

	private boolean createCamera() {
		if (currentCamera != null) {
			return false;
		}

		try {
			currentCamera = Camera.open(currentCameraInformation.cameraId);
			if (ProjectManager.getInstance().isCurrentProjectLandscapeMode()) {
				currentCamera.setDisplayOrientation(0);
			} else {
				currentCamera.setDisplayOrientation(90);
			}

			Camera.Parameters cameraParameters = currentCamera.getParameters();
			List<Camera.Size> previewSizes = cameraParameters.getSupportedPreviewSizes();
			int previewHeight = 0;
			int previewWidth = 0;
			for (int i = 0; i < previewSizes.size()
					&& previewSizes.get(i).height <= ScreenValues.SCREEN_HEIGHT; i++) {
				if (previewSizes.get(i).height > previewHeight) {
					previewHeight = previewSizes.get(i).height;
					previewWidth = previewSizes.get(i).width;
				}
			}

			cameraParameters.setPreviewSize(previewWidth, previewHeight);
			currentCamera.setParameters(cameraParameters);
		} catch (RuntimeException runtimeException) {
			Log.e(TAG, "Creating camera caused an exception", runtimeException);
			return false;
		}

		currentCamera.setPreviewCallbackWithBuffer(this);
		if (texture != null) {
			try {
				setTexture();
			} catch (IOException ioException) {
				Log.e(TAG, "Setting preview texture failed!", ioException);
				return false;
			}
		}
		return true;
	}

	public boolean hasCurrentCameraFlash() {
		return (currentCameraInformation != null && currentCameraInformation.flashAvailable);
	}

	public boolean hasBackCameraFlash() {
		return (hasBackCamera() && backCameraInformation.flashAvailable);
	}

	public boolean hasFrontCameraFlash() {
		return (hasFrontCamera() && frontCameraInformation.flashAvailable);
	}

	public CameraState getState() {
		return state;
	}

	public Camera getCurrentCamera() {
		return currentCamera;
	}

	public void addOnJpgPreviewFrameCallback(JpgPreviewCallback callback) {
		if (callbacks.contains(callback)) {
			return;
		}
		callbacks.add(callback);
	}

	public void removeOnJpgPreviewFrameCallback(JpgPreviewCallback callback) {
		callbacks.remove(callback);
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (callbacks.size() == 0) {
			return;
		}
		byte[] jpgData = getDecodeableBytesFromCameraFrame(data);
		for (JpgPreviewCallback callback : callbacks) {
			callback.onFrame(jpgData);
		}
	}

	private byte[] getDecodeableBytesFromCameraFrame(byte[] cameraData) {
		byte[] decodableBytes;
		YuvImage image = new YuvImage(cameraData, previewFormat, previewWidth, previewHeight, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compressToJpeg(new Rect(0, 0, previewWidth, previewHeight), 50, out);
		decodableBytes = out.toByteArray();
		return decodableBytes;
	}

	private void createTexture() {
		texture = new SurfaceTexture(TEXTURE_NAME);
	}

	private void setTexture() throws IOException {
		currentCamera.setPreviewTexture(texture);
	}

	public void setFlashParams(Parameters flash) {
		Log.d(TAG, flash.toString());
		if (currentCamera != null && flash != null) {
			Parameters current = currentCamera.getParameters();
			current.setFlashMode(flash.getFlashMode());
			currentCamera.setParameters(current);
		}
	}

	public void prepareCamera() {
		state = CameraState.previewRunning;

		if (cameraSurface == null) {
			cameraSurface = new CameraSurface(stageActivity);
		}

		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
				.LayoutParams.WRAP_CONTENT);

		//java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
		ViewGroup parent = (ViewGroup) cameraSurface.getParent();
		if (parent != null) {
			parent.removeView(cameraSurface);
		}
		stageActivity.addContentView(cameraSurface, params);
		startCamera();
	}

	public void stopPreview() {
		state = CameraState.notUsed;
		if (cameraSurface != null) {
			ViewParent parentView = cameraSurface.getParent();
			if (parentView instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) parentView;
				viewGroup.removeView(cameraSurface);
			}

			cameraSurface = null;
			try {
				if (currentCamera != null) {
					currentCamera.stopPreview();
					setTexture();
				}
				if (FaceDetectionHandler.isFaceDetectionRunning() || FlashUtil.isAvailable()) {
					currentCamera.startPreview();
				}
			} catch (IOException e) {
				Log.e(TAG, "reset Texture failed at stopPreview");
				Log.e(TAG, e.getMessage());
			}
		}
	}

	public void pausePreview() {
		if (state == CameraState.previewRunning) {
			wasRunning = true;
		}

		stopPreview();
		state = CameraState.previewPaused;
	}

	public void resumePreview() {
		prepareCamera();
		wasRunning = false;
	}

	public void pauseForScene() {
		Runnable r = new Runnable() {
			public void run() {
				pausePreview();
			}
		};
		stageActivity.post(r);
		wasRunning = false;
		state = CameraState.notUsed;
	}

	public void resumeForScene() {
		Runnable r = new Runnable() {
			public void run() {
				resumePreview();
			}
		};
		stageActivity.post(r);
	}

	public void prepareCameraAsync() {
		Runnable r = new Runnable() {
			public void run() {
				prepareCamera();
			}
		};
		stageActivity.post(r);
	}

	public void stopPreviewAsync() {
		Runnable r = new Runnable() {
			public void run() {
				stopPreview();
			}
		};
		stageActivity.post(r);
	}

	public void resumePreviewAsync() {
		if (state != CameraState.previewPaused || !wasRunning) {
			return;
		}

		Runnable r = new Runnable() {
			public void run() {
				resumePreview();
			}
		};
		stageActivity.post(r);
	}

	public boolean isReady() {
		return currentCamera != null;
	}

	public void updatePreview(CameraState newState) {

		synchronized (cameraChangeLock) {
			if (state == CameraState.previewRunning
					&& newState != CameraState.prepare) {
				stopPreviewAsync();
			} else if (state == CameraState.notUsed
					&& newState != CameraState.stopped) {
				prepareCameraAsync();
			}
		}
	}

	private void changeCameraAsync() {
		Runnable r = new Runnable() {
			public void run() {
				changeCamera();
			}
		};
		stageActivity.post(r);
	}

	private void changeCamera() {
		stopPreview();
		prepareCamera();
	}

	public void setStageActivity(StageActivity stageActivity) {
		this.stageActivity = stageActivity;
	}

	public void destroyStage() {
		if (this.stageActivity != null) {
			stageActivity.finish();
		}
	}

	public boolean isCameraFlashAvailable() {
		return (hasBackCameraFlash() || hasFrontCameraFlash());
	}

	public void switchToCameraWithFlash() {
		if (hasCurrentCameraFlash()) {
			return;
		}

		if (hasFrontCameraFlash()) {
			updateCamera(frontCameraInformation);
		} else if (hasBackCameraFlash()) {
			updateCamera(backCameraInformation);
		}
	}
}

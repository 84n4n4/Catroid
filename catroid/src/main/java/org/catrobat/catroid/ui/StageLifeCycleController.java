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

package org.catrobat.catroid.ui;

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.common.CatroidService;
import org.catrobat.catroid.common.ServiceProvider;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.io.SoundManager;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.utils.FlashUtil;
import org.catrobat.catroid.utils.VibratorUtil;

import java.util.List;

import static org.catrobat.catroid.ui.RequiresPermissionTask.checkPermission;
import static org.catrobat.catroid.ui.StageResourceHolder.getRequiredPermissionsList;

public final class StageLifeCycleController {
	public static final String TAG = StageLifeCycleController.class.getSimpleName();

	private static final int REQUEST_PERMISSIONS_STAGE_RESOURCE_CREATE = 601;
	private static final int REQUEST_PERMISSIONS_STAGE_RESOURCE_RESUME = 602;
	private static final int REQUEST_PERMISSIONS_STAGE_RESOURCE_PAUSE = 603;

	public static void stageCreate(final StageActivity stageActivity) {
		stageActivity.stageResourceHolder = new StageResourceHolder(stageActivity);

		List<String> requiredPermissions = getRequiredPermissionsList();
			if (requiredPermissions.isEmpty()) {
				stageActivity.stageResourceHolder.initResources();
			} else {
				new RequiresPermissionTask(REQUEST_PERMISSIONS_STAGE_RESOURCE_CREATE, requiredPermissions, R.string.runtime_permission_all) {
					public void task() {
						stageActivity.stageResourceHolder.initResources();
					}
				}.execute(stageActivity);
			}
	}

	public static void stagePause(final StageActivity stageActivity) {
		new RequiresPermissionTask(REQUEST_PERMISSIONS_STAGE_RESOURCE_PAUSE, REQUEST_PERMISSIONS_STAGE_RESOURCE_CREATE,
				getRequiredPermissionsList(), R.string.runtime_permission_all) {
			public void task() {
				if (stageActivity.nfcAdapter != null) {
					try {
						stageActivity.nfcAdapter.disableForegroundDispatch(stageActivity);
					} catch (IllegalStateException illegalStateException) {
						Log.e(TAG, "Disabling NFC foreground dispatching went wrong!", illegalStateException);
					}
				}
				SensorHandler.stopSensorListeners();
				SoundManager.getInstance().pause();
				stageActivity.stageListener.menuPause();
				stageActivity.stageAudioFocus.releaseAudioFocus();
				if (CameraManager.getInstance() != null) {
					FlashUtil.pauseFlash();
					FaceDetectionHandler.pauseFaceDetection();
					CameraManager.getInstance().pausePreview();
					CameraManager.getInstance().releaseCamera();
				}
				ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE).pause();
				VibratorUtil.pauseVibrator();
				if (ProjectManager.getInstance().getCurrentProject().isCastProject()) {
					CastManager.getInstance().setRemoteLayoutToPauseScreen(stageActivity);
				}
				if (stageActivity.stageResourceHolder.droneLifeCycleHolder != null) {
					stageActivity.stageResourceHolder.droneLifeCycleHolder.onPause();
				}
			}
		}.executeDownStream(stageActivity);
	}

	public static void stageResume(final StageActivity stageActivity) {
		if (stageActivity.stageDialog.isShowing() || stageActivity.askDialog != null) {
			return;
		}

		new RequiresPermissionTask(REQUEST_PERMISSIONS_STAGE_RESOURCE_RESUME, REQUEST_PERMISSIONS_STAGE_RESOURCE_CREATE,
				getRequiredPermissionsList(), R.string.runtime_permission_all) {
			public void task() {
				Brick.ResourcesSet resourcesSet = ProjectManager.getInstance().getCurrentProject().getRequiredResources();
				List<Sprite> spriteList = ProjectManager.getInstance().getCurrentlyPlayingScene().getSpriteList();

				SensorHandler.startSensorListener(stageActivity);

				for (Sprite sprite : spriteList) {
					if (sprite.getPlaySoundBricks().size() > 0) {
						stageActivity.stageAudioFocus.requestAudioFocus();
						break;
					}
				}

				if (resourcesSet.contains(Brick.CAMERA_FLASH)) {
					FlashUtil.resumeFlash();
				}

				if (resourcesSet.contains(Brick.VIBRATOR)) {
					VibratorUtil.resumeVibrator();
				}

				if (resourcesSet.contains(Brick.FACE_DETECTION)) {
					FaceDetectionHandler.resumeFaceDetection();
				}

				if (resourcesSet.contains(Brick.BLUETOOTH_LEGO_NXT)
						|| resourcesSet.contains(Brick.BLUETOOTH_PHIRO)
						|| resourcesSet.contains(Brick.BLUETOOTH_SENSORS_ARDUINO)) {
					ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE).start();
				}

				if (resourcesSet.contains(Brick.CAMERA_BACK)
						|| resourcesSet.contains(Brick.CAMERA_FRONT)
						|| resourcesSet.contains(Brick.VIDEO)) {
					CameraManager.getInstance().resumePreviewAsync();
				}

				if (resourcesSet.contains(Brick.TEXT_TO_SPEECH)) {
					stageActivity.stageAudioFocus.requestAudioFocus();
				}

				if (resourcesSet.contains(Brick.NFC_ADAPTER)
						&& stageActivity.nfcAdapter != null) {
					stageActivity.nfcAdapter.enableForegroundDispatch(stageActivity, stageActivity.pendingIntent, null, null);
				}

				if (ProjectManager.getInstance().getCurrentProject().isCastProject()) {
					CastManager.getInstance().resumeRemoteLayoutFromPauseScreen();
				}

				if (CameraManager.getInstance() != null) {
					FaceDetectionHandler.resumeFaceDetection();
				}

				SoundManager.getInstance().resume();
				stageActivity.stageListener.menuResume();
				if (stageActivity.stageResourceHolder.droneLifeCycleHolder != null) {
					stageActivity.stageResourceHolder.droneLifeCycleHolder.onResume();
				}
			}
		}.executeDownStream(stageActivity);
	}

	public static void destroyStage(StageActivity stageActivity) {
		if (checkPermission(stageActivity, getRequiredPermissionsList())) {
			stageActivity.stageResourceHolder.onStageDestroy();
			stageActivity.jumpingSumoDisconnect();
			ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE).destroy();
			VibratorUtil.destroy();
			SensorHandler.stopSensorListeners();
			if (CameraManager.getInstance() != null) {
				FlashUtil.destroy();
				FaceDetectionHandler.stopFaceDetection();
				CameraManager.getInstance().stopPreviewAsync();
				CameraManager.getInstance().releaseCamera();
				CameraManager.getInstance().setToDefaultCamera();
			}
			if (ProjectManager.getInstance().getCurrentProject().isCastProject()) {
				CastManager.getInstance().onStageDestroyed();
			}
			stageActivity.stageListener.finish();
			stageActivity.manageLoadAndFinish();
			if (stageActivity.stageResourceHolder.droneLifeCycleHolder != null) {
				stageActivity.stageResourceHolder.droneLifeCycleHolder.onDestroy();
			}
		}
		ProjectManager.getInstance().setCurrentlyPlayingScene(ProjectManager.getInstance().getCurrentlyEditedScene());
	}
}

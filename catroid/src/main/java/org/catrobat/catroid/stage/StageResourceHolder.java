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

package org.catrobat.catroid.stage;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.formulaeditor.SensorLoudness;
import org.catrobat.catroid.sensing.GatherCollisionInformationTask;
import org.catrobat.catroid.ui.runtimepermissions.BrickResourcesToRuntimePermissions;
import org.catrobat.catroid.utils.FlashUtil;
import org.catrobat.catroid.utils.TouchUtil;
import org.catrobat.catroid.utils.Utils;
import org.catrobat.catroid.utils.VibratorUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;

public class StageResourceHolder implements GatherCollisionInformationTask.OnPolygonLoadedListener {
	private static final String TAG = StageResourceHolder.class.getSimpleName();

	private static final int REQUEST_CONNECT_DEVICE = 1000;
	private static final int REQUEST_GPS = 1;

	private Brick.ResourcesSet requiredResourcesSet;
	private int requiredResourceCounter;
	private Set<Integer> failedResources;
	private StageActivity stageActivity;

	StageResourceHolder(final StageActivity stageActivity) {
		this.stageActivity = stageActivity;
		TouchUtil.reset();
	}

	@VisibleForTesting(otherwise = VisibleForTesting.PACKAGE_PRIVATE)
	public static List<String> getProjectsRuntimePermissionList() {
		return BrickResourcesToRuntimePermissions.translate(
				ProjectManager.getInstance().getCurrentProject().getRequiredResources());
	}

	public void initResources() {
		failedResources = new HashSet<>();
		requiredResourcesSet = ProjectManager.getInstance().getCurrentProject().getRequiredResources();
		requiredResourceCounter = requiredResourcesSet.size();

		SensorHandler sensorHandler = SensorHandler.getInstance(stageActivity);
		if (requiredResourcesSet.contains(Brick.SENSOR_ACCELERATION)) {
			if (sensorHandler.accelerationAvailable()) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.SENSOR_ACCELERATION);
			}
		}

		if (requiredResourcesSet.contains(Brick.SENSOR_INCLINATION)) {
			if (sensorHandler.inclinationAvailable()) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.SENSOR_INCLINATION);
			}
		}

		if (requiredResourcesSet.contains(Brick.SENSOR_COMPASS)) {
			if (sensorHandler.compassAvailable()) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.SENSOR_COMPASS);
			}
		}

		if (requiredResourcesSet.contains(Brick.MICROPHONE)) {
			sensorHandler.setSensorLoudness(new SensorLoudness());
			resourceInitialized();
		}

		if (requiredResourcesSet.contains(Brick.SENSOR_GPS)) {
			sensorHandler.setLocationManager((LocationManager) stageActivity.getSystemService(Context.LOCATION_SERVICE));
			if (SensorHandler.gpsAvailable()) {
				resourceInitialized();
			} else {
				Intent checkIntent = new Intent();
				checkIntent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				stageActivity.startActivityForResult(checkIntent, REQUEST_GPS);
			}
		}

		if (requiredResourcesSet.contains(Brick.TEXT_TO_SPEECH)) {
			TextToSpeechHolder.getInstance().initTextToSpeech(stageActivity, this);
		}

		if (requiredResourcesSet.contains(Brick.CAMERA_BACK)) {
			CameraManager.makeInstance();
			if (CameraManager.getInstance().hasBackCamera()) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.CAMERA_BACK);
			}
		}

		if (requiredResourcesSet.contains(Brick.CAMERA_FRONT)) {
			CameraManager.makeInstance();
			if (CameraManager.getInstance().hasFrontCamera()) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.CAMERA_FRONT);
			}
		}

		if (requiredResourcesSet.contains(Brick.VIDEO)) {
			CameraManager.makeInstance();
			if (CameraManager.getInstance().hasFrontCamera()
					|| CameraManager.getInstance().hasBackCamera()) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.VIDEO);
			}
		}

		if (requiredResourcesSet.contains(Brick.CAMERA_FLASH)) {
			CameraManager.makeInstance();
			if (CameraManager.getInstance().isCameraFlashAvailable()) {
				CameraManager.getInstance().switchToCameraWithFlash();
			}
			FlashUtil.initializeFlash();
			resourceInitialized();
		}

		if (requiredResourcesSet.contains(Brick.VIBRATOR)) {
			Vibrator vibrator = (Vibrator) stageActivity.getSystemService(VIBRATOR_SERVICE);
			if (vibrator != null) {
				VibratorUtil.setContext(stageActivity);
				VibratorUtil.activateVibratorThread();
				resourceInitialized();
			} else {
				resourceFailed(Brick.VIBRATOR);
			}
		}

		if (requiredResourcesSet.contains(Brick.FACE_DETECTION)) {
			CameraManager.makeInstance();
			FaceDetectionHandler.resetFaceDedection();

			boolean success = FaceDetectionHandler.startFaceDetection();
			if (success) {
				resourceInitialized();
			} else {
				resourceFailed(Brick.FACE_DETECTION);
			}
		}

		if (requiredResourcesSet.contains(Brick.COLLISION)) {
			GatherCollisionInformationTask task = new GatherCollisionInformationTask(this);
			task.execute();
		}

		if (requiredResourcesSet.contains(Brick.NETWORK_CONNECTION)) {
			if (!Utils.isNetworkAvailable(stageActivity)) {
				new AlertDialog.Builder(stageActivity)
						.setTitle(R.string.error_no_network_title)
						.setPositiveButton(R.string.preference_title, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								stageActivity.startActivity(new Intent(Settings.ACTION_SETTINGS));
							}
						})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								endStageActivity();
							}
						})
						.setOnDismissListener(new DialogInterface.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								endStageActivity();
							}
						})
						.create()
						.show();
			} else {
				resourceInitialized();
			}
		}

		if (initFinished()) {
			initFinishedRunStage();
		}
	}

	public boolean initFinished() {
		return requiredResourceCounter == 0 && failedResources.isEmpty();
	}

	public void initFinishedRunStage() {
		stageActivity.setupAskHandler();
		stageActivity.pendingIntent = PendingIntent.getActivity(stageActivity, 0,
				new Intent(stageActivity, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		stageActivity.nfcAdapter = NfcAdapter.getDefaultAdapter(stageActivity);
		if (CameraManager.getInstance() != null) {
			CameraManager.getInstance().setStageActivity(stageActivity);
		}
		StageActivity.stageListener.setPaused(false);
	}

	public synchronized void resourceFailed(int failedResource) {
		Log.d(TAG, "resourceFailed: " + failedResource);
		failedResources.add(failedResource);
		resourceInitialized();
	}

	public synchronized void resourceInitialized() {
		requiredResourceCounter--;
		if (requiredResourceCounter == 0) {

			if (failedResources.isEmpty()) {
				initFinishedRunStage();
			} else {
				showResourceFailedErrorDialog();
			}
		}
	}

	public void endStageActivity() {
		Intent returnToActivityIntent = new Intent();
		stageActivity.setResult(RESULT_CANCELED, returnToActivityIntent);
		stageActivity.finish();
	}

	private void showResourceFailedErrorDialog() {
		String failedResourcesMessage = stageActivity.getString(R.string.prestage_resource_not_available_text);
		Iterator resourceIter = failedResources.iterator();
		while (resourceIter.hasNext()) {
			switch ((int) resourceIter.next()) {
				case Brick.SENSOR_ACCELERATION:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_acceleration_sensor_available);
					break;
				case Brick.SENSOR_INCLINATION:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_inclination_sensor_available);
					break;
				case Brick.SENSOR_COMPASS:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_compass_sensor_available);
					break;
				case Brick.SENSOR_GPS:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_gps_sensor_available);
					break;
				case Brick.TEXT_TO_SPEECH:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_text_to_speech_error);
					break;
				case Brick.CAMERA_BACK:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_back_camera_available);
					break;
				case Brick.CAMERA_FRONT:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_front_camera_available);
					break;
				case Brick.VIBRATOR:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_vibrator_available);
					break;
				case Brick.FACE_DETECTION:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_camera_available);
					break;
				default:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_default_resource_not_available);
					break;
			}
		}

		AlertDialog.Builder failedResourceAlertBuilder = new AlertDialog.Builder(stageActivity);
		failedResourceAlertBuilder.setTitle(R.string.prestage_resource_not_available_title);
		failedResourceAlertBuilder.setMessage(failedResourcesMessage).setCancelable(false)
				.setPositiveButton(stageActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						endStageActivity();
					}
				});
		AlertDialog alert = failedResourceAlertBuilder.create();
		alert.show();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CONNECT_DEVICE:
				switch (resultCode) {
					case RESULT_OK:
						resourceInitialized();
						break;

					case RESULT_CANCELED:
						endStageActivity();
						break;
				}
				break;
			case REQUEST_GPS:
				if (resultCode == RESULT_CANCELED && SensorHandler.gpsAvailable()) {
					resourceInitialized();
				} else {
					resourceFailed(Brick.SENSOR_GPS);
				}
				break;
			default:
				endStageActivity();
				break;
		}
	}

	// for GatherCollisionInformationTask.OnPolygonLoadedListener, this is NOT any Activity or Lifecycle event
	@Override
	public void onFinished() {
		resourceInitialized();
	}
}

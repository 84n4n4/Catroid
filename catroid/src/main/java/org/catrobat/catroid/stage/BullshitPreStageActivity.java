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

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parrot.arsdk.arcontroller.ARCONTROLLER_DEVICE_STATE_ENUM;
import com.parrot.arsdk.arcontroller.ARControllerException;
import com.parrot.arsdk.arcontroller.ARDeviceController;

import org.catrobat.catroid.BuildConfig;
import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.bluetooth.base.BluetoothDevice;
import org.catrobat.catroid.bluetooth.base.BluetoothDeviceService;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.common.CatroidService;
import org.catrobat.catroid.common.ServiceProvider;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.devices.raspberrypi.RaspberryPiService;
import org.catrobat.catroid.drone.ardrone.DroneInitializer;
import org.catrobat.catroid.drone.ardrone.DroneServiceWrapper;
import org.catrobat.catroid.drone.ardrone.DroneStageActivity;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoDeviceController;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoInitializer;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoServiceWrapper;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.sensing.GatherCollisionInformationTask;
import org.catrobat.catroid.ui.BaseActivity;
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment;
import org.catrobat.catroid.utils.FlashUtil;
import org.catrobat.catroid.utils.ToastUtil;
import org.catrobat.catroid.utils.TouchUtil;
import org.catrobat.catroid.utils.Utils;
import org.catrobat.catroid.utils.VibratorUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("deprecation")
public class BullshitPreStageActivity extends BaseActivity implements GatherCollisionInformationTask.OnPolygonLoadedListener {

//	private static final String TAG = PreStageActivity.class.getSimpleName();
//	private static final int REQUEST_CONNECT_DEVICE = 1000;
//	public static final int REQUEST_START_STAGE = 101;
//	public static final int REQUEST_GPS = 1;
//	private int requiredResourceCounter;
//	private Set<Integer> failedResources;
//
//	private DroneInitializer droneInitializer = null;
//	private JumpingSumoInitializer jumpingSumoInitializer = null;
//
//	private Brick.ResourcesSet requiredResourcesSet;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (isFinishing()) {
//			return;
//		}
//
//		setContentView(R.layout.activity_prestage);
//
//		TouchUtil.reset();
//		SensorHandler sensorHandler = SensorHandler.getInstance(getApplicationContext());
//		failedResources = new HashSet<>();
//		requiredResourcesSet = ProjectManager.getInstance().getCurrentProject().getRequiredResources();
//		requiredResourceCounter = requiredResourcesSet.size();
//
//		if (requiredResourcesSet.contains(Brick.SENSOR_ACCELERATION)) {
//			if (sensorHandler.accelerationAvailable()) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.SENSOR_ACCELERATION);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.SENSOR_INCLINATION)) {
//			if (sensorHandler.inclinationAvailable()) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.SENSOR_INCLINATION);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.SENSOR_COMPASS)) {
//			if (sensorHandler.compassAvailable()) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.SENSOR_COMPASS);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.SENSOR_GPS)) {
//			if (SensorHandler.gpsAvailable()) {
//				resourceInitialized();
//			} else {
//				Intent checkIntent = new Intent();
//				checkIntent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//				startActivityForResult(checkIntent, REQUEST_GPS);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.TEXT_TO_SPEECH)) {
//			//TextToSpeechHolder.getInstance().initTextToSpeech(this);
//		}
//
//		if (requiredResourcesSet.contains(Brick.BLUETOOTH_LEGO_NXT)) {
//			connectBTDevice(BluetoothDevice.LEGO_NXT);
//		}
//
//		if (requiredResourcesSet.contains(Brick.BLUETOOTH_LEGO_EV3)) {
//			connectBTDevice(BluetoothDevice.LEGO_EV3);
//		}
//
//		if (requiredResourcesSet.contains(Brick.BLUETOOTH_PHIRO)) {
//			connectBTDevice(BluetoothDevice.PHIRO);
//		}
//
//		if (requiredResourcesSet.contains(Brick.BLUETOOTH_SENSORS_ARDUINO)) {
//			connectBTDevice(BluetoothDevice.ARDUINO);
//		}
//
//		if (DroneServiceWrapper.checkARDroneAvailability()) {
//			CatroidApplication.loadNativeLibs();
//			if (CatroidApplication.parrotLibrariesLoaded) {
//				droneInitializer = getDroneInitialiser();
//				droneInitializer.initialise();
//			}
//		}
//
//		if (BuildConfig.FEATURE_PARROT_JUMPING_SUMO_ENABLED && requiredResourcesSet.contains(Brick.JUMPING_SUMO)) {
//			CatroidApplication.loadSDKLib();
//			if (CatroidApplication.parrotJSLibrariesLoaded) {
//				JumpingSumoServiceWrapper.initJumpingSumo(this);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.CAMERA_BACK)) {
//			if (CameraManager.getInstance().hasBackCamera()) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.CAMERA_BACK);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.CAMERA_FRONT)) {
//			if (CameraManager.getInstance().hasFrontCamera()) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.CAMERA_FRONT);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.VIDEO)) {
//			if (CameraManager.getInstance().hasFrontCamera()
//					|| CameraManager.getInstance().hasBackCamera()) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.VIDEO);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.CAMERA_FLASH)) {
//			flashInitialize();
//		}
//
//		if (requiredResourcesSet.contains(Brick.VIBRATOR)) {
//			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//			if (vibrator != null) {
//				VibratorUtil.setContext(this.getBaseContext());
//				VibratorUtil.activateVibratorThread();
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.VIBRATOR);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.NFC_ADAPTER)) {
//			if (requiredResourcesSet.contains(Brick.FACE_DETECTION)) {
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setMessage(getString(R.string.nfc_facedetection_support)).setCancelable(false)
//						.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int id) {
//								nfcInitialize();
//							}
//						});
//				AlertDialog alert = builder.create();
//				alert.show();
//			} else {
//				nfcInitialize();
//			}
//		}
//
//		FaceDetectionHandler.resetFaceDedection();
//		if (requiredResourcesSet.contains(Brick.FACE_DETECTION)) {
//			boolean success = FaceDetectionHandler.startFaceDetection();
//			if (success) {
//				resourceInitialized();
//			} else {
//				resourceFailed(Brick.FACE_DETECTION);
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.CAST_REQUIRED)) {
//
//			if (CastManager.getInstance().isConnected()) {
//				resourceInitialized();
//			} else {
//
//				if (!SettingsFragment.isCastSharedPreferenceEnabled(this)) {
//					ToastUtil.showError(this, getString(R.string.cast_enable_cast_feature));
//				} else if (ProjectManager.getInstance().getCurrentProject().isCastProject()) {
//					ToastUtil.showError(this, getString(R.string.cast_error_not_connected_msg));
//				} else {
//					ToastUtil.showError(this, getString(R.string.cast_error_cast_bricks_in_no_cast_project));
//				}
//				resourceFailed();
//			}
//		}
//
//		if (requiredResourcesSet.contains(Brick.COLLISION)) {
//			GatherCollisionInformationTask task = new GatherCollisionInformationTask(this);
//			task.execute();
//		}
//
//		if (requiredResourcesSet.contains(Brick.NETWORK_CONNECTION)) {
//			if (!Utils.isNetworkAvailable(this)) {
//				new AlertDialog.Builder(this)
//						.setTitle(R.string.error_no_network_title)
//						.setPositiveButton(R.string.preference_title, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								startActivity(new Intent(Settings.ACTION_SETTINGS));
//							}
//						})
//						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialog, int which) {
//								resourceFailed();
//							}
//						})
//						.setOnDismissListener(new DialogInterface.OnDismissListener() {
//							@Override
//							public void onDismiss(DialogInterface dialog) {
//								resourceFailed();
//							}
//						})
//						.create()
//						.show();
//			} else {
//				resourceInitialized();
//			}
//		}
//
//		if (requiredResourceCounter == 0) {
//			startStageActivity();
//		}
//
//		if (requiredResourcesSet.contains(Brick.SOCKET_RASPI)) {
//			Project currentProject = ProjectManager.getInstance().getCurrentProject();
//			RaspberryPiService.getInstance().enableRaspberryInterruptPinsForProject(currentProject);
//			connectRaspberrySocket();
//		}
//	}
//
//	private void connectBTDevice(Class<? extends BluetoothDevice> service) {
//		BluetoothDeviceService btService = ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE);
//
//		if (btService.connectDevice(service, this, REQUEST_CONNECT_DEVICE)
//				== BluetoothDeviceService.ConnectDeviceResult.ALREADY_CONNECTED) {
//			resourceInitialized();
//		}
//	}
//
//	private void connectRaspberrySocket() {
//		String host = SettingsFragment.getRaspiHost(this.getBaseContext());
//		int port = SettingsFragment.getRaspiPort(this.getBaseContext());
//
//		if (RaspberryPiService.getInstance().connect(host, port)) {
//			resourceInitialized();
//		} else {
//			ToastUtil.showError(this, getString(R.string.error_connecting_to, host, port));
//			resourceFailed();
//		}
//	}
//
//	public DroneInitializer getDroneInitialiser() {
//		if (droneInitializer == null) {
//			droneInitializer = new DroneInitializer(this);
//		}
//		return droneInitializer;
//	}
//
//	public JumpingSumoInitializer getJumpingSumoInitialiser() {
//		if (jumpingSumoInitializer == null) {
//			jumpingSumoInitializer = JumpingSumoInitializer.getInstance();
//			jumpingSumoInitializer.setPreStageActivity(this);
//		}
//		return jumpingSumoInitializer;
//	}
//
//	@Override
//	public void onResume() {
//		if (droneInitializer != null) {
//			droneInitializer.onPrestageActivityResume();
//		}
//
//		super.onResume();
//		if (requiredResourceCounter == 0 && failedResources.isEmpty()) {
//			Log.d(TAG, "onResume()");
//			Intent returnToActivityIntent = new Intent();
//			setResult(RESULT_CANCELED, returnToActivityIntent);
//			finish();
//		}
//	}
//
//	@Override
//	protected void onPause() {
//		if (droneInitializer != null) {
//			droneInitializer.onPrestageActivityPause();
//		}
//
//		super.onPause();
//	}
//
//	@Override
//	protected void onDestroy() {
//		if (droneInitializer != null) {
//			droneInitializer.onStageActivityDestroy();
//		}
//
//		super.onDestroy();
//	}
//
//	public void resourceFailed() {
//		Intent returnToActivityIntent = new Intent();
//		setResult(RESULT_CANCELED, returnToActivityIntent);
//		finish();
//	}
//
//	public void showResourceFailedErrorDialog() {
//		String failedResourcesMessage = getString(R.string.prestage_resource_not_available_text);
//		Iterator resourceIter = failedResources.iterator();
//		while (resourceIter.hasNext()) {
//			switch ((int) resourceIter.next()) {
//				case Brick.SENSOR_ACCELERATION:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_acceleration_sensor_available);
//					break;
//				case Brick.SENSOR_INCLINATION:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_inclination_sensor_available);
//					break;
//				case Brick.SENSOR_COMPASS:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_compass_sensor_available);
//					break;
//				case Brick.SENSOR_GPS:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_gps_sensor_available);
//					break;
//				case Brick.TEXT_TO_SPEECH:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_text_to_speech_error);
//					break;
//				case Brick.CAMERA_BACK:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_back_camera_available);
//					break;
//				case Brick.CAMERA_FRONT:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_front_camera_available);
//					break;
//				case Brick.CAMERA_FLASH:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_flash_available);
//					break;
//				case Brick.VIBRATOR:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_vibrator_available);
//					break;
//				case Brick.FACE_DETECTION:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_camera_available);
//					break;
//				case Brick.JUMPING_SUMO:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_no_jumping_sumo_available);
//					break;
//				default:
//					failedResourcesMessage = failedResourcesMessage + getString(R.string
//							.prestage_default_resource_not_available);
//					break;
//			}
//		}
//
//		AlertDialog.Builder failedResourceAlertBuilder = new AlertDialog.Builder(this);
//		failedResourceAlertBuilder.setTitle(R.string.prestage_resource_not_available_title);
//		failedResourceAlertBuilder.setMessage(failedResourcesMessage).setCancelable(false)
//				.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int id) {
//						resourceFailed();
//					}
//				});
//		AlertDialog alert = failedResourceAlertBuilder.create();
//		alert.show();
//	}
//
//	public void showResourceInUseErrorDialog() {
//		String failedResourcesMessage = getString(R.string.prestage_resource_in_use_text);
//		AlertDialog.Builder failedResourceAlertBuilder = new AlertDialog.Builder(this);
//		failedResourceAlertBuilder.setTitle(R.string.prestage_resource_not_available_title);
//		failedResourceAlertBuilder.setMessage(failedResourcesMessage).setCancelable(false)
//				.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int id) {
//						resourceFailed();
//					}
//				});
//		AlertDialog alert = failedResourceAlertBuilder.create();
//		alert.show();
//	}
//
//	public synchronized void resourceFailed(int failedResource) {
//		Log.d(TAG, "resourceFailed: " + failedResource);
//		failedResources.add(failedResource);
//		resourceInitialized();
//	}
//
//	public synchronized void resourceInitialized() {
//		requiredResourceCounter--;
//		if (requiredResourceCounter == 0) {
//			if (failedResources.isEmpty()) {
//				Log.d(TAG, "Start Stage");
//				if (BuildConfig.FEATURE_PARROT_JUMPING_SUMO_ENABLED
//						&& requiredResourcesSet.contains(Brick.JUMPING_SUMO)
//						&& !verifyJSConnection()) {
//					return;
//				}
//				startStageActivity();
//			} else {
//				showResourceFailedErrorDialog();
//			}
//		}
//	}
//
//	public void startStageActivity() {
//		for (Scene scene : ProjectManager.getInstance().getCurrentProject().getSceneList()) {
//			scene.firstStart = true;
//			scene.getDataContainer().resetUserData();
//		}
//		if (DroneServiceWrapper.checkARDroneAvailability()) {
//			startActivity(new Intent(this, DroneStageActivity.class));
//		} else {
//			startActivity(new Intent(this, StageActivity.class));
//		}
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (requestCode) {
//			case StageActivity.STAGE_ACTIVITY_FINISH:
//				SensorHandler.stopSensorListeners();
//				FaceDetectionHandler.stopFaceDetection();
//				Intent returnToActivityIntent = new Intent();
//				setResult(RESULT_OK, returnToActivityIntent);
//				finish();
//				break;
//			case REQUEST_CONNECT_DEVICE:
//				switch (resultCode) {
//					case AppCompatActivity.RESULT_OK:
//						resourceInitialized();
//						break;
//
//					case AppCompatActivity.RESULT_CANCELED:
//						resourceFailed();
//						break;
//				}
//				break;
//			case REQUEST_GPS:
//				if (resultCode == RESULT_CANCELED && SensorHandler.gpsAvailable()) {
//					resourceInitialized();
//				} else {
//					resourceFailed(Brick.SENSOR_GPS);
//				}
//				break;
//			default:
//				resourceFailed();
//				break;
//		}
//	}
//
//	private void flashInitialize() {
//		if (CameraManager.getInstance().switchToCameraWithFlash()) {
//			FlashUtil.initializeFlash();
//			resourceInitialized();
//		} else {
//			resourceFailed(Brick.CAMERA_FLASH);
//		}
//	}
//
//	private void nfcInitialize() {
//		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
//		if (adapter != null && !adapter.isEnabled()) {
//			ToastUtil.showError(this, R.string.nfc_not_activated);
//			Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
//			startActivity(intent);
//		} else if (adapter == null) {
//			ToastUtil.showError(this, R.string.no_nfc_available);
//			// TODO: resourceFailed() & startActivityForResult(), if behaviour needed
//		}
//		resourceInitialized();
//	}
//
//	private boolean verifyJSConnection() {
//		boolean connected;
//		ARCONTROLLER_DEVICE_STATE_ENUM state = ARCONTROLLER_DEVICE_STATE_ENUM
//				.eARCONTROLLER_DEVICE_STATE_UNKNOWN_ENUM_VALUE;
//		try {
//			JumpingSumoDeviceController controller = JumpingSumoDeviceController.getInstance();
//			ARDeviceController deviceController = controller.getDeviceController();
//			state = deviceController.getState();
//		} catch (ARControllerException e) {
//			Log.e(TAG, "Error could not connect to drone", e);
//		}
//		if (state != ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING) {
//			resourceFailed(Brick.JUMPING_SUMO);
//			showResourceInUseErrorDialog();
//			connected = false;
//		} else {
//			connected = true;
//		}
//		return connected;
//	}

	@Override
	public void onFinished() {
		//resourceInitialized();
	}
}

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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;

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
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.devices.raspberrypi.RaspberryPiService;
import org.catrobat.catroid.drone.ardrone.DroneInitializer;
import org.catrobat.catroid.drone.ardrone.DroneLifeCycleHolder;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoDeviceController;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoInitializer;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoServiceWrapper;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.formulaeditor.SensorLoudness;
import org.catrobat.catroid.sensing.GatherCollisionInformationTask;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.stage.TextToSpeechHolder;
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment;
import org.catrobat.catroid.utils.FlashUtil;
import org.catrobat.catroid.utils.ToastUtil;
import org.catrobat.catroid.utils.TouchUtil;
import org.catrobat.catroid.utils.Utils;
import org.catrobat.catroid.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.CHANGE_WIFI_MULTICAST_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.NFC;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.VIBRATE;
import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.VIBRATOR_SERVICE;

import static org.catrobat.catroid.common.Constants.CATROBAT_TERMS_OF_USE_URL;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_PARROT_AR_DRONE_CATROBAT_TERMS_OF_SERVICE_ACCEPTED_PERMANENTLY;

public class StageResourceHolder implements GatherCollisionInformationTask.OnPolygonLoadedListener {
	private static final String TAG = StageResourceHolder.class.getSimpleName();

	private static final int REQUEST_CONNECT_DEVICE = 1000;
	private static final int REQUEST_GPS = 1;

	private Brick.ResourcesSet requiredResourcesSet;
	private int requiredResourceCounter;
	private Set<Integer> failedResources;

	public DroneInitializer droneInitializer;
	private JumpingSumoInitializer jumpingSumoInitializer;

	public DroneLifeCycleHolder droneLifeCycleHolder;

	private StageActivity stageActivity;


	StageResourceHolder(final StageActivity stageActivity) {
		this.stageActivity = stageActivity;
		TouchUtil.reset();
	}

	static List<String> getProjectsRuntimePermissionList() {
		return translateBrickResourcesToRuntimePermissions(
				ProjectManager.getInstance().getCurrentProject().getRequiredResources());
	}

	@SuppressLint("UseSparseArrays")
	public static List<String> translateBrickResourcesToRuntimePermissions(Brick.ResourcesSet brickResources) {
		Map<Integer, List<String>> brickResourcesToPermissions = new HashMap<>();
		brickResourcesToPermissions.put(Brick.SENSOR_GPS, Arrays.asList(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION));
		List<String> bluetoothPermissions = Arrays.asList(BLUETOOTH_ADMIN, BLUETOOTH);
		brickResourcesToPermissions.put(Brick.BLUETOOTH_LEGO_NXT, bluetoothPermissions);
		brickResourcesToPermissions.put(Brick.BLUETOOTH_LEGO_EV3, bluetoothPermissions);
		brickResourcesToPermissions.put(Brick.BLUETOOTH_PHIRO, bluetoothPermissions);
		brickResourcesToPermissions.put(Brick.BLUETOOTH_SENSORS_ARDUINO, bluetoothPermissions);
		List<String> wifiPermissions = Arrays.asList(CHANGE_WIFI_MULTICAST_STATE, CHANGE_WIFI_STATE, ACCESS_WIFI_STATE);
		brickResourcesToPermissions.put(Brick.ARDRONE_SUPPORT, wifiPermissions);
		brickResourcesToPermissions.put(Brick.JUMPING_SUMO, wifiPermissions);
		brickResourcesToPermissions.put(Brick.CAST_REQUIRED, wifiPermissions);
		brickResourcesToPermissions.put(Brick.CAMERA_BACK, Arrays.asList(CAMERA));
		brickResourcesToPermissions.put(Brick.CAMERA_FRONT, Arrays.asList(CAMERA));
		brickResourcesToPermissions.put(Brick.VIDEO, Arrays.asList(CAMERA));
		brickResourcesToPermissions.put(Brick.CAMERA_FLASH, Arrays.asList(CAMERA));
		brickResourcesToPermissions.put(Brick.VIBRATOR, Arrays.asList(VIBRATE));
		brickResourcesToPermissions.put(Brick.NFC_ADAPTER, Arrays.asList(NFC));
		brickResourcesToPermissions.put(Brick.FACE_DETECTION, Arrays.asList(CAMERA));
		brickResourcesToPermissions.put(Brick.MICROPHONE, Arrays.asList(RECORD_AUDIO));

		Set<String> requiredPermissions = new HashSet<>();
		for (int brickResource : brickResources) {
			if (brickResourcesToPermissions.containsKey(brickResource)) {
				requiredPermissions.addAll(brickResourcesToPermissions.get(brickResource));
			}
		}

		return new ArrayList<>(requiredPermissions);
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

		if (requiredResourcesSet.contains(Brick.BLUETOOTH_LEGO_NXT)) {
			connectBTDevice(BluetoothDevice.LEGO_NXT);
		}

		if (requiredResourcesSet.contains(Brick.BLUETOOTH_LEGO_EV3)) {
			connectBTDevice(BluetoothDevice.LEGO_EV3);
		}

		if (requiredResourcesSet.contains(Brick.BLUETOOTH_PHIRO)) {
			connectBTDevice(BluetoothDevice.PHIRO);
		}

		if (requiredResourcesSet.contains(Brick.BLUETOOTH_SENSORS_ARDUINO)) {
			connectBTDevice(BluetoothDevice.ARDUINO);
		}

		if (requiredResourcesSet.contains(Brick.ARDRONE_SUPPORT) && BuildConfig.FEATURE_PARROT_AR_DRONE_ENABLED) {
			boolean agreedToDroneTermsOfUsePermanently = PreferenceManager.getDefaultSharedPreferences(stageActivity)
					.getBoolean(SETTINGS_PARROT_AR_DRONE_CATROBAT_TERMS_OF_SERVICE_ACCEPTED_PERMANENTLY, false);

			if (agreedToDroneTermsOfUsePermanently) {
				onDroneTermsOfUseAgreed();
			} else {
				final AlertDialog alertDialog = new AlertDialog.Builder(stageActivity)
						.setTitle(R.string.dialog_terms_of_use_title)
						.setView(R.layout.dialog_terms_of_use)
						.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								CheckBox checkBox = ((AlertDialog) dialog)
										.findViewById(R.id.dialog_terms_of_use_check_box_agree_permanently);
								PreferenceManager.getDefaultSharedPreferences(stageActivity)
										.edit()
										.putBoolean(SETTINGS_PARROT_AR_DRONE_CATROBAT_TERMS_OF_SERVICE_ACCEPTED_PERMANENTLY, checkBox.isChecked());
								onDroneTermsOfUseAgreed();
							}
						})
						.setCancelable(false)
						.create();

				alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
					@Override
					public void onShow(DialogInterface dialog) {
						TextView textView = alertDialog.findViewById(R.id.dialog_terms_of_use_text_view_info);
						textView.setText(R.string.dialog_terms_of_use_parrot_reminder_text);

						CheckBox checkBox = alertDialog
								.findViewById(R.id.dialog_terms_of_use_check_box_agree_permanently);
						checkBox.setText(R.string.dialog_terms_of_use_parrot_reminder_do_not_remind_again);

						String url = alertDialog.getContext()
								.getString(R.string.dialog_terms_of_use_link_text_parrot_reminder);
						url = alertDialog.getContext()
								.getString(R.string.terms_of_use_link_template, CATROBAT_TERMS_OF_USE_URL, url);
						TextView urlView = alertDialog.findViewById(R.id.dialog_terms_of_use_text_view_url);
						urlView.setText(Html.fromHtml(url));
					}
				});
			}
		}

		if (BuildConfig.FEATURE_PARROT_JUMPING_SUMO_ENABLED && requiredResourcesSet.contains(Brick.JUMPING_SUMO)) {
			CatroidApplication.loadJumpingSumoSDKLib();
			if (CatroidApplication.parrotJSLibrariesLoaded) {
				jumpingSumoInitializer = getJumpingSumoInitialiser(stageActivity);
				JumpingSumoServiceWrapper.initJumpingSumo(stageActivity);
			}
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
			flashInitialize();
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

		if (requiredResourcesSet.contains(Brick.NFC_ADAPTER)) {
			if (requiredResourcesSet.contains(Brick.FACE_DETECTION)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(stageActivity);
				builder.setMessage(stageActivity.getString(R.string.nfc_facedetection_support)).setCancelable(false)
						.setPositiveButton(stageActivity.getString(R.string.ok), new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								nfcInitialize();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			} else {
				nfcInitialize();
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

		if (requiredResourcesSet.contains(Brick.CAST_REQUIRED)) {
			if (CastManager.getInstance().isConnected()) {
				resourceInitialized();
			} else {

				if (!SettingsFragment.isCastSharedPreferenceEnabled(stageActivity)) {
					ToastUtil.showError(stageActivity, stageActivity.getString(R.string.cast_enable_cast_feature));
				} else if (ProjectManager.getInstance().getCurrentProject().isCastProject()) {
					ToastUtil.showError(stageActivity, stageActivity.getString(R.string.cast_error_not_connected_msg));
				} else {
					ToastUtil.showError(stageActivity, stageActivity.getString(R.string.cast_error_cast_bricks_in_no_cast_project));
				}
				endStageActivity();
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

		if (requiredResourcesSet.contains(Brick.SOCKET_RASPI)) {
			Project currentProject = ProjectManager.getInstance().getCurrentProject();
			RaspberryPiService.getInstance().enableRaspberryInterruptPinsForProject(currentProject);
			connectRaspberrySocket();
		}

		if (requiredResourceCounter == 0) {
			stageActivity.run();
		}
	}

	private void onDroneTermsOfUseAgreed() {
		if (CatroidApplication.OS_ARCH.startsWith("arm") && CatroidApplication.loadNativeLibs()) {
			droneInitializer = new DroneInitializer(stageActivity, this);
			droneInitializer.startDroneNetworkAvailabilityTask();
		} else {
			new AlertDialog.Builder(stageActivity)
					.setTitle(R.string.error_drone_wrong_platform_title)
					.setMessage(R.string.error_drone_wrong_platform)
					.setCancelable(false)
					.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							resourceFailed(Brick.ARDRONE_SUPPORT);
						}
					});
		}
	}

	public void onDroneInitialized() {
		droneInitializer = null;
		resourceInitialized();
		droneLifeCycleHolder = new DroneLifeCycleHolder(stageActivity);
	}

	public void onDroneInitFailed() {
		droneInitializer = null;
		resourceFailed(Brick.ARDRONE_SUPPORT);
	}

	private void connectBTDevice(Class<? extends BluetoothDevice> service) {
		BluetoothDeviceService btService = ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE);

		if (btService.connectDevice(service, stageActivity, REQUEST_CONNECT_DEVICE)
				== BluetoothDeviceService.ConnectDeviceResult.ALREADY_CONNECTED) {
			resourceInitialized();
		}
	}

	private void connectRaspberrySocket() {
		String host = SettingsFragment.getRaspiHost(stageActivity);
		int port = SettingsFragment.getRaspiPort(stageActivity);

		if (RaspberryPiService.getInstance().connect(host, port)) {
			resourceInitialized();
		} else {
			ToastUtil.showError(stageActivity, stageActivity.getString(R.string.error_connecting_to, host, port));
			endStageActivity();
		}
	}

	private JumpingSumoInitializer getJumpingSumoInitialiser(StageActivity stageActivity) {
		if (jumpingSumoInitializer == null) {
			jumpingSumoInitializer = JumpingSumoInitializer.getInstance();
			jumpingSumoInitializer.setStageActivity(stageActivity);
			jumpingSumoInitializer.setStageResourceHolder(this);
		}
		return jumpingSumoInitializer;
	}

	public void
	endStageActivity() {
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
				case Brick.CAMERA_FLASH:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_flash_available);
					break;
				case Brick.VIBRATOR:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_vibrator_available);
					break;
				case Brick.FACE_DETECTION:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_camera_available);
					break;
				case Brick.JUMPING_SUMO:
					failedResourcesMessage = failedResourcesMessage + stageActivity.getString(R.string
							.prestage_no_jumping_sumo_available);
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

	private void showResourceInUseErrorDialog() {
		String failedResourcesMessage = stageActivity.getString(R.string.prestage_resource_in_use_text);
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

	public synchronized void resourceFailed(int failedResource) {
		Log.d(TAG, "resourceFailed: " + failedResource);
		failedResources.add(failedResource);
		resourceInitialized();
	}

	public synchronized void resourceInitialized() {
		requiredResourceCounter--;
		if (requiredResourceCounter == 0) {
			if (failedResources.isEmpty()) {
				Log.d(TAG, "Start Stage");
				if (BuildConfig.FEATURE_PARROT_JUMPING_SUMO_ENABLED
						&& requiredResourcesSet.contains(Brick.JUMPING_SUMO)
						&& !verifyJSConnection()) {
					return;
				}
				stageActivity.run();
			} else {
				showResourceFailedErrorDialog();
				endStageActivity();
			}
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CONNECT_DEVICE:
				switch (resultCode) {
					case AppCompatActivity.RESULT_OK:
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

	private void flashInitialize() {
		if (CameraManager.getInstance().switchToCameraWithFlash()) {
			FlashUtil.initializeFlash();
			resourceInitialized();
		} else {
			resourceFailed(Brick.CAMERA_FLASH);
		}
	}

	private void nfcInitialize() {
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(stageActivity);
		if (adapter != null && !adapter.isEnabled()) {
			ToastUtil.showError(stageActivity, R.string.nfc_not_activated);
			Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
			stageActivity.startActivity(intent);
		} else if (adapter == null) {
			ToastUtil.showError(stageActivity, R.string.no_nfc_available);
			// TODO: resourceFailed() & startActivityForResult(), if behaviour needed
		}
		resourceInitialized();
	}

	private boolean verifyJSConnection() {
		boolean connected;
		ARCONTROLLER_DEVICE_STATE_ENUM state = ARCONTROLLER_DEVICE_STATE_ENUM
				.eARCONTROLLER_DEVICE_STATE_UNKNOWN_ENUM_VALUE;
		try {
			JumpingSumoDeviceController controller = JumpingSumoDeviceController.getInstance();
			ARDeviceController deviceController = controller.getDeviceController();
			state = deviceController.getState();
		} catch (ARControllerException e) {
			Log.e(TAG, "Error could not connect to drone", e);
		}
		if (state != ARCONTROLLER_DEVICE_STATE_ENUM.ARCONTROLLER_DEVICE_STATE_RUNNING) {
			resourceFailed(Brick.JUMPING_SUMO);
			showResourceInUseErrorDialog();
			connected = false;
		} else {
			connected = true;
		}
		return connected;
	}

	// for GatherCollisionInformationTask.OnPolygonLoadedListener, this is NOT any Activity or Lifecycle event
	@Override
	public void onFinished() {
		resourceInitialized();
	}
}

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

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.EditText;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceView20;

import org.catrobat.catroid.BuildConfig;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.common.CatroidService;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.common.ServiceProvider;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.actions.AskAction;
import org.catrobat.catroid.devices.raspberrypi.RaspberryPiService;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoDeviceController;
import org.catrobat.catroid.drone.jumpingsumo.JumpingSumoInitializer;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.io.StageAudioFocus;
import org.catrobat.catroid.nfc.NfcHandler;
import org.catrobat.catroid.ui.MarketingActivity;
import org.catrobat.catroid.ui.PermissionHandlingActivity;
import org.catrobat.catroid.ui.RequiresPermissionTask;
import org.catrobat.catroid.ui.StageLifeCycleResourceController9000;
import org.catrobat.catroid.ui.StageResourceHolder;
import org.catrobat.catroid.ui.dialogs.StageDialog;
import org.catrobat.catroid.utils.FlashUtil;
import org.catrobat.catroid.utils.ScreenValueHandler;
import org.catrobat.catroid.utils.SnackbarUtil;
import org.catrobat.catroid.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StageActivity extends AndroidApplication implements PermissionHandlingActivity {

	public static final String TAG = StageActivity.class.getSimpleName();
	public static StageListener stageListener;
	public static final int STAGE_ACTIVITY_FINISH = 7777;
	public static final int REQUEST_START_STAGE = 101;

	public static final int ASK_MESSAGE = 0;
	public static final int REGISTER_INTENT = 1;
	private static final int PERFORM_INTENT = 2;

	public StageAudioFocus stageAudioFocus;
	public PendingIntent pendingIntent;
	public NfcAdapter nfcAdapter;
	private static NdefMessage nfcTagMessage;
	private StageDialog stageDialog;
	private boolean resizePossible;
	private boolean askDialogUnanswered = false;

	private static int numberOfSpritesCloned;

	public static Handler messageHandler;
	private JumpingSumoDeviceController jumpingSumoDeviceController;

	public static SparseArray<IntentListener> intentListeners = new SparseArray<>();
	public static Random randomGenerator = new Random();

	AndroidApplicationConfiguration configuration = null;

	public StageResourceHolder stageResourceHolder;
	private final List<RequiresPermissionTask> permissionTaskList;

	public StageActivity() {
		permissionTaskList = new ArrayList<>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");

		if (ProjectManager.getInstance().getCurrentProject() == null) {
			finish();
			Log.d(TAG, "no current project set, cowardly refusing to run");
			return;
		}

		numberOfSpritesCloned = 0;

		if (ProjectManager.getInstance().isCurrentProjectLandscapeMode()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		for (Scene scene : ProjectManager.getInstance().getCurrentProject().getSceneList()) {
			scene.firstStart = true;
			scene.getDataContainer().resetUserData();
		}

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		stageListener = new StageListener();
		stageDialog = new StageDialog(this, stageListener, R.style.StageDialog);
		calculateScreenSizes();

		configuration = new AndroidApplicationConfiguration();
		configuration.r = configuration.g = configuration.b = configuration.a = 8;
		if (ProjectManager.getInstance().getCurrentProject().isCastProject()) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setContentView(R.layout.activity_stage_gamepad);
			CastManager.getInstance().initializeGamepadActivity(this);
			CastManager.getInstance()
					.addStageViewToLayout((GLSurfaceView20) initializeForView(stageListener, configuration));
		} else {
			initialize(stageListener, configuration);
		}

		if (graphics.getView() instanceof SurfaceView) {
			SurfaceView glView = (SurfaceView) graphics.getView();
			glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		}

		pendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		stageResourceHolder = new StageResourceHolder(StageActivity.this);
		ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE).initialise();
		stageAudioFocus = new StageAudioFocus(this);

	}

	public void run() {
		setupAskHandler();
		jumpingSumoDeviceController = JumpingSumoDeviceController.getInstance();
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		Log.d(TAG, "onCreate()");
		CameraManager.getInstance().setStageActivity(this);
		JumpingSumoInitializer.getInstance().setStageActivity(this);
		SnackbarUtil.showHintSnackbar(this, R.string.hint_stage);
		if (nfcAdapter == null) {
			Log.d(TAG, "could not get nfc adapter :(");
		}
		stageListener.setPaused(false);
	}

	private void setupAskHandler() {
		final StageActivity currentStage = this;
		messageHandler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message message) {
				List<Object> params = (ArrayList<Object>) message.obj;

				switch (message.what) {
					case ASK_MESSAGE:
						showDialog((String) params.get(1), (AskAction) params.get(0));
						break;
					case REGISTER_INTENT:
						currentStage.queueIntent((IntentListener) params.get(0));
						break;
					case PERFORM_INTENT:
						currentStage.startQueuedIntent((Integer) params.get(0));
						break;
					default:
						Log.e(TAG, "Unhandled message in messagehandler, case " + message.what);
						break;
				}
			}
		};
	}

	private void showDialog(String question, final AskAction askAction) {
		StageLifeCycleResourceController9000.stagePause(this);

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Dialog));
		final EditText edittext = new EditText(getContext());
		alertBuilder.setView(edittext);
		alertBuilder.setMessage(getContext().getString(R.string.brick_ask_dialog_hint));
		alertBuilder.setTitle(question);
		alertBuilder.setCancelable(false);

		alertBuilder.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					onBackPressed();
					return true;
				}
				return false;
			}
		});

		alertBuilder.setPositiveButton(getContext().getString(R.string.brick_ask_dialog_submit), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String questionAnswer = edittext.getText().toString();
				askAction.setAnswerText(questionAnswer);
				askDialogUnanswered = false;
				StageLifeCycleResourceController9000.stageResume(StageActivity.this);
			}
		});

		AlertDialog dialog = alertBuilder.create();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		askDialogUnanswered = true;
		dialog.show();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		NfcHandler.processIntent(intent);

		if (nfcTagMessage != null) {
			Tag currentTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			synchronized (StageActivity.class) {
				NfcHandler.writeTag(currentTag, nfcTagMessage);
				setNfcTagMessage(null);
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (BuildConfig.FEATURE_APK_GENERATOR_ENABLED) {
			ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE).disconnectDevices();

			TextToSpeechHolder.getInstance().deleteSpeechFiles();
			if (FlashUtil.isAvailable()) {
				FlashUtil.destroy();
			}
			if (VibratorUtil.isActive()) {
				VibratorUtil.destroy();
			}
			Intent marketingIntent = new Intent(this, MarketingActivity.class);
			startActivity(marketingIntent);
			finish();
		} else {
			StageLifeCycleResourceController9000.stagePause(this);
			stageDialog.show();
		}
	}

	public void manageLoadAndFinish() {
		stageListener.pause();
		stageListener.finish();

		TextToSpeechHolder.getInstance().shutDownTextToSpeech();

		ServiceProvider.getService(CatroidService.BLUETOOTH_DEVICE_SERVICE).pause();

		if (FaceDetectionHandler.isFaceDetectionRunning()) {
			FaceDetectionHandler.stopFaceDetection();
		}

		if (VibratorUtil.isActive()) {
			VibratorUtil.pauseVibrator();
		}

		RaspberryPiService.getInstance().disconnect();
	}

	@Override
	public void onPause() {
		StageLifeCycleResourceController9000.stagePause(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		StageLifeCycleResourceController9000.stageResume(this);
		super.onResume();
	}

	public boolean jumpingSumoDisconnect() {
		boolean success;
		if (!jumpingSumoDeviceController.isConnected()) {
			return true;
		}
		success = JumpingSumoInitializer.getInstance().disconnect();
		return success;
	}

	public boolean getResizePossible() {
		return resizePossible;
	}

	private void calculateScreenSizes() {
		ScreenValueHandler.updateScreenWidthAndHeight(getContext());
		int virtualScreenWidth = ProjectManager.getInstance().getCurrentProject().getXmlHeader().virtualScreenWidth;
		int virtualScreenHeight = ProjectManager.getInstance().getCurrentProject().getXmlHeader().virtualScreenHeight;
		if (virtualScreenHeight > virtualScreenWidth) {
			iflandscapeModeSwitchWidthAndHeight();
		} else {
			ifPortraitSwitchWidthAndHeight();
		}
		float aspectRatio = (float) virtualScreenWidth / (float) virtualScreenHeight;
		float screenAspectRatio = ScreenValues.getAspectRatio();

		if ((virtualScreenWidth == ScreenValues.SCREEN_WIDTH && virtualScreenHeight == ScreenValues.SCREEN_HEIGHT)
				|| Float.compare(screenAspectRatio, aspectRatio) == 0
				|| ProjectManager.getInstance().getCurrentProject().isCastProject()) {
			resizePossible = false;
			stageListener.maximizeViewPortWidth = ScreenValues.SCREEN_WIDTH;
			stageListener.maximizeViewPortHeight = ScreenValues.SCREEN_HEIGHT;
			return;
		}

		resizePossible = true;

		float scale = 1f;
		float ratioHeight = (float) ScreenValues.SCREEN_HEIGHT / (float) virtualScreenHeight;
		float ratioWidth = (float) ScreenValues.SCREEN_WIDTH / (float) virtualScreenWidth;

		if (aspectRatio < screenAspectRatio) {
			scale = ratioHeight / ratioWidth;
			stageListener.maximizeViewPortWidth = (int) (ScreenValues.SCREEN_WIDTH * scale);
			stageListener.maximizeViewPortX = (int) ((ScreenValues.SCREEN_WIDTH - stageListener.maximizeViewPortWidth) / 2f);
			stageListener.maximizeViewPortHeight = ScreenValues.SCREEN_HEIGHT;
		} else if (aspectRatio > screenAspectRatio) {
			scale = ratioWidth / ratioHeight;
			stageListener.maximizeViewPortHeight = (int) (ScreenValues.SCREEN_HEIGHT * scale);
			stageListener.maximizeViewPortY = (int) ((ScreenValues.SCREEN_HEIGHT - stageListener.maximizeViewPortHeight) / 2f);
			stageListener.maximizeViewPortWidth = ScreenValues.SCREEN_WIDTH;
		}
	}

	private void iflandscapeModeSwitchWidthAndHeight() {
		if (ScreenValues.SCREEN_WIDTH > ScreenValues.SCREEN_HEIGHT) {
			int tmp = ScreenValues.SCREEN_HEIGHT;
			ScreenValues.SCREEN_HEIGHT = ScreenValues.SCREEN_WIDTH;
			ScreenValues.SCREEN_WIDTH = tmp;
		}
	}

	private void ifPortraitSwitchWidthAndHeight() {
		if (ScreenValues.SCREEN_WIDTH < ScreenValues.SCREEN_HEIGHT) {
			int tmp = ScreenValues.SCREEN_HEIGHT;
			ScreenValues.SCREEN_HEIGHT = ScreenValues.SCREEN_WIDTH;
			ScreenValues.SCREEN_WIDTH = tmp;
		}
	}

	@Override
	protected void onDestroy() {
		StageLifeCycleResourceController9000.destroyStage(this);
		super.onDestroy();
	}

	@Override
	public ApplicationListener getApplicationListener() {
		return stageListener;
	}

	@Override
	public void log(String tag, String message, Throwable exception) {
		Log.d(tag, message, exception);
	}

	@Override
	public int getLogLevel() {
		return 0;
	}

	//for running Asynchronous Tasks from the stage
	public void post(Runnable r) {
		handler.post(r);
	}

	public void jsDestroy() {
		stageListener.finish();
		manageLoadAndFinish();
		exit();
	}

	public static int getAndIncrementNumberOfClonedSprites() {
		return ++numberOfSpritesCloned;
	}

	public static void resetNumberOfClonedSprites() {
		numberOfSpritesCloned = 0;
	}

	public static void setNfcTagMessage(NdefMessage message) {
		nfcTagMessage = message;
	}

	public static NdefMessage getNfcTagMessage() {
		return nfcTagMessage;
	}

	public synchronized void queueIntent(IntentListener asker) {
		if (StageActivity.messageHandler == null) {
			return;
		}
		int newIdentId;
		do {
			newIdentId = StageActivity.randomGenerator.nextInt(Integer.MAX_VALUE);
		} while (intentListeners.indexOfKey(newIdentId) >= 0);

		intentListeners.put(newIdentId, asker);
		ArrayList<Object> params = new ArrayList<>();
		params.add(newIdentId);
		Message message = StageActivity.messageHandler.obtainMessage(StageActivity.PERFORM_INTENT, params);
		message.sendToTarget();
	}

	private void startQueuedIntent(int intentKey) {
		if (intentListeners.indexOfKey(intentKey) < 0) {
			return;
		}
		Intent i = intentListeners.get(intentKey).getTargetIntent();
		i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getClass().getPackage().getName());
		this.startActivityForResult(i, intentKey);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//Register your intent with "queueIntent"
		if (intentListeners.indexOfKey(requestCode) >= 0) {
			IntentListener asker = intentListeners.get(requestCode);
			asker.onIntentResult(resultCode, data);
			intentListeners.remove(requestCode);
		}
		stageResourceHolder.onActivityResult(requestCode, resultCode, data);
	}

	public interface IntentListener {
		Intent getTargetIntent();
		void onIntentResult(int resultCode, Intent data); //don't do heavy processing here
	}

	@Override
	public void addToRequiresPermissionTaskList(RequiresPermissionTask task) {
		permissionTaskList.add(task);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
		RequiresPermissionTask task = popAllRequiredPermissionTask(requestCode);

		if(permissions.length == 0) {
			return;
		}

		List<String> deniedPermissions = new ArrayList<>();
		for (int resultIndex = 0; resultIndex < permissions.length; resultIndex ++) {
			if(grantResults[resultIndex] == PackageManager.PERMISSION_DENIED) {
				deniedPermissions.add(permissions[resultIndex]);
			}
		}

		if(task != null) {
			if (deniedPermissions.isEmpty()) {
				task.execute(this);
			} else {
				task.setPermissions(deniedPermissions);
				addToRequiresPermissionTaskList(task);
				showPermissionRationale(task);
			}
		}
	}

	private RequiresPermissionTask popAllRequiredPermissionTask(int requestCode) {
		RequiresPermissionTask matchedTask = null;
		for (RequiresPermissionTask task : new ArrayList<>(permissionTaskList)) {
			if(task.getPermissionRequestId() == requestCode) {
				matchedTask = task;
				permissionTaskList.remove(task);
			}
		}
		return matchedTask;
	}

	@TargetApi(23)
	private void showPermissionRationale(final RequiresPermissionTask task) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
				&& shouldShowRequestPermissionRationale(task.getPermissions().get(0))) {
			showAlertOKCancel(getResources().getString(task.getRationaleString()), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					requestPermissions(task.getPermissions().toArray(new String[0]), task.getPermissionRequestId());
				}
			});
		}
	}

	public void showAlertOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(this)
				.setMessage(message)
				.setPositiveButton(R.string.ok, okListener)
				.setNegativeButton(R.string.cancel, null)
				.create()
				.show();
	}
}

/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.drone.ardrone.DroneServiceWrapper;
import org.catrobat.catroid.drone.ardrone.DroneStageActivity;
import org.catrobat.catroid.facedetection.FaceDetectionHandler;
import org.catrobat.catroid.formulaeditor.SensorHandler;
import org.catrobat.catroid.stage.PreStageActivity;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.dialogs.LegoEV3SensorConfigInfoDialog;
import org.catrobat.catroid.ui.dialogs.LegoNXTSensorConfigInfoDialog;
import org.catrobat.catroid.ui.dialogs.PlaySceneDialog;
import org.catrobat.catroid.ui.recyclerview.fragment.RecyclerViewFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.SceneFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.SpriteFragment;

public class ProjectActivity extends BaseCastActivity implements PlaySceneDialog.PlaySceneInterface {

	private static final String TAG = ProjectActivity.class.getSimpleName();

	public static final String EXTRA_FRAGMENT_POSITION = "FRAGMENT_POSITION";

	public static final int FRAGMENT_SCENES = 0;
	public static final int FRAGMENT_SPRITES = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_recycler);
		int fragmentPosition = FRAGMENT_SCENES;

		if (savedInstanceState == null) {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				fragmentPosition = bundle.getInt(EXTRA_FRAGMENT_POSITION, FRAGMENT_SCENES);
			}
		}

		Project currentProject = ProjectManager.getInstance().getCurrentProject();

		if (currentProject.getSceneList().size() <= 1) {
			ProjectManager.getInstance().setCurrentScene(currentProject.getDefaultScene());
			fragmentPosition = FRAGMENT_SPRITES;
		}

		loadFragment(fragmentPosition);
		showLegoInfoFragmentIfNeeded(this.getFragmentManager());
	}

	private void loadFragment(int fragmentPosition) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		switch (fragmentPosition) {
			case FRAGMENT_SCENES:
				fragmentTransaction.replace(R.id.fragment_container, new SceneFragment(), SceneFragment.TAG);
				break;
			case FRAGMENT_SPRITES:
				fragmentTransaction.replace(R.id.fragment_container, new SpriteFragment(), SpriteFragment.TAG);
				break;
			default:
				return;
		}

		fragmentTransaction.commit();
	}

	@Override
	protected void onStart() {
		super.onStart();
		SettingsActivity.setLegoMindstormsNXTSensorChooserEnabled(this, true);
		SettingsActivity.setLegoMindstormsEV3SensorChooserEnabled(this, true);
		SettingsActivity.setDroneChooserEnabled(this, true);
	}

	@Override
	public void onBackPressed() {
		if (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_project, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		RecyclerViewFragment currentFragment = ((RecyclerViewFragment) getFragmentManager()
				.findFragmentById(R.id
				.fragment_container));

		menu.findItem(R.id.show_details).setVisible(currentFragment.hasDetails);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
			case R.id.upload:
				ProjectManager projectManager = ProjectManager.getInstance();
				projectManager.uploadProject(projectManager.getCurrentProject().getName(),this);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void handleAddButton(View view) {
		((RecyclerViewFragment) getFragmentManager().findFragmentById(R.id.fragment_container)).handleAddButton();
	}

	public void handlePlayButton(View view) {
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		Scene currentScene = ProjectManager.getInstance().getCurrentScene();

		if (currentScene.getName().equals(currentProject.getDefaultScene().getName())) {
			Intent intent = new Intent(this, PreStageActivity.class);
			startActivityForResult(intent, PreStageActivity.REQUEST_RESOURCES_INIT);
		} else {
			PlaySceneDialog playSceneDialog = new PlaySceneDialog(this);
			playSceneDialog.show(getFragmentManager(), PlaySceneDialog.TAG);
		}
	}

	public void startPreStageActivity() {
		Intent intent = new Intent(this, PreStageActivity.class);
		startActivityForResult(intent, PreStageActivity.REQUEST_RESOURCES_INIT);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case PreStageActivity.REQUEST_RESOURCES_INIT:
				if (resultCode == RESULT_OK) {
					if (DroneServiceWrapper.checkARDroneAvailability()) {
						startActivity(new Intent(this, DroneStageActivity.class));
					} else {
						startActivity(new Intent(this, StageActivity.class));
					}
				}
				break;
			case StageActivity.STAGE_ACTIVITY_FINISH:
				SensorHandler.stopSensorListeners();
				FaceDetectionHandler.stopFaceDetection();
				break;
		}

		if (requestCode != RESULT_OK
				&& SettingsActivity.isCastSharedPreferenceEnabled(this)
				&& ProjectManager.getInstance().getCurrentProject().isCastProject()
				&& !CastManager.getInstance().isConnected()) {

			CastManager.getInstance().openDeviceSelectorOrDisconnectDialog(this);
		}
	}

	private boolean needToShowLegoEV3InfoDialog() {
		boolean isLegoEV3InfoDialogDisabled = SettingsActivity
				.getShowLegoEV3MindstormsSensorInfoDialog(getApplicationContext());

		boolean legoEV3ResourcesRequired = (ProjectManager.getInstance().getCurrentProject().getRequiredResources()
				& Brick.BLUETOOTH_LEGO_EV3) != 0;

		boolean dialogAlreadyShown = !ProjectManager.getInstance().getShowLegoSensorInfoDialog();

		return !isLegoEV3InfoDialogDisabled && legoEV3ResourcesRequired && !dialogAlreadyShown;
	}

	private boolean needToShowLegoNXTInfoDialog() {
		boolean isLegoNXTInfoDialogDisabled = SettingsActivity
				.getShowLegoNXTMindstormsSensorInfoDialog(getApplicationContext());

		boolean legoNXTResourcesRequired = (ProjectManager.getInstance().getCurrentProject().getRequiredResources()
				& Brick.BLUETOOTH_LEGO_NXT) != 0;

		boolean dialogAlreadyShown = !ProjectManager.getInstance().getShowLegoSensorInfoDialog();

		return !isLegoNXTInfoDialogDisabled && legoNXTResourcesRequired && !dialogAlreadyShown;
	}

	private void showLegoInfoFragmentIfNeeded(FragmentManager fragmentManager) {

		if (needToShowLegoNXTInfoDialog()) {
			DialogFragment dialog = new LegoNXTSensorConfigInfoDialog();
			dialog.show(fragmentManager, LegoNXTSensorConfigInfoDialog.DIALOG_FRAGMENT_TAG);
		}
		if (needToShowLegoEV3InfoDialog()) {
			DialogFragment dialog = new LegoEV3SensorConfigInfoDialog();
			dialog.show(fragmentManager, LegoEV3SensorConfigInfoDialog.DIALOG_FRAGMENT_TAG);
		}
		ProjectManager.getInstance().setShowLegoSensorInfoDialog(false);
	}
}

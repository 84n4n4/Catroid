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

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.BroadcastHandler;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.drone.ardrone.DroneServiceWrapper;
import org.catrobat.catroid.drone.ardrone.DroneStageActivity;
import org.catrobat.catroid.stage.PreStageActivity;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.dialogs.PlaySceneDialog;
import org.catrobat.catroid.ui.fragment.FormulaEditorCategoryListFragment;
import org.catrobat.catroid.ui.fragment.FormulaEditorDataFragment;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.ui.fragment.ScriptFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.LookFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.NfcTagFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.RecyclerViewFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.SoundFragment;

public class ScriptActivity extends BaseActivity {
	public static final int FRAGMENT_SCRIPTS = 0;
	public static final int FRAGMENT_LOOKS = 1;
	public static final int FRAGMENT_SOUNDS = 2;
	public static final int FRAGMENT_NFCTAGS = 3;

	public static final String EXTRA_FRAGMENT_POSITION = "org.catrobat.catroid.ui.fragmentPosition";

	public static final String ACTION_SPRITE_RENAMED = "org.catrobat.catroid.SPRITE_RENAMED";
	public static final String ACTION_SPRITES_LIST_INIT = "org.catrobat.catroid.SPRITES_LIST_INIT";
	public static final String ACTION_SPRITES_LIST_CHANGED = "org.catrobat.catroid.SPRITES_LIST_CHANGED";
	public static final String ACTION_BRICK_LIST_CHANGED = "org.catrobat.catroid.BRICK_LIST_CHANGED";
	public static final String ACTION_LOOK_DELETED = "org.catrobat.catroid.LOOK_DELETED";
	public static final String ACTION_LOOK_RENAMED = "org.catrobat.catroid.LOOK_RENAMED";
	public static final String ACTION_LOOKS_LIST_INIT = "org.catrobat.catroid.LOOKS_LIST_INIT";
	public static final String ACTION_SOUND_DELETED = "org.catrobat.catroid.SOUND_DELETED";
	public static final String ACTION_SOUND_COPIED = "org.catrobat.catroid.SOUND_COPIED";
	public static final String ACTION_SOUND_RENAMED = "org.catrobat.catroid.SOUND_RENAMED";
	public static final String ACTION_SOUNDS_LIST_INIT = "org.catrobat.catroid.SOUNDS_LIST_INIT";
	public static final String ACTION_NFCTAG_DELETED = "org.catrobat.catroid.NFCTAG_DELETED";
	public static final String ACTION_NFCTAG_COPIED = "org.catrobat.catroid.NFCTAG_COPIED";
	public static final String ACTION_NFCTAG_RENAMED = "org.catrobat.catroid.NFCTAG_RENAMED";
	public static final String ACTION_NFCTAGS_LIST_INIT = "org.catrobat.catroid.NFCTAGS_LIST_INIT";
	public static final String ACTION_VARIABLE_DELETED = "org.catrobat.catroid.VARIABLE_DELETED";
	public static final String ACTION_USERLIST_DELETED = "org.catrobat.catroid.USERLIST_DELETED";
	public static final String ACTION_SPRITE_TOUCH_ACTION_UP = "org.catrobat.catroid.SPRITE_TOUCH_ACTION_UP";
	public static final String ACTION_LOOK_TOUCH_ACTION_UP = "org.catrobat.catroid.LOOK_TOUCH_ACTION_UP";
	public static final String ACTION_NFC_TOUCH_ACTION_UP = "org.catrobat.catroid.NFC_TOUCH_ACTION_UP";
	public static final String ACTION_SOUND_TOUCH_ACTION_UP = "org.catrobat.catroid.SOUND_TOUCH_ACTION_UP";

	private static final String TAG = ScriptActivity.class.getSimpleName();
	private static int fragmentPosition;

	private ScriptFragment scriptFragment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_script);

		if (savedInstanceState == null) {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				fragmentPosition = bundle.getInt(EXTRA_FRAGMENT_POSITION, FRAGMENT_SCRIPTS);
			}
		}

		switchToFragment(fragmentPosition);
	}

	private void switchToFragment(int fragmentTag) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		switch (fragmentTag) {
			case FRAGMENT_SCRIPTS:
				scriptFragment = new ScriptFragment();
				fragmentTransaction.replace(R.id.fragment_container, scriptFragment, ScriptFragment.TAG);
				break;
			case FRAGMENT_LOOKS:
				fragmentTransaction.replace(R.id.fragment_container, new LookFragment(), LookFragment.TAG);
				break;
			case FRAGMENT_SOUNDS:
				fragmentTransaction.replace(R.id.fragment_container, new SoundFragment(), SoundFragment.TAG);
				break;
			case FRAGMENT_NFCTAGS:
				fragmentTransaction.replace(R.id.fragment_container, new NfcTagFragment(), SoundFragment.TAG);
				break;
			default:
				return;
		}

		fragmentTransaction.commit();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (fragmentPosition == FRAGMENT_NFCTAGS) {
			((NfcTagFragment) getFragmentManager().findFragmentById(R.id.fragment_container)).onNewIntent(intent);
		}
	}

	public void handleAddButton(View view) {
		switch (fragmentPosition) {
			case FRAGMENT_SCRIPTS:
				((ScriptFragment) getFragmentManager().findFragmentById(R.id.fragment_container)).handleAddButton();
				break;
			case FRAGMENT_LOOKS:
			case FRAGMENT_SOUNDS:
			case FRAGMENT_NFCTAGS:
				((RecyclerViewFragment) getFragmentManager()
						.findFragmentById(R.id.fragment_container)).handleAddButton();
				break;
			default:
				break;
		}
	}

	private void setupBottomBar() {
		BottomBar.showBottomBar(this);
		BottomBar.showAddButton(this);
		BottomBar.showPlayButton(this);
	}

	public void setupActionBar() {
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		try {
			Sprite sprite = ProjectManager.getInstance().getCurrentSprite();
			Scene scene = ProjectManager.getInstance().getCurrentScene();
			if (sprite != null && scene != null) {
				String title;
				if (ProjectManager.getInstance().getCurrentProject().getSceneList().size() == 1) {
					title = sprite.getName();
				} else {
					title = scene.getName() + ": " + sprite.getName();
				}
				actionBar.setTitle(title);
			}
		} catch (NullPointerException nullPointerException) {
			Log.e(TAG, Log.getStackTraceString(nullPointerException));
			finish();
		}
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		setupActionBar();
		setupBottomBar();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (fragmentPosition == FRAGMENT_SCRIPTS) {
			menu.findItem(R.id.comment_in_out).setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_script_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PreStageActivity.REQUEST_RESOURCES_INIT && resultCode == RESULT_OK) {
			Intent intent;
			if (DroneServiceWrapper.checkARDroneAvailability()) {
				intent = new Intent(ScriptActivity.this, DroneStageActivity.class);
			} else {
				intent = new Intent(ScriptActivity.this, StageActivity.class);
			}
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Find currently visible FormulaEditorFragment
		// it has multiple positions so the correct one has to be found and the back button press handed over.

		// Check Editor Fragment
		FormulaEditorFragment editorFragment = (FormulaEditorFragment) getFragmentManager()
				.findFragmentByTag(FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG);

		if (editorFragment != null && editorFragment.isVisible()) {
			return editorFragment.onKey(null, keyCode, event);
		}

		// Check all Category Fragments
		for (String tag : FormulaEditorCategoryListFragment.TAGS) {
			FormulaEditorCategoryListFragment categoryListFragment =
					(FormulaEditorCategoryListFragment) getFragmentManager().findFragmentByTag(tag);

			if (categoryListFragment != null && categoryListFragment.isVisible()) {
				return categoryListFragment.onKey(null, keyCode, event);
			}
		}

		// Check Data Fragment
		FormulaEditorDataFragment dataFragment = (FormulaEditorDataFragment) getFragmentManager()
				.findFragmentByTag(FormulaEditorDataFragment.USER_DATA_TAG);

		if (dataFragment != null && dataFragment.isVisible()) {
			return dataFragment.onKey(null, keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}

	public void handlePlayButton(View view) {

		if (isHoveringActive()) {
			ScriptFragment fragment = ((ScriptFragment) getFragmentManager().findFragmentById(R.id.fragment_container));
			fragment.getListView().animateHoveringBrick();
			return;
		}

		while (getFragmentManager().getBackStackEntryCount() > 0) {
			getFragmentManager().popBackStack();
		}

		BroadcastHandler.clearActionMaps();

		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		Scene currentScene = ProjectManager.getInstance().getCurrentScene();

		if (currentScene.getName().equals(currentProject.getDefaultScene().getName())) {
			ProjectManager.getInstance().setSceneToPlay(currentScene);
			ProjectManager.getInstance().setStartScene(currentScene);
			startPreStageActivity();
			return;
		}

		PlaySceneDialog playSceneDialog = new PlaySceneDialog();
		playSceneDialog.show(getFragmentManager(), PlaySceneDialog.DIALOG_FRAGMENT_TAG);
	}

	public void updateHandleAddButtonClickListener() {
	}

	public void startPreStageActivity() {
		Intent intent = new Intent(this, PreStageActivity.class);
		startActivityForResult(intent, PreStageActivity.REQUEST_RESOURCES_INIT);
	}

	public boolean isHoveringActive() {
		if (fragmentPosition == FRAGMENT_SCRIPTS) {
			ScriptFragment fragment = ((ScriptFragment) getFragmentManager().findFragmentById(R.id.fragment_container));
			return fragment.getListView().isCurrentlyDragging();
		} else {
			return false;
		}
	}

	public void redrawBricks() {
		if (fragmentPosition == FRAGMENT_SCRIPTS) {
			ScriptFragment fragment = ((ScriptFragment) getFragmentManager().findFragmentById(R.id.fragment_container));
			fragment.getAdapter().notifyDataSetInvalidated();
		}
	}

	public Fragment getFragment(int fragmentPosition) {
		return scriptFragment;
	}

	public void switchToFragmentFromScriptFragment(int fragmentPosition) {
	}
}


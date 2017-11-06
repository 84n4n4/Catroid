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

package org.catrobat.catroid.ui.recyclerview.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.controller.BackPackSceneController;
import org.catrobat.catroid.ui.recyclerview.adapter.SceneAdapter;
import org.catrobat.catroid.ui.recyclerview.adapter.ViewHolder;
import org.catrobat.catroid.ui.recyclerview.backpack.BackpackActivity;
import org.catrobat.catroid.ui.recyclerview.backpack.controller.SceneController;
import org.catrobat.catroid.ui.recyclerview.dialog.NewSceneDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.RenameItemDialog;
import org.catrobat.catroid.utils.SnackbarUtil;
import org.catrobat.catroid.utils.ToastUtil;
import org.catrobat.catroid.utils.Utils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SceneFragment extends RecyclerViewFragment<Scene> {

	public static final String TAG = SceneFragment.class.getSimpleName();

	private SceneController sceneController = new SceneController();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hasDetails = false;
	}

	@Override
	public void onResume() {
		super.onResume();
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		ProjectManager.getInstance().setCurrentScene(currentProject.getDefaultScene());
		getActivity().getActionBar().setTitle(currentProject.getName());
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.new_scene).setVisible(false);
		menu.findItem(R.id.create_group).setVisible(false);
	}

	@Override
	protected void initializeAdapter() {
		SnackbarUtil.showHintSnackbar(getActivity(), R.string.hint_scenes);
		List<Scene> items = ProjectManager.getInstance().getCurrentProject().getSceneList();
		adapter = new SceneAdapter(items) {

			@Override
			public void onBindViewHolder(ViewHolder holder, int position) {
				super.onBindViewHolder(holder, position);

				Scene item = items.get(holder.getAdapterPosition());
				String name = item.getName();

				if (holder.getAdapterPosition() == 0) {
					name = getActivity().getString(R.string.start_scene_name, name);
				}

				holder.name.setText(name);
			}
		};
		onAdapterReady();
	}

	@Override
	public void handleAddButton() {
		NewSceneDialog dialog = new NewSceneDialog(this);
		dialog.show(getFragmentManager(), NewSceneDialog.TAG);
	}

	@Override
	public void addItem(Scene item) {
		adapter.add(item);
	}

	@Override
	protected void packItems(List<Scene> selectedItems) {
		finishActionMode();
		try {
			for (Scene item : selectedItems) {
				sceneController.pack(item);
			}
			ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.packed_scenes,
					selectedItems.size(),
					selectedItems.size()));

			switchToBackpack();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	@Override
	protected boolean isBackpackEmpty() {
		return BackPackListManager.getInstance().getBackPackedScenes().isEmpty();
	}

	@Override
	protected void switchToBackpack() {
		Intent intent = new Intent(getActivity(), BackpackActivity.class);
		intent.putExtra(BackpackActivity.EXTRA_FRAGMENT_POSITION, BackpackActivity.FRAGMENT_SCENES);
		startActivity(intent);
	}

	@Override
	protected void copyItems(List<Scene> selectedItems) {
		finishActionMode();
		try {
			for (Scene item : selectedItems) {
				sceneController.copy(item, ProjectManager.getInstance().getCurrentProject());
			}
			ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.copied_scenes,
					selectedItems.size(),
					selectedItems.size()));

		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	@Override
	protected Set<String> getScope() {
		Set<String> scope = new HashSet<>();
		for (Scene item : adapter.getItems()) {
			scope.add(item.getName());
		}
		return scope;
	}

	@Override
	protected int getDeleteAlertTitle() {
		return R.plurals.delete_scenes;
	}

	@Override
	protected void deleteItems(List<Scene> selectedItems) {
		finishActionMode();
		for (Scene item : selectedItems) {
			try {
				StorageHandler.deleteDir(Utils.buildScenePath(item.getProject().getName(), item.getName()));
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
			adapter.remove(item);
		}

		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.deleted_scenes,
				selectedItems.size(),
				selectedItems.size()));

		if (adapter.getItemCount() < 2) {
			if (adapter.getItems().isEmpty()) {
				createDefaultScene();
			}
			ProjectManager.getInstance().setCurrentScene(adapter.getItems().get(0));
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new SpriteFragment(), SpriteFragment.TAG)
					.commit();
		}
	}

	private void createDefaultScene() {
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		Scene scene = new Scene(getActivity(), getString(R.string.default_scene_name, 1), currentProject);
		adapter.add(scene);
		onItemClick(scene);
	}

	@Override
	protected void showRenameDialog(List<Scene> selectedItems) {
		String name = selectedItems.get(0).getName();
		RenameItemDialog dialog = new RenameItemDialog(
				R.string.rename_scene_dialog,
				R.string.scene_name, name,
				this);
		dialog.show(getFragmentManager(), RenameItemDialog.TAG);
	}

	@Override
	public boolean isNameUnique(String name) {
		return !getScope().contains(name);
	}

	@Override
	public void renameItem(String name) {
		// TODO: update scene order.
		adapter.getSelectedItems().get(0).rename(name, getActivity(), false);
		finishActionMode();
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		actionMode.setTitle(actionModeTitle + " " + getResources().getQuantityString(R.plurals.am_scenes_title,
				selectedItemCnt,
				selectedItemCnt));
	}

	@Override
	public void onItemClick(Scene item) {
		if (actionModeType == ActionModeType.NONE) {
			ProjectManager.getInstance().setCurrentScene(item);
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new SpriteFragment(), SpriteFragment.TAG)
					.addToBackStack(SpriteFragment.TAG)
					.commit();
		}
	}
}

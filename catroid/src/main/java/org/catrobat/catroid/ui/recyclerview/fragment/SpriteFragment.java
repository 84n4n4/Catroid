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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.SpriteAttributesActivity;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.recyclerview.adapter.SpriteAdapter;
import org.catrobat.catroid.ui.recyclerview.adapter.ViewHolder;
import org.catrobat.catroid.ui.recyclerview.backpack.BackpackActivity;
import org.catrobat.catroid.ui.recyclerview.backpack.controller.SpriteController;
import org.catrobat.catroid.ui.recyclerview.dialog.NewLookDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.NewSceneDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.NewSpriteDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.RenameItemDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;
import org.catrobat.catroid.utils.SnackbarUtil;
import org.catrobat.catroid.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpriteFragment extends RecyclerViewFragment<Sprite> {

	public static final String TAG = SpriteFragment.class.getSimpleName();

	class NewSceneInterface implements NewItemInterface<Scene> {

		@Override
		public void addItem(Scene item) {
			ProjectManager.getInstance().getCurrentProject().getSceneList().add(item);

			if (getFragmentManager().getBackStackEntryCount() > 0) {
				getFragmentManager().popBackStack();
			}

			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, new SceneFragment(), SceneFragment.TAG)
					.commit();
		}
	}

	class NewLookInterface implements NewItemInterface<LookData> {

		private NewItemInterface<Sprite> newSpriteInterface;

		NewLookInterface(NewItemInterface<Sprite> newSpriteInterface) {
			this.newSpriteInterface = newSpriteInterface;
		}

		@Override
		public void addItem(LookData item) {
			NewSpriteDialog dialog = new NewSpriteDialog(newSpriteInterface, item);
			dialog.show(getFragmentManager(), NewSpriteDialog.TAG);
		}
	}

	private NewSceneInterface newSceneInterface = new NewSceneInterface();
	private NewLookInterface newLookInterface = new NewLookInterface(this);
	private SpriteController spriteController = new SpriteController();

	@Override
	public void onResume() {
		super.onResume();
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		Scene currentScene = ProjectManager.getInstance().getCurrentScene();
		String title = currentProject.getName();

		if (currentProject.getSceneList().size() > 1) {
			title += ": " + currentScene.getName();
		}

		getActivity().getActionBar().setTitle(title);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.new_scene).setVisible(true);
		menu.findItem(R.id.create_group).setVisible(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_scene:
				NewSceneDialog dialog = new NewSceneDialog(newSceneInterface);
				dialog.show(getFragmentManager(), NewSceneDialog.TAG);
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void initializeAdapter() {
		SnackbarUtil.showHintSnackbar(getActivity(), R.string.hint_objects);
		List<Sprite> items = ProjectManager.getInstance().getCurrentScene().getSpriteList();
		adapter = new SpriteAdapter(items) {

			@Override
			public void onBindViewHolder(ViewHolder holder, int position) {
				super.onBindViewHolder(holder, position);

				if (holder.getAdapterPosition() == 0) {
					holder.background.setOnLongClickListener(null);
					holder.checkBox.setVisibility(View.GONE);
				}
			}

			@Override
			public boolean onItemMove(int fromPosition, int toPosition) {
				return fromPosition == 0 || toPosition == 0 || super.onItemMove(fromPosition, toPosition);
			}
		};
		onAdapterReady();
	}

	@Override
	public void handleAddButton() {
		Sprite sprite = new Sprite();
		ProjectManager.getInstance().setCurrentSprite(sprite);
		NewLookDialog dialog = new NewLookDialog(newLookInterface);
		dialog.show(getFragmentManager(), NewSpriteDialog.TAG);
	}

	@Override
	public void addItem(Sprite item) {
		adapter.add(item);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void packItems(List<Sprite> selectedItems) {
		finishActionMode();
		try {
			for (Sprite item : selectedItems) {
				spriteController.pack(item);
			}
			ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.packed_sprites,
					selectedItems.size(),
					selectedItems.size()));

			switchToBackpack();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	@Override
	protected boolean isBackpackEmpty() {
		return BackPackListManager.getInstance().getBackPackedSprites().isEmpty();
	}

	@Override
	protected void switchToBackpack() {
		Intent intent = new Intent(getActivity(), BackpackActivity.class);
		intent.putExtra(BackpackActivity.EXTRA_FRAGMENT_POSITION, BackpackActivity.FRAGMENT_SPRITES);
		startActivity(intent);
	}

	@Override
	protected void copyItems(List<Sprite> selectedItems) {
		finishActionMode();
		for (Sprite item : selectedItems) {
			Sprite sprite = new Sprite(item.getName());
			copyLooks(item, sprite);
			copySounds(item, sprite);
			adapter.add(sprite);
		}
		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.copied_sprites,
				selectedItems.size(),
				selectedItems.size()));
	}

	private void copyLooks(Sprite srcSprite, Sprite dstSprite) {
		for (LookData item : srcSprite.getLookDataList()) {
			try {
				String fileName = StorageHandler.copyFile(item.getAbsolutePath()).getName();
				dstSprite.getLookDataList().add(new LookData(item.getLookName(), fileName));
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}
	}

	private void copySounds(Sprite srcSprite, Sprite dstSprite) {
		for (SoundInfo item : srcSprite.getSoundList()) {
			try {
				String fileName = StorageHandler.copyFile(item.getAbsolutePath()).getName();
				dstSprite.getSoundList().add(new SoundInfo(item.getTitle(), fileName));
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}
	}

	@Override
	protected Set<String> getScope() {
		Set<String> scope = new HashSet<>();
		for (Sprite item : adapter.getItems()) {
			scope.add(item.getName());
		}
		return scope;
	}

	@Override
	protected int getDeleteAlertTitle() {
		return R.plurals.delete_sprites;
	}

	@Override
	protected void deleteItems(List<Sprite> selectedItems) {
		finishActionMode();
		for (Sprite item : selectedItems) {
			deleteLooks(item.getLookDataList());
			deleteSounds(item.getSoundList());
			adapter.remove(item);
		}
		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.deleted_sprites,
				selectedItems.size(),
				selectedItems.size()));
	}

	private void deleteLooks(List<LookData> items) {
		for (LookData item : items) {
			try {
				StorageHandler.deleteFile(item.getAbsolutePath());
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}
	}

	private void deleteSounds(List<SoundInfo> items) {
		for (SoundInfo item : items) {
			try {
				StorageHandler.deleteFile(item.getAbsolutePath());
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
		}
	}

	@Override
	protected void showRenameDialog(List<Sprite> selectedItems) {
		String name = selectedItems.get(0).getName();
		RenameItemDialog dialog = new RenameItemDialog(
				R.string.rename_sprite_dialog,
				R.string.sprite_name, name,
				this);
		dialog.show(getFragmentManager(), RenameItemDialog.TAG);
	}

	@Override
	public boolean isNameUnique(String name) {
		return !getScope().contains(name);
	}

	@Override
	public void renameItem(String name) {
		Sprite item = adapter.getSelectedItems().get(0);
		if (!item.getName().equals(name)) {
			item.setName(name);
		}
		finishActionMode();
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		actionMode.setTitle(actionModeTitle + " " + getResources().getQuantityString(R.plurals.am_sprites_title,
				selectedItemCnt,
				selectedItemCnt));
	}

	@Override
	public void onItemClick(Sprite item) {
		if (actionModeType == ActionModeType.NONE) {
			ProjectManager.getInstance().setCurrentSprite(item);
			Intent intent = new Intent(getActivity(), SpriteAttributesActivity.class);
			startActivity(intent);
		}
	}
}

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

package org.catrobat.catroid.ui.recyclerview.backpack;

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.recyclerview.adapter.SoundAdapter;
import org.catrobat.catroid.utils.ToastUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.catrobat.catroid.common.Constants.SOUND_DIRECTORY;
import static org.catrobat.catroid.utils.Utils.buildPath;
import static org.catrobat.catroid.utils.Utils.buildScenePath;

public class BackpackSoundFragment extends BackpackRecyclerViewFragment<SoundInfo> {

	public static final String TAG = BackpackSoundFragment.class.getSimpleName();

	@Override
	protected void initializeAdapter() {
		List<SoundInfo> items = BackPackListManager.getInstance().getBackPackedSounds();
		adapter = new SoundAdapter(items);
		onAdapterReady();
	}

	@Override
	protected void unpackItems(List<SoundInfo> selectedItems) {
		if (actionModeType != ActionModeType.NONE) {
			finishActionMode();
		}

		try {
			for (SoundInfo item : selectedItems) {
				String name = uniqueNameProvider.getUniqueName(item.getTitle(), getDstScope());
				String fileName = StorageHandler.copyFile(item.getAbsolutePath(), getUnpackDstPath()).getName();
				getDstItemList().add(new SoundInfo(name, fileName));
			}
			ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.unpacked_sounds,
					selectedItems.size(),
					selectedItems.size()));
			getActivity().finish();
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	protected String getUnpackDstPath() {
		return buildPath(buildScenePath(
				ProjectManager.getInstance().getCurrentProject().getName(),
				ProjectManager.getInstance().getCurrentScene().getName()),
				SOUND_DIRECTORY);
	}

	protected List<SoundInfo> getDstItemList() {
		return ProjectManager.getInstance().getCurrentSprite().getSoundList();
	}

	@Override
	protected Set<String> getDstScope() {
		Set<String> scope = new HashSet<>();
		for (SoundInfo item : getDstItemList()) {
			scope.add(item.getTitle());
		}
		return scope;
	}

	@Override
	protected int getDeleteAlertTitle() {
		return R.plurals.delete_sounds;
	}

	@Override
	protected void deleteItems(List<SoundInfo> selectedItems) {
		if (actionModeType != ActionModeType.NONE) {
			finishActionMode();
		}

		for (SoundInfo item : selectedItems) {
			try {
				StorageHandler.deleteFile(item.getAbsolutePath());
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
			adapter.items.remove(item);
		}

		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.deleted_sounds,
				selectedItems.size(),
				selectedItems.size()));
		adapter.notifyDataSetChanged();

		BackPackListManager.getInstance().saveBackpack();
		if (adapter.items.isEmpty()) {
			getActivity().finish();
		}
	}

	@Override
	public String getItemName(SoundInfo item) {
		return item.getTitle();
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		actionMode.setTitle(actionModeTitle + " " + getResources().getQuantityString(R.plurals.am_sounds_title,
				selectedItemCnt,
				selectedItemCnt));
	}
}

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

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.ProjectData;
import org.catrobat.catroid.exceptions.ProjectException;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.recyclerview.adapter.ProjectAdapter;
import org.catrobat.catroid.ui.recyclerview.adapter.ViewHolder;
import org.catrobat.catroid.ui.recyclerview.dialog.RenameItemDialog;
import org.catrobat.catroid.ui.recyclerview.fragment.asynctask.ProjectCreatorTask;
import org.catrobat.catroid.ui.recyclerview.fragment.asynctask.ProjectLoaderTask;
import org.catrobat.catroid.utils.ToastUtil;
import org.catrobat.catroid.utils.UtilFile;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectFragment extends RecyclerViewFragment<ProjectData> implements
		ProjectLoaderTask.ProjectLoaderListener,
		ProjectCreatorTask.ProjectCreatorListener {

	public static final String TAG = ProjectFragment.class.getSimpleName();

	@Override
	public void onResume() {
		initializeAdapter();
		super.onResume();
	}

	@Override
	protected void initializeAdapter() {
		adapter = new ProjectAdapter(getItemList());
		onAdapterReady();
	}

	private List<ProjectData> getItemList() {
		File rootDirectory = new File(Constants.DEFAULT_ROOT);
		List<ProjectData> items = new ArrayList<>();

		for (String projectName : UtilFile.getProjectNames(rootDirectory)) {
			File codeFile = new File(Utils.buildPath(Utils.buildProjectPath(projectName), Constants.PROJECTCODE_NAME));
			items.add(new ProjectData(projectName, codeFile.lastModified()));
		}

		Collections.sort(items, new Comparator<ProjectData>() {
			@Override
			public int compare(ProjectData project1, ProjectData project2) {
				return Long.valueOf(project2.lastUsed).compareTo(project1.lastUsed);
			}
		});

		return items;
	}

	@Override
	public void handleAddButton() {
		// This is handled through the NewProjectDialog.
	}

	@Override
	public void addItem(ProjectData item) {
		// This is handled through the NewProjectDialog.
	}

	@Override
	protected void packItems(List<ProjectData> selectedItems) {
		throw new IllegalStateException(TAG + ": Projects cannot be backpacked");
	}

	@Override
	protected boolean isBackpackEmpty() {
		return true;
	}

	@Override
	protected void switchToBackpack() {
		throw new IllegalStateException(TAG + ": Projects cannot be backpacked");
	}

	@Override
	protected void copyItems(List<ProjectData> selectedItems) {
		finishActionMode();
		// TODO: implement AsyncTask.
	}

	@Override
	protected Set<String> getScope() {
		Set<String> scope = new HashSet<>();
		for (ProjectData item : adapter.getItems()) {
			scope.add(item.projectName);
		}
		return scope;
	}

	@Override
	protected int getDeleteAlertTitle() {
		return R.plurals.delete_projects;
	}

	@Override
	protected void deleteItems(List<ProjectData> selectedItems) {
		finishActionMode();

		for (ProjectData item : selectedItems) {
			try {
				ProjectManager.getInstance().deleteProject(item.projectName, getActivity());
			} catch (IOException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			} catch (IllegalArgumentException e) {
				Log.e(TAG, Log.getStackTraceString(e));
			}
			adapter.remove(item);
		}

		ToastUtil.showSuccess(getActivity(), getResources().getQuantityString(R.plurals.deleted_projects,
				selectedItems.size(),
				selectedItems.size()));

		if (adapter.getItems().isEmpty()) {
			setShowProgressBar(true);
			ProjectCreatorTask creatorTask = new ProjectCreatorTask(getActivity(), this);
			creatorTask.execute();
		}
	}

	@Override
	protected void showRenameDialog(List<ProjectData> selectedItems) {
		String name = selectedItems.get(0).projectName;
		RenameItemDialog dialog = new RenameItemDialog(
				R.string.rename_project,
				R.string.project_name, name,
				this);
		dialog.show(getFragmentManager(), RenameItemDialog.TAG);
	}

	@Override
	public boolean isNameUnique(String name) {
		return !getScope().contains(name);
	}

	@Override
	public void renameItem(String name) {
		ProjectManager projectManager = ProjectManager.getInstance();
		try {
			projectManager.loadProject(adapter.getSelectedItems().get(0).projectName, getActivity());
			projectManager.renameProject(name, getActivity());
			projectManager.loadProject(name, getActivity());
		} catch (ProjectException e) {
			Log.e(TAG, Log.getStackTraceString(e));
			Utils.showErrorDialog(getActivity(), R.string.error_rename_incompatible_project);
		}

		finishActionMode();
		initializeAdapter();
	}

	@Override
	public void onCreateFinished(boolean success) {
		if (success) {
			initializeAdapter();
		} else {
			ToastUtil.showError(getActivity(), R.string.wtf_error);
			getActivity().finish();
		}
	}

	@Override
	public void onLoadFinished(boolean success, String message) {
		if (success) {
			Intent intent = new Intent(getActivity(), ProjectActivity.class);
			intent.putExtra(ProjectActivity.EXTRA_FRAGMENT_POSITION, ProjectActivity.FRAGMENT_SCENES);
			startActivity(intent);
		} else {
			setShowProgressBar(false);
			ToastUtil.showError(getActivity(), message);
		}
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		actionMode.setTitle(actionModeTitle + " " + getResources().getQuantityString(R.plurals.am_projects_title,
				selectedItemCnt,
				selectedItemCnt));
	}

	@Override
	public void onItemClick(ProjectData item) {
		if (actionModeType == ActionModeType.NONE) {
			ProjectLoaderTask loaderTask = new ProjectLoaderTask(getActivity(), this);
			setShowProgressBar(true);
			loaderTask.execute(item.projectName);
		}
	}

	@Override
	public void onItemLongClick(ProjectData item, ViewHolder holder) {
	}
}

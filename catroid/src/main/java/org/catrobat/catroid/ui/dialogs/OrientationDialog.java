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

package org.catrobat.catroid.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.utils.ToastUtil;

import java.io.IOException;

public class OrientationDialog extends DialogFragment {

	public static final String TAG = OrientationDialog.class.getSimpleName();

	public String name;
	public boolean createEmptyProject;

	private RadioGroup radioGroup;

	@Override
	@SuppressLint("InflateParams")
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_orientation, null);

		int title = R.string.project_orientation_title;

		if (SettingsActivity.isCastSharedPreferenceEnabled(getActivity())) {
			title = R.string.project_select_screen_title;
			view.findViewById(R.id.cast).setVisibility(View.VISIBLE);
		}

		radioGroup = view.findViewById(R.id.radio_group);

		builder.setTitle(title)
				.setView(view)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (radioGroup.getCheckedRadioButtonId()) {
							case R.id.portrait:
								createProject(false, false);
								break;
							case R.id.landscape_mode:
								createProject(true, false);
								break;
							case R.id.cast:
								createProject(false, true);
								break;
							default:
								throw new IllegalStateException(TAG + ": Cannot find RadioButton.");
						}
						dismiss();
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		return builder.create();
	}

	private void createProject(boolean createLandscapeProject, boolean createCastProject) {
		try {
			ProjectManager.getInstance().initializeNewProject(name, getActivity(), createEmptyProject, false,
					createLandscapeProject, createCastProject, false);
		} catch (IOException e) {
			ToastUtil.showError(getActivity(), R.string.error_new_project);
		}

		Intent intent = new Intent(getActivity(), ProjectActivity.class);
		getActivity().startActivity(intent);
	}
}

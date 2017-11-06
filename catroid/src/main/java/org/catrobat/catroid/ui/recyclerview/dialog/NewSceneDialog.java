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

package org.catrobat.catroid.ui.recyclerview.dialog;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.ui.dialogs.TextDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;

import java.util.HashSet;
import java.util.Set;

public class NewSceneDialog extends TextDialog {

	public static final String TAG = NewSceneDialog.class.getSimpleName();

	private NewItemInterface<Scene> newItemInterface;

	public NewSceneDialog(NewItemInterface<Scene> newItemInterface) {
		super(R.string.new_scene_dialog, R.string.scene_name, "", false);
		this.newItemInterface = newItemInterface;
	}

	@Override
	protected boolean handlePositiveButtonClick() {
		String newName = input.getText().toString().trim();

		if (newName.isEmpty()) {
			input.setError(getString(R.string.name_consists_of_spaces_only));
			return false;
		}

		if (isNameUnique(newName)) {
			Project currentProject = ProjectManager.getInstance().getCurrentProject();
			newItemInterface.addItem(new Scene(getActivity(), newName, currentProject));
			return true;
		} else {
			input.setError(getString(R.string.name_already_exists));
			return false;
		}
	}

	@Override
	protected void handleNegativeButtonClick() {
	}

	private boolean isNameUnique(String name) {
		return !getScope().contains(name);
	}

	private Set<String> getScope() {
		Set<String> scope = new HashSet<>();
		for (Scene item : ProjectManager.getInstance().getCurrentProject().getSceneList()) {
			scope.add(item.getName());
		}
		return scope;
	}

}

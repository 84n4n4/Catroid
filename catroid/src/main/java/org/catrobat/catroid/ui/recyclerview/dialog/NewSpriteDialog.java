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

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.dialogs.TextDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class NewSpriteDialog extends TextDialog {

	public static final String TAG = NewSpriteDialog.class.getSimpleName();

	private NewItemInterface<Sprite> newItemInterface;
	private LookData look;

	public NewSpriteDialog(NewItemInterface<Sprite>  newItemInterface, LookData look) {
		super(R.string.new_sprite_dialog_title, R.string.sprite_name, "", false);
		this.newItemInterface = newItemInterface;
		this.look = look;
	}

	@Override
	protected boolean handlePositiveButtonClick() {
		String newName = input.getText().toString().trim();

		if (newName.isEmpty()) {
			input.setError(getString(R.string.name_consists_of_spaces_only));
			return false;
		}

		if (isNameUnique(newName)) {
			Sprite sprite = ProjectManager.getInstance().getCurrentSprite();
			sprite.setName(newName);
			sprite.getLookDataList().add(look);
			ProjectManager.getInstance().setCurrentSprite(null);
			newItemInterface.addItem(sprite);
			return true;
		} else {
			input.setError(getString(R.string.name_already_exists));
			return false;
		}
	}

	@Override
	protected void handleNegativeButtonClick() {
		ProjectManager.getInstance().setCurrentSprite(null);
		try {
			StorageHandler.deleteFile(look.getAbsolutePath());
		} catch (IOException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	private boolean isNameUnique(String name) {
		return !getScope().contains(name);
	}

	private Set<String> getScope() {
		Set<String> scope = new HashSet<>();
		for (Sprite item : ProjectManager.getInstance().getCurrentScene().getSpriteList()) {
			scope.add(item.getName());
		}
		return scope;
	}
}

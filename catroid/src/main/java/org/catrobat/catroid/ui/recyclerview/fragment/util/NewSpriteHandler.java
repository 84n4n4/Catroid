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

package org.catrobat.catroid.ui.recyclerview.fragment.util;

import android.app.Activity;

import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.ui.recyclerview.dialog.NewLookDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.NewSpriteDialog;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;

public class NewSpriteHandler implements NewItemInterface<LookData> {

	public static final String TAG = NewSpriteHandler.class.getSimpleName();

	private NewItemInterface<Sprite>  newItemInterface;
	private Activity activity;

	public void handleAddSprite(NewItemInterface<Sprite> newItemInterface, Activity activity) {
		this.newItemInterface = newItemInterface;
		this.activity = activity;

		NewLookDialog dialog = new NewLookDialog(this);
		dialog.show(activity.getFragmentManager(), NewLookDialog.TAG);
	}

	@Override
	public void addItem(LookData item) {
		NewSpriteDialog dialog = new NewSpriteDialog(newItemInterface, item);
		dialog.show(activity.getFragmentManager(), NewSpriteDialog.TAG);
	}
}

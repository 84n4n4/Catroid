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
package org.catrobat.catroid.ui.controller;

import org.catrobat.catroid.common.LookData;

public final class LookController {

	public static final String BUNDLE_ARGUMENTS_SELECTED_LOOK = "selected_look";
	public static final String TAG = LookController.class.getSimpleName();

	private static final LookController INSTANCE = new LookController();

	private LookController() {
	}

	public static LookController getInstance() {
		return INSTANCE;
	}

	public boolean otherLookDataItemsHaveAFileReference(LookData lookData) {
		return false;
	}

	public LookData backPackHiddenLook(LookData lookData) {
		return null;
	}

	public LookData unpack(LookData lookData, boolean deleteAfterUnpacking, boolean fromHiddenBackPack) {
		return null;
	}
}

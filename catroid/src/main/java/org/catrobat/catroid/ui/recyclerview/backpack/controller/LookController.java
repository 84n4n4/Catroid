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

package org.catrobat.catroid.ui.recyclerview.backpack.controller;

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.recyclerview.fragment.util.UniqueNameProvider;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.catrobat.catroid.common.Constants.IMAGE_DIRECTORY;
import static org.catrobat.catroid.utils.Utils.buildBackpackScenePath;
import static org.catrobat.catroid.utils.Utils.buildPath;
import static org.catrobat.catroid.utils.Utils.buildScenePath;

public class LookController {

	private static final String BACKPACK_DIRECTORY = buildPath(
			Constants.DEFAULT_ROOT,
			Constants.BACKPACK_DIRECTORY,
			Constants.BACKPACK_IMAGE_DIRECTORY);

	private UniqueNameProvider uniqueNameProvider = new UniqueNameProvider();

	public void pack(LookData lookToPack) throws IOException {
		String fileName = StorageHandler.copyFile(lookToPack.getAbsolutePath(), BACKPACK_DIRECTORY).getName();
		LookData look = new LookData(lookToPack.getLookName(), fileName);
		look.isBackpackLookData = true;

		BackPackListManager.getInstance().getBackPackedLooks().add(look);
		BackPackListManager.getInstance().saveBackpack();
	}

	LookData packForSprite(LookData lookToPack, Sprite dstSprite) throws IOException {
		for (LookData look : dstSprite.getLookDataList()) {
			if (look.getLookName().equals(lookToPack.getLookName())) {
				return look;
			}
		}

		String fileName = StorageHandler.copyFile(lookToPack.getAbsolutePath(), BACKPACK_DIRECTORY).getName();
		LookData look = new LookData(lookToPack.getLookName(), fileName);
		look.isBackpackLookData = true;

		dstSprite.getLookDataList().add(look);
		return look;
	}

	LookData packForScene(LookData lookToPack, Scene dstScene, Sprite dstSprite) throws IOException {
		for (LookData look : dstSprite.getLookDataList()) {
			if (look.getLookName().equals(lookToPack.getLookName())) {
				return look;
			}
		}

		String fileName = StorageHandler.copyFile(
				lookToPack.getAbsolutePath(),
				buildBackpackScenePath(dstScene.getName())).getName();

		LookData look = new LookData(lookToPack.getLookName(), fileName);
		dstSprite.getLookDataList().add(look);
		return look;
	}

	public void unpack(LookData lookToUnpack, Scene dstScene, Sprite dstSprite) throws IOException {
		String name = uniqueNameProvider.getUniqueName(
				lookToUnpack.getLookName(),
				getScope(dstSprite.getLookDataList()));

		String fileName = StorageHandler.copyFile(lookToUnpack.getAbsolutePath(), getDstPath(dstScene)).getName();

		dstSprite.getLookDataList().add(new LookData(name, fileName));
	}

	LookData unpackForSprite(LookData lookToUnpack, Scene dstScene, Sprite dstSprite) throws IOException {
		for (LookData look : dstSprite.getLookDataList()) {
			if (look.getLookName().equals(lookToUnpack.getLookName())) {
				return look;
			}
		}

		String fileName = StorageHandler.copyFile(lookToUnpack.getAbsolutePath(), getDstPath(dstScene)).getName();
		LookData look = new LookData(lookToUnpack.getLookName(), fileName);

		dstSprite.getLookDataList().add(look);
		return look;
	}

	LookData unpackForScene(LookData lookToUnpack, Scene srcScene, Scene dstScene, Sprite dstSprite) throws
			IOException {
		for (LookData look : dstSprite.getLookDataList()) {
			if (look.getLookName().equals(lookToUnpack.getLookName())) {
				return look;
			}
		}

		String pathInBackpack = buildPath(buildBackpackScenePath(srcScene.getName()), lookToUnpack.getLookFileName());
		String fileName = StorageHandler.copyFile(pathInBackpack, getDstPath(dstScene)).getName();
		LookData look = new LookData(lookToUnpack.getLookName(), fileName);

		dstSprite.getLookDataList().add(look);
		return look;
	}

	private String getDstPath(Scene dstScene) {
		return buildPath(buildScenePath(
				dstScene.getProject().getName(),
				dstScene.getName()),
				IMAGE_DIRECTORY);
	}

	private Set<String> getScope(List<LookData> items) {
		Set<String> scope = new HashSet<>();
		for (LookData item : items) {
			scope.add(item.getLookName());
		}
		return scope;
	}
}

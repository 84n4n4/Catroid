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
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.recyclerview.fragment.util.UniqueNameProvider;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.catrobat.catroid.common.Constants.SOUND_DIRECTORY;
import static org.catrobat.catroid.utils.Utils.buildBackpackScenePath;
import static org.catrobat.catroid.utils.Utils.buildPath;
import static org.catrobat.catroid.utils.Utils.buildScenePath;

public class SoundController {

	private static final String BACKPACK_DIRECTORY = buildPath(
			Constants.DEFAULT_ROOT,
			Constants.BACKPACK_DIRECTORY,
			Constants.BACKPACK_SOUND_DIRECTORY);

	private UniqueNameProvider uniqueNameProvider = new UniqueNameProvider();

	public void pack(SoundInfo soundToPack) throws IOException {
		String fileName = StorageHandler.copyFile(soundToPack.getAbsolutePath(), BACKPACK_DIRECTORY).getName();
		SoundInfo sound = new SoundInfo(soundToPack.getTitle(), fileName);
		sound.isBackpackSoundInfo = true;

		BackPackListManager.getInstance().getBackPackedSounds().add(sound);
		BackPackListManager.getInstance().saveBackpack();
	}

	SoundInfo packForSprite(SoundInfo soundToPack, Sprite dstSprite) throws IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (sound.getTitle().equals(soundToPack.getTitle())) {
				return sound;
			}
		}

		String fileName = StorageHandler.copyFile(soundToPack.getAbsolutePath(), BACKPACK_DIRECTORY).getName();
		SoundInfo sound = new SoundInfo(soundToPack.getTitle(), fileName);
		sound.isBackpackSoundInfo = true;

		dstSprite.getSoundList().add(sound);
		return sound;
	}

	SoundInfo packForScene(SoundInfo soundToPack, Scene dstScene, Sprite dstSprite) throws IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (sound.getTitle().equals(soundToPack.getTitle())) {
				return sound;
			}
		}

		String fileName = StorageHandler.copyFile(
				soundToPack.getAbsolutePath(),
				buildBackpackScenePath(dstScene.getName())).getName();

		SoundInfo sound = new SoundInfo(soundToPack.getTitle(), fileName);
		dstSprite.getSoundList().add(sound);
		return sound;
	}

	public void unpack(SoundInfo soundToUnpack, Scene dstScene, Sprite dstSprite) throws IOException {
		String name = uniqueNameProvider.getUniqueName(
				soundToUnpack.getTitle(),
				getScope(dstSprite.getSoundList()));

		String fileName = StorageHandler.copyFile(soundToUnpack.getAbsolutePath(), getDstPath(dstScene)).getName();

		dstSprite.getSoundList().add(new SoundInfo(name, fileName));
	}

	SoundInfo unpackForSprite(SoundInfo soundToUnpack, Scene dstScene, Sprite dstSprite) throws IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (sound.getTitle().equals(soundToUnpack.getTitle())) {
				return sound;
			}
		}

		String fileName = StorageHandler.copyFile(soundToUnpack.getAbsolutePath(), getDstPath(dstScene)).getName();
		SoundInfo sound = new SoundInfo(soundToUnpack.getTitle(), fileName);

		dstSprite.getSoundList().add(sound);
		return sound;
	}

	SoundInfo unpackForScene(SoundInfo soundToUnpack, Scene srcScene, Scene dstScene, Sprite dstSprite) throws
			IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (sound.getTitle().equals(soundToUnpack.getTitle())) {
				return sound;
			}
		}

		String pathInBackpack = buildPath(buildBackpackScenePath(srcScene.getName()), soundToUnpack.getSoundFileName());
		String fileName = StorageHandler.copyFile(pathInBackpack, getDstPath(dstScene)).getName();
		SoundInfo sound = new SoundInfo(soundToUnpack.getTitle(), fileName);

		dstSprite.getSoundList().add(sound);
		return sound;
	}

	private String getDstPath(Scene dstScene) {
		return buildPath(buildScenePath(
				dstScene.getProject().getName(),
				dstScene.getName()),
				SOUND_DIRECTORY);
	}

	private Set<String> getScope(List<SoundInfo> items) {
		Set<String> scope = new HashSet<>();
		for (SoundInfo item : items) {
			scope.add(item.getTitle());
		}
		return scope;
	}
}

/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
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

package org.catrobat.catroid.ui.recyclerview.controller;

import android.content.Context;

import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.io.StorageOperations;
import org.catrobat.catroid.ui.controller.BackpackListManager;
import org.catrobat.catroid.ui.recyclerview.util.UniqueNameProvider;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.catrobat.catroid.common.Constants.BACKPACK_SOUND_DIRECTORY;
import static org.catrobat.catroid.common.Constants.SOUND_DIRECTORY_NAME;

public class SoundController {

	private UniqueNameProvider uniqueNameProvider = new UniqueNameProvider();

	public SoundInfo copy(Context context, SoundInfo soundToCopy, Scene dstScene, Sprite dstSprite) throws IOException {
		String name = uniqueNameProvider.getUniqueName(soundToCopy.getName(), getScope(dstSprite.getSoundList()));

		File dstDir = getSoundDir(context, dstScene);
		File file = StorageOperations.copyFileToDir(soundToCopy.getFile(), dstDir);

		return new SoundInfo(name, file);
	}

	SoundInfo findOrCopy(Context context, SoundInfo soundToCopy, Scene dstScene, Sprite dstSprite) throws IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (compareByChecksum(sound.getFile(), soundToCopy.getFile())) {
				return sound;
			}
		}
		SoundInfo copiedSound = copy(context, soundToCopy, dstScene, dstSprite);
		dstSprite.getSoundList().add(copiedSound);
		return copiedSound;
	}

	public void delete(SoundInfo soundToDelete) throws IOException {
		StorageOperations.deleteFile(soundToDelete.getFile());
	}

	public SoundInfo pack(Context context, SoundInfo soundToPack) throws IOException {
		String name = uniqueNameProvider.getUniqueName(soundToPack.getName(),
				getScope(BackpackListManager.getInstance().getBackpackedSounds()));

		File file = StorageOperations.copyFileToDir(soundToPack.getFile(), new File(context.getFilesDir(), BACKPACK_SOUND_DIRECTORY));

		return new SoundInfo(name, file);
	}

	SoundInfo packForSprite(Context context, SoundInfo soundToPack, Sprite dstSprite) throws IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (compareByChecksum(sound.getFile(), soundToPack.getFile())) {
				return sound;
			}
		}

		File file = StorageOperations.copyFileToDir(soundToPack.getFile(), new File(context.getFilesDir(), BACKPACK_SOUND_DIRECTORY));
		SoundInfo sound = new SoundInfo(soundToPack.getName(), file);
		dstSprite.getSoundList().add(sound);

		return sound;
	}

	public SoundInfo unpack(Context context, SoundInfo soundToUnpack, Scene dstScene, Sprite dstSprite) throws IOException {
		String name = uniqueNameProvider.getUniqueName(soundToUnpack.getName(), getScope(dstSprite.getSoundList()));
		File file = StorageOperations.copyFileToDir(soundToUnpack.getFile(), getSoundDir(context, dstScene));
		return new SoundInfo(name, file);
	}

	SoundInfo unpackForSprite(Context context, SoundInfo soundToUnpack, Scene dstScene, Sprite dstSprite) throws IOException {
		for (SoundInfo sound : dstSprite.getSoundList()) {
			if (compareByChecksum(sound.getFile(), soundToUnpack.getFile())) {
				return sound;
			}
		}

		SoundInfo soundInfo = unpack(context, soundToUnpack, dstScene, dstSprite);
		dstSprite.getSoundList().add(soundInfo);
		return soundInfo;
	}

	private List<String> getScope(List<SoundInfo> items) {
		List<String> scope = new ArrayList<>();
		for (SoundInfo item : items) {
			scope.add(item.getName());
		}
		return scope;
	}

	private File getSoundDir(Context context, Scene scene) {
		return new File(scene.getDirectory(context), SOUND_DIRECTORY_NAME);
	}

	private boolean compareByChecksum(File file1, File file2) {
		String checksum1 = Utils.md5Checksum(file1);
		String checksum2 = Utils.md5Checksum(file2);

		return checksum1.equals(checksum2);
	}
}

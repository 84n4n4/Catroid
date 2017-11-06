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

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.PlaySoundAndWaitBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.ScriptBrick;
import org.catrobat.catroid.content.bricks.SetLookBrick;
import org.catrobat.catroid.content.bricks.WhenBackgroundChangesBrick;

import java.io.IOException;

public class ScriptController {

	public static final String TAG = ScriptController.class.getSimpleName();

	private LookController lookController = new LookController();
	private SoundController soundController = new SoundController();

	public void pack(Script scriptToPack) {
		//TODO: packForSprite
	}

	void packForSprite(Script scriptToPack, Sprite srcSprite, Sprite dstSprite) throws IOException {
		Script script = scriptToPack.copyScriptForSprite(srcSprite);

		for (Brick brick : script.getBrickList()) {
			if (brick instanceof SetLookBrick) {
				((SetLookBrick) brick).setLook(lookController
						.packForSprite(((SetLookBrick) brick).getLook(), dstSprite));
			}

			if (brick instanceof WhenBackgroundChangesBrick) {
				((WhenBackgroundChangesBrick) brick).setLook(lookController
						.packForSprite(((WhenBackgroundChangesBrick) brick).getLook(), dstSprite));
			}

			if (brick instanceof PlaySoundBrick) {
				((PlaySoundBrick) brick).setSoundInfo(soundController
						.packForSprite(((PlaySoundBrick) brick).getSound(), dstSprite));
			}

			if (brick instanceof PlaySoundAndWaitBrick) {
				((PlaySoundAndWaitBrick) brick).setSoundInfo(soundController
						.packForSprite(((PlaySoundAndWaitBrick) brick).getSound(), dstSprite));
			}
		}

		dstSprite.getScriptList().add(script);
	}

	void packForScene(Script scriptToPack, Sprite srcSprite, Scene dstScene, Sprite dstSprite) throws IOException {
		Script script = scriptToPack.copyScriptForSprite(srcSprite);
		try {
			script.setBrick((ScriptBrick) scriptToPack.getScriptBrick().clone());
		} catch (CloneNotSupportedException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}

		for (Brick brick : script.getBrickList()) {
			if (brick instanceof SetLookBrick) {
				((SetLookBrick) brick).setLook(lookController
						.packForScene(((SetLookBrick) brick).getLook(), dstScene, dstSprite));
			}

			if (brick instanceof WhenBackgroundChangesBrick) {
				((WhenBackgroundChangesBrick) brick).setLook(lookController
						.packForScene(((WhenBackgroundChangesBrick) brick).getLook(), dstScene, dstSprite));
			}

			if (brick instanceof PlaySoundBrick) {
				((PlaySoundBrick) brick).setSoundInfo(soundController
						.packForScene(((PlaySoundBrick) brick).getSound(), dstScene, dstSprite));
			}

			if (brick instanceof PlaySoundAndWaitBrick) {
				((PlaySoundAndWaitBrick) brick).setSoundInfo(soundController
						.packForScene(((PlaySoundAndWaitBrick) brick).getSound(), dstScene, dstSprite));
			}
		}

		dstSprite.getScriptList().add(script);
	}

	public void unpack(Script scriptToUnpack, Sprite scrSprite, Scene dstScene, Sprite dstSprite) {
		//TODO: unpack.
	}

	void unpackForSprite(Script scriptToUnpack, Sprite scrSprite, Scene dstScene, Sprite dstSprite) throws IOException {
		Script script = scriptToUnpack.copyScriptForSprite(scrSprite);

		for (Brick brick : script.getBrickList()) {
			if (ProjectManager.getInstance().getCurrentProject().isCastProject()
					&& CastManager.unsupportedBricks.contains(brick.getClass())) {
				Log.e(TAG, "CANNOT insert bricks into ChromeCast project");
				return;
			}

			if (brick instanceof SetLookBrick) {
				((SetLookBrick) brick)
						.setLook(lookController
						.unpackForSprite(((SetLookBrick) brick).getLook(),
								dstScene,
								dstSprite));
			}

			if (brick instanceof WhenBackgroundChangesBrick) {
				((WhenBackgroundChangesBrick) brick)
						.setLook(lookController
						.unpackForSprite(((WhenBackgroundChangesBrick) brick).getLook(),
								dstScene,
								dstSprite));
			}

			if (brick instanceof PlaySoundBrick) {
				((PlaySoundBrick) brick)
						.setSoundInfo(soundController
						.unpackForSprite(((PlaySoundBrick) brick).getSound(),
								dstScene,
								dstSprite));
			}

			if (brick instanceof PlaySoundAndWaitBrick) {
				((PlaySoundAndWaitBrick) brick)
						.setSoundInfo(soundController
						.unpackForSprite(((PlaySoundAndWaitBrick) brick).getSound(),
								dstScene,
								dstSprite));
			}
		}

		dstSprite.getScriptList().add(script);
	}

	void unpackForScene(Script scriptToUnpack, Scene srcScene, Sprite scrSprite, Scene dstScene, Sprite dstSprite)
			throws IOException {
		Script script = scriptToUnpack.copyScriptForSprite(scrSprite);
		try {
			script.setBrick((ScriptBrick) scriptToUnpack.getScriptBrick().clone());
		} catch (CloneNotSupportedException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}

		for (Brick brick : script.getBrickList()) {
			if (ProjectManager.getInstance().getCurrentProject().isCastProject()
					&& CastManager.unsupportedBricks.contains(brick.getClass())) {
				Log.e(TAG, "CANNOT insert bricks into ChromeCast project");
				return;
			}

			if (brick instanceof SetLookBrick) {
				((SetLookBrick) brick)
						.setLook(lookController
								.unpackForScene(((SetLookBrick) brick).getLook(),
										srcScene,
										dstScene,
										dstSprite));
			}

			if (brick instanceof WhenBackgroundChangesBrick) {
				((WhenBackgroundChangesBrick) brick)
						.setLook(lookController
								.unpackForScene(((WhenBackgroundChangesBrick) brick).getLook(),
										srcScene,
										dstScene,
										dstSprite));
			}

			if (brick instanceof PlaySoundBrick) {
				((PlaySoundBrick) brick)
						.setSoundInfo(soundController
								.unpackForScene(((PlaySoundBrick) brick).getSound(),
										srcScene,
										dstScene,
										dstSprite));
			}

			if (brick instanceof PlaySoundAndWaitBrick) {
				((PlaySoundAndWaitBrick) brick)
						.setSoundInfo(soundController
								.unpackForScene(((PlaySoundAndWaitBrick) brick).getSound(),
										srcScene,
										dstScene,
										dstSprite));
			}
		}

		dstSprite.getScriptList().add(script);
	}
}

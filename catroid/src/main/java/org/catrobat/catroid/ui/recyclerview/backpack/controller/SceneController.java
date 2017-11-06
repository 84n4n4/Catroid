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
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.physics.PhysicsWorld;
import org.catrobat.catroid.ui.controller.BackPackListManager;
import org.catrobat.catroid.ui.recyclerview.fragment.util.UniqueNameProvider;
import org.catrobat.catroid.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SceneController {

	private UniqueNameProvider uniqueNameProvider = new UniqueNameProvider();
	private SpriteController spriteController = new SpriteController();

	public void pack(Scene sceneToPack) throws IOException {
		String name = uniqueNameProvider.getUniqueName(
				sceneToPack.getName(),
				getScope(BackPackListManager.getInstance().getAllBackpackedScenes()));

		File dir = new File(Utils.buildBackpackScenePath(name));
		File imgDir = new File(Utils.buildPath(Utils.buildBackpackScenePath(name), Constants.IMAGE_DIRECTORY));
		File sndDir = new File(Utils.buildPath(Utils.buildBackpackScenePath(name), Constants.SOUND_DIRECTORY));

		dir.mkdir();
		imgDir.mkdir();
		sndDir.mkdir();

		if (!dir.isDirectory() || !imgDir.isDirectory() || !sndDir.isDirectory()) {
			return;
		}

		Scene scene = new Scene();
		scene.setSceneName(name);

		scene.setProject(null);
		scene.isBackPackScene = true;

		for (Sprite sprite : sceneToPack.getSpriteList()) {
			spriteController.packForScene(sprite, scene);
		}

		BackPackListManager.getInstance().getBackPackedScenes().add(scene);
		BackPackListManager.getInstance().saveBackpack();
	}

	public void unpack(Scene sceneToUnpack, Project dstProject) throws IOException {
		String name = uniqueNameProvider.getUniqueName(
				sceneToUnpack.getName(),
				getScope(dstProject.getSceneList()));

		File dir = new File(Utils.buildScenePath(dstProject.getName(), name));
		File imgDir = new File(Utils.buildPath(
				Utils.buildScenePath(dstProject.getName(), name),
				Constants.IMAGE_DIRECTORY));
		File sndDir = new File(Utils.buildPath(
				Utils.buildScenePath(dstProject.getName(), name),
				Constants.SOUND_DIRECTORY));

		dir.mkdir();
		imgDir.mkdir();
		sndDir.mkdir();

		if (!dir.isDirectory() || !imgDir.isDirectory() || !sndDir.isDirectory()) {
			return;
		}

		Scene scene = new Scene();
		scene.setSceneName(name);

		scene.setProject(dstProject);

		scene.setPhysicsWorld(new PhysicsWorld());
		scene.setDataContainer(new DataContainer(dstProject));

		for (Sprite sprite : sceneToUnpack.getSpriteList()) {
			spriteController.unpackForScene(sprite, sceneToUnpack,scene);
		}

		scene.mergeProjectVariables();
		scene.correctUserVariableAndListReferences();

		dstProject.addScene(scene);
	}

	public void copy(Scene sceneToCopy, Project dstProject) throws IOException {
		String name = uniqueNameProvider.getUniqueName(
				sceneToCopy.getName(),
				getScope(dstProject.getSceneList()));

		StorageHandler.copyDirectory(
				Utils.buildScenePath(sceneToCopy.getProject().getName(), sceneToCopy.getName()),
				Utils.buildScenePath(dstProject.getName(), name));

		Scene scene = sceneToCopy.clone();
		scene.setSceneName(name);
		scene.setProject(dstProject);

		dstProject.addScene(scene);
	}

	private Set<String> getScope(List<Scene> items) {
		Set<String> scope = new HashSet<>();
		for (Scene item : items) {
			scope.add(item.getName());
		}
		return scope;
	}
}

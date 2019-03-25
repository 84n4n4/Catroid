/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2019 The Catrobat Team
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

package org.catrobat.catroid.formulaeditor;

import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;

public final class UserDataWrapper {

	public static UserVariable getUserVariable(String name, Sprite sprite, Project project) {
		UserVariable userVariable = null;
		if (sprite != null) {
			userVariable = sprite.getUserVariable(name);
		}
		if (project != null && userVariable == null) {
			return project.getUserVariable(name);
		}
		return userVariable;
	}

	public static UserList getUserList(String name, Sprite sprite, Project project) {
		UserList userList = null;
		if (sprite != null) {
			userList = sprite.getUserList(name);
		}
		if (project != null && userList == null) {
			return project.getUserList(name);
		}
		return userList;
	}

	public static void resetAllUserData(Project project) {
		project.resetUserData();
		for (Scene scene : project.getSceneList()) {
			for (Sprite sprite : scene.getSpriteList()) {
				sprite.resetUserData();
			}
		}
	}

	private UserDataWrapper() {
		throw new AssertionError("No.");
	}
}

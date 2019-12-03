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

package org.catrobat.catroid.test.robolectric.bricks;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Spinner;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.CameraBrick;
import org.catrobat.catroid.content.bricks.ChooseCameraBrick;
import org.catrobat.catroid.content.bricks.FlashBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.StopScriptBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.ui.fragment.CategoryBricksFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class BrickStringSpinnerDefaultValueTest {

	private CategoryBricksFactory categoryBricksFactory;
	private Sprite sprite;
	private Activity activity;

	@ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"StopScriptBrick - R.id.brick_stop_script_spinner", "Control", StopScriptBrick.class, R.id.brick_stop_script_spinner, "this script"},
				{"CameraBrick - R.id.brick_video_spinner", "Looks", CameraBrick.class, R.id.brick_video_spinner, "on"},
				{"ChooseCameraBrick - R.id.brick_choose_camera_spinner", "Looks", ChooseCameraBrick.class, R.id.brick_choose_camera_spinner, "front"},
				{"FlashBrick - R.id.brick_flash_spinner", "Looks", FlashBrick.class, R.id.brick_flash_spinner, "on"}
		});
	}

	@SuppressWarnings("PMD.UnusedPrivateField")
	public String name;

	public String category;

	public Class brickClazz;

	public int spinnerId;

	public String expected;

	public BrickStringSpinnerDefaultValueTest(String name, String category, Class brickClazz, int formulaTextFieldId, String expected) {
		this.name = name;
		this.category = category;
		this.brickClazz = brickClazz;
		this.spinnerId = formulaTextFieldId;
		this.expected = expected;
	}

	@Before
	public void setUp() throws Exception {
		ActivityController<SpriteActivity> activityController = Robolectric.buildActivity(SpriteActivity.class);
		activity = activityController.get();
		createProject(activity);
		activityController.create().resume();
		categoryBricksFactory = new CategoryBricksFactory();
	}

	public void createProject(Context context) {
		Project project = new Project(context, getClass().getSimpleName());
		sprite = new Sprite("testSprite");
		Script script = new StartScript();
		script.addBrick(new SetXBrick());
		sprite.addScript(script);
		project.getDefaultScene().addSprite(sprite);
		ProjectManager.getInstance().setCurrentProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentlyEditedScene(project.getDefaultScene());
	}

	private Brick getBrickFromCategroyBricksFactory() {
		List<Brick> categoryBricks = categoryBricksFactory.getBricks(category, false, activity);

		Brick brickInAdapter = null;
		for (Brick brick : categoryBricks) {
			if (brickClazz.isInstance(brick)) {
				brickInAdapter = brick;
				break;
			}
		}
		assertNotNull(brickInAdapter);
		return brickInAdapter;
	}

	@Test
	public void testDefaultSpinnerSelection() {
		Brick brick = getBrickFromCategroyBricksFactory();
		View brickView = brick.getView(activity);
		assertNotNull(brickView);

		Spinner brickSpinner = (Spinner) brickView.findViewById(spinnerId);
		assertNotNull(brickSpinner);

		assertEquals(expected, (String) brickSpinner.getSelectedItem());
	}
}

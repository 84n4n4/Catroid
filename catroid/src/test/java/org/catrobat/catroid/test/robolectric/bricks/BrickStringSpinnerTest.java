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
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
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
import org.catrobat.catroid.content.bricks.StopScriptBrick;
import org.catrobat.catroid.physics.content.bricks.SetPhysicsObjectTypeBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.ui.recyclerview.fragment.ScriptFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ParameterizedRobolectricTestRunner.class)
public class BrickStringSpinnerTest {

	private SpriteActivity activity;

	Spinner brickSpinner;

	@ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{StopScriptBrick.class.getSimpleName(), new StopScriptBrick(), R.id.brick_stop_script_spinner, "this script", Arrays.asList("this script", "all scripts", "other scripts of this actor or object")},
				{SetPhysicsObjectTypeBrick.class.getSimpleName(), new SetPhysicsObjectTypeBrick(), R.id.brick_set_physics_object_type_spinner, "not moving or bouncing under gravity (default)", Arrays.asList("moving and bouncing under gravity", "not moving under gravity, but others bounce off you under gravity", "not moving or bouncing under gravity (default)")},
				{CameraBrick.class.getSimpleName(), new CameraBrick(), R.id.brick_video_spinner, "on", Arrays.asList("off", "on")},
				{ChooseCameraBrick.class.getSimpleName(), new ChooseCameraBrick(), R.id.brick_choose_camera_spinner, "front", Arrays.asList("rear", "front")},
				{FlashBrick.class.getSimpleName(), new FlashBrick(), R.id.brick_flash_spinner, "on", Arrays.asList("off", "on")},
		});
	}

	@SuppressWarnings("PMD.UnusedPrivateField")
	private String name;

	private Brick brick;

	private @IdRes int spinnerId;

	private String expectedSelection;

	private List<String> expectedContent;

	public BrickStringSpinnerTest(String name, Brick brick, @IdRes int spinnerId, String expectedSelection, List<String> expectedContent) {
		this.name = name;
		this.brick = brick;
		this.spinnerId = spinnerId;
		this.expectedSelection = expectedSelection;
		this.expectedContent = expectedContent;
	}

	@Before
	public void setUp() throws Exception {
		ActivityController<SpriteActivity> activityController = Robolectric.buildActivity(SpriteActivity.class);
		activity = activityController.get();
		createProject(activity);
		activityController.create().resume();

		Fragment scriptFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		assertNotNull(scriptFragment);
		assertThat(scriptFragment, is(instanceOf(ScriptFragment.class)));

		View brickView = brick.getView(activity);
		assertNotNull(brickView);

		brickSpinner = (Spinner) brickView.findViewById(spinnerId);
		assertNotNull(brickSpinner);
	}

	@Test
	public void spinnerDefaultSelectionTest() {
		assertEquals(expectedSelection, (String) brickSpinner.getSelectedItem());
	}

	@Test
	public void spinnerContentTest() {
		List<String> spinnerContent = new ArrayList<>();
		for (int index = 0; index < brickSpinner.getAdapter().getCount(); index++) {
			spinnerContent.add((String) brickSpinner.getAdapter().getItem(index));
		}
		assertEquals(expectedContent, spinnerContent);
	}

	public void createProject(Activity activity) {
		Project project = new Project(activity, getClass().getSimpleName());
		Sprite sprite = new Sprite("testSprite");
		Script script = new StartScript();
		script.addBrick(brick);
		sprite.addScript(script);
		project.getDefaultScene().addSprite(sprite);
		ProjectManager.getInstance().setCurrentProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentlyEditedScene(project.getDefaultScene());
	}
}

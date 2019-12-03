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

package org.catrobat.catroid.test.content.bricks;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.AskBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeColorByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoNStepsBackBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NoteBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.RepeatUntilBrick;
import org.catrobat.catroid.content.bricks.SayBubbleBrick;
import org.catrobat.catroid.content.bricks.SayForBubbleBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetColorBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.ThinkBubbleBrick;
import org.catrobat.catroid.content.bricks.ThinkForBubbleBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.VibrationBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WaitUntilBrick;
import org.catrobat.catroid.content.bricks.WhenConditionBrick;
import org.catrobat.catroid.physics.content.bricks.SetBounceBrick;
import org.catrobat.catroid.physics.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.content.bricks.SetMassBrick;
import org.catrobat.catroid.ui.fragment.CategoryBricksFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(Parameterized.class)
public class BrickFormulaDefaultValueTest {

	@Parameterized.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{"SetXBrick - R.id.brick_set_x_edit_text", "Motion", SetXBrick.class, R.id.brick_set_x_edit_text, "100 "},
				{"SetYBrick - R.id.brick_set_y_edit_text", "Motion", SetYBrick.class, R.id.brick_set_y_edit_text, "200 "},
				{"ChangeXByNBrick - R.id.brick_change_x_edit_text", "Motion", ChangeXByNBrick.class, R.id.brick_change_x_edit_text, "10 "},
				{"ChangeYByNBrick - R.id.brick_change_y_edit_text", "Motion", ChangeYByNBrick.class, R.id.brick_change_y_edit_text, "10 "},
				{"MoveNStepsBrick - R.id.brick_move_n_steps_edit_text", "Motion", MoveNStepsBrick.class, R.id.brick_move_n_steps_edit_text, "10 "},
				{"TurnLeftBrick - R.id.brick_turn_left_edit_text", "Motion", TurnLeftBrick.class, R.id.brick_turn_left_edit_text, "15 "},
				{"TurnRightBrick - R.id.brick_turn_right_edit_text", "Motion", TurnRightBrick.class, R.id.brick_turn_right_edit_text, "15 "},
				{"PointInDirectionBrick - R.id.brick_point_in_direction_edit_text", "Motion", PointInDirectionBrick.class, R.id.brick_point_in_direction_edit_text, "90 "},
				{"GlideToBrick - R.id.brick_glide_to_edit_text_x", "Motion", GlideToBrick.class, R.id.brick_glide_to_edit_text_x, "100 "},
				{"GlideToBrick - R.id.brick_glide_to_edit_text_y", "Motion", GlideToBrick.class, R.id.brick_glide_to_edit_text_y, "200 "},
				{"GlideToBrick - R.id.brick_glide_to_edit_text_duration", "Motion", GlideToBrick.class, R.id.brick_glide_to_edit_text_duration, "1 "},
				{"GoNStepsBackBrick - R.id.brick_go_back_edit_text", "Motion", GoNStepsBackBrick.class, R.id.brick_go_back_edit_text, "1 "},
				{"VibrationBrick - R.id.brick_vibration_edit_text", "Motion", VibrationBrick.class, R.id.brick_vibration_edit_text, "1 "},
				{"TurnLeftBrick - R.id.brick_turn_left_edit_text", "Motion", TurnLeftBrick.class, R.id.brick_turn_left_edit_text, "15 "},
				{"TurnRightBrick - R.id.brick_turn_right_edit_text", "Motion", TurnRightBrick.class, R.id.brick_turn_right_edit_text, "15 "},
				{"SetGravityBrick - R.id.brick_set_gravity_edit_text_x", "Motion", SetGravityBrick.class, R.id.brick_set_gravity_edit_text_x, "0 "},
				{"SetGravityBrick, R.id.brick_set_gravity_edit_text_y", "Motion", SetGravityBrick.class, R.id.brick_set_gravity_edit_text_y, "- 10 "},
				{"SetMassBrick - R.id.brick_set_mass_edit_text", "Motion", SetMassBrick.class, R.id.brick_set_mass_edit_text, "1 "},
				{"SetBounceBrick - R.id.brick_set_bounce_factor_edit_text", "Motion", SetBounceBrick.class, R.id.brick_set_bounce_factor_edit_text, "80 "},
				{"SetFrictionBrick - R.id.brick_set_friction_edit_text", "Motion", SetFrictionBrick.class, R.id.brick_set_friction_edit_text, "20 "},
				{"SetSizeToBrick - R.id.brick_set_size_to_edit_text", "Looks", SetSizeToBrick.class, R.id.brick_set_size_to_edit_text, "60 "},
				{"ChangeSizeByNBrick - R.id.brick_change_size_by_edit_text", "Looks", ChangeSizeByNBrick.class, R.id.brick_change_size_by_edit_text, "10 "},
				{"AskBrick - R.id.brick_ask_question_edit_text", "Looks", AskBrick.class, R.id.brick_ask_question_edit_text, "'What's your name?' "},
				{"SayBubbleBrick - R.id.brick_bubble_edit_text", "Looks", SayBubbleBrick.class, R.id.brick_bubble_edit_text, "'Hello!' "},
				{"SayForBubbleBrick - R.id.brick_for_bubble_edit_text_text", "Looks", SayForBubbleBrick.class, R.id.brick_for_bubble_edit_text_text, "'Hello!' "},
				{"ThinkBubbleBrick - R.id.brick_bubble_edit_text", "Looks", ThinkBubbleBrick.class, R.id.brick_bubble_edit_text, "'Hello!' "},
				{"ThinkForBubbleBrick - R.id.brick_for_bubble_edit_text_text", "Looks", ThinkForBubbleBrick.class, R.id.brick_for_bubble_edit_text_text, "'Hello!' "},
				{"SetTransparencyBrick - R.id.brick_set_transparency_to_edit_text", "Looks", SetTransparencyBrick.class, R.id.brick_set_transparency_to_edit_text, "50 "},
				{"ChangeTransparencyByNBrick - R.id.brick_change_transparency_edit_text", "Looks", ChangeTransparencyByNBrick.class, R.id.brick_change_transparency_edit_text, "25 "},
				{"SetBrightnessBrick - R.id.brick_set_brightness_edit_text", "Looks", SetBrightnessBrick.class, R.id.brick_set_brightness_edit_text, "50 "},
				{"ChangeBrightnessByNBrick - R.id.brick_change_brightness_edit_text", "Looks", ChangeBrightnessByNBrick.class, R.id.brick_change_brightness_edit_text, "25 "},
				{"SetColorBrick - R.id.brick_set_color_edit_text", "Looks", SetColorBrick.class, R.id.brick_set_color_edit_text, "0 "},
				{"ChangeColorByNBrick - R.id.brick_change_color_by_edit_text", "Looks", ChangeColorByNBrick.class, R.id.brick_change_color_by_edit_text, "25 "},
				{"SetVariableBrick - R.id.brick_set_variable_edit_text", "Data", SetVariableBrick.class, R.id.brick_set_variable_edit_text, "1 "},
				{"ChangeVariableBrick - R.id.brick_change_variable_edit_text", "Data", ChangeVariableBrick.class, R.id.brick_change_variable_edit_text, "1 "},
				{"ShowTextBrick - R.id.brick_show_variable_edit_text_x", "Data", ShowTextBrick.class, R.id.brick_show_variable_edit_text_x, "100 "},
				{"ShowTextBrick - R.id.brick_show_variable_edit_text_y", "Data", ShowTextBrick.class, R.id.brick_show_variable_edit_text_y, "200 "},
				{"AddItemToUserListBrick - R.id.brick_add_item_to_userlist_edit_text", "Data", AddItemToUserListBrick.class, R.id.brick_add_item_to_userlist_edit_text, "1 "},
				{"DeleteItemOfUserListBrick - R.id.brick_delete_item_of_userlist_edit_text", "Data", DeleteItemOfUserListBrick.class, R.id.brick_delete_item_of_userlist_edit_text, "1 "},
				{"WhenConditionBrick - R.id.brick_when_condition_edit_text", "Event", WhenConditionBrick.class, R.id.brick_when_condition_edit_text, "1 < 2 "},
				{"IfLogicBeginBrick - R.id.brick_if_begin_edit_text", "Control", IfLogicBeginBrick.class, R.id.brick_if_begin_edit_text, "1 < 2 "},
				{"IfThenLogicBeginBrick - R.id.brick_if_begin_edit_text", "Control", IfThenLogicBeginBrick.class, R.id.brick_if_begin_edit_text, "1 < 2 "},
				{"WaitBrick - R.id.brick_wait_edit_text", "Control", WaitBrick.class, R.id.brick_wait_edit_text, "1 "},
				{"NoteBrick - R.id.brick_wait_edit_text", "Control", NoteBrick.class, R.id.brick_note_edit_text, "'add comment here…' "},
				{"WaitUntilBrick - R.id.brick_wait_until_edit_text", "Control", WaitUntilBrick.class, R.id.brick_wait_until_edit_text, "1 < 2 "},
				{"RepeatUntilBrick - R.id.brick_repeat_until_edit_text", "Control", RepeatUntilBrick.class, R.id.brick_repeat_until_edit_text, "1 < 2 "},
				{"RepeatUntilBrick - R.id.brick_repeat_edit_text", "Control", RepeatBrick.class, R.id.brick_repeat_edit_text, "10 "},
		});
	}

	@Parameterized.Parameter
	public String name;

	@Parameterized.Parameter(1)
	public String category;

	@Parameterized.Parameter(2)
	public Class brickClazz;

	@Parameterized.Parameter(3)
	public int formulaTextFieldId;

	@Parameterized.Parameter(4)
	public String expected;

	private CategoryBricksFactory categoryBricksFactory;
	private Sprite sprite;

	@Before
	public void setUp() throws Exception {
		createProject(InstrumentationRegistry.getTargetContext());

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

	@Test
	public void testBrickCategory() {
		List<Brick> categoryBricks = categoryBricksFactory.getBricks(category, false,
				InstrumentationRegistry.getTargetContext());

		Brick brickInAdapter = null;
		for (Brick brick : categoryBricks) {
			if (brickClazz.isInstance(brick)) {
				brickInAdapter = brick;
				break;
			}
		}
		assertNotNull(brickInAdapter);

		Brick.BrickField brickField = ((FormulaBrick) brickInAdapter).brickFieldToTextViewIdMap.inverse().get(formulaTextFieldId);
		String actual = ((FormulaBrick) brickInAdapter).getFormulaWithBrickField(brickField).getTrimmedFormulaString(InstrumentationRegistry.getTargetContext());
		assertEquals(expected, actual);
	}
}

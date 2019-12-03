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
import android.widget.TextView;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.AskBrick;
import org.catrobat.catroid.content.bricks.AskSpeechBrick;
import org.catrobat.catroid.content.bricks.AssertEqualsBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeColorByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeVolumeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoNStepsBackBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.InsertItemIntoUserListBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NoteBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SayBubbleBrick;
import org.catrobat.catroid.content.bricks.SayForBubbleBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundByIndexAndWaitBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundByIndexBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetColorBrick;
import org.catrobat.catroid.content.bricks.SetLookByIndexBrick;
import org.catrobat.catroid.content.bricks.SetPenSizeBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetVolumeToBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.ShowTextColorSizeAlignmentBrick;
import org.catrobat.catroid.content.bricks.SpeakAndWaitBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.TapAtBrick;
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
import org.catrobat.catroid.physics.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.physics.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.content.bricks.TurnRightSpeedBrick;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.ui.recyclerview.fragment.ScriptFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.ParameterizedRobolectricTestRunner;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ParameterizedRobolectricTestRunner.class)
@Config(sdk = {25})
public class BrickSingleFormulaFieldTest {

	private SpriteActivity activity;

	@ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				//Bricks with single formula fields
				{WhenConditionBrick.class.getSimpleName(), new WhenConditionBrick(), R.id.brick_when_condition_edit_text},
				{SetXBrick.class.getSimpleName(), new SetXBrick(), R.id.brick_set_x_edit_text},
				{SetYBrick.class.getSimpleName(), new SetYBrick(), R.id.brick_set_y_edit_text},
				{WaitBrick.class.getSimpleName(), new WaitBrick(), R.id.brick_wait_edit_text},
				{NoteBrick.class.getSimpleName(), new NoteBrick(), R.id.brick_note_edit_text},
				{WaitUntilBrick.class.getSimpleName(), new WaitUntilBrick(), R.id.brick_wait_until_edit_text},
				{ChangeXByNBrick.class.getSimpleName(), new ChangeXByNBrick(), R.id.brick_change_x_edit_text},
				{ChangeYByNBrick.class.getSimpleName(), new ChangeYByNBrick(), R.id.brick_change_y_edit_text},
				{MoveNStepsBrick.class.getSimpleName(), new MoveNStepsBrick(), R.id.brick_move_n_steps_edit_text},
				{TurnLeftBrick.class.getSimpleName(), new TurnLeftBrick(), R.id.brick_turn_left_edit_text},
				{TurnRightBrick.class.getSimpleName(), new TurnRightBrick(), R.id.brick_turn_right_edit_text},
				{PointInDirectionBrick.class.getSimpleName(), new PointInDirectionBrick(), R.id.brick_point_in_direction_edit_text},
				{GoNStepsBackBrick.class.getSimpleName(), new GoNStepsBackBrick(), R.id.brick_go_back_edit_text},
				{VibrationBrick.class.getSimpleName(), new VibrationBrick(), R.id.brick_vibration_edit_text},
				{TurnLeftSpeedBrick.class.getSimpleName(), new TurnLeftSpeedBrick(), R.id.brick_turn_left_speed_edit_text},
				{TurnRightSpeedBrick.class.getSimpleName(), new TurnRightSpeedBrick(), R.id.brick_turn_right_speed_edit_text},
				{SetMassBrick.class.getSimpleName(), new SetMassBrick(), R.id.brick_set_mass_edit_text},
				{SetBounceBrick.class.getSimpleName(), new SetBounceBrick(), R.id.brick_set_bounce_factor_edit_text},
				{SetFrictionBrick.class.getSimpleName(), new SetFrictionBrick(), R.id.brick_set_friction_edit_text},
				{SetVolumeToBrick.class.getSimpleName(), new SetVolumeToBrick(), R.id.brick_set_volume_to_edit_text},
				{ChangeVolumeByNBrick.class.getSimpleName(), new ChangeVolumeByNBrick(), R.id.brick_change_volume_by_edit_text},
				{SpeakBrick.class.getSimpleName(), new SpeakBrick(), R.id.brick_speak_edit_text},
				{SpeakAndWaitBrick.class.getSimpleName(), new SpeakAndWaitBrick(), R.id.brick_speak_and_wait_edit_text},
				{AskBrick.class.getSimpleName(), new AskBrick(), R.id.brick_ask_question_edit_text},
				{SetLookByIndexBrick.class.getSimpleName(), new SetLookByIndexBrick(), R.id.brick_set_look_by_index_edit_text},
				{SetSizeToBrick.class.getSimpleName(), new SetSizeToBrick(), R.id.brick_set_size_to_edit_text},
				{ChangeSizeByNBrick.class.getSimpleName(), new ChangeSizeByNBrick(), R.id.brick_change_size_by_edit_text},
				{AskSpeechBrick.class.getSimpleName(), new AskSpeechBrick(), R.id.brick_ask_speech_question_edit_text},
				{SayBubbleBrick.class.getSimpleName(), new SayBubbleBrick(), R.id.brick_bubble_edit_text},
				{ThinkBubbleBrick.class.getSimpleName(), new ThinkBubbleBrick(), R.id.brick_bubble_edit_text},
				{SetTransparencyBrick.class.getSimpleName(), new SetTransparencyBrick(), R.id.brick_set_transparency_to_edit_text},
				{ChangeTransparencyByNBrick.class.getSimpleName(), new ChangeTransparencyByNBrick(), R.id.brick_change_transparency_edit_text},
				{SetBrightnessBrick.class.getSimpleName(), new SetBrightnessBrick(), R.id.brick_set_brightness_edit_text},
				{ChangeBrightnessByNBrick.class.getSimpleName(), new ChangeBrightnessByNBrick(), R.id.brick_change_brightness_edit_text},
				{SetColorBrick.class.getSimpleName(), new SetColorBrick(), R.id.brick_set_color_edit_text},
				{ChangeColorByNBrick.class.getSimpleName(), new ChangeColorByNBrick(), R.id.brick_change_color_by_edit_text},
				{SetBackgroundByIndexBrick.class.getSimpleName(), new SetBackgroundByIndexBrick(), R.id.brick_set_background_by_index_edit_text},
				{SetBackgroundByIndexAndWaitBrick.class.getSimpleName(), new SetBackgroundByIndexAndWaitBrick(), R.id.brick_set_background_by_index_edit_text},
				{SetBackgroundByIndexAndWaitBrick.class.getSimpleName(), new SetBackgroundByIndexAndWaitBrick(), R.id.brick_set_background_by_index_edit_text},
				{SetPenSizeBrick.class.getSimpleName(), new SetPenSizeBrick(), R.id.brick_set_pen_size_edit_text},
				{SetVariableBrick.class.getSimpleName(), new SetVariableBrick(), R.id.brick_set_variable_edit_text},
				{ChangeVariableBrick.class.getSimpleName(), new ChangeVariableBrick(), R.id.brick_change_variable_edit_text},
				{AddItemToUserListBrick.class.getSimpleName(), new AddItemToUserListBrick(), R.id.brick_add_item_to_userlist_edit_text},
				{DeleteItemOfUserListBrick.class.getSimpleName(), new DeleteItemOfUserListBrick(), R.id.brick_delete_item_of_userlist_edit_text},
				{IfLogicBeginBrick.class.getSimpleName(), new IfLogicBeginBrick(), R.id.brick_if_begin_edit_text},
				{IfThenLogicBeginBrick.class.getSimpleName(), new IfThenLogicBeginBrick(), R.id.brick_if_begin_edit_text},

				//Bricks with multiple formula fields:
				{GlideToBrick.class.getSimpleName() + " x", new GlideToBrick(), R.id.brick_glide_to_edit_text_x},
				{GlideToBrick.class.getSimpleName() + " y", new GlideToBrick(), R.id.brick_glide_to_edit_text_y},
				{GlideToBrick.class.getSimpleName() + " duration", new GlideToBrick(), R.id.brick_glide_to_edit_text_duration},
				{SetVelocityBrick.class.getSimpleName() + " x", new SetVelocityBrick(), R.id.brick_set_velocity_edit_text_x},
				{SetVelocityBrick.class.getSimpleName() + " y", new SetVelocityBrick(), R.id.brick_set_velocity_edit_text_y},
				{SetGravityBrick.class.getSimpleName() + " x", new SetGravityBrick(), R.id.brick_set_gravity_edit_text_x},
				{SetGravityBrick.class.getSimpleName() + " y", new SetGravityBrick(), R.id.brick_set_gravity_edit_text_y},
				{SayForBubbleBrick.class.getSimpleName() + " text", new SayForBubbleBrick(), R.id.brick_for_bubble_edit_text_text},
				{SayForBubbleBrick.class.getSimpleName() + " duration", new SayForBubbleBrick(), R.id.brick_for_bubble_edit_text_duration},
				{ThinkForBubbleBrick.class.getSimpleName() + " text", new ThinkForBubbleBrick(), R.id.brick_for_bubble_edit_text_text},
				{ThinkForBubbleBrick.class.getSimpleName() + " duration", new ThinkForBubbleBrick(), R.id.brick_for_bubble_edit_text_duration},
				{ShowTextBrick.class.getSimpleName() + " x", new ShowTextBrick(), R.id.brick_show_variable_edit_text_x},
				{ShowTextBrick.class.getSimpleName() + " y", new ShowTextBrick(), R.id.brick_show_variable_edit_text_y},
				{ShowTextColorSizeAlignmentBrick.class.getSimpleName() + " x", new ShowTextColorSizeAlignmentBrick(), R.id.brick_show_variable_color_size_edit_text_x},
				{ShowTextColorSizeAlignmentBrick.class.getSimpleName() + " y", new ShowTextColorSizeAlignmentBrick(), R.id.brick_show_variable_color_size_edit_text_y},
				{ShowTextColorSizeAlignmentBrick.class.getSimpleName() + " size", new ShowTextColorSizeAlignmentBrick(), R.id.brick_show_variable_color_size_edit_relative_size},
				{InsertItemIntoUserListBrick.class.getSimpleName() + " value", new InsertItemIntoUserListBrick(), R.id.brick_insert_item_into_userlist_value_edit_text},
				{InsertItemIntoUserListBrick.class.getSimpleName() + " index", new InsertItemIntoUserListBrick(), R.id.brick_insert_item_into_userlist_at_index_edit_text},
				{ReplaceItemInUserListBrick.class.getSimpleName() + " value", new ReplaceItemInUserListBrick(), R.id.brick_replace_item_in_userlist_value_edit_text},
				{ReplaceItemInUserListBrick.class.getSimpleName() + " index", new ReplaceItemInUserListBrick(), R.id.brick_replace_item_in_userlist_at_index_edit_text},
				{ReplaceItemInUserListBrick.class.getSimpleName() + " index", new ReplaceItemInUserListBrick(), R.id.brick_replace_item_in_userlist_at_index_edit_text},

				//Testing bricks
				{TapAtBrick.class.getSimpleName() + " x", new TapAtBrick(), R.id.brick_tap_at_edit_text_x},
				{TapAtBrick.class.getSimpleName() + " y", new TapAtBrick(), R.id.brick_tap_at_edit_text_y},
				{AssertEqualsBrick.class.getSimpleName() + " actual", new AssertEqualsBrick(), R.id.brick_assert_actual},
				{AssertEqualsBrick.class.getSimpleName() + " expected", new AssertEqualsBrick(), R.id.brick_assert_expected},
		});
	}

	@SuppressWarnings("PMD.UnusedPrivateField")
	private String name;

	private FormulaBrick brick;

	private @IdRes int formulaTextFieldId;

	public BrickSingleFormulaFieldTest(String name, FormulaBrick brick, @IdRes int formulaTextFieldId) {
		this.name = name;
		this.brick = brick;
		this.formulaTextFieldId = formulaTextFieldId;
	}

	@Before
	public void setUp() throws Exception {
		ActivityController<SpriteActivity> activityController = Robolectric.buildActivity(SpriteActivity.class);
		activity = activityController.get();
		createProject(activity);
		activityController.create().resume();

		assertCurrentFragmentEquals(ScriptFragment.class);
	}

	@Test
	public void openFormulaEditorTest() {
		clickOnBricksFormulaTextView();

		assertCurrentFragmentEquals(FormulaEditorFragment.class);

		enterSomeValueAndSave();

		assertCurrentFragmentEquals(ScriptFragment.class);

		assertBricksFormulaTextView();
	}

	private void assertBricksFormulaTextView() {
		View brickView = brick.getView(activity);
		assertNotNull(brickView);

		TextView brickFormulaTextView = brickView.findViewById(formulaTextFieldId);
		assertNotNull(brickFormulaTextView);
		assertEquals("5 ", brickFormulaTextView.getText().toString());
	}

	private void clickOnBricksFormulaTextView() {
		View brickView = brick.getView(activity);
		assertNotNull(brickView);

		TextView brickFormulaTextView = brickView.findViewById(formulaTextFieldId);
		assertNotNull(brickFormulaTextView);

		brick.onClick(brickFormulaTextView);
	}

	private void enterSomeValueAndSave() {
		Fragment formulaEditorFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

		((FormulaEditorFragment) formulaEditorFragment)
				.getFormulaEditorEditText().handleKeyEvent(R.id.formula_editor_keyboard_5, "");

		((FormulaEditorFragment) formulaEditorFragment)
				.endFormulaEditor();
	}

	private void assertCurrentFragmentEquals(Class fragmentClazz) {
		Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
		assertNotNull(fragment);
		assertThat(fragment, is(instanceOf(fragmentClazz)));
	}

	private void createProject(Activity activity) {
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

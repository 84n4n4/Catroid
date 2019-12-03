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

package org.catrobat.catroid.uiespresso.content.brick.rtl;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.AskBrick;
import org.catrobat.catroid.content.bricks.AskSpeechBrick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.CameraBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeColorByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeVolumeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.ChooseCameraBrick;
import org.catrobat.catroid.content.bricks.ClearBackgroundBrick;
import org.catrobat.catroid.content.bricks.ClearGraphicEffectBrick;
import org.catrobat.catroid.content.bricks.CloneBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.DeleteThisCloneBrick;
import org.catrobat.catroid.content.bricks.FlashBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoToBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfThenLogicBeginBrick;
import org.catrobat.catroid.content.bricks.InsertItemIntoUserListBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NextLookBrick;
import org.catrobat.catroid.content.bricks.NoteBrick;
import org.catrobat.catroid.content.bricks.PenDownBrick;
import org.catrobat.catroid.content.bricks.PenUpBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.PlaySoundAndWaitBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.PreviousLookBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.RepeatUntilBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SceneStartBrick;
import org.catrobat.catroid.content.bricks.SceneTransitionBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundAndWaitBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundByIndexAndWaitBrick;
import org.catrobat.catroid.content.bricks.SetBackgroundByIndexBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetColorBrick;
import org.catrobat.catroid.content.bricks.SetPenColorBrick;
import org.catrobat.catroid.content.bricks.SetPenSizeBrick;
import org.catrobat.catroid.content.bricks.SetRotationStyleBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetVolumeToBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.SpeakAndWaitBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.StampBrick;
import org.catrobat.catroid.content.bricks.StopAllSoundsBrick;
import org.catrobat.catroid.content.bricks.StopScriptBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.VibrationBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WaitUntilBrick;
import org.catrobat.catroid.content.bricks.WhenBackgroundChangesBrick;
import org.catrobat.catroid.content.bricks.WhenBrick;
import org.catrobat.catroid.content.bricks.WhenClonedBrick;
import org.catrobat.catroid.content.bricks.WhenConditionBrick;
import org.catrobat.catroid.content.bricks.WhenStartedBrick;
import org.catrobat.catroid.content.bricks.WhenTouchDownBrick;
import org.catrobat.catroid.physics.content.bricks.SetBounceBrick;
import org.catrobat.catroid.physics.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.content.bricks.SetPhysicsObjectTypeBrick;
import org.catrobat.catroid.physics.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.physics.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.content.bricks.TurnRightSpeedBrick;
import org.catrobat.catroid.physics.content.bricks.WhenBounceOffBrick;
import org.catrobat.catroid.testsuites.annotations.Cat;
import org.catrobat.catroid.testsuites.annotations.Level;
import org.catrobat.catroid.ui.SpriteActivity;
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.RtlUiTestUtils;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.uiespresso.util.matchers.BrickCategoryListMatchers;
import org.catrobat.catroid.uiespresso.util.matchers.BrickPrototypeListMatchers;
import org.catrobat.catroid.uiespresso.util.rules.FragmentActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.PARROT_JUMPING_SUMO_SCREEN_KEY;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_MINDSTORMS_EV3_BRICKS_ENABLED;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_MINDSTORMS_NXT_BRICKS_ENABLED;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_ARDUINO_BRICKS;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_NFC_BRICKS;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_PARROT_AR_DRONE_BRICKS;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_PHIRO_BRICKS;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_RASPI_BRICKS;
import static org.catrobat.catroid.uiespresso.util.matchers.rtl.RtlViewDirection.isViewRtl;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class RtlBrickTest {

	@Rule
	public FragmentActivityTestRule<SpriteActivity> baseActivityTestRule = new
			FragmentActivityTestRule<>(SpriteActivity.class, SpriteActivity.EXTRA_FRAGMENT_POSITION,
			SpriteActivity.FRAGMENT_SCRIPTS);

	private Locale arLocale = new Locale("ar");
	private List<String> allPeripheralCategories = new ArrayList<>(Arrays.asList(SETTINGS_MINDSTORMS_NXT_BRICKS_ENABLED,
			SETTINGS_MINDSTORMS_EV3_BRICKS_ENABLED, SETTINGS_SHOW_PARROT_AR_DRONE_BRICKS, SETTINGS_SHOW_PHIRO_BRICKS,
			SETTINGS_SHOW_ARDUINO_BRICKS, SETTINGS_SHOW_RASPI_BRICKS, SETTINGS_SHOW_NFC_BRICKS,
			PARROT_JUMPING_SUMO_SCREEN_KEY));
	private List<String> enabledByThisTestPeripheralCategories = new ArrayList<>();

	@Before
	public void setUp() throws Exception {
		SettingsFragment.setLanguageSharedPreference(getTargetContext(), "ar");
		createProject("RtlBricksTest");
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());

		for (String category : allPeripheralCategories) {
			boolean categoryEnabled = sharedPreferences.getBoolean(category, false);
			if (!categoryEnabled) {
				sharedPreferences.edit().putBoolean(category, true).commit();
				enabledByThisTestPeripheralCategories.add(category);
			}
		}
		baseActivityTestRule.launchActivity();
	}

	@After
	public void tearDown() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());
		for (String category : enabledByThisTestPeripheralCategories) {
			sharedPreferences.edit().putBoolean(category, false).commit();
		}
		enabledByThisTestPeripheralCategories.clear();
		SettingsFragment.removeLanguageSharedPreference(getTargetContext());
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void eventBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_event);

		checkIfBrickISRtl(WhenStartedBrick.class, R.id.brick_when_started_layout);

		checkIfBrickISRtl(WhenBrick.class, R.id.brick_when_layout);

		checkIfBrickISRtl(WhenTouchDownBrick.class, R.id.brick_when_screen_touched_layout);

		checkIfBrickISRtl(BroadcastReceiverBrick.class, R.id.brick_broadcast_receive_layout);

		checkIfBrickAtPositionIsRtl(BroadcastBrick.class, 0, R.id.brick_broadcast_layout);

		checkIfBrickISRtl(BroadcastWaitBrick.class, R.id.brick_broadcast_wait_layout);

		checkIfBrickISRtl(WhenConditionBrick.class, R.id.brick_when_condition_layout);

		checkIfBrickISRtl(WhenBounceOffBrick.class, R.id.brick_when_bounce_off_layout);

		checkIfBrickISRtl(WhenBackgroundChangesBrick.class, R.id.brick_when_background_layout);

		checkIfBrickISRtl(WhenClonedBrick.class, R.id.brick_when_cloned_layout);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void controlBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_control);

		checkIfBrickISRtl(WaitBrick.class, R.id.brick_wait_layout);

		checkIfBrickISRtl(NoteBrick.class, R.id.brick_note_layout);

		checkIfBrickISRtl(ForeverBrick.class, R.id.brick_forever_layout);

		checkIfBrickAtPositionIsRtl(IfLogicBeginBrick.class, 0, R.id.brick_if_begin_layout);

		checkIfBrickISRtl(IfThenLogicBeginBrick.class, R.id.brick_if_begin_layout);

		checkIfBrickISRtl(WaitUntilBrick.class, R.id.brick_wait_until_layout);

		checkIfBrickISRtl(RepeatBrick.class, R.id.brick_repeat_layout);

		checkIfBrickISRtl(RepeatUntilBrick.class, R.id.brick_repeat_until_layout);

		checkIfBrickISRtl(SceneTransitionBrick.class, R.id.brick_scene_transition_layout);

		checkIfBrickISRtl(SceneStartBrick.class, R.id.brick_scene_start_layout);

		checkIfBrickISRtl(StopScriptBrick.class, R.id.brick_stop_script_layout);

		checkIfBrickISRtl(CloneBrick.class, R.id.brick_clone_layout);

		checkIfBrickISRtl(DeleteThisCloneBrick.class, R.id.brick_delete_clone_layout);

		checkIfBrickISRtl(WhenClonedBrick.class, R.id.brick_when_cloned_layout);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void motionBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_motion);

		checkIfBrickISRtl(PlaceAtBrick.class, R.id.brick_place_at_layout);

		checkIfBrickISRtl(SetXBrick.class, R.id.brick_set_x_layout);

		checkIfBrickISRtl(SetYBrick.class, R.id.brick_set_y_layout);

		checkIfBrickISRtl(ChangeXByNBrick.class, R.id.brick_change_x_layout);

		checkIfBrickISRtl(ChangeYByNBrick.class, R.id.brick_change_y_layout);

		checkIfBrickISRtl(GoToBrick.class, R.id.brick_go_to_layout);

		checkIfBrickISRtl(MoveNStepsBrick.class, R.id.brick_move_n_steps_layout);

		checkIfBrickISRtl(TurnLeftBrick.class, R.id.brick_turn_left_layout);

		checkIfBrickISRtl(TurnRightBrick.class, R.id.brick_turn_right_layout);

		checkIfBrickISRtl(PointInDirectionBrick.class, R.id.brick_point_in_direction_layout);

		checkIfBrickISRtl(PointToBrick.class, R.id.brick_point_to_layout);

		checkIfBrickISRtl(SetRotationStyleBrick.class, R.id.brick_set_rotation_style_normal_layout);

		checkIfBrickISRtl(GlideToBrick.class, R.id.brick_glide_to_layout);

		checkIfBrickISRtl(VibrationBrick.class, R.id.brick_vibration_layout);

		checkIfBrickISRtl(SetPhysicsObjectTypeBrick.class, R.id.brick_set_physics_object_layout);

		checkIfBrickISRtl(SetVelocityBrick.class, R.id.brick_set_velocity_layout);

		checkIfBrickISRtl(TurnLeftSpeedBrick.class, R.id.brick_turn_left_speed_layout);

		checkIfBrickISRtl(TurnRightSpeedBrick.class, R.id.brick_turn_right_speed_layout);

		checkIfBrickISRtl(SetGravityBrick.class, R.id.brick_set_gravity_layout);

		checkIfBrickISRtl(SetMassBrick.class, R.id.brick_set_mass_layout);

		checkIfBrickISRtl(SetBounceBrick.class, R.id.brick_set_bounce_factor_layout);

		checkIfBrickISRtl(SetFrictionBrick.class, R.id.brick_set_friction_layout);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void soundBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_sound);

		checkIfBrickAtPositionIsRtl(PlaySoundBrick.class, 0, R.id.brick_play_sound_layout);

		checkIfBrickISRtl(PlaySoundAndWaitBrick.class, R.id.brick_play_sound_layout);

		checkIfBrickISRtl(StopAllSoundsBrick.class, R.id.brick_stop_all_sounds_layout);

		checkIfBrickISRtl(SetVolumeToBrick.class, R.id.brick_set_volume_to_layout);

		checkIfBrickISRtl(ChangeVolumeByNBrick.class, R.id.brick_change_volume_by_layout);

		checkIfBrickISRtl(SpeakBrick.class, R.id.brick_speak_layout);

		checkIfBrickISRtl(SpeakAndWaitBrick.class, R.id.brick_speak_and_wait_layout);

		checkIfBrickISRtl(AskSpeechBrick.class, R.id.brick_set_variable_layout);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void looksBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_looks);

		checkIfBrickISRtl(NextLookBrick.class, R.id.brick_next_look_layout);

		checkIfBrickISRtl(PreviousLookBrick.class, R.id.brick_previous_look_layout);

		checkIfBrickISRtl(SetSizeToBrick.class, R.id.brick_set_size_to_layout);

		checkIfBrickISRtl(ChangeSizeByNBrick.class, R.id.brick_change_size_by_layout);

		checkIfBrickISRtl(HideBrick.class, R.id.brick_hide_layout);

		checkIfBrickISRtl(ShowBrick.class, R.id.brick_show_layout);

		checkIfBrickISRtl(AskBrick.class, R.id.brick_set_variable_layout);

		checkIfBrickISRtl(SetTransparencyBrick.class, R.id.brick_set_transparency_layout);

		checkIfBrickISRtl(ChangeTransparencyByNBrick.class, R.id.brick_change_transparency_layout);

		checkIfBrickISRtl(SetBrightnessBrick.class, R.id.brick_set_brightness_layout);

		checkIfBrickISRtl(ChangeBrightnessByNBrick.class, R.id.brick_change_brightness_layout);

		checkIfBrickISRtl(SetColorBrick.class, R.id.brick_set_color_layout);

		checkIfBrickISRtl(ChangeColorByNBrick.class, R.id.brick_change_color_by_layout);

		checkIfBrickISRtl(ClearGraphicEffectBrick.class, R.id.brick_clear_graphic_effect_layout);

		checkIfBrickISRtl(WhenBackgroundChangesBrick.class, R.id.brick_when_background_layout);

		checkIfBrickAtPositionIsRtl(SetBackgroundBrick.class, 0, R.id.brick_set_background_layout);

		checkIfBrickAtPositionIsRtl(SetBackgroundAndWaitBrick.class, 0, R.id.brick_set_background_layout);

		checkIfBrickAtPositionIsRtl(SetBackgroundByIndexBrick.class, 1, R.id.brick_set_background_by_index_layout);

		checkIfBrickISRtl(SetBackgroundByIndexAndWaitBrick.class, R.id.brick_set_background_by_index_layout);

		checkIfBrickISRtl(CameraBrick.class, R.id.brick_video_layout);

		checkIfBrickISRtl(ChooseCameraBrick.class, R.id.brick_choose_camera_layout);

		checkIfBrickISRtl(FlashBrick.class, R.id.brick_flash_layout);

	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void penBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_pen);

		checkIfBrickISRtl(PenDownBrick.class, R.id.brick_pen_down_layout);

		checkIfBrickISRtl(PenUpBrick.class, R.id.brick_pen_up_layout);

		checkIfBrickISRtl(SetPenSizeBrick.class, R.id.brick_set_pen_size_layout);

		checkIfBrickISRtl(SetPenColorBrick.class, R.id.brick_set_pen_color_layout);

		checkIfBrickISRtl(StampBrick.class, R.id.brick_stamp_layout);

		checkIfBrickISRtl(ClearBackgroundBrick.class, R.id.brick_clear_background_layout);
	}

	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void dataBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_data);

		checkIfBrickISRtl(SetVariableBrick.class, R.id.brick_set_variable_layout);

		checkIfBrickISRtl(ChangeVariableBrick.class, R.id.brick_change_variable_layout);

		checkIfBrickISRtl(ShowTextBrick.class, R.id.brick_show_variable_layout);

		checkIfBrickISRtl(HideTextBrick.class, R.id.brick_hide_variable_layout);

		checkIfBrickISRtl(AddItemToUserListBrick.class, R.id.brick_add_item_to_userlist_layout);

		checkIfBrickISRtl(DeleteItemOfUserListBrick.class, R.id.brick_delete_item_of_userlist_layout);

		checkIfBrickISRtl(InsertItemIntoUserListBrick.class, R.id.brick_insert_item_into_userlist_layout);

		checkIfBrickISRtl(ReplaceItemInUserListBrick.class, R.id.brick_replace_item_in_userlist_layout);

		checkIfBrickISRtl(AskBrick.class, R.id.brick_set_variable_layout);

		checkIfBrickISRtl(AskSpeechBrick.class, R.id.brick_set_variable_layout);
	}
	@Category({Cat.AppUi.class, Level.Smoke.class, Cat.RTLTests.class})
	@Test
	public void userBricks() {
		assertEquals(arLocale.getDisplayLanguage(), Locale.getDefault().getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));
		openCategory(R.string.category_user_bricks);
		onView(allOf(withId(android.R.id.list), withParent(withId(R.id.add_brick_fragment_list)))).check(matches(hasChildCount(0)));
	}

	private void checkIfBrickISRtl(Class brickClass, int bricksId) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.onChildView(withId(bricksId))
				.check(matches(isViewRtl()));
	}

	private void checkIfBrickAtPositionIsRtl(Class brickClass, int position, int brickId) {
		onData(instanceOf(brickClass)).inAdapterView(BrickPrototypeListMatchers.isBrickPrototypeView())
				.atPosition(position)
				.onChildView(withId(brickId))
				.check(matches(isViewRtl()));
	}

	private void createProject(String projectName) {
		String nameSpriteTwo = "testSpriteTwo";

		Project project = new Project(InstrumentationRegistry.getTargetContext(), projectName);
		Sprite spriteOne = new Sprite("testSpriteOne");
		project.getDefaultScene().addSprite(spriteOne);

		Sprite spriteTwo = new Sprite(nameSpriteTwo);
		Script script = new StartScript();
		spriteTwo.addScript(script);

		project.getDefaultScene().addSprite(spriteTwo);
		ProjectManager.getInstance().setCurrentProject(project);
		ProjectManager.getInstance().setCurrentlyEditedScene(project.getDefaultScene());
		ProjectManager.getInstance().setCurrentSprite(spriteTwo);
	}

	private void openCategory(int categoryNameStringResourceId) {
		onView(withId(R.id.button_add))
				.perform(click());

		onData(allOf(is(instanceOf(String.class)), is(UiTestUtils.getResourcesString(categoryNameStringResourceId))))
				.inAdapterView(BrickCategoryListMatchers.isBrickCategoryView())
				.perform(click());
	}
}

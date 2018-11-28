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

package org.catrobat.catroid.uiespresso.testsuites;

import org.catrobat.catroid.uiespresso.SmokeTest;
import org.catrobat.catroid.uiespresso.annotations.FlakyTestTest;
import org.catrobat.catroid.uiespresso.content.brick.app.*;
import org.catrobat.catroid.uiespresso.content.brick.rtl.RtlBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.AskBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.BroadcastBricksStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.CameraResourceTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.FlashBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.InsertItemToUserListStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.PlaySoundBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.SayBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.SayForBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.SceneTransitionBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.ThinkBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.ThinkForBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.VibrationBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.WhenNfcBrickHardwareStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.WhenNfcBrickStageFromScriptTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.WhenNfcBrickStageTest;
import org.catrobat.catroid.uiespresso.content.messagecontainer.BroadcastAndWaitBrickMessageContainerTest;
import org.catrobat.catroid.uiespresso.content.messagecontainer.BroadcastReceiveBrickMessageContainerTest;
import org.catrobat.catroid.uiespresso.content.messagecontainer.BroadcastSendBrickMessageContainerTest;
import org.catrobat.catroid.uiespresso.facedetection.FaceDetectionFormulaEditorComputeDialogTest;
import org.catrobat.catroid.uiespresso.facedetection.FaceDetectionResourceStartedTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorAddVariableTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorDeleteVariableTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorEditTextTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorFragmentTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorFunctionListTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorKeyboardTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorRenameVariableTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormularEditorVariableScopeTest;
import org.catrobat.catroid.uiespresso.intents.PocketPaintEditLookIntentTest;
import org.catrobat.catroid.uiespresso.intents.PocketPaintFromProjectActivityDiscardIntentTest;
import org.catrobat.catroid.uiespresso.intents.PocketPaintFromSpriteActivityDiscardIntentTest;
import org.catrobat.catroid.uiespresso.intents.PocketPaintNewLookIntentTest;
import org.catrobat.catroid.uiespresso.pocketmusic.PocketMusicActivityTest;
import org.catrobat.catroid.uiespresso.stage.BroadcastAndWaitForDeletedClonesRegressionTest;
import org.catrobat.catroid.uiespresso.stage.BroadcastForClonesRegressionTest;
import org.catrobat.catroid.uiespresso.stage.BroadcastForDeletedClonesRegressionTest;
import org.catrobat.catroid.uiespresso.stage.BroadcastReceiverRegressionTest;
import org.catrobat.catroid.uiespresso.stage.DisabledBrickInClonesRegressionTest;
import org.catrobat.catroid.uiespresso.stage.MultipleBroadcastsTest;
import org.catrobat.catroid.uiespresso.stage.ObjectVariableTest;
import org.catrobat.catroid.uiespresso.stage.StagePausedTest;
import org.catrobat.catroid.uiespresso.stage.StageSimpleTest;
import org.catrobat.catroid.uiespresso.stage.StartStageTouchTest;
import org.catrobat.catroid.uiespresso.ui.actionbar.ActionBarFormulaEditorTitleTest;
import org.catrobat.catroid.uiespresso.ui.actionbar.ActionBarScriptTitleAfterExitingFormulaEditorOneSceneProjectTest;
import org.catrobat.catroid.uiespresso.ui.actionbar.ActionBarScriptTitleAfterExitingFormulaEditorTwoScenesProjectTest;
import org.catrobat.catroid.uiespresso.ui.actionbar.ActionModeDataFragmentTitleTest;
import org.catrobat.catroid.uiespresso.ui.activity.ImportProjectsFromExternalStorageTest;
import org.catrobat.catroid.uiespresso.ui.activity.PrivacyPolicyDisclaimerTest;
import org.catrobat.catroid.uiespresso.ui.activity.ProjectActivityNumberOfBricksRegressionTest;
import org.catrobat.catroid.uiespresso.ui.activity.SettingsFragmentTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.HindiNumberAtShowDetailsAtProjectActivityTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.LanguageSwitchMainMenuTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.LanguageSwitchThroughSharedPreferenceTest;
import org.catrobat.catroid.uiespresso.ui.dialog.AboutDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.DeleteLookDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.DeleteSoundDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.DeleteSpriteDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.FormulaEditorComputeDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.LegoSensorPortConfigDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.OrientationDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.PlaySceneDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.TermsOfUseDialogTest;
import org.catrobat.catroid.uiespresso.ui.fragment.CopyLookTest;
import org.catrobat.catroid.uiespresso.ui.fragment.CopyProjectTest;
import org.catrobat.catroid.uiespresso.ui.fragment.CopySoundTest;
import org.catrobat.catroid.uiespresso.ui.fragment.DeleteLookTest;
import org.catrobat.catroid.uiespresso.ui.fragment.DeleteProjectTest;
import org.catrobat.catroid.uiespresso.ui.fragment.DeleteSoundTest;
import org.catrobat.catroid.uiespresso.ui.fragment.RenameLookTest;
import org.catrobat.catroid.uiespresso.ui.fragment.RenameProjectTest;
import org.catrobat.catroid.uiespresso.ui.fragment.RenameSoundTest;
import org.catrobat.catroid.uiespresso.ui.fragment.RenameSpriteTest;
import org.catrobat.catroid.uiespresso.ui.fragment.SpriteListFragmentExplanationTextNoObjectsProjectTest;
import org.catrobat.catroid.uiespresso.ui.fragment.SpriteListFragmentExplanationTextProjectWithObjectTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.FormulaEditorFragmentActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.LegoConfigDialogActivityRecreationRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.LookFragmentActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.MainMenuFragemtnActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.NewMessageDialogActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.ProjectListActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.SceneFragmentActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.ScriptFragmentActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.SoundFragmentActivityRecreateRegressionTest;
import org.catrobat.catroid.uiespresso.ui.regression.activitydestroy.SpriteListFragmentActivityRecreateRegressionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		PocketPaintEditLookIntentTest.class,
		PocketPaintNewLookIntentTest.class,
		PocketPaintFromProjectActivityDiscardIntentTest.class,
		PocketPaintFromSpriteActivityDiscardIntentTest.class,
		FaceDetectionResourceStartedTest.class,
		FaceDetectionFormulaEditorComputeDialogTest.class,
		FormulaEditorFragmentTest.class,
		FormulaEditorTest.class,
		FormulaEditorRenameVariableTest.class,
		FormulaEditorAddVariableTest.class,
		FormularEditorVariableScopeTest.class,
		FormulaEditorEditTextTest.class,
		FormulaEditorDeleteVariableTest.class,
		FormulaEditorFunctionListTest.class,
		FormulaEditorKeyboardTest.class,
		PocketMusicActivityTest.class,
		RenameLookTest.class,
		DeleteProjectTest.class,
		DeleteSoundTest.class,
		DeleteLookTest.class,
		RenameSoundTest.class,
		SpriteListFragmentExplanationTextNoObjectsProjectTest.class,
		CopyLookTest.class,
		RenameProjectTest.class,
		SpriteListFragmentExplanationTextProjectWithObjectTest.class,
		CopyProjectTest.class,
		CopySoundTest.class,
		RenameSpriteTest.class,
		LegoConfigDialogActivityRecreationRegressionTest.class,
		FormulaEditorFragmentActivityRecreateRegressionTest.class,
		SceneFragmentActivityRecreateRegressionTest.class,
		SoundFragmentActivityRecreateRegressionTest.class,
		NewMessageDialogActivityRecreateRegressionTest.class,
		MainMenuFragemtnActivityRecreateRegressionTest.class,
		ScriptFragmentActivityRecreateRegressionTest.class,
		LookFragmentActivityRecreateRegressionTest.class,
		ProjectListActivityRecreateRegressionTest.class,
		SpriteListFragmentActivityRecreateRegressionTest.class,
		PrivacyPolicyDisclaimerTest.class,
		HindiNumberAtShowDetailsAtProjectActivityTest.class,
		LanguageSwitchMainMenuTest.class,
		LanguageSwitchThroughSharedPreferenceTest.class,
		ProjectActivityNumberOfBricksRegressionTest.class,
		SettingsFragmentTest.class,
		ImportProjectsFromExternalStorageTest.class,
		ActionBarScriptTitleAfterExitingFormulaEditorOneSceneProjectTest.class,
		ActionBarFormulaEditorTitleTest.class,
		ActionModeDataFragmentTitleTest.class,
		ActionBarScriptTitleAfterExitingFormulaEditorTwoScenesProjectTest.class,
		TermsOfUseDialogTest.class,
		DeleteLookDialogTest.class,
		LegoSensorPortConfigDialogTest.class,
		FormulaEditorComputeDialogTest.class,
		AboutDialogTest.class,
		PlaySceneDialogTest.class,
		DeleteSoundDialogTest.class,
		DeleteSpriteDialogTest.class,
		OrientationDialogTest.class,
		FlakyTestTest.class,
		SmokeTest.class,
		BroadcastForDeletedClonesRegressionTest.class,
		StageSimpleTest.class,
		BroadcastAndWaitForDeletedClonesRegressionTest.class,
		DisabledBrickInClonesRegressionTest.class,
		BroadcastReceiverRegressionTest.class,
		StartStageTouchTest.class,
		ObjectVariableTest.class,
		MultipleBroadcastsTest.class,
		StagePausedTest.class,
		BroadcastForClonesRegressionTest.class,
		BroadcastAndWaitBrickMessageContainerTest.class,
		BroadcastSendBrickMessageContainerTest.class,
		BroadcastReceiveBrickMessageContainerTest.class,
		ChangeYByNBrickTest.class,
		ARDroneTurnLeftBrickTest.class,
		RepeatBrickTest.class,
		PhiroPlayToneBrickTest.class,
		SetSizeToBrickTest.class,
		ARDroneMoveUpBrickTest.class,
		ForeverBrickTest.class,
		SetLookBrickTest.class,
		SetTransparencyBrickTest.class,
		PlaceAtBrickTest.class,
		LegoNxtMotorTurnAngleBrickTest.class,
		InsertItemToUserListTest.class,
		IfThenElseBrickTest.class,
		ARDroneMoveRightBrickTest.class,
		ArduinoSendDigitalValueBrickTest.class,
		LegoEv3MotorTurnAngleBrickTest.class,
		TurnLeftBrickTest.class,
		SetYBrickTest.class,
		RepeatUntilBrickTest.class,
		ArduinoSendPWMValueBrickTest.class,
		SceneTransmitionBrickTest.class,
		JumpingSumoRotateRightBrickTest.class,
		ARDroneMoveLeftBrickTest.class,
		WhenConditionBrickTest.class,
		CameraBrickTest.class,
		TurnRightBrickTest.class,
		ChangeColorByNBrickTest.class,
		GoNStepsBackTest.class,
		PhiroIfBrickTest.class,
		AddItemToUserListTest.class,
		LegoEv3PlayToneBrickTest.class,
		SetBrightnessBrickTest.class,
		PhiroColorBrickTest.class,
		ARDroneMoveBackwardBrickTest.class,
		SetColorBrickTest.class,
		SetVolumeToBrickTest.class,
		ARDroneTurnRightBrickTest.class,
		ThinkForBubbleBrickTest.class,
		SetVariableTest.class,
		WhenStartedBrickTest.class,
		DeleteItemOfUserListBrickTest.class,
		ChangeBrightnessByNBrickTest.class,
		DragNDropBricksTest.class,
		PointToBrickTest.class,
		LegoNxtPlayToneBrickTest.class,
		SayBubbleBrickTest.class,
		LoopBrickTest.class,
		PhiroSeekBarColorBrickTest.class,
		BroadcastReceiveBrickTest.class,
		PointInDirectionBrickTest.class,
		JumpingSumoRotateLeftBrickTest.class,
		BroadcastAndWaitBrickTest.class,
		ChangeTransparencyByNBrickTest.class,
		ChangeXByNBrickTest.class,
		ThinkBubbleBrickTest.class,
		ARDronePlayLedAnimationBrickTest.class,
		ARDroneMoveDownBrickTest.class,
		JumpingSumoMoveBackwardBrickTest.class,
		PhiroMoveMotorForwardBrickTest.class,
		PlaySoundAndWaitBrickTest.class,
		ChangeVolumeByNBrickTest.class,
		SpeakBrickTest.class,
		LegoNXTMotorStopBrickTest.class,
		JumpingSumoMoveForwardBrickTest.class,
		JumpingSumoSoundBrickTest.class,
		SetLookByIndexBrickTest.class,
		BroadcastSendBrickTest.class,
		PhiroStopMotorBrickTest.class,
		ComeToFrontBrickTest.class,
		ARDroneMoveForwardBrickTest.class,
		LegoEV3SetLedBrickTest.class,
		NoteBrickTest.class,
		BrickValueParameterTest.class,
		ClearGraphicEffectBrickTest.class,
		GlideToBrickTest.class,
		IfThenBrickTest.class,
		ChangeVariableTest.class,
		LegoEv3MotorStopBrickTest.class,
		WhenGamepadButtonBrickTest.class,
		MoveNStepsBrickTest.class,
		VariableBrickTest.class,
		GoToBrickTest.class,
		ShowBrickTest.class,
		ReplaceItemInUserListTest.class,
		AskBrickTest.class,
		SetBackgroundBrickTest.class,
		SetRotationStyleBrickTest.class,
		SpeakAndWaitBrickTest.class,
		HideBrickTest.class,
		ShowTextBrickTest.class,
		LegoEv3MotorMoveBrickTest.class,
		ChangeSizeByNBrickTest.class,
		SayForBubbleBrickTest.class,
		PenBricksTest.class,
		JumpingSumoAnimationBrickTest.class,
		StopScriptBrickTest.class,
		FlashBrickTest.class,
		SetXBrickTest.class,
		StopAllSoundsBrickTest.class,
		PlaySoundBrickTest.class,
		WaitBrickTest.class,
		PhiroMoveMotorBackwardBrickTest.class,
		LegoNXTMotorMoveBrickTest.class,
		API19SpinnerAndFormulaFieldCrashesRegressionTest.class,
		NextLookBrickTest.class,
		VibrationBrickTest.class,
		WhenNfcBrickTest.class,
		RtlBrickTest.class,
		SayBubbleBrickStageTest.class,
		VibrationBrickStageTest.class,
		SceneTransitionBrickStageTest.class,
		WhenNfcBrickStageTest.class,
		WhenNfcBrickHardwareStageTest.class,
		AskBrickStageTest.class,
		WhenNfcBrickStageFromScriptTest.class,
		BroadcastBricksStageTest.class,
		SayForBubbleBrickStageTest.class,
		PlaySoundBrickStageTest.class,
		CameraResourceTest.class,
		ThinkBubbleBrickStageTest.class,
		FlashBrickStageTest.class,
		ThinkForBubbleBrickStageTest.class,
		InsertItemToUserListStageTest.class
})
public class AllEspressoTestsDebugSuite {
}

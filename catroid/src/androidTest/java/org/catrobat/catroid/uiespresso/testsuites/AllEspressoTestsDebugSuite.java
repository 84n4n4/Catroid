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

import org.catrobat.catroid.uiespresso.annotations.FlakyTestTest;
import org.catrobat.catroid.uiespresso.content.brick.app.AddItemToUserListTest;
import org.catrobat.catroid.uiespresso.content.brick.app.API19SpinnerAndFormulaFieldCrashesRegressionTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveBackwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveDownBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveForwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveLeftBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveRightBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveUpBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDronePlayLedAnimationBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneTurnLeftBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneTurnRightBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ArduinoSendDigitalValueBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ArduinoSendPWMValueBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.AskBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.BrickValueParameterTest;
import org.catrobat.catroid.uiespresso.content.brick.app.BroadcastAndWaitBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.BroadcastReceiveBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.BroadcastSendBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.CameraBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeBrightnessByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeColorByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeSizeByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeTransparencyByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeVariableTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeVolumeByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeXByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeYByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ClearGraphicEffectBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ComeToFrontBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.DeleteItemOfUserListBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.DragNDropBricksTest;
import org.catrobat.catroid.uiespresso.content.brick.app.FlashBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ForeverBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.GlideToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.GoNStepsBackTest;
import org.catrobat.catroid.uiespresso.content.brick.app.GoToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.HideBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.IfThenBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.IfThenElseBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.InsertItemToUserListTest;
import org.catrobat.catroid.uiespresso.content.brick.app.JumpingSumoAnimationBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.JumpingSumoMoveBackwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.JumpingSumoMoveForwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.JumpingSumoRotateLeftBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.JumpingSumoRotateRightBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.JumpingSumoSoundBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3MotorMoveBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3MotorStopBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3MotorTurnAngleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3PlayToneBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEV3SetLedBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNXTMotorMoveBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNXTMotorStopBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNxtMotorTurnAngleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNxtPlayToneBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LoopBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.MoveNStepsBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.NextLookBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.NoteBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PenBricksTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroColorBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroIfBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroMoveMotorBackwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroMoveMotorForwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroPlayToneBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroSeekBarColorBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroStopMotorBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PlaceAtBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PlaySoundAndWaitBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PlaySoundBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PointInDirectionBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PointToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.RepeatBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.RepeatUntilBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ReplaceItemInUserListTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SayBubbleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SayForBubbleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SceneTransmitionBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetBackgroundBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetBrightnessBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetColorBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetLookBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetLookByIndexBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetRotationStyleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetSizeToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetTransparencyBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetVariableTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetVolumeToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetXBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetYBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ShowBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ShowTextBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SpeakAndWaitBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SpeakBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.StopAllSoundsBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.StopScriptBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ThinkBubbleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ThinkForBubbleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.TurnLeftBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.TurnRightBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.VariableBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.VibrationBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WaitBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WhenConditionBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WhenGamepadButtonBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WhenNfcBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WhenStartedBrickTest;
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
import org.catrobat.catroid.uiespresso.SmokeTest;
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
import org.catrobat.catroid.uiespresso.ui.activity.rtl.HindiNumberAtShowDetailsAtProjectActivityTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.LanguageSwitchMainMenuTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.LanguageSwitchThroughSharedPreferenceTest;
import org.catrobat.catroid.uiespresso.ui.activity.SettingsFragmentTest;
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

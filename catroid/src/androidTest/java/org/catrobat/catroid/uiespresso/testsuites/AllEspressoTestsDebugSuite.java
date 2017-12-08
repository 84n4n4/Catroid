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
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveBackwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveDownBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveForwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveLeftBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveRightBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneMoveUpBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneTurnLeftBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ARDroneTurnRightBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.AddItemToUserListTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ArduinoSendDigitalValueBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ArduinoSendPWMValueBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.AskBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.BrickValueParameterTest;
import org.catrobat.catroid.uiespresso.content.brick.app.BroadcastBricksTest;
import org.catrobat.catroid.uiespresso.content.brick.app.CameraBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeBrightnessByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeColorByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeSizeByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeTransparencyByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeVolumeByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeXByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ChangeYByNBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ClearGraphicEffectBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ComeToFrontBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.DeleteItemOfUserListBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.DragNDropBricksTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ForeverBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.GlideToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.GoNStepsBackTest;
import org.catrobat.catroid.uiespresso.content.brick.app.GoToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.HideBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.IfThenBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.IfThenElseBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEV3SetLedBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3MotorMoveBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3MotorStopBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3MotorTurnAngleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoEv3PlayToneBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNXTMotorMoveBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNXTMotorStopBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNxtMotorTurnAngleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LegoNxtPlayToneBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.LoopBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.MoveNStepsBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.NextLookBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.NoteBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroIfBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroMoveMotorBackwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroMoveMotorForwardBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroPlayToneBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PhiroStopMotorBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PlaceAtBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PlaySoundAndWaitBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PointInDirectionBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.PointToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.RepeatBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.RepeatUntilBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.ReplaceItemInUserListTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SayBubbleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SayForBubbleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SceneTransmitionBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetBrightnessBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetColorBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetLookByIndexBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetRotationStyleBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetSizeToBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.SetTransparencyBrickTest;
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
import org.catrobat.catroid.uiespresso.content.brick.app.WaitBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WhenNfcBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.app.WhenStartedBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.rtl.RtlBrickTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.AskBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.BroadcastBricksStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.CameraResourceTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.SayBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.SayForBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.ThinkBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.ThinkForBubbleBrickStageTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.WhenNfcBrickStageFromScriptTest;
import org.catrobat.catroid.uiespresso.content.brick.stage.WhenNfcBrickStageTest;
import org.catrobat.catroid.uiespresso.facedetection.FaceDetectionFormulaEditorComputeDialogTest;
import org.catrobat.catroid.uiespresso.facedetection.FaceDetectionResourceStartedTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorKeyboardTest;
import org.catrobat.catroid.uiespresso.formulaeditor.FormulaEditorTest;
import org.catrobat.catroid.uiespresso.pocketmusic.PocketMusicActivityTest;
import org.catrobat.catroid.uiespresso.stage.BroadcastForClonesRegressionTest;
import org.catrobat.catroid.uiespresso.stage.BroadcastReceiverRegressionTest;
import org.catrobat.catroid.uiespresso.stage.MultipleBroadcastsTest;
import org.catrobat.catroid.uiespresso.stage.ObjectVariableTest;
import org.catrobat.catroid.uiespresso.stage.StagePausedTest;
import org.catrobat.catroid.uiespresso.stage.StageSimpleTest;
import org.catrobat.catroid.uiespresso.ui.activity.ProjectActivityNumberOfBricksRegressionTest;
import org.catrobat.catroid.uiespresso.ui.activity.SettingsActivityTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.HindiNumberAtShowDetailsAtProjectActivityTest;
import org.catrobat.catroid.uiespresso.ui.activity.rtl.LanguageSwitchMainMenuTest;
import org.catrobat.catroid.uiespresso.ui.dialog.AboutDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.DeleteLookDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.DeleteSoundDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.DeleteSpriteDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.FormulaEditorComputeDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.RenameSpriteDialogTest;
import org.catrobat.catroid.uiespresso.ui.dialog.TermsOfUseDialogTest;
import org.catrobat.catroid.uiespresso.ui.fragment.RenameLookFragmentTest;
import org.catrobat.catroid.uiespresso.ui.fragment.RenameSoundFragmentTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		BroadcastBricksTest.class, // known bug, fails also in develop
		DeleteItemOfUserListBrickTest.class, // fails rightly, user list still available in bricks spinner
		IfThenBrickTest.class, // fails, probably not rightly, looping brick references wrong, something took care of
		// this in the past when creating projects in that manner, but not anymore....
		IfThenElseBrickTest.class, // fails, probably not rightly, looping brick references wrong, something took
		// care of this in the past when creating projects in that manner, but not anymore....
		FormulaEditorKeyboardTest.class, // fails rightly, something majorly wrong with formula editor
		ObjectVariableTest.class, // fails rightly, something wrong with layer change / initial layer in which sprite
		// lives
		ProjectActivityNumberOfBricksRegressionTest.class, // fails rightly, number of bricks incorrect
		HindiNumberAtShowDetailsAtProjectActivityTest.class, // fails rightly, number of bricks incorrect
})
public class AllEspressoTestsDebugSuite {
}

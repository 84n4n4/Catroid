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
package org.catrobat.catroid.content;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.camera.CameraManager;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.actions.AddItemToUserListAction;
import org.catrobat.catroid.content.actions.AskAction;
import org.catrobat.catroid.content.actions.AskSpeechAction;
import org.catrobat.catroid.content.actions.AssertEqualsAction;
import org.catrobat.catroid.content.actions.CameraBrickAction;
import org.catrobat.catroid.content.actions.ChangeBrightnessByNAction;
import org.catrobat.catroid.content.actions.ChangeColorByNAction;
import org.catrobat.catroid.content.actions.ChangeSizeByNAction;
import org.catrobat.catroid.content.actions.ChangeTransparencyByNAction;
import org.catrobat.catroid.content.actions.ChangeVariableAction;
import org.catrobat.catroid.content.actions.ChangeVolumeByNAction;
import org.catrobat.catroid.content.actions.ChangeXByNAction;
import org.catrobat.catroid.content.actions.ChangeYByNAction;
import org.catrobat.catroid.content.actions.ChooseCameraAction;
import org.catrobat.catroid.content.actions.ClearBackgroundAction;
import org.catrobat.catroid.content.actions.ClearGraphicEffectAction;
import org.catrobat.catroid.content.actions.CloneAction;
import org.catrobat.catroid.content.actions.ComeToFrontAction;
import org.catrobat.catroid.content.actions.DeleteItemOfUserListAction;
import org.catrobat.catroid.content.actions.DeleteThisCloneAction;
import org.catrobat.catroid.content.actions.EventAction;
import org.catrobat.catroid.content.actions.EventThread;
import org.catrobat.catroid.content.actions.FlashAction;
import org.catrobat.catroid.content.actions.GoNStepsBackAction;
import org.catrobat.catroid.content.actions.GoToOtherSpritePositionAction;
import org.catrobat.catroid.content.actions.GoToRandomPositionAction;
import org.catrobat.catroid.content.actions.GoToTouchPositionAction;
import org.catrobat.catroid.content.actions.HideTextAction;
import org.catrobat.catroid.content.actions.IfLogicAction;
import org.catrobat.catroid.content.actions.InsertItemIntoUserListAction;
import org.catrobat.catroid.content.actions.MoveNStepsAction;
import org.catrobat.catroid.content.actions.NextLookAction;
import org.catrobat.catroid.content.actions.NotifyEventWaiterAction;
import org.catrobat.catroid.content.actions.PenDownAction;
import org.catrobat.catroid.content.actions.PenUpAction;
import org.catrobat.catroid.content.actions.PlaySoundAction;
import org.catrobat.catroid.content.actions.PointInDirectionAction;
import org.catrobat.catroid.content.actions.PointToAction;
import org.catrobat.catroid.content.actions.PreviousLookAction;
import org.catrobat.catroid.content.actions.ReadListFromDeviceAction;
import org.catrobat.catroid.content.actions.ReadVariableFromDeviceAction;
import org.catrobat.catroid.content.actions.RepeatAction;
import org.catrobat.catroid.content.actions.RepeatUntilAction;
import org.catrobat.catroid.content.actions.ReplaceItemInUserListAction;
import org.catrobat.catroid.content.actions.SceneStartAction;
import org.catrobat.catroid.content.actions.SceneTransitionAction;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.content.actions.SetBackgroundLookAction;
import org.catrobat.catroid.content.actions.SetBackgroundLookByIndexAction;
import org.catrobat.catroid.content.actions.SetBrightnessAction;
import org.catrobat.catroid.content.actions.SetColorAction;
import org.catrobat.catroid.content.actions.SetLookAction;
import org.catrobat.catroid.content.actions.SetLookByIndexAction;
import org.catrobat.catroid.content.actions.SetPenColorAction;
import org.catrobat.catroid.content.actions.SetPenSizeAction;
import org.catrobat.catroid.content.actions.SetRotationStyleAction;
import org.catrobat.catroid.content.actions.SetSizeToAction;
import org.catrobat.catroid.content.actions.SetTextAction;
import org.catrobat.catroid.content.actions.SetTransparencyAction;
import org.catrobat.catroid.content.actions.SetVariableAction;
import org.catrobat.catroid.content.actions.SetVisibleAction;
import org.catrobat.catroid.content.actions.SetVolumeToAction;
import org.catrobat.catroid.content.actions.SetXAction;
import org.catrobat.catroid.content.actions.SetYAction;
import org.catrobat.catroid.content.actions.ShowTextAction;
import org.catrobat.catroid.content.actions.ShowTextColorSizeAlignmentAction;
import org.catrobat.catroid.content.actions.SpeakAction;
import org.catrobat.catroid.content.actions.StampAction;
import org.catrobat.catroid.content.actions.StopAllScriptsAction;
import org.catrobat.catroid.content.actions.StopAllSoundsAction;
import org.catrobat.catroid.content.actions.StopOtherScriptsAction;
import org.catrobat.catroid.content.actions.StopThisScriptAction;
import org.catrobat.catroid.content.actions.TapAtAction;
import org.catrobat.catroid.content.actions.ThinkSayBubbleAction;
import org.catrobat.catroid.content.actions.TurnLeftAction;
import org.catrobat.catroid.content.actions.TurnRightAction;
import org.catrobat.catroid.content.actions.VibrateAction;
import org.catrobat.catroid.content.actions.WaitAction;
import org.catrobat.catroid.content.actions.WaitForBubbleBrickAction;
import org.catrobat.catroid.content.actions.WaitTillIdleAction;
import org.catrobat.catroid.content.actions.WaitUntilAction;
import org.catrobat.catroid.content.actions.WebRequestAction;
import org.catrobat.catroid.content.actions.WriteListOnDeviceAction;
import org.catrobat.catroid.content.actions.WriteVariableOnDeviceAction;
import org.catrobat.catroid.content.actions.conditional.GlideToAction;
import org.catrobat.catroid.content.actions.conditional.IfOnEdgeBounceAction;
import org.catrobat.catroid.content.eventids.BroadcastEventId;
import org.catrobat.catroid.content.eventids.EventId;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.physics.PhysicsObject;
import org.catrobat.catroid.web.WebConnectionFactory;

public class ActionFactory extends Actions {

	public EventAction createBroadcastAction(String broadcastMessage, @EventWrapper.WaitMode int waitMode) {
		BroadcastEventId id = new BroadcastEventId(broadcastMessage);
		return createEventAction(id, waitMode);
	}

	private EventAction createEventAction(EventId eventId, @EventWrapper.WaitMode int waitMode) {
		EventWrapper event = new EventWrapper(eventId, waitMode);
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		EventAction action = Actions.action(EventAction.class);
		action.setEvent(event);
		action.setReceivingSprites(currentProject.getSpriteListWithClones());
		return action;
	}

	public Action createNotifyEventWaiterAction(Sprite sprite, EventWrapper event) {
		NotifyEventWaiterAction action = Actions.action(NotifyEventWaiterAction.class);
		action.setEvent(event);
		action.setSprite(sprite);
		return action;
	}

	public Action createWaitAction(Sprite sprite, Formula delay) {
		WaitAction action = action(WaitAction.class);
		action.setSprite(sprite);
		action.setDelay(delay);
		return action;
	}

	public Action createWaitForBubbleBrickAction(Sprite sprite, Formula delay) {
		WaitForBubbleBrickAction action = Actions.action(WaitForBubbleBrickAction.class);
		action.setSprite(sprite);
		action.setDelay(delay);
		return action;
	}

	public Action createChangeBrightnessByNAction(Sprite sprite, Formula changeBrightness) {
		ChangeBrightnessByNAction action = Actions.action(ChangeBrightnessByNAction.class);
		action.setSprite(sprite);
		action.setBrightness(changeBrightness);
		return action;
	}

	public Action createChangeColorByNAction(Sprite sprite, Formula changeColor) {
		ChangeColorByNAction action = Actions.action(ChangeColorByNAction.class);
		action.setSprite(sprite);
		action.setColor(changeColor);
		return action;
	}

	public Action createChangeTransparencyByNAction(Sprite sprite, Formula transparency) {
		ChangeTransparencyByNAction action = Actions.action(ChangeTransparencyByNAction.class);
		action.setSprite(sprite);
		action.setTransparency(transparency);
		return action;
	}

	public Action createChangeSizeByNAction(Sprite sprite, Formula size) {
		ChangeSizeByNAction action = Actions.action(ChangeSizeByNAction.class);
		action.setSprite(sprite);
		action.setSize(size);
		return action;
	}

	public Action createChangeVolumeByNAction(Sprite sprite, Formula volume) {
		ChangeVolumeByNAction action = Actions.action(ChangeVolumeByNAction.class);
		action.setVolume(volume);
		action.setSprite(sprite);
		return action;
	}

	public Action createChangeXByNAction(Sprite sprite, Formula xMovement) {
		ChangeXByNAction action = Actions.action(ChangeXByNAction.class);
		action.setSprite(sprite);
		action.setxMovement(xMovement);
		return action;
	}

	public Action createChangeYByNAction(Sprite sprite, Formula yMovement) {
		ChangeYByNAction action = Actions.action(ChangeYByNAction.class);
		action.setSprite(sprite);
		action.setyMovement(yMovement);
		return action;
	}

	public Action createSetRotationStyleAction(Sprite sprite, @Look.RotationStyle int mode) {
		SetRotationStyleAction action = Actions.action(SetRotationStyleAction.class);
		action.setRotationStyle(mode);
		action.setSprite(sprite);
		return action;
	}

	public Action createClearGraphicEffectAction(Sprite sprite) {
		ClearGraphicEffectAction action = Actions.action(ClearGraphicEffectAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createComeToFrontAction(Sprite sprite) {
		ComeToFrontAction action = Actions.action(ComeToFrontAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createGlideToAction(Sprite sprite, Formula x, Formula y, Formula duration) {
		GlideToAction action = Actions.action(GlideToAction.class);
		action.setPosition(x, y);
		action.setDuration(duration);
		action.setSprite(sprite);
		return action;
	}

	public Action createPlaceAtAction(Sprite sprite, Formula x, Formula y) {
		GlideToAction action = Actions.action(GlideToAction.class);
		action.setPosition(x, y);
		action.setDuration(0);
		action.setInterpolation(null);
		action.setSprite(sprite);
		return action;
	}

	public Action createGoToAction(Sprite sprite, Sprite destinationSprite, int spinnerSelection) {
		switch (spinnerSelection) {
			case BrickValues.GO_TO_TOUCH_POSITION:
				GoToTouchPositionAction touchPositionAction = Actions.action(GoToTouchPositionAction.class);
				touchPositionAction.setSprite(sprite);
				return touchPositionAction;
			case BrickValues.GO_TO_RANDOM_POSITION:
				GoToRandomPositionAction randomPositionAction = Actions.action(GoToRandomPositionAction.class);
				randomPositionAction.setSprite(sprite);
				return randomPositionAction;
			case BrickValues.GO_TO_OTHER_SPRITE_POSITION:
				GoToOtherSpritePositionAction otherSpritePositionAction = Actions
						.action(GoToOtherSpritePositionAction.class);
				otherSpritePositionAction.setSprite(sprite);
				otherSpritePositionAction.setDestinationSprite(destinationSprite);
				return otherSpritePositionAction;
			default:
				return null;
		}
	}

	public Action createGoNStepsBackAction(Sprite sprite, Formula steps) {
		GoNStepsBackAction action = Actions.action(GoNStepsBackAction.class);
		action.setSprite(sprite);
		action.setSteps(steps);
		return action;
	}

	public Action createHideAction(Sprite sprite) {
		SetVisibleAction action = Actions.action(SetVisibleAction.class);
		action.setSprite(sprite);
		action.setVisible(false);
		return action;
	}

	public Action createIfOnEdgeBounceAction(Sprite sprite) {
		IfOnEdgeBounceAction action = Actions.action(IfOnEdgeBounceAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createMoveNStepsAction(Sprite sprite, Formula steps) {
		MoveNStepsAction action = Actions.action(MoveNStepsAction.class);
		action.setSprite(sprite);
		action.setSteps(steps);
		return action;
	}

	public Action createPenDownAction(Sprite sprite) {
		PenDownAction action = Actions.action(PenDownAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createPenUpAction(Sprite sprite) {
		PenUpAction action = Actions.action(PenUpAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createSetPenSizeAction(Sprite sprite, Formula penSize) {
		SetPenSizeAction action = Actions.action(SetPenSizeAction.class);
		action.setSprite(sprite);
		action.setPenSize(penSize);
		return action;
	}

	public Action createSetPenColorAction(Sprite sprite, Formula red, Formula green, Formula blue) {
		SetPenColorAction action = Actions.action(SetPenColorAction.class);
		action.setSprite(sprite);
		action.setRed(red);
		action.setGreen(green);
		action.setBlue(blue);
		return action;
	}

	public Action createClearBackgroundAction() {
		return Actions.action(ClearBackgroundAction.class);
	}

	public Action createStampAction(Sprite sprite) {
		StampAction action = Actions.action(StampAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createNextLookAction(Sprite sprite) {
		NextLookAction action = Actions.action(NextLookAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createPlaySoundAction(Sprite sprite, SoundInfo sound) {
		PlaySoundAction action = Actions.action(PlaySoundAction.class);
		action.setSprite(sprite);
		action.setSound(sound);
		return action;
	}

	public Action createPointInDirectionAction(Sprite sprite, Formula degrees) {
		PointInDirectionAction action = Actions.action(PointInDirectionAction.class);
		action.setSprite(sprite);
		action.setDegreesInUserInterfaceDimensionUnit(degrees);
		return action;
	}

	public Action createPointToAction(Sprite sprite, Sprite pointedSprite) {
		PointToAction action = Actions.action(PointToAction.class);
		action.setSprite(sprite);
		action.setPointedSprite(pointedSprite);
		return action;
	}

	public Action createCloneAction(Sprite sprite) {
		CloneAction action = Actions.action(CloneAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createDeleteThisCloneAction(Sprite sprite) {
		DeleteThisCloneAction action = Actions.action(DeleteThisCloneAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createSetBrightnessAction(Sprite sprite, Formula brightness) {
		SetBrightnessAction action = Actions.action(SetBrightnessAction.class);
		action.setSprite(sprite);
		action.setBrightness(brightness);
		return action;
	}

	public Action createSetColorAction(Sprite sprite, Formula color) {
		SetColorAction action = Actions.action(SetColorAction.class);
		action.setSprite(sprite);
		action.setColor(color);
		return action;
	}

	public Action createSetTransparencyAction(Sprite sprite, Formula transparency) {
		SetTransparencyAction action = Actions.action(SetTransparencyAction.class);
		action.setSprite(sprite);
		action.setTransparency(transparency);
		return action;
	}

	public Action createSetLookAction(Sprite sprite, LookData lookData, @EventWrapper.WaitMode int waitMode) {
		return createSetLookEventAction((SetLookAction) createSetLookAction(sprite, lookData), waitMode);
	}

	public Action createSetLookAction(Sprite sprite, LookData lookData) {
		SetLookAction action = Actions.action(SetLookAction.class);
		action.setSprite(sprite);
		action.setLookData(lookData);
		return action;
	}

	private Action createSetLookEventAction(SetLookAction action, @EventWrapper.WaitMode int waitMode) {
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		action.setWaitMode(waitMode);
		action.setReceivingSprites(currentProject.getSpriteListWithClones());
		return action;
	}

	public Action createSetBackgroundLookAction(LookData lookData, @EventWrapper.WaitMode int waitMode) {
		return createSetBackgroundLookEventAction((SetBackgroundLookAction) createSetBackgroundLookAction(lookData), waitMode);
	}

	public Action createSetBackgroundLookAction(LookData lookData) {
		SetBackgroundLookAction action = Actions.action(SetBackgroundLookAction.class);
		action.setLookData(lookData);
		return action;
	}

	private Action createSetBackgroundLookEventAction(SetBackgroundLookAction action, @EventWrapper.WaitMode int waitMode) {
		Project currentProject = ProjectManager.getInstance().getCurrentProject();
		action.setWaitMode(waitMode);
		action.setReceivingSprites(currentProject.getSpriteListWithClones());
		return action;
	}

	public Action createSetLookByIndexAction(Sprite sprite, Formula formula, @EventWrapper.WaitMode int waitMode) {
		return createSetLookEventAction((SetLookAction) createSetLookByIndexAction(sprite, formula), waitMode);
	}

	public Action createSetLookByIndexAction(Sprite sprite, Formula formula) {
		SetLookByIndexAction action = Actions.action(SetLookByIndexAction.class);
		action.setSprite(sprite);
		action.setFormula(formula);
		return action;
	}

	public Action createSetBackgroundLookByIndexAction(Sprite scopeSprite, Formula formula, @EventWrapper.WaitMode int waitMode) {
		return createSetBackgroundLookEventAction((SetBackgroundLookAction) createSetBackgroundLookByIndexAction(scopeSprite, formula), waitMode);
	}

	public Action createSetBackgroundLookByIndexAction(Sprite scopeSprite, Formula formula) {
		SetBackgroundLookByIndexAction action = Actions.action(SetBackgroundLookByIndexAction.class);
		action.setScopeSprite(scopeSprite);
		action.setFormula(formula);
		return action;
	}

	public Action createSetSizeToAction(Sprite sprite, Formula size) {
		SetSizeToAction action = Actions.action(SetSizeToAction.class);
		action.setSprite(sprite);
		action.setSize(size);
		return action;
	}

	public Action createSetVolumeToAction(Sprite sprite, Formula volume) {
		SetVolumeToAction action = Actions.action(SetVolumeToAction.class);
		action.setVolume(volume);
		action.setSprite(sprite);
		return action;
	}

	public Action createSetXAction(Sprite sprite, Formula x) {
		SetXAction action = Actions.action(SetXAction.class);
		action.setSprite(sprite);
		action.setX(x);
		return action;
	}

	public Action createSetYAction(Sprite sprite, Formula y) {
		SetYAction action = Actions.action(SetYAction.class);
		action.setSprite(sprite);
		action.setY(y);
		return action;
	}

	public Action createShowAction(Sprite sprite) {
		SetVisibleAction action = Actions.action(SetVisibleAction.class);
		action.setSprite(sprite);
		action.setVisible(true);
		return action;
	}

	public Action createSpeakAction(Sprite sprite, Formula text) {
		SpeakAction action = action(SpeakAction.class);
		action.setSprite(sprite);
		action.setText(text);
		return action;
	}

	public Action createStopAllSoundsAction() {
		return Actions.action(StopAllSoundsAction.class);
	}

	public Action createTurnLeftAction(Sprite sprite, Formula degrees) {
		TurnLeftAction action = Actions.action(TurnLeftAction.class);
		action.setSprite(sprite);
		action.setDegrees(degrees);
		return action;
	}

	public Action createTurnRightAction(Sprite sprite, Formula degrees) {
		TurnRightAction action = Actions.action(TurnRightAction.class);
		action.setSprite(sprite);
		action.setDegrees(degrees);
		return action;
	}

	public Action createChangeVariableAction(Sprite sprite, Formula variableFormula, UserVariable userVariable) {
		ChangeVariableAction action = Actions.action(ChangeVariableAction.class);
		action.setSprite(sprite);
		action.setChangeVariable(variableFormula);
		action.setUserVariable(userVariable);
		return action;
	}

	public Action createSetVariableAction(Sprite sprite, Formula variableFormula, UserVariable userVariable) {
		SetVariableAction action = Actions.action(SetVariableAction.class);
		action.setSprite(sprite);
		action.setChangeVariable(variableFormula);
		action.setUserVariable(userVariable);
		return action;
	}

	public Action createAskAction(Sprite sprite, Formula questionFormula, UserVariable answerVariable) {
		AskAction action = Actions.action(AskAction.class);
		action.setSprite(sprite);
		action.setQuestionFormula(questionFormula);
		action.setAnswerVariable(answerVariable);
		return action;
	}

	public Action createAskSpeechAction(Sprite sprite, Formula questionFormula, UserVariable answerVariable) {
		AskSpeechAction action = Actions.action(AskSpeechAction.class);
		action.setSprite(sprite);
		action.setQuestionFormula(questionFormula);
		action.setAnswerVariable(answerVariable);
		return action;
	}

	public Action createDeleteItemOfUserListAction(Sprite sprite, Formula userListFormula, UserList userList) {
		DeleteItemOfUserListAction action = action(DeleteItemOfUserListAction.class);
		action.setSprite(sprite);
		action.setFormulaIndexToDelete(userListFormula);
		action.setUserList(userList);
		return action;
	}

	public Action createAddItemToUserListAction(Sprite sprite, Formula userListFormula, UserList userList) {
		AddItemToUserListAction action = action(AddItemToUserListAction.class);
		action.setSprite(sprite);
		action.setFormulaItemToAdd(userListFormula);
		action.setUserList(userList);
		return action;
	}

	public Action createInsertItemIntoUserListAction(Sprite sprite, Formula userListFormulaIndexToInsert,
			Formula userListFormulaItemToInsert, UserList userList) {
		InsertItemIntoUserListAction action = action(InsertItemIntoUserListAction.class);
		action.setSprite(sprite);
		action.setFormulaIndexToInsert(userListFormulaIndexToInsert);
		action.setFormulaItemToInsert(userListFormulaItemToInsert);
		action.setUserList(userList);
		return action;
	}

	public Action createReplaceItemInUserListAction(Sprite sprite, Formula userListFormulaIndexToReplace,
			Formula userListFormulaItemToInsert, UserList userList) {
		ReplaceItemInUserListAction action = action(ReplaceItemInUserListAction.class);
		action.setSprite(sprite);
		action.setFormulaIndexToReplace(userListFormulaIndexToReplace);
		action.setFormulaItemToInsert(userListFormulaItemToInsert);
		action.setUserList(userList);
		return action;
	}

	public Action createThinkSayBubbleAction(Sprite sprite, Formula text, int type) {
		ThinkSayBubbleAction action = action(ThinkSayBubbleAction.class);
		action.setText(text);
		action.setSprite(sprite);
		action.setType(type);
		return action;
	}

	public Action createThinkSayForBubbleAction(Sprite sprite, Formula text, int type) {
		ThinkSayBubbleAction action = action(ThinkSayBubbleAction.class);
		action.setText(text);
		action.setSprite(sprite);
		action.setType(type);
		return action;
	}

	public Action createSceneTransitionAction(String sceneName) {
		SceneTransitionAction action = action(SceneTransitionAction.class);
		action.setScene(sceneName);
		return action;
	}

	public Action createSceneStartAction(String sceneName) {
		SceneStartAction action = action(SceneStartAction.class);
		action.setScene(sceneName);
		return action;
	}

	public Action createIfLogicAction(Sprite sprite, Formula condition, Action ifAction, Action elseAction) {
		IfLogicAction action = Actions.action(IfLogicAction.class);
		action.setIfAction(ifAction);
		action.setIfCondition(condition);
		action.setElseAction(elseAction);
		action.setSprite(sprite);
		return action;
	}

	public Action createRepeatAction(Sprite sprite, Formula count, Action repeatedAction) {
		RepeatAction action = Actions.action(RepeatAction.class);
		action.setRepeatCount(count);
		action.setAction(repeatedAction);
		action.setSprite(sprite);
		return action;
	}

	public Action createWaitUntilAction(Sprite sprite, Formula condition) {
		WaitUntilAction action = Actions.action(WaitUntilAction.class);
		action.setSprite(sprite);
		action.setCondition(condition);
		return action;
	}

	public Action createRepeatUntilAction(Sprite sprite, Formula condition, Action repeatedAction) {
		RepeatUntilAction action = action(RepeatUntilAction.class);
		action.setRepeatCondition(condition);
		action.setAction(repeatedAction);
		action.setSprite(sprite);
		return action;
	}

	public Action createDelayAction(Sprite sprite, Formula delay) {
		WaitAction action = Actions.action(WaitAction.class);
		action.setSprite(sprite);
		action.setDelay(delay);
		return action;
	}

	public Action createForeverAction(Sprite sprite, ScriptSequenceAction foreverSequence) {
		RepeatAction action = Actions.action(RepeatAction.class);
		action.setIsForeverRepeat(true);
		action.setAction(foreverSequence);
		action.setSprite(sprite);
		return action;
	}

	public static Action eventSequence(Script script) {
		return new ScriptSequenceAction(script);
	}

	public static Action createEventThread(Script script) {
		return new EventThread(script);
	}

	public Action createSetBounceFactorAction(Sprite sprite, Formula bounceFactor) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createTurnRightSpeedAction(Sprite sprite, Formula degreesPerSecond) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createTurnLeftSpeedAction(Sprite sprite, Formula degreesPerSecond) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createSetVelocityAction(Sprite sprite, Formula velocityX, Formula velocityY) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createSetPhysicsObjectTypeAction(Sprite sprite, PhysicsObject.Type type) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createSetMassAction(Sprite sprite, Formula mass) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createSetGravityAction(Sprite sprite, Formula gravityX, Formula gravityY) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createSetFrictionAction(Sprite sprite, Formula friction) {
		throw new RuntimeException("No physics action available in non-physics sprite!");
	}

	public Action createSetTextAction(Sprite sprite, Formula x, Formula y, Formula text) {
		SetTextAction action = action(SetTextAction.class);

		action.setPosition(x, y);
		action.setText(text);
		action.setDuration(5);
		action.setSprite(sprite);
		return action;
	}

	public Action createShowVariableAction(Sprite sprite, Formula xPosition, Formula yPosition, UserVariable userVariable) {
		ShowTextAction action = action(ShowTextAction.class);
		action.setPosition(xPosition, yPosition);
		action.setVariableToShow(userVariable);
		action.setSprite(sprite);
		return action;
	}

	public Action createShowVariableColorAndSizeAction(Sprite sprite, Formula xPosition, Formula yPosition,
			Formula relativeTextSize, Formula color, UserVariable userVariable, int alignment) {
		ShowTextColorSizeAlignmentAction action = action(ShowTextColorSizeAlignmentAction.class);
		action.setPosition(xPosition, yPosition);
		action.setRelativeTextSize(relativeTextSize);
		action.setColor(color);
		action.setVariableToShow(userVariable);
		action.setSprite(sprite);
		action.setAlignment(alignment);
		return action;
	}

	public Action createHideVariableAction(Sprite sprite, UserVariable userVariable) {
		HideTextAction action = action(HideTextAction.class);
		action.setVariableToHide(userVariable);
		action.setSprite(sprite);
		return action;
	}

	public Action createTurnFlashOnAction() {
		FlashAction action = action(FlashAction.class);
		action.turnFlashOn();
		return action;
	}

	public Action createTurnFlashOffAction() {
		FlashAction action = action(FlashAction.class);
		action.turnFlashOff();
		return action;
	}

	public Action createVibrateAction(Sprite sprite, Formula duration) {
		VibrateAction action = action(VibrateAction.class);
		action.setSprite(sprite);
		action.setDuration(duration);
		return action;
	}

	public Action createUpdateCameraPreviewAction(CameraManager.CameraState state) {
		CameraBrickAction action = action(CameraBrickAction.class);
		action.setCameraAction(state);
		return action;
	}

	public Action createSetFrontCameraAction() {
		ChooseCameraAction action = action(ChooseCameraAction.class);
		action.setFrontCamera();
		return action;
	}

	public Action createSetBackCameraAction() {
		ChooseCameraAction action = action(ChooseCameraAction.class);
		action.setBackCamera();
		return action;
	}

	public Action createPreviousLookAction(Sprite sprite) {
		PreviousLookAction action = action(PreviousLookAction.class);
		action.setSprite(sprite);
		return action;
	}

	public Action createStopScriptAction(int spinnerSelection, Script currentScript) {
		switch (spinnerSelection) {
			case BrickValues.STOP_THIS_SCRIPT:
				StopThisScriptAction stopThisScriptAction = Actions.action(StopThisScriptAction.class);
				stopThisScriptAction.setCurrentScript(currentScript);
				return stopThisScriptAction;
			case BrickValues.STOP_OTHER_SCRIPTS:
				StopOtherScriptsAction stopOtherScriptsAction = Actions.action(StopOtherScriptsAction.class);
				stopOtherScriptsAction.setCurrentScript(currentScript);
				return stopOtherScriptsAction;
			default:
				return Actions.action(StopAllScriptsAction.class);
		}
	}

	public Action createAssertEqualsAction(Sprite sprite, Formula actual, Formula expected,
			UserVariable actualVariable, UserVariable expectedVariable, UserVariable setupVariable) {
		AssertEqualsAction action = action(AssertEqualsAction.class);
		action.setActual(actual);
		action.setExpected(expected);

		action.setSprite(sprite);

		action.setActualVariable(actualVariable);
		action.setExpectedVariable(expectedVariable);
		action.setSetupVariable(setupVariable);

		return action;
	}

	public Action createTapAtAction(Sprite sprite, Formula x, Formula y) {
		TapAtAction action = Actions.action(TapAtAction.class);
		action.setPosition(x, y);
		action.setSprite(sprite);
		return action;
	}

	public Action createWriteVariableOnDeviceAction(UserVariable userVariable) {
		WriteVariableOnDeviceAction action = Actions.action(WriteVariableOnDeviceAction.class);
		action.setUserVariable(userVariable);

		return action;
	}

	public Action createWriteListOnDeviceAction(UserList userList) {
		WriteListOnDeviceAction action = Actions.action(WriteListOnDeviceAction.class);
		action.setUserList(userList);

		return action;
	}

	public Action createWaitTillIdleAction() {
		return action(WaitTillIdleAction.class);
	}

	public Action createReadVariableFromDeviceAction(UserVariable userVariable) {
		ReadVariableFromDeviceAction action = Actions.action(ReadVariableFromDeviceAction.class);
		action.setUserVariable(userVariable);

		return action;
	}

	public Action createReadListFromDeviceAction(UserList userList) {
		ReadListFromDeviceAction action = Actions.action(ReadListFromDeviceAction.class);
		action.setUserList(userList);

		return action;
	}

	public Action createWebRequestAction(Sprite sprite, Formula variableFormula, UserVariable userVariable) {
		WebRequestAction action = action(WebRequestAction.class);
		action.setSprite(sprite);
		action.setFormula(variableFormula);
		action.interpretUrl();
		action.setUserVariable(userVariable);
		action.setWebConnectionFactory(new WebConnectionFactory());
		return action;
	}
}

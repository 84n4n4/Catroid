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
package org.catrobat.catroid.catrobatTestRunner;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.EventWrapper;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.eventids.BroadcastEventId;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.uiespresso.util.actions.CustomActions;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;

import static junit.framework.Assert.assertEquals;

import static org.catrobat.catroid.uiespresso.util.UserVariableAssertions.assertUserVariableEqualsWithTimeout;

@RunWith(AndroidJUnit4.class)
public class InjectBroadcastTest {

	@Rule
	public BaseActivityInstrumentationRule<StageActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(StageActivity.class, true, false);

	private Project project;
	private UserVariable userVariable;
	private Sprite sprite;

	private String setUp = "setUp";
	private String runTest = "runTest";

	@Before
	public void setUp() throws Exception {
		createProject();
	}

	@Test
	public void testInjectBroadcast() {
		baseActivityTestRule.launchActivity(null);
		onView(isRoot()).perform(CustomActions.wait(1000));

		fireBroadcast(setUp);

		assertUserVariableEqualsWithTimeout(userVariable, 27, 2000);
	}

	private void createProject() {
		project = new Project(InstrumentationRegistry.getTargetContext(), getClass().getSimpleName());
		ProjectManager.getInstance().setCurrentProject(project);
		ProjectManager.getInstance().setCurrentlyEditedScene(project.getDefaultScene());
		sprite = new Sprite("TestLifecycleSprite");
		project.getDefaultScene().addSprite(sprite);
		ProjectManager.getInstance().setCurrentSprite(sprite);

		BroadcastScript broadcastScript = new BroadcastScript(setUp);

		DataContainer dataContainer = project.getDefaultScene().getDataContainer();
		userVariable = new UserVariable("userVariable27");
		dataContainer.addUserVariable(userVariable);

		SetVariableBrick setVariableBrick = new SetVariableBrick(new Formula(27), userVariable);
		broadcastScript.addBrick(setVariableBrick);


		sprite.addScript(broadcastScript);

		ProjectManager.getInstance().saveProject(InstrumentationRegistry.getTargetContext());
	}

	public void fireBroadcast(String broadcastMessage) {
		BroadcastEventId id = new BroadcastEventId(broadcastMessage);
		EventWrapper event = new EventWrapper(id, EventWrapper.NO_WAIT);
		project.fireToAllSprites(event);
	}
}

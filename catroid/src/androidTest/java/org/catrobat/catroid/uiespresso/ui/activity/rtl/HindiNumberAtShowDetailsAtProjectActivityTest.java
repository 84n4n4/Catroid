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

package org.catrobat.catroid.uiespresso.ui.activity.rtl;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.uiespresso.testsuites.Cat;
import org.catrobat.catroid.uiespresso.testsuites.Level;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class HindiNumberAtShowDetailsAtProjectActivityTest {
	@Rule
	public BaseActivityInstrumentationRule<ProjectActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(ProjectActivity.class);
	private Locale arLocale = new Locale("ar");
	private String expectedHindiNumberOfScripts = "٢"; // 2
	private String expectedHindiNumberOfBricks = "٧"; // 7
	private String expectedHindiNumberOfLooks = "٢"; // 2
	private String expectedHindiNumberOfSounds = "٠"; // 0

	@Before
	public void setUp() {
		createProject();
		SettingsActivity.setLanguageSharedPreference(getTargetContext(), "ar");
		baseActivityTestRule.launchActivity(null);
		// setShowDetails()
	}

	@After
	public void tearDown() {
		SettingsActivity.removeLanguageSharedPreference(getTargetContext());
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void hindiNumbers() throws Exception {
		assertEquals(Locale.getDefault().getDisplayLanguage(), arLocale.getDisplayLanguage());
		assertTrue(RtlUiTestUtils.checkTextDirectionIsRtl(Locale.getDefault().getDisplayName()));

		onView(allOf(withId(R.id.details_left_bottom), isDisplayed()))
				.check(matches(withText(UiTestUtils.getResourcesString(R.string.number_of_scripts) + " " + expectedHindiNumberOfScripts)));

		onView(allOf(withId(R.id.details_right_bottom), isDisplayed()))
				.check(matches(withText(UiTestUtils.getResourcesString(R.string.number_of_bricks) + " " + expectedHindiNumberOfBricks)));

		onView(allOf(withId(R.id.details_left_top), isDisplayed()))
				.check(matches(withText(UiTestUtils.getResourcesString(R.string.number_of_looks) + " " + expectedHindiNumberOfLooks)));

		onView(allOf(withId(R.id.details_right_top), isDisplayed()))
				.check(matches(withText(UiTestUtils.getResourcesString(R.string.number_of_sounds) + " " + expectedHindiNumberOfSounds)));
	}

	private void createProject() {
		String projectName = "HindiNumberTest";
		Project project = new Project(null, projectName);

		Sprite firstSprite = new SingleSprite("firstSprite");

		Script firstScript = new StartScript();
		firstScript.addBrick(new SetXBrick());
		firstScript.addBrick(new SetXBrick());
		firstSprite.addScript(firstScript);

		Script secondScript = new StartScript();
		secondScript.addBrick(new SetXBrick());
		secondScript.addBrick(new SetYBrick());
		secondScript.addBrick(new SetXBrick());
		firstSprite.addScript(secondScript);

		LookData lookData = new LookData();
		lookData.setFileName("red");
		lookData.setName("red");
		firstSprite.getLookList().add(lookData);

		LookData anotherLookData = new LookData();
		anotherLookData.setFileName("blue");
		anotherLookData.setName("blue");
		firstSprite.getLookList().add(anotherLookData);

		project.getDefaultScene().addSprite(firstSprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(firstSprite);
	}
}

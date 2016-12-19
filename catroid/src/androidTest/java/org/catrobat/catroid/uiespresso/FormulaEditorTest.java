package org.catrobat.catroid.uiespresso;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.ui.BaseActivity;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.uiespresso.actions.CustomActions;
import org.catrobat.catroid.uiespresso.util.BaseActivityInstrumentationRule;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertTrue;

import static org.catrobat.catroid.R.id.brick_set_variable_edit_text;

import static java.lang.Character.toUpperCase;

@RunWith(AndroidJUnit4.class)
public class FormulaEditorTest {
	private static final String TAG = FormulaEditorTest.class.getSimpleName();

	private Project project;
	private BaseActivity scriptActivity;

	@Rule
	public BaseActivityInstrumentationRule<ScriptActivity> BaseActivityTestRule = new
			BaseActivityInstrumentationRule<>(ScriptActivity.class, true, false);

	@Before
	public void setUp() throws Exception {
		project = UiTestUtils.createProject("formulaEditorInputTest");
		BaseActivityTestRule.launchActivity(null);
	}

	@Test
	public void numericValuesTest(){
		onView(withId(R.id.brick_set_variable_edit_text)).perform(click());
		onView(isRoot()).perform(CustomActions.wait(1000));

		//typeText not working for formula editor, so use CustomActions.typeInValue
		onView(withId(R.id.formula_editor_edit_field)).perform(CustomActions.typeInValue("12345,678"));
		onView(isRoot()).perform(CustomActions.wait(3000));

		onView(withId(R.id.formula_editor_keyboard_ok)).perform(click());
		onView(isRoot()).perform(CustomActions.wait(8000));
	}

	@After
	public void tearDown() throws Exception {
	}
}

package org.catrobat.catroid.uiespresso;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.BaseActivity;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.uiespresso.util.BaseActivityInstrumentationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class FormulaEditorKeyboardTest {
	private static final String TAG = FormulaEditorKeyboardTest.class.getSimpleName();

	private Project project;
	private BaseActivity scriptActivity;
	DataContainer projectDataContainer;
	UserVariable userVariable;

	@Rule
	public BaseActivityInstrumentationRule<ScriptActivity> BaseActivityTestRule = new
			BaseActivityInstrumentationRule<>(ScriptActivity.class, true, false);

	@Before
	public void setUp() throws Exception {
		project = createProject("formulaEditorKeyboardTest");
		BaseActivityTestRule.launchActivity(null);
	}

	@Test
	public void numericKeysTest(){
		//best practice - get the object you are manipulating on the frontend
		userVariable.setValue((double)0);
		onView(withId(R.id.brick_set_variable_edit_text)).perform(click());

		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_2)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_3)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_4)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_5)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_6)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_7)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_8)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_9)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_0)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_decimal_mark)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_ok)).perform(click());

		//and assert that the object directly instead of checking via frontend
		assert(userVariable.getValue().equals(1234567890.1));
	}

	@Test
	public void basicMathOperatorKeysTest(){
		userVariable.setValue((double)0);
		onView(withId(R.id.brick_set_variable_edit_text)).perform(click());

		onView(withId(R.id.formula_editor_keyboard_bracket_open)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_bracket_close)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_plus)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_minus)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_mult)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_divide)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_equal)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_1)).perform(click());
		onView(withId(R.id.formula_editor_keyboard_ok)).perform(click());

		//if however mocking an object to compare it to would be too complicated, check via frontend
		//here make sure to check not only that it is displayed somewhere on the screen, but in the very element
		//it should be shown.
		onView(allOf(withId(R.id.brick_set_variable_edit_text), isDisplayed()))
				.check(matches(withText("( 1 ) + 1 - 1 × 1 ÷ 1 = 1 ")));
	}

	@Test
	public void enterStringTest(){
		userVariable.setValue((double)0);
		onView(withId(R.id.brick_set_variable_edit_text)).perform(click());

		onView(withId(R.id.formula_editor_keyboard_string)).perform(click());

		onView(withText(R.string.formula_editor_new_string_name)).check(matches(isDisplayed()));
		onView(withId(R.id.formula_editor_string_name_edit_text)).perform(typeText("Foo"));
		onView(withText(R.string.ok)).perform(click());

		onView(withId(R.id.formula_editor_keyboard_ok)).perform(click());
		assert(userVariable.getValue().equals("Foo"));
	}

	@After
	public void tearDown() throws Exception {
	}

	public Project createProject(String projectName) {
		Project project = new Project(null, projectName);
		Sprite sprite = new Sprite("testSprite");
		Script script = new StartScript();

		SetVariableBrick setVariableBrick = new SetVariableBrick();
		DataContainer dataContainer = project.getDefaultScene().getDataContainer();
		userVariable = dataContainer.addProjectUserVariable("Global1");
		setVariableBrick.setUserVariable(userVariable);

		script.addBrick(setVariableBrick);
		sprite.addScript(script);
		project.getDefaultScene().addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);

		return project;
	}
}

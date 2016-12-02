package org.catrobat.catroid.uiespresso;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.MainMenuActivity;
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

@RunWith(AndroidJUnit4.class)
public class MainMenuActivityTest{
	private static final String TAG = SmokeTest.class.getSimpleName();
	private IdlingResource mIdlingResource;

	@Rule
	public BaseActivityInstrumentationRule<MainMenuActivity> BaseActivityTestRule = new
			BaseActivityInstrumentationRule<>(MainMenuActivity.class);

	@Before
	public void registerIdlingResource() {
		//class under test, in this case the mainMenuActivity has to implement getIdlingResource that returns
		//the idlingResource instance it has. (in this case its a countingIdlingResource
		mIdlingResource = BaseActivityTestRule.getActivity().getIdlingResource();
		Espresso.registerIdlingResources(mIdlingResource);
	}

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void newProject(){
		onView(withId(R.id.main_menu_button_new)).perform(click());

		//check if dialog title is displayed
		onView(withText(R.string.new_project_dialog_title)).check(matches(isDisplayed()));

		//enter new project name
		onView(withId(R.id.project_name_edittext)).perform(typeText("TestProject"));
		onView(withText(R.string.ok)).perform(click());

		//check if orientation dialog is displayed
		onView(withText(R.string.project_orientation_title)).check(matches(isDisplayed()));
		onView(withText(R.string.ok)).perform(click());


	}

	@After
	public void tearDown() throws Exception {
	}

	@After
	public void unregisterResource() {
		Espresso.unregisterIdlingResources(mIdlingResource);
	}
}

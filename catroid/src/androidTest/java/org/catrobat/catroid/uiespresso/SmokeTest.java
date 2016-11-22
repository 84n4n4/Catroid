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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SmokeTest{
	private static final String TAG = SmokeTest.class.getSimpleName();
	private IdlingResource mIdlingResource;

	@Rule
	public BaseActivityInstrumentationRule<MainMenuActivity> BaseActivityTestRule = new
			BaseActivityInstrumentationRule<>(MainMenuActivity.class);

	@Before
	public void registerIdlingResource() {
		mIdlingResource = BaseActivityTestRule.getActivity().getIdlingResource();
		Espresso.registerIdlingResources(mIdlingResource);
	}

	@Before
	public void setUp() throws Exception {

	}

//
//	@Test
//	public void testYay(){
//		onView(withId(R.id.main_menu_button_new))
//				.perform(click());
//		try {
//			Thread.sleep(10000);
//		}
//		catch (InterruptedException e) {
//			//donothing
//		}
//	}

	@Test
	public void newProject(){
		onView(withId(R.id.main_menu_button_new)).perform(click());
		onView(withId(R.id.project_name_edittext)).perform(typeText("TestProject"));
		onView(withText(R.string.ok)).perform(click());
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

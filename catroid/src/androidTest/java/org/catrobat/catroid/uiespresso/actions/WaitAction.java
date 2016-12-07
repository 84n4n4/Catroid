package org.catrobat.catroid.uiespresso.actions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;


public class WaitAction implements ViewAction {

	int milliSeconds;

	public WaitAction(int milliSeconds){
		this.milliSeconds = milliSeconds;
	}

	@Override
	public String getDescription() {
		return "Wait for X milliseconds";
	}

	@Override
	public Matcher<View> getConstraints() {
		return isDisplayed();
	}

	@Override
	public void perform(UiController uiController, View view) {
		uiController.loopMainThreadUntilIdle();
		uiController.loopMainThreadForAtLeast(milliSeconds);
	}
}

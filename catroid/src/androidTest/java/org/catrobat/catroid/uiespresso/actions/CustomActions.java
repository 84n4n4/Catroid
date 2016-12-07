package org.catrobat.catroid.uiespresso.actions;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import org.catrobat.catroid.R;
import org.catrobat.catroid.formulaeditor.FormulaEditorEditText;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.hamcrest.core.AllOf.allOf;

public class CustomActions {
	public static ViewAction wait(final int milliSeconds) {
		return new ViewAction() {
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
		};
	}

	public static ViewAction typeInValue(final String value){
		return new ViewAction(){
			@Override
			public Matcher<View> getConstraints() {
				return allOf(isDisplayed(), withId(R.id.formula_editor_edit_field));
			}

			@Override
			public String getDescription() {
				return "type value in formula editor keyboard";
			}

			@Override
			public void perform(UiController uiController, View view) {
				FormulaEditorEditText formulaEditorEditText = (FormulaEditorEditText) view;

				//formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_1,"");

				for (char item : value.toCharArray()) {
					switch (item) {
						case '-':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_minus,"");
							break;
						case '0':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_0,"");
							break;
						case '1':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_1,"");
							break;
						case '2':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_2,"");
							break;
						case '3':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_3,"");
							break;
						case '4':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_4,"");
							break;
						case '5':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_5,"");
							break;
						case '6':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_6,"");
							break;
						case '7':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_7,"");
							break;
						case '8':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_8,"");
							break;
						case '9':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_9,"");
							break;
						case '.':
						case ',':
							formulaEditorEditText.handleKeyEvent(R.id.formula_editor_keyboard_decimal_mark,"");
					}
				}
			}
		};
	}
}
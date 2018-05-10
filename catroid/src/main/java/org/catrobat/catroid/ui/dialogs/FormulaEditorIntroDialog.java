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

package org.catrobat.catroid.ui.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.LinkedList;
import java.util.Queue;

import static org.catrobat.catroid.ui.fragment.FormulaEditorFragment.FORMULA_EDITOR_FRAGMENT_TAG;

public class FormulaEditorIntroDialog extends Dialog implements View.OnClickListener {

	TextView introTitle;
	TextView introSummary;
	private Queue<IntroSlide> introSlides;
	private FormulaEditorFragment formulaEditorFragment;

	public FormulaEditorIntroDialog(FormulaEditorFragment formulaEditorFragment, int style) {
		super(formulaEditorFragment.getActivity(), style);
		this.formulaEditorFragment = formulaEditorFragment;
		fillIntroDialogSlides();
	}

	private FormulaEditorIntroDialog(FormulaEditorFragment formulaEditorFragment, int style,
			Queue<IntroSlide> remainingIntroslides) {
		super(formulaEditorFragment.getActivity(), style);
		this.formulaEditorFragment = formulaEditorFragment;
		introSlides = remainingIntroslides;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_formula_editor_intro);

		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT
				, WindowManager.LayoutParams.WRAP_CONTENT);

		Drawable background = new ColorDrawable(Color.BLACK);
		background.setAlpha(130);
		getWindow().setBackgroundDrawable(background);
		getWindow().setDimAmount(0f);

		getWindow().setGravity(Gravity.CENTER);

		introTitle = (TextView) findViewById(R.id.intro_dialog_title);

		introSummary = (TextView) findViewById(R.id.intro_dialog_summary);

		(findViewById(R.id.intro_dialog_skip_button)).setOnClickListener(this);
		(findViewById(R.id.intro_dialog_next_button)).setOnClickListener(this);

		introSlides.remove().applySlide(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.intro_dialog_skip_button:
				onBackPressed();
				break;
			case R.id.intro_dialog_next_button:
				nextSlide();
			default:
				break;
		}
	}

	private void nextSlide() {
		if(introSlides.isEmpty()) {
			dismiss();
		} else {
			dismiss();
			new FormulaEditorIntroDialog(formulaEditorFragment, R.style.stage_dialog, introSlides).show();
		}
	}

	@Override
	public void onBackPressed() {
		//TODO write to shared preferences that its been skipped
		super.onBackPressed();
	}

	private static int NONE = -1;
	private class IntroSlide {
		int titleStringId;
		int summaryStringId;
		int targetViewId;

		IntroSlide(int titleStringId,
				int summaryStringId,
				int targetViewId) {
			this.titleStringId = titleStringId;
			this.summaryStringId = summaryStringId;
			this.targetViewId = targetViewId;
		}

		void applySlide(FormulaEditorIntroDialog dialog) {
			dialog.introTitle.setText(titleStringId);
			dialog.introSummary.setText(summaryStringId);

			if(targetViewId != NONE) {
				int[] targetLocation = new int[2];
				formulaEditorFragment.getView().findViewById(targetViewId).getLocationOnScreen(targetLocation); //PX!
				int fragmentHeight = formulaEditorFragment.getView().getMeasuredHeight();
				dialog.getWindow().setGravity(Gravity.TOP);

				WindowManager.LayoutParams dialogLayoutParams = dialog.getWindow().getAttributes();
				if(targetLocation[1] > fragmentHeight / 2)
				{
					dialogLayoutParams.y = targetLocation[1] - dialogLayoutParams.height;

				} else {
					dialogLayoutParams.y = targetLocation[1];
				}
				dialog.getWindow().setAttributes(dialogLayoutParams);
			} else {
				dialog.getWindow().setGravity(Gravity.CENTER);
			}
		}
	}

	private void fillIntroDialogSlides() {
		introSlides = new LinkedList<>();
		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_formula_editor,
				R.string.formula_editor_intro_summary_formula_editor,
				NONE));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_input_field,
				R.string.formula_editor_intro_summary_input_field,
				R.id.formula_editor_edit_field));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_keyboard,
				R.string.formula_editor_intro_summary_keyboard,
				R.id.formula_editor_keyboard_7));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_compute,
				R.string.formula_editor_intro_summary_compute,
				R.id.formula_editor_keyboard_compute));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_object,
				R.string.formula_editor_intro_summary_object,
				R.id.formula_editor_keyboard_object));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_functions,
				R.string.formula_editor_intro_summary_functions,
				R.id.formula_editor_keyboard_function));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_logic,
				R.string.formula_editor_intro_summary_logic,
				R.id.formula_editor_keyboard_logic));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_device,
				R.string.formula_editor_intro_summary_device,
				R.id.formula_editor_keyboard_sensors));

		introSlides.add(new IntroSlide(
				R.string.formula_editor_intro_title_data,
				R.string.formula_editor_intro_summary_data,
				R.id.formula_editor_keyboard_data));
	}
}

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
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.ui.fragment.ColorSeekbar;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class PhiroRGBLightBrick extends FormulaBrick {
	private static final long serialVersionUID = 1L;

	public enum Eye {
		LEFT, RIGHT, BOTH
	}

	private String eye;
	private transient Eye eyeEnum;

	private transient ColorSeekbar colorSeekbar = new ColorSeekbar(this, BrickField.PHIRO_LIGHT_RED,
			BrickField.PHIRO_LIGHT_GREEN, BrickField.PHIRO_LIGHT_BLUE);

	protected Object readResolve() {
		if (eye != null) {
			eyeEnum = Eye.valueOf(eye);
		}
		return this;
	}

	public PhiroRGBLightBrick() {
		addAllowedBrickField(BrickField.PHIRO_LIGHT_RED);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_GREEN);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_BLUE);
	}

	public PhiroRGBLightBrick(Eye eye, int red, int green, int blue) {
		this.eyeEnum = eye;
		this.eye = eyeEnum.name();

		initializeBrickFields(new Formula(red), new Formula(green), new Formula(blue));
	}

	public PhiroRGBLightBrick(String eye, Formula red, Formula green, Formula blue) {
		if (eye != null) {
			this.eye = eye;
			readResolve();
		}

		initializeBrickFields(red, green, blue);
	}

	private void initializeBrickFields(Formula red, Formula green, Formula blue) {
		addAllowedBrickField(BrickField.PHIRO_LIGHT_RED);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_GREEN);
		addAllowedBrickField(BrickField.PHIRO_LIGHT_BLUE);
		setFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED, red);
		setFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN, green);
		setFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE, blue);
	}

	@Override
	public int getRequiredResources() {
		return BLUETOOTH_PHIRO
				| getFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED).getRequiredResources()
				| getFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN).getRequiredResources()
				| getFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE).getRequiredResources();
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = super.getPrototypeView(context);

		TextView textValueRed = prototypeView.findViewById(R.id.brick_phiro_rgb_led_action_red_edit_text);
		textValueRed.setText(formatNumberForPrototypeView(BrickValues.PHIRO_VALUE_RED));

		TextView textValueGreen = prototypeView.findViewById(R.id.brick_phiro_rgb_led_action_green_edit_text);
		textValueGreen.setText(formatNumberForPrototypeView(BrickValues.PHIRO_VALUE_GREEN));

		TextView textValueBlue = prototypeView.findViewById(R.id.brick_phiro_rgb_led_action_blue_edit_text);
		textValueBlue.setText(formatNumberForPrototypeView(BrickValues.PHIRO_VALUE_BLUE));

		Spinner eyeSpinner = prototypeView.findViewById(R.id.brick_phiro_rgb_light_spinner);

		ArrayAdapter<CharSequence> eyeAdapter = ArrayAdapter.createFromResource(context,
				R.array.brick_phiro_select_light_spinner, android.R.layout.simple_spinner_item);
		eyeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		eyeSpinner.setAdapter(eyeAdapter);
		eyeSpinner.setSelection(eyeEnum.ordinal());

		return prototypeView;
	}

	@Override
	public BrickBaseType clone() {
		return new PhiroRGBLightBrick(eye,
				getFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED).clone(),
				getFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN).clone(),
				getFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE).clone());
	}

	@Override
	public View getCustomView(Context context, int brickId, BaseAdapter baseAdapter) {
		return colorSeekbar.getView(context);
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_phiro_rgb_light;
	}

	@Override
	public View getView(Context context) {
		super.getView(context);
		TextView editRedValue = view.findViewById(R.id.brick_phiro_rgb_led_action_red_edit_text);
		getFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED).setTextFieldId(R.id.brick_phiro_rgb_led_action_red_edit_text);
		getFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED).refreshTextField(view);

		editRedValue.setOnClickListener(this);

		TextView editGreenValue = view.findViewById(R.id.brick_phiro_rgb_led_action_green_edit_text);
		getFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN).setTextFieldId(R.id.brick_phiro_rgb_led_action_green_edit_text);
		getFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN).refreshTextField(view);

		editGreenValue.setOnClickListener(this);

		TextView editBlueValue = view.findViewById(R.id.brick_phiro_rgb_led_action_blue_edit_text);
		getFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE).setTextFieldId(R.id.brick_phiro_rgb_led_action_blue_edit_text);
		getFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE).refreshTextField(view);

		editBlueValue.setOnClickListener(this);

		ArrayAdapter<CharSequence> eyeAdapter = ArrayAdapter.createFromResource(context,
				R.array.brick_phiro_select_light_spinner, android.R.layout.simple_spinner_item);
		eyeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner eyeSpinner = view.findViewById(R.id.brick_phiro_rgb_light_spinner);

		eyeSpinner.setAdapter(eyeAdapter);
		eyeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				eyeEnum = Eye.values()[position];
				eye = eyeEnum.name();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		if (eyeEnum == null) {
			readResolve();
		}
		eyeSpinner.setSelection(eyeEnum.ordinal());

		return view;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		if (areAllBrickFieldsNumbers()) {
			FormulaEditorFragment.showCustomFragment(view, this, getClickedBrickField(view));
		} else {
			FormulaEditorFragment.showFragment(view, this, getClickedBrickField(view));
		}
	}

	private boolean areAllBrickFieldsNumbers() {
		return (getFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED).getRoot().getElementType() == FormulaElement.ElementType.NUMBER)
				&& (getFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN).getRoot().getElementType() == FormulaElement.ElementType.NUMBER)
				&& (getFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE).getRoot().getElementType() == FormulaElement.ElementType.NUMBER);
	}

	private BrickField getClickedBrickField(View view) {
		switch (view.getId()) {
			case R.id.brick_phiro_rgb_led_action_green_edit_text:
				return BrickField.PHIRO_LIGHT_GREEN;
			case R.id.brick_phiro_rgb_led_action_blue_edit_text:
				return BrickField.PHIRO_LIGHT_BLUE;
			case R.id.brick_phiro_rgb_led_action_red_edit_text:
			default:
				return BrickField.PHIRO_LIGHT_RED;
		}
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createPhiroRgbLedEyeActionAction(sprite, eyeEnum,
				getFormulaWithBrickField(BrickField.PHIRO_LIGHT_RED),
				getFormulaWithBrickField(BrickField.PHIRO_LIGHT_GREEN),
				getFormulaWithBrickField(BrickField.PHIRO_LIGHT_BLUE)));
		return null;
	}
}

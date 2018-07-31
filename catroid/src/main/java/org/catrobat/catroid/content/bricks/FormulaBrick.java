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
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import org.catrobat.catroid.ui.adapter.BrickAdapter;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class FormulaBrick extends BrickBaseType implements View.OnClickListener {

	ConcurrentFormulaHashMap formulaMap = new ConcurrentFormulaHashMap();

	private transient BiMap<BrickField, Integer> brickFieldToTextViewIdMap = HashBiMap.create(2);

	public Formula getFormulaWithBrickField(BrickField brickField) throws IllegalArgumentException {
		if (formulaMap.containsKey(brickField)) {
			return formulaMap.get(brickField);
		} else {
			throw new IllegalArgumentException("Incompatible Brick Field : " + brickField.toString());
		}
	}

	public void setFormulaWithBrickField(BrickField brickField, Formula formula) throws IllegalArgumentException {
		if (formulaMap.containsKey(brickField)) {
			formulaMap.replace(brickField, formula);
		} else {
			throw new IllegalArgumentException("Incompatible Brick Field : " + brickField.toString());
		}
	}

	protected void addAllowedBrickField(BrickField brickField) {
		formulaMap.putIfAbsent(brickField, new Formula(0));
	}

	protected void addAllowedBrickField(BrickField brickField, int textViewResourceId) {
		formulaMap.putIfAbsent(brickField, new Formula(0));
		brickFieldToTextViewIdMap.put(brickField, textViewResourceId);
	}

	void replaceFormulaBrickField(BrickField oldBrickField, BrickField newBrickField) {
		if (formulaMap.containsKey(oldBrickField)) {
			Formula brickFormula = formulaMap.get(oldBrickField);
			formulaMap.remove(oldBrickField);
			formulaMap.put(newBrickField, brickFormula);
		}
	}

	@Override
	public BrickBaseType clone() throws CloneNotSupportedException {
		FormulaBrick clone = (FormulaBrick) super.clone();
		clone.formulaMap = formulaMap.clone();
		return clone;
	}

	@Override
	public View getView(Context context) {
		super.getView(context);

		for (BiMap.Entry<BrickField, Integer> entry : brickFieldToTextViewIdMap.entrySet()) {
			TextView brickFieldView = view.findViewById(entry.getValue());
			brickFieldView.setText(getFormulaWithBrickField(entry.getKey()).getTrimmedFormulaString(context));
			brickFieldView.setOnClickListener(this);
		}

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = super.getPrototypeView(context);

		for (BiMap.Entry<BrickField, Integer> entry : brickFieldToTextViewIdMap.entrySet()) {
			TextView brickFieldView = prototypeView.findViewById(entry.getValue());
			brickFieldView.setText(getFormulaWithBrickField(entry.getKey()).getTrimmedFormulaString(context));
		}

		return prototypeView;
	}

	public List<Formula> getFormulas() {
		return new ArrayList<>(formulaMap.values());
	}

	@Override
	public void onClick(View view) {
		if (adapter == null) {
			return;
		}
		if (adapter.getActionMode() != BrickAdapter.ActionModeEnum.NO_ACTION) {
			return;
		}
		if (adapter.isDragging) {
			return;
		}
		showFormulaEditorToEditFormula(view);
	}

	public void showFormulaEditorToEditFormula(View view) {
		if (brickFieldToTextViewIdMap.inverse().containsKey(view.getId())) {
			FormulaEditorFragment.showFragment(view, this, brickFieldToTextViewIdMap.inverse().get(view.getId()));
		} else {
			FormulaEditorFragment.showFragment(view, this, formulaMap.keys().nextElement());
		}
	}

	public View getCustomView(Context context, int brickId, BaseAdapter baseAdapter) {
		return null;
	}

	protected void setSecondsLabel(View view, BrickField brickField) {
		TextView textView = view.findViewById(R.id.brick_seconds_label);
		Context context = textView.getContext();

		if (getFormulaWithBrickField(brickField).isSingleNumberFormula()) {
			try {
				Double formulaValue = formulaMap.get(brickField)
						.interpretDouble(ProjectManager.getInstance().getCurrentSprite());
				textView.setText(context.getResources().getQuantityString(R.plurals.second_plural,
						Utils.convertDoubleToPluralInteger(formulaValue)));
			} catch (InterpretationException e) {
				Log.e(getClass().getSimpleName(),
						"Interpretation of formula failed, fallback to quantity \"other\"", e);
			}
			return;
		}
		textView.setText(context.getResources()
				.getQuantityString(R.plurals.second_plural, Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
	}
}

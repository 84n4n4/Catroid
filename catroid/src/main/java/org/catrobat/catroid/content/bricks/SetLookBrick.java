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

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.brickspinner.SpinnerAdapterWithNewOption;
import org.catrobat.catroid.ui.recyclerview.dialog.NewLookDialogFragment;
import org.catrobat.catroid.ui.recyclerview.dialog.dialoginterface.NewItemInterface;

import java.util.ArrayList;
import java.util.List;

public class SetLookBrick extends BrickBaseType implements
		SpinnerAdapterWithNewOption.OnNewOptionInDropDownClickListener,
		NewItemInterface<LookData> {

	private static final long serialVersionUID = 1L;

	protected LookData look;
	private transient LookData previouslySelectedLook;

	private transient Spinner spinner;
	private transient SpinnerAdapterWithNewOption spinnerAdapter;

	protected transient boolean wait;

	public SetLookBrick() {
		wait = false;
	}

	public LookData getLook() {
		return this.look;
	}

	public void setLook(LookData lookData) {
		this.look = lookData;
	}

	@Override
	public Brick clone() {
		SetLookBrick clone = new SetLookBrick();
		clone.setLook(look);
		return clone;
	}

	private LookData getLookByName(String name) {
		for (LookData look : ProjectManager.getInstance().getCurrentSprite().getLookList()) {
			if (look.getName().equals(name)) {
				return look;
			}
		}
		return null;
	}

	private List<String> getLookNames() {
		List<String> lookNames = new ArrayList<>();
		for (LookData look : ProjectManager.getInstance().getCurrentSprite().getLookList()) {
			lookNames.add(look.getName());
		}
		return lookNames;
	}

	@Override
	public View getView(final Context context, int brickId, BaseAdapter baseAdapter) {

		view = View.inflate(context, R.layout.brick_set_look, null);
		view = BrickViewProvider.setAlphaOnView(view, alphaValue);
		setCheckboxView(R.id.brick_set_look_checkbox);

		spinner = view.findViewById(R.id.brick_set_look_spinner);
		spinnerAdapter = new SpinnerAdapterWithNewOption(context, getLookNames());
		spinnerAdapter.setOnDropDownItemClickListener(this);

		spinner.setAdapter(spinnerAdapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position != 0) {
					look = getLookByName(spinnerAdapter.getItem(position));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinner.setSelection(spinnerAdapter.getPosition(look != null ? look.getName() : null));

		if (getSprite().getName().equals(context.getString(R.string.background))) {
			TextView textField = view.findViewById(R.id.brick_set_look_prototype_text_view);
			textField.setText(R.string.brick_set_background);
		}

		if (!wait) {
			view.findViewById(R.id.brick_set_look_and_wait).setVisibility(View.GONE);
		}

		return view;
	}

	@Override
	public boolean onNewOptionInDropDownClicked(View v) {
		previouslySelectedLook = look;
		new NewLookDialogFragment(this,
				ProjectManager.getInstance().getCurrentScene(),
				ProjectManager.getInstance().getCurrentSprite()) {

			@Override
			public void onCancel(DialogInterface dialog) {
				super.onCancel(dialog);
				look = previouslySelectedLook;
				spinner.setSelection(spinnerAdapter.getPosition(look != null ? look.getName() : null));
			}
		}.show(((Activity) v.getContext()).getFragmentManager(), NewLookDialogFragment.TAG);
		return false;
	}

	@Override
	public void addItem(LookData item) {
		ProjectManager.getInstance().getCurrentSprite().getLookList().add(item);
		spinnerAdapter.add(item.getName());
		look = item;
		spinner.setSelection(spinnerAdapter.getPosition(item.getName()));
	}

	@Override
	public View getPrototypeView(Context context) {
		View view = View.inflate(context, R.layout.brick_set_look, null);

		if (getSprite().getName().equals(context.getString(R.string.background))) {
			TextView textField = view.findViewById(R.id.brick_set_look_prototype_text_view);
			textField.setText(R.string.brick_set_background);
		}

		if (!wait) {
			view.findViewById(R.id.brick_set_look_and_wait).setVisibility(View.GONE);
		}
		spinner = view.findViewById(R.id.brick_set_look_spinner);

		spinnerAdapter = new SpinnerAdapterWithNewOption(context, getLookNames());
		spinner.setAdapter(spinnerAdapter);
		spinner.setSelection(spinnerAdapter.getPosition(look != null ? look.getName() : null));
		return view;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetLookAction(sprite, look, wait));
		return null;
	}

	protected Sprite getSprite() {
		return ProjectManager.getInstance().getCurrentSprite();
	}
}

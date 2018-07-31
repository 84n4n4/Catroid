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
import android.widget.Spinner;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ScriptSequenceAction;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.ui.adapter.DataAdapter;
import org.catrobat.catroid.ui.adapter.UserListAdapterWrapper;

import java.util.List;

public class AddItemToUserListBrick extends UserListBrick {

	private static final long serialVersionUID = 1L;

	public AddItemToUserListBrick() {
		this(BrickValues.ADD_ITEM_TO_USERLIST);
	}

	public AddItemToUserListBrick(double value) {
		this(new Formula(value));
	}

	public AddItemToUserListBrick(Formula formula) {
		addAllowedBrickField(BrickField.LIST_ADD_ITEM, R.id.brick_add_item_to_userlist_edit_text);
		setFormulaWithBrickField(BrickField.LIST_ADD_ITEM, formula);
	}

	@Override
	public int getViewResource() {
		return R.layout.brick_add_item_to_userlist;
	}

	@Override
	public View getView(Context context) {
		super.getView(context);

		Spinner userListSpinner = view.findViewById(R.id.add_item_to_userlist_spinner);
		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentlyEditedScene().getDataContainer()
				.createDataAdapter(context, ProjectManager.getInstance().getCurrentSprite());
		UserListAdapterWrapper userListAdapterWrapper = new UserListAdapterWrapper(context, dataAdapter);
		userListAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);

		userListSpinner.setAdapter(userListAdapterWrapper);

		setSpinnerSelection(userListSpinner, null);

		userListSpinner.setOnTouchListener(createSpinnerOnTouchListener());
		userListSpinner.setOnItemSelectedListener(createListSpinnerItemSelectedListener());
		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = super.getPrototypeView(context);
		Spinner userListSpinner = prototypeView.findViewById(R.id.add_item_to_userlist_spinner);

		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentlyEditedScene().getDataContainer()
				.createDataAdapter(context, ProjectManager.getInstance().getCurrentSprite());

		UserListAdapterWrapper userListAdapterWrapper = new UserListAdapterWrapper(context, dataAdapter);

		userListAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);
		userListSpinner.setAdapter(userListAdapterWrapper);
		setSpinnerSelection(userListSpinner, null);

		return prototypeView;
	}

	@Override
	public void onNewList(UserList userList) {
		Spinner spinner = view.findViewById(R.id.add_item_to_userlist_spinner);
		setSpinnerSelection(spinner, userList);
	}

	@Override
	public List<ScriptSequenceAction> addActionToSequence(Sprite sprite, ScriptSequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory()
				.createAddItemToUserListAction(sprite, getFormulaWithBrickField(BrickField.LIST_ADD_ITEM), userList));
		return null;
	}
}

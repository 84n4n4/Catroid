/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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

package org.catrobat.catroid.ui.recyclerview.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.catrobat.catroid.formulaeditor.UserData;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.recyclerview.viewholder.ViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<ViewHolder> implements RVAdapter.SelectionListener {

	public boolean allowMultiSelection = true;

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({VAR_GLOBAL, VAR_LOCAL, LIST_GLOBAL, LIST_LOCAL})
	@interface DataType {}
	private static final int VAR_GLOBAL = 0;
	private static final int VAR_LOCAL = 1;
	private static final int LIST_GLOBAL = 2;
	private static final int LIST_LOCAL = 3;

	private VariableRVAdapter globalVarAdapter;
	private VariableRVAdapter localVarAdapter;
	private ListRVAdapter globalListAdapter;
	private ListRVAdapter localListAdapter;

	private RVAdapter.SelectionListener selectionListener;

	public DataListAdapter(List<UserVariable> globalVars,
			List<UserVariable> localVars,
			List<UserList> globalLists,
			List<UserList> localLists) {
		globalVarAdapter = new VariableRVAdapter(globalVars);
		globalVarAdapter.setSelectionListener(this);

		localVarAdapter = new VariableRVAdapter(localVars){
			@Override
			protected void onCheckBoxClick(int position) {
				super.onCheckBoxClick(getRelativeItemPosition(position, VAR_LOCAL));
			}
		};
		localVarAdapter.setSelectionListener(this);

		globalListAdapter = new ListRVAdapter(globalLists) {
			@Override
			protected void onCheckBoxClick(int position) {
				super.onCheckBoxClick(getRelativeItemPosition(position, LIST_GLOBAL));
			}
		};
		globalListAdapter.setSelectionListener(this);

		localListAdapter = new ListRVAdapter(localLists) {
			@Override
			protected void onCheckBoxClick(int position) {
				super.onCheckBoxClick(getRelativeItemPosition(position, LIST_LOCAL));
			}
		};
		localListAdapter.setSelectionListener(this);
	}

	private int getRelativeItemPosition(int position, @DataType int dataType) {
		switch (dataType) {
			case VAR_GLOBAL:
				return position;
			case VAR_LOCAL:
				return position - globalVarAdapter.getItemCount();
			case LIST_GLOBAL:
				return position - (globalVarAdapter.getItemCount() + localVarAdapter.getItemCount());
			case LIST_LOCAL:
				return position - (globalVarAdapter.getItemCount()
						+ localVarAdapter.getItemCount()
						+ globalVarAdapter.getItemCount());
			default:
				throw new IllegalArgumentException("DataType is not specified: this would throw an index out of "
						+ "bounds exception.");
		}
	}

	public void showCheckBoxes(boolean visible) {
		globalVarAdapter.showCheckBoxes = visible;
		localVarAdapter.showCheckBoxes = visible;
		globalListAdapter.showCheckBoxes = visible;
		localListAdapter.showCheckBoxes = visible;
	}

	public void setSelectionListener(RVAdapter.SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}

	public void setOnItemClickListener(RVAdapter.OnItemClickListener onItemClickListener) {
		globalVarAdapter.setOnItemClickListener(onItemClickListener);
		localVarAdapter.setOnItemClickListener(onItemClickListener);
		globalListAdapter.setOnItemClickListener(onItemClickListener);
		localListAdapter.setOnItemClickListener(onItemClickListener);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, @DataType int viewType) {
		switch (viewType) {
			case VAR_GLOBAL:
				return globalVarAdapter.onCreateViewHolder(parent, viewType);
			case VAR_LOCAL:
				return localVarAdapter.onCreateViewHolder(parent, viewType);
			case LIST_GLOBAL:
				return globalListAdapter.onCreateViewHolder(parent, viewType);
			case LIST_LOCAL:
				return localListAdapter.onCreateViewHolder(parent, viewType);
			default:
				throw new IllegalArgumentException("viewType must be either variable or list.");
		}
	}

	@Override
	public @DataType int getItemViewType(int position) {
		if (position < globalVarAdapter.getItemCount()) {
			return VAR_GLOBAL;
		}
		if (position < (globalVarAdapter.getItemCount() + localVarAdapter.getItemCount())) {
			return VAR_LOCAL;
		}
		if (position < (globalVarAdapter.getItemCount()
				+ localVarAdapter.getItemCount()
				+ globalListAdapter.getItemCount())) {
			return LIST_GLOBAL;
		}
		if (position < (globalVarAdapter.getItemCount()
				+ localVarAdapter.getItemCount()
				+ globalListAdapter.getItemCount()
				+ localListAdapter.getItemCount())) {
			return LIST_LOCAL;
		}
		throw new ArrayIndexOutOfBoundsException("position is not within any of the adapters");
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		switch (getItemViewType(position)) {
			case VAR_GLOBAL:
				globalVarAdapter.onBindViewHolder(holder, position);
				break;
			case VAR_LOCAL:
				position -= globalVarAdapter.getItemCount();
				localVarAdapter.onBindViewHolder(holder, position);
				break;
			case LIST_GLOBAL:
				position -= (globalVarAdapter.getItemCount() + localVarAdapter.getItemCount());
				globalListAdapter.onBindViewHolder(holder, position);
				break;
			case LIST_LOCAL:
				position -= (globalVarAdapter.getItemCount()
						+ localVarAdapter.getItemCount()
						+ globalListAdapter.getItemCount());
				localListAdapter.onBindViewHolder(holder, position);
				break;
		}
	}

	@Override
	public void onSelectionChanged(int selectedItemCnt) {
		selectionListener.onSelectionChanged(globalVarAdapter.getSelectedItems().size()
				+ localVarAdapter.getSelectedItems().size()
				+ globalListAdapter.getSelectedItems().size()
				+ localListAdapter.getItemCount());
	}

	public void updateDataSet() {
		notifyDataSetChanged();
		globalVarAdapter.notifyDataSetChanged();
		localVarAdapter.notifyDataSetChanged();
		globalListAdapter.notifyDataSetChanged();
		localListAdapter.notifyDataSetChanged();
	}

	public void clearSelection() {
		globalVarAdapter.clearSelection();
		localVarAdapter.clearSelection();
		globalListAdapter.clearSelection();
		localListAdapter.clearSelection();
	}

	public void remove(UserData item) {
		if (item instanceof UserVariable) {
			if (!globalVarAdapter.remove((UserVariable) item)) {
				localVarAdapter.remove((UserVariable) item);
			}
		} else {
			if (!globalListAdapter.remove((UserList) item)) {
				localListAdapter.remove((UserList) item);
			}
		}
		notifyDataSetChanged();
	}

	public List<UserData> getItems() {
		List<UserData> items = new ArrayList<>();
		items.addAll(globalVarAdapter.getItems());
		items.addAll(localVarAdapter.getItems());
		items.addAll(globalListAdapter.getItems());
		items.addAll(localListAdapter.getItems());
		return items;
	}

	public List<UserData> getSelectedItems() {
		List<UserData> selectedItems = new ArrayList<>();
		selectedItems.addAll(globalVarAdapter.getSelectedItems());
		selectedItems.addAll(localVarAdapter.getSelectedItems());
		selectedItems.addAll(globalListAdapter.getSelectedItems());
		selectedItems.addAll(localListAdapter.getSelectedItems());
		return selectedItems;
	}

	public void setSelection(UserData item, boolean selection) {
		if (item instanceof UserVariable) {
			if (!globalVarAdapter.setSelection((UserVariable) item, selection)) {
				localVarAdapter.setSelection((UserVariable) item, selection);
			}
		} else {
			if (!globalListAdapter.setSelection((UserList) item, selection)) {
				localListAdapter.setSelection((UserList) item, selection);
			}
		}
	}

	@Override
	public int getItemCount() {
		return globalVarAdapter.getItemCount()
				+ localVarAdapter.getItemCount()
				+ globalListAdapter.getItemCount()
				+ localListAdapter.getItemCount();
	}
}

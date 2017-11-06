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

package org.catrobat.catroid.ui.recyclerview.backpack;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.recyclerview.adapter.RecyclerViewAdapter;
import org.catrobat.catroid.ui.recyclerview.adapter.draganddrop.TouchHelperCallback;
import org.catrobat.catroid.ui.recyclerview.adapter.ViewHolder;
import org.catrobat.catroid.ui.recyclerview.fragment.util.UniqueNameProvider;
import org.catrobat.catroid.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BackpackRecyclerViewFragment<T> extends Fragment implements
			RecyclerViewAdapter.SelectionListener,
			RecyclerViewAdapter.OnItemClickListener<T> {

	protected View view;
	protected RecyclerView recyclerView;
	protected RecyclerViewAdapter<T> adapter;
	protected ActionMode actionMode;
	protected String actionModeTitle;

	protected UniqueNameProvider uniqueNameProvider = new UniqueNameProvider();
	protected ItemTouchHelper touchHelper;

	protected enum ActionModeType {
		NONE, UNPACK, DELETE
	}

	protected ActionModeType actionModeType = ActionModeType.NONE;

	protected ActionMode.Callback callback = new ActionMode.Callback() {
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			adapter.showCheckBoxes = true;
			adapter.notifyDataSetChanged();

			switch (actionModeType) {
				case UNPACK:
					actionModeTitle = getString(R.string.am_unpack);
					break;
				case DELETE:
					actionModeTitle = getString(R.string.am_delete);
					break;
				case NONE:
					actionMode.finish();
					return false;
			}
			mode.setTitle(actionModeTitle);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			if (adapter.getSelectedItems().isEmpty()) {
				finishActionMode();
			}

			switch (actionModeType) {
				case UNPACK:
					unpackItems(adapter.getSelectedItems());
					break;
				case DELETE:
					showDeleteAlert(adapter.getSelectedItems());
					break;
				case NONE:
					break;
			}
		}
	};

	protected void finishActionMode() {
		actionModeType = ActionModeType.NONE;
		actionModeTitle = "";
		adapter.showCheckBoxes = false;
		adapter.clearSelection();
		adapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_list_view, container, false);
		recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
		setHasOptionsMenu(true);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstance) {
		super.onActivityCreated(savedInstance);
		initializeAdapter();
	}

	public void onAdapterReady() {
		recyclerView.setAdapter(adapter);
		adapter.setSelectionListener(this);
		adapter.setOnItemClickListener(this);

		ItemTouchHelper.Callback callback = new TouchHelperCallback(adapter);
		touchHelper = new ItemTouchHelper(callback);
		touchHelper.attachToRecyclerView(recyclerView);
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.unpack:
				startActionMode(ActionModeType.UNPACK);
				break;
			case R.id.delete:
				startActionMode(ActionModeType.DELETE);
				break;
			case R.id.show_details:
				adapter.showDetails = !adapter.showDetails;
				adapter.notifyDataSetChanged();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private void startActionMode(ActionModeType type) {
		if (adapter.items.isEmpty()) {
			ToastUtil.showError(getActivity(), R.string.am_empty_list);
		} else {
			actionModeType = type;
			actionMode = getActivity().startActionMode(callback);
		}
	}

	public void showDeleteAlert(final List<T> selectedItems) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getResources().getQuantityString(getDeleteAlertTitle(), selectedItems.size()))
				.setMessage(R.string.dialog_confirm_delete_object_message)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						deleteItems(selectedItems);
					}
				})
				.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						if (actionModeType != ActionModeType.NONE) {
							finishActionMode();
						}
						dialog.dismiss();
					}
				})
				.setCancelable(false);

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (actionModeType != ActionModeType.NONE) {
			adapter.clearSelection();
			actionMode.finish();
		}
	}

	@Override
	public void onItemClick(final T item) {
		if (actionModeType != ActionModeType.NONE) {
			return;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		CharSequence[] items = new CharSequence[] { getString(R.string.unpack), getString(R.string.delete) };
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case 0:
						unpackItems(new ArrayList<>(Collections.singletonList(item)));
						break;
					case 1:
						showDeleteAlert(new ArrayList<>(Collections.singletonList(item)));
				}
			}
		});
		builder.setTitle(getItemName(item));
		builder.setCancelable(true);
		builder.show();
	}

	@Override
	public void onItemLongClick(T item, ViewHolder holder) {
		onItemClick(item);
	}

	protected abstract void initializeAdapter();
	protected abstract void unpackItems(List<T> selectedItems);
	protected abstract String getUnpackDstPath();
	protected abstract List<T> getDstItemList();
	protected abstract Set<String> getDstScope();
	protected abstract int getDeleteAlertTitle();
	protected abstract void deleteItems(List<T> selectedItems);
	protected abstract String getItemName(T item);
}

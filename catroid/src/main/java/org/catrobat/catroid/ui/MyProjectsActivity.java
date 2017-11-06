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
package org.catrobat.catroid.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.dialogs.NewProjectDialog;
import org.catrobat.catroid.ui.recyclerview.fragment.ProjectFragment;
import org.catrobat.catroid.utils.SnackbarUtil;

import java.util.concurrent.locks.Lock;

public class MyProjectsActivity extends BaseCastActivity {

	public static final String TAG = MyProjectsActivity.class.getSimpleName();

	private Lock viewSwitchLock = new ViewSwitchLock();
	private ProjectFragment projectListFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setTitle(R.string.my_projects_activity_title);
		setContentView(R.layout.activity_my_projects);

		BottomBar.hidePlayButton(this);

		loadFragment(ProjectFragment.class, false);
		projectListFragment = (ProjectFragment) getFragmentManager().findFragmentById(R.id.fragment_container);
		SnackbarUtil.showHintSnackbar(this, R.string.hint_merge);
	}

	public void loadFragment(Class<? extends Fragment> fragmentClass, boolean addCurrentFragmentToBackStack) {
		loadFragment(fragmentClass, null, addCurrentFragmentToBackStack);
	}

	public void loadFragment(Class<? extends Fragment> fragmentClass, Bundle bundle, boolean addCurrentFragmentToBackStack) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		try {
			Fragment newFragment = fragmentClass.newInstance();
			fragmentTransaction.replace(R.id.fragment_container, newFragment, fragmentClass.getSimpleName());

			if (addCurrentFragmentToBackStack) {
				fragmentTransaction.addToBackStack(null);
			}

			if (bundle != null) {
				newFragment.setArguments(bundle);
			}

			fragmentTransaction.commit();
			getFragmentManager().executePendingTransactions();
		} catch (Exception e) {
			Log.e(TAG, "Error while loading fragment" + e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_projects_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
			default:
				return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void handleAddButton(View view) {
		if (!viewSwitchLock.tryLock()) {
			return;
		}
		NewProjectDialog dialog = new NewProjectDialog();
		dialog.setOpenedFromProjectList(true);
		dialog.show(getFragmentManager(), NewProjectDialog.DIALOG_FRAGMENT_TAG);
	}

	@Override
	public void onBackPressed() {
		//projectListFragment.cancelLoadProjectTask();
		super.onBackPressed();
	}
}

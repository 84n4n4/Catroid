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

package org.catrobat.catroid.ui.settingsfragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.catrobat.catroid.R;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.ui.recyclerview.SimpleRVItem;
import org.catrobat.catroid.ui.recyclerview.adapter.SimpleRVAdapter;
import org.catrobat.catroid.utils.ToastUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.BEGINNER_BRICKS;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.DRAGNDROP_DELAY;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.ELEMENT_SPACING;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.FONT_STYLE;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.HIGH_CONTRAST;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.ICONS;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.ICON_HIGH_CONTRAST;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.LARGE_ICONS;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.LARGE_TEXT;

public class AccessibilityProfilesFragment extends Fragment  implements SimpleRVAdapter.OnItemClickListener {
	public static final String TAG = AccessibilityProfilesFragment.class.getSimpleName();

	private RecyclerView recyclerView;

	@Retention(RetentionPolicy.SOURCE)
	@IntDef({DEFAULT, ARGUS, FENRIR, ODIN, TIRO})
	@interface ProfileId {}
	private static final int DEFAULT = 0;
	private static final int ARGUS = 1;
	private static final int FENRIR = 2;
	private static final int ODIN = 3;
	private static final int TIRO = 4;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View parent = inflater.inflate(R.layout.fragment_list_view, container, false);
		recyclerView = parent.findViewById(R.id.recycler_view);
		return parent;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SimpleRVAdapter adapter;
		List<SimpleRVItem> items = getItems();
		adapter = new SimpleRVAdapter(items);
		adapter.setOnItemClickListener(this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		((AppCompatActivity) getActivity()).getSupportActionBar()
				.setTitle(R.string.preference_title_accessibility_profiles);
	}

	@Override
	public void onItemClick(@ProfileId int profileId) {
		SharedPreferences.Editor preferenceEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
		preferenceEditor.putBoolean(LARGE_TEXT, false);
		preferenceEditor.putBoolean(HIGH_CONTRAST, false);
		preferenceEditor.putString(FONT_STYLE, AccessibilitySettingsFragment.REGULAR);
		preferenceEditor.putBoolean(ICONS, false);
		preferenceEditor.putBoolean(LARGE_ICONS, false);
		preferenceEditor.putBoolean(ICON_HIGH_CONTRAST, false);
		preferenceEditor.putBoolean(ELEMENT_SPACING, false);
		preferenceEditor.putBoolean(BEGINNER_BRICKS, false);
		preferenceEditor.putBoolean(DRAGNDROP_DELAY, false);

		switch (profileId) {
			case ARGUS:
				preferenceEditor.putBoolean(HIGH_CONTRAST, true);
				preferenceEditor.putBoolean(ICONS, true);
				break;
			case FENRIR:
				preferenceEditor.putBoolean(ELEMENT_SPACING, true);
				preferenceEditor.putBoolean(DRAGNDROP_DELAY, true);
				break;
			case ODIN:
				preferenceEditor.putBoolean(LARGE_TEXT, true);
				preferenceEditor.putBoolean(HIGH_CONTRAST, false);
				preferenceEditor.putBoolean(ICONS, true);
				preferenceEditor.putBoolean(LARGE_ICONS, true);
				preferenceEditor.putBoolean(ELEMENT_SPACING, true);
				break;
			case TIRO:
				preferenceEditor.putBoolean(BEGINNER_BRICKS, true);
				break;
			case DEFAULT:
				break;
		}
		preferenceEditor.commit();
		startActivity(new Intent(getActivity().getBaseContext(), MainMenuActivity.class));
		startActivity(new Intent(getActivity().getBaseContext(), SettingsActivity.class));
		ToastUtil.showSuccess(getActivity(), getString(R.string.accessibility_settings_applied));
		getActivity().finishAffinity();
	}


	private List<SimpleRVItem> getItems() {
		List<SimpleRVItem> items = new ArrayList<>();
		items.add(new SimpleRVItem(DEFAULT,
				ContextCompat.getDrawable(getActivity(), R.drawable.nolb_default_myprofile),
				getString(R.string.preference_access_title_profile_default),
				getString(R.string.preference_access_summary_profile_default)));
		items.add(new SimpleRVItem(ARGUS,
				ContextCompat.getDrawable(getActivity(), R.drawable.nolb_argus),
				getString(R.string.preference_access_title_profile_argus),
				getString(R.string.preference_access_summary_profile_argus)));
		items.add(new SimpleRVItem(FENRIR,
				ContextCompat.getDrawable(getActivity(), R.drawable.nolb_fenrir),
				getString(R.string.preference_access_title_profile_fenrir),
				getString(R.string.preference_access_summary_profile_fenrir)));
		items.add(new SimpleRVItem(ODIN,
				ContextCompat.getDrawable(getActivity(), R.drawable.nolb_odin),
				getString(R.string.preference_access_title_profile_odin),
				getString(R.string.preference_access_summary_profile_odin)));
		items.add(new SimpleRVItem(TIRO,
				ContextCompat.getDrawable(getActivity(), R.drawable.nolb_tiro),
				getString(R.string.preference_access_title_profile_tiro),
				getString(R.string.preference_access_summary_profile_tiro)));
		return items;
	}
}

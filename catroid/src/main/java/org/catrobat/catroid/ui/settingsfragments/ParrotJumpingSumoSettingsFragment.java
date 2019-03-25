/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2019 The Catrobat Team
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

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.catrobat.catroid.BuildConfig;
import org.catrobat.catroid.R;

import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.PARROT_JUMPING_SUMO_SCREEN_KEY;

public class ParrotJumpingSumoSettingsFragment extends PreferenceFragment {
	public static final String TAG = ParrotJumpingSumoSettingsFragment.class.getSimpleName();

	@Override
	public void onResume() {
		super.onResume();
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getPreferenceScreen().getTitle());
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		SettingsFragment.setToChosenLanguage(getActivity());
		addPreferencesFromResource(R.xml.jumping_sumo_preferences);

		if (!BuildConfig.FEATURE_PARROT_JUMPING_SUMO_ENABLED) {
			PreferenceScreen jsPreference = (PreferenceScreen) findPreference(PARROT_JUMPING_SUMO_SCREEN_KEY);
			jsPreference.setEnabled(false);
			getPreferenceScreen().removePreference(jsPreference);
		}
	}
}

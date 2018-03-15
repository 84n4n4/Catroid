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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.catrobat.catroid.R;
import org.catrobat.catroid.cast.CastManager;
import org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment;
import org.catrobat.catroid.ui.settingsfragments.SettingsFragment;
import org.catrobat.catroid.utils.CrashReporter;

import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.DYSLEXIC;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.ELEMENT_SPACING;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.FONT_STYLE;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.HIGH_CONTRAST;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.ICONS;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.ICON_HIGH_CONTRAST;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.LARGE_ICONS;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.LARGE_TEXT;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.REGULAR;
import static org.catrobat.catroid.ui.settingsfragments.AccessibilitySettingsFragment.SERIF;

public abstract class BaseActivity extends AppCompatActivity {

	public static final String RECOVERED_FROM_CRASH = "RECOVERED_FROM_CRASH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applyAccessibilityStyles();

		Thread.setDefaultUncaughtExceptionHandler(new BaseExceptionHandler(this));
		checkIfCrashRecoveryAndFinishActivity(this);

		if (SettingsFragment.isCastSharedPreferenceEnabled(this)) {
			CastManager.getInstance().initializeCast(this);
		}
	}

	private void applyAccessibilityStyles() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		if (sharedPreferences.getBoolean(LARGE_TEXT, false)) {
			getTheme().applyStyle(R.style.FontSizeLarge, true);
		}
		if (sharedPreferences.getBoolean(HIGH_CONTRAST, false)) {
			getTheme().applyStyle(R.style.ContrastHigh, true);
		}
		if (sharedPreferences.getBoolean(ELEMENT_SPACING, false)) {
			getTheme().applyStyle(R.style.SpacingLarge, true);
		}
		if (sharedPreferences.getBoolean(ICON_HIGH_CONTRAST, false)) {
			getTheme().applyStyle(R.style.CategoryIconContrastHigh, true);
		}
		if (sharedPreferences.getBoolean(ICONS, false)) {
			getTheme().applyStyle(R.style.CategoryIconVisible, true);
		}
		if (sharedPreferences.getBoolean(LARGE_ICONS, false)) {
			getTheme().applyStyle(R.style.CategoryIconSizeLarge, true);
		}
		if (sharedPreferences.getString(FONT_STYLE,	REGULAR).equals(SERIF)) {
			getTheme().applyStyle(R.style.FontSerif, true);
		}
		if (sharedPreferences.getString(FONT_STYLE,	REGULAR).equals(DYSLEXIC)) {
			getTheme().applyStyle(R.style.FontDyslexic, true);
		}
	}

	@SuppressWarnings("PMD.DoNotCallGarbageCollectionExplicitly")
	@Override
	protected void onDestroy() {
		// Partly from http://stackoverflow.com/a/5069354
		unbindDrawables(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0));
		System.gc();

		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (SettingsFragment.isCastSharedPreferenceEnabled(this)) {
			CastManager.getInstance().initializeCast(this);
		}

		invalidateOptionsMenu();
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

	private void checkIfCrashRecoveryAndFinishActivity(final Activity context) {
		if (isRecoveringFromCrash()) {
			CrashReporter.logUnhandledException();
			if (!(context instanceof MainMenuActivity)) {
				context.finish();
			} else {
				PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(RECOVERED_FROM_CRASH, false).commit();
			}
		}
	}

	private boolean isRecoveringFromCrash() {
		return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(RECOVERED_FROM_CRASH, false);
	}

	// Code from Stackoverflow to reduce memory problems
	// onDestroy() and unbindDrawables() methods taken from
	// http://stackoverflow.com/a/6779067
	protected void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}
}

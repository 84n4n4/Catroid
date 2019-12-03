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

package org.catrobat.catroid.uiespresso.ui.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.PreferenceMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.R;
import org.catrobat.catroid.testsuites.annotations.Cat;
import org.catrobat.catroid.testsuites.annotations.Level;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.catrobat.catroid.common.SharedPreferenceKeys.ACCESSIBILITY_PROFILE_PREFERENCE_KEY;
import static org.catrobat.catroid.common.SharedPreferenceKeys.LANGUAGE_CODE;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_CRASH_REPORTS;
import static org.catrobat.catroid.ui.settingsfragments.SettingsFragment.SETTINGS_SHOW_HINTS;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
public class SettingsFragmentTest {

	@Rule
	public BaseActivityTestRule<SettingsActivity> baseActivityTestRule = new
			BaseActivityTestRule<>(SettingsActivity.class, true, false);

	private List<String> allSettings = new ArrayList<>(Arrays.asList(SETTINGS_SHOW_HINTS, SETTINGS_CRASH_REPORTS));
	private Map<String, Boolean> initialSettings = new HashMap<>();

	@Before
	public void setUp() throws Exception {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());

		for (String setting : allSettings) {
			initialSettings.put(setting, sharedPreferences.getBoolean(setting, false));
		}
		setAllSettingsTo(true);
		baseActivityTestRule.launchActivity(null);
	}

	private void setAllSettingsTo(boolean value) {
		SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext()).edit();

		for (String setting : allSettings) {
			sharedPreferencesEditor.putBoolean(setting, value);
		}
		sharedPreferencesEditor.putInt(ACCESSIBILITY_PROFILE_PREFERENCE_KEY, R.id.default_profile);
		sharedPreferencesEditor.commit();
	}

	@After
	public void tearDown() {
		SharedPreferences.Editor sharedPreferencesEditor = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext()).edit();
		for (String setting : initialSettings.keySet()) {
			sharedPreferencesEditor.putBoolean(setting, initialSettings.get(setting));
		}
		sharedPreferencesEditor.putInt(ACCESSIBILITY_PROFILE_PREFERENCE_KEY, R.id.default_profile);
		sharedPreferencesEditor.commit();
		initialSettings.clear();
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void basicSettingsTest() {
		checkPreference(R.string.preference_title_enable_hints, SETTINGS_SHOW_HINTS);
		checkPreference(R.string.preference_title_enable_crash_reports, SETTINGS_CRASH_REPORTS);
	}

	@Category({Cat.AppUi.class, Level.Functional.class, Cat.Quarantine.class})
	@Test
	public void noMultipleSelectAccessibilityProfilesTest() {
		onData(PreferenceMatchers.withTitle(R.string.preference_title_accessibility))
				.perform(click());

		onData(PreferenceMatchers.withTitle(R.string.preference_title_accessibility_predefined_profile_headline))
				.perform(click());

		onView(allOf(withId(R.id.radio_button), withParent(withId(R.id.argus))))
				.perform(click());
	}

	@Category({Cat.AppUi.class, Level.Smoke.class})
	@Test
	public void languageSettingTest() {
		onData(PreferenceMatchers.withTitle(R.string.preference_title_language))
				.perform(click());
		onView(withText(R.string.preference_title_language))
				.check(matches(isDisplayed()));
		onData(is(instanceOf(String.class))).atPosition(0)
				.check(matches(withText(R.string.device_language)));
		for (String rtlLanguage : LANGUAGE_CODE) {

			if (rtlLanguage.equals("sd")) {
				onData(hasToString("سنڌي"))
						.check(matches(isDisplayed()));
			} else if (rtlLanguage.length() == 2) {
				Locale rtlLocale = new Locale(rtlLanguage);
				onData(hasToString(rtlLocale.getDisplayName(rtlLocale)))
						.check(matches(isDisplayed()));
			} else if (rtlLanguage.length() == 6) {
				String language = rtlLanguage.substring(0, 2);
				String country = rtlLanguage.substring(4);
				Locale rtlLocale = new Locale(language, country);
				onData(hasToString(rtlLocale.getDisplayName(rtlLocale)))
						.check(matches(isDisplayed()));
			}
		}
		onView(withId(android.R.id.button2))
				.check(matches(isDisplayed()));
	}

	private void checkPreference(int displayedTitleResourceString, String sharedPreferenceTag) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext());

		onData(PreferenceMatchers.withTitle(displayedTitleResourceString))
				.perform(click());

		assertFalse(sharedPreferences.getBoolean(sharedPreferenceTag, false));

		onData(PreferenceMatchers.withTitle(displayedTitleResourceString))
				.perform(click());

		assertTrue(sharedPreferences.getBoolean(sharedPreferenceTag, false));
	}
}

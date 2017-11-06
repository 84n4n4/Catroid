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
package org.catrobat.catroid.uitest.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.view.View;

import com.robotium.solo.Solo;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.ui.SpriteAttributesActivity;
import org.catrobat.catroid.ui.ProjectActivity;
import org.catrobat.catroid.ui.SettingsActivity;
import org.catrobat.catroid.uitest.util.BaseActivityInstrumentationTestCase;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import java.io.File;
import java.util.List;

public class ProgramMenuActivityTest extends BaseActivityInstrumentationTestCase<MainMenuActivity> {

	private String backgroundName = "BackgroundSprite";
	private String objectName = "ObjectSprite";
	private File lookFile;

	public ProgramMenuActivityTest() {
		super(MainMenuActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		super.setUp();
		createProject();
		UiTestUtils.prepareStageForTest();
		lookFile = UiTestUtils.setUpLookFile(solo, getActivity());
	}

	@Override
	public void tearDown() throws Exception {
		lookFile.delete();
		super.tearDown();
	}

	public void testOrientation() throws NameNotFoundException {
		/// Method 1: Assert it is currently in portrait mode.
		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.clickOnText(backgroundName);
		solo.waitForActivity(SpriteAttributesActivity.class.getSimpleName());
		assertEquals("ProgramMenuActivity not in Portrait mode!", Configuration.ORIENTATION_PORTRAIT, solo
				.getCurrentActivity().getResources().getConfiguration().orientation);

		/// Method 2: Retrieve info about Activity as collected from AndroidManifest.xml
		// https://developer.android.com/reference/android/content/pm/ActivityInfo.html
		PackageManager packageManager = solo.getCurrentActivity().getPackageManager();
		ActivityInfo activityInfo = packageManager.getActivityInfo(solo.getCurrentActivity().getComponentName(),
				PackageManager.GET_META_DATA);

		// Note that the activity is _indeed_ rotated on your device/emulator!
		// Robotium can _force_ the activity to be in landscapeMode mode (and so could we, programmatically)
		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.sleep(200);

		assertEquals(SpriteAttributesActivity.class.getSimpleName()
						+ " not set to be in portrait mode in AndroidManifest.xml!", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
				activityInfo.screenOrientation);
	}

	public void testTitle() {
		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		solo.waitForFragmentById(R.id.fragment_container);

		String spriteName = "sprite1";

		UiTestUtils.addNewSprite(solo, spriteName, lookFile, null);
		solo.clickOnText(backgroundName);
		solo.waitForActivity(SpriteAttributesActivity.class.getSimpleName());

		String currentSpriteName = ProjectManager.getInstance().getCurrentSprite().getName();

		assertEquals("Current sprite is not " + backgroundName, backgroundName, currentSpriteName);
		assertTrue("Title doesn't match " + backgroundName, solo.waitForText(currentSpriteName, 0, 200, false, true));

		solo.goBack();
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		solo.waitForFragmentById(R.id.fragment_container);
		solo.waitForText(spriteName);
		solo.clickOnText(spriteName);
		solo.waitForActivity(SpriteAttributesActivity.class.getSimpleName());

		currentSpriteName = ProjectManager.getInstance().getCurrentSprite().getName();

		assertEquals("Current sprite is not " + spriteName, spriteName, currentSpriteName);
		assertTrue("Title doesn't match " + spriteName, solo.waitForText(currentSpriteName, 0, 200, false, true));
	}

	public void testLookButtonTextChange() {
		String spriteName = "sprite1";
		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		UiTestUtils.addNewSprite(solo, spriteName, lookFile, null);
		solo.clickOnText(spriteName);
		solo.waitForActivity(SpriteAttributesActivity.class.getSimpleName());
		assertTrue("Text on look button is not 'Looks'", solo.searchText(solo.getString(R.string.looks)));
		solo.goBack();
		solo.goBack();
		solo.waitForText(solo.getString(R.string.main_menu_continue));
		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.clickOnText(backgroundName);
		solo.waitForText(solo.getString(R.string.backgrounds));
		assertTrue("Text on look button is not 'Backgrounds'", solo.searchText(solo.getString(R.string.backgrounds)));
	}

	public void testPlayButton() {
		solo.assertMemoryNotLow();
		solo.waitForActivity(MainMenuActivity.class.getSimpleName());
		UiTestUtils.getIntoProgramMenuFromMainMenu(solo, 0);

		assertTrue("Bottombar is not visible", solo.getView(R.id.button_play).getVisibility() == View.VISIBLE);
		assertTrue("Play button is not visible", solo.getView(R.id.button_play).getVisibility() == View.VISIBLE);
		assertTrue("Add button is not visible", solo.getView(R.id.button_add).getVisibility() == View.GONE);
		assertTrue("Bottombar separator is not visible",
				solo.getView(R.id.bottom_bar_separator).getVisibility() == View.GONE);

		UiTestUtils.clickOnBottomBar(solo, R.id.button_play);
		solo.waitForActivity(StageActivity.class.getSimpleName());
		solo.assertCurrentActivity("Not in StageActivity", StageActivity.class);
		solo.goBack();
		solo.goBack();
		solo.waitForActivity(SpriteAttributesActivity.class.getSimpleName());
		solo.assertCurrentActivity("Not in ProgramMenuActivity", SpriteAttributesActivity.class);
	}

	public void testMainMenuItemsNotVisible() {
		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.sendKey(Solo.MENU);

		assertFalse("rate us is visible", solo.waitForText(solo.getString(R.string.main_menu_rate_app), 1, 5000, false));
		assertFalse("terms of use is visible", solo.waitForText(solo.getString(R.string.main_menu_terms_of_use), 1, 1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.main_menu_about), 1, 1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.main_menu_login), 1,
				1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.main_menu_logout), 1, 1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.settings), 1,
				1000, false));
	}

	public void testMainMenuItemsNotVisibleInProgramActivity() {
		UiTestUtils.getIntoProgramMenuFromMainMenu(solo, 0);

		solo.sendKey(Solo.MENU);

		assertFalse("rate us is visible", solo.waitForText(solo.getString(R.string.main_menu_rate_app), 1, 5000, false));
		assertFalse("terms of use is visible", solo.waitForText(solo.getString(R.string.main_menu_terms_of_use), 1, 1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.main_menu_about), 1, 1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.main_menu_login), 1,
				1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.main_menu_logout), 1, 1000, false));
		assertFalse("about is visible", solo.waitForText(solo.getString(R.string.settings), 1,
				1000, false));
	}

	public void testMenuItemSettings() {
		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		solo.clickOnText(backgroundName);
		solo.clickOnMenuItem(solo.getString(R.string.settings));
		solo.assertCurrentActivity("Not in SettingsActivity", SettingsActivity.class);
	}

	public void testRename() {
		String rename = solo.getString(R.string.rename);
		String newName = "new object name";

		solo.clickOnText(solo.getString(R.string.main_menu_continue));
		solo.waitForActivity(ProjectActivity.class.getSimpleName());
		solo.clickOnText(objectName);

		UiTestUtils.openActionMode(solo, rename, R.id.rename);
		solo.waitForDialogToOpen();
		solo.clearEditText(0);
		solo.enterText(0, newName);
		solo.clickOnButton(solo.getString(R.string.ok));
		solo.waitForDialogToClose();
		assertTrue("Group was not renamed", solo.searchText(newName, 0, false, true));
	}

	private void createProject() {
		Project project = new Project(null, UiTestUtils.PROJECTNAME1);

		Sprite spriteCat = new SingleSprite(backgroundName);
		Sprite secondSprite = new SingleSprite(objectName);
		Script startScriptCat = new StartScript();
		Script scriptTappedCat = new WhenScript();
		Brick setXBrick = new SetXBrick(50);
		Brick setYBrick = new SetYBrick(50);
		Brick changeXBrick = new ChangeXByNBrick(50);
		startScriptCat.addBrick(setYBrick);
		startScriptCat.addBrick(setXBrick);
		scriptTappedCat.addBrick(changeXBrick);

		spriteCat.addScript(startScriptCat);
		spriteCat.addScript(scriptTappedCat);
		project.getDefaultScene().addSprite(spriteCat);
		project.getDefaultScene().addSprite(secondSprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(spriteCat);
		ProjectManager.getInstance().setCurrentScript(startScriptCat);

		File imageFile = UiTestUtils.saveFileToProject(project.getName(), project.getDefaultScene().getName(), "catroid_sunglasses.png",
				org.catrobat.catroid.test.R.drawable.catroid_sunglasses, getInstrumentation().getContext(), UiTestUtils.FileTypes.IMAGE);

		ProjectManager projectManager = ProjectManager.getInstance();
		List<LookData> lookDataList = projectManager.getCurrentSprite().getLookDataList();
		LookData lookData = new LookData();
		lookData.setLookFilename(imageFile.getName());
		lookData.setLookName("Catroid sun");
		lookDataList.add(lookData);
		projectManager.getFileChecksumContainer().addChecksum(lookData.getChecksum(), lookData.getAbsolutePath());

		File soundFile = UiTestUtils.saveFileToProject(project.getName(), project.getDefaultScene().getName(), "longsound.mp3",
				org.catrobat.catroid.test.R.raw.longsound, getInstrumentation().getContext(),
				UiTestUtils.FileTypes.SOUND);
		SoundInfo soundInfo = new SoundInfo();
		soundInfo.setSoundFileName(soundFile.getName());
		soundInfo.setTitle("longsound");

		List<SoundInfo> soundInfoList = ProjectManager.getInstance().getCurrentSprite().getSoundList();
		soundInfoList.add(soundInfo);
		ProjectManager.getInstance().getFileChecksumContainer()
				.addChecksum(soundInfo.getChecksum(), soundInfo.getAbsolutePath());
	}
}

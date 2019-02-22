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
package org.catrobat.catroid.catrobatTestRunner;

import android.support.test.InstrumentationRegistry;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.catrobat.catroid.io.StorageOperations;
import org.catrobat.catroid.io.asynctask.ProjectUnzipAndImportTask;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.test.utils.TestUtils;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.catrobat.catroid.common.Constants.CACHE_DIR;
import static org.catrobat.catroid.common.FlavoredConstants.DEFAULT_ROOT_DIRECTORY;
import static org.catrobat.catroid.uiespresso.util.UserVariableAssertions.assertUserVariableEqualsWithTimeout;

@RunWith(Parameterized.class)
public class CatrobatTestRunner {

	@Rule
	public BaseActivityInstrumentationRule<StageActivity> baseActivityTestRule = new
			BaseActivityInstrumentationRule<>(StageActivity.class, true, false);

	private static String TEST_ASSETS = "catrobatTests/";
	private static String RESULT_VARIABLE_NAME = "testResult";

	private Project project;
	private UserVariable testResult;

	@Parameterized.Parameters(name = "{0}")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{"testMultipleBroadcastReceivers", 7.5, 2000},
				{"testSinusFunction", 0.984807753012208, 2000},
				{"testCosinFunction", 0.17364817766693041, 2000},
				{"testTanFunction", 5.671281819617707, 2000},
		});
	}

	@Parameterized.Parameter
	public String projectName;

	@Parameterized.Parameter(1)
	public double expected;

	@Parameterized.Parameter(2)
	public int timeOutMs;

	@Before
	public void setUp() throws Exception {
		TestUtils.deleteProjects(projectName);
		DEFAULT_ROOT_DIRECTORY.mkdir();
		CACHE_DIR.mkdir();

		String assetName = projectName + ".catrobat";
		InputStream inputStream = InstrumentationRegistry.getContext().getAssets().open(TEST_ASSETS + assetName);
		File projectCatrobatFile = StorageOperations.copyStreamToDir(inputStream, CACHE_DIR, assetName);
		ProjectUnzipAndImportTask.task(projectCatrobatFile);

		ProjectManager.getInstance().loadProject(projectName, InstrumentationRegistry.getTargetContext());
		project = ProjectManager.getInstance().getCurrentProject();
		DataContainer dataContainer = project.getDefaultScene().getDataContainer();
		testResult = dataContainer.getProjectUserVariable(RESULT_VARIABLE_NAME);
	}

	@After
	public void tearDown() throws IOException {
		StorageOperations.deleteDir(CACHE_DIR);
		TestUtils.deleteProjects(projectName);
	}

	@Test
	public void runCatrobatTest() {
		baseActivityTestRule.launchActivity(null);
		assertUserVariableEqualsWithTimeout(testResult, expected, timeOutMs);
	}
}

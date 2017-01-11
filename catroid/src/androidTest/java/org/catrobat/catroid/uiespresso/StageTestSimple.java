package org.catrobat.catroid.uiespresso;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.SurfaceView;

import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceView20;
import com.badlogic.gdx.utils.ScreenUtils;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.ScreenValues;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.uiespresso.actions.CustomActions;
import org.catrobat.catroid.uiespresso.matchers.StageMatchers;
import org.catrobat.catroid.uiespresso.util.BaseActivityInstrumentationRule;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.utils.UtilUi;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isFocusable;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

@RunWith(AndroidJUnit4.class)
public class StageTestSimple {
	private static final String TAG = StageTestSimple.class.getSimpleName();
	private static final int PROJECT_WIDTH = 480;
	private static final int PROJECT_HEIGHT = 800;

	@Rule
	public BaseActivityInstrumentationRule<StageActivity> BaseActivityTestRule = new
			BaseActivityInstrumentationRule<>(StageActivity.class, true, false);

	@Before
	public void setUp() throws Exception {


	}

	@Test
	public void checkForBlueSpriteColor() {
		Project blueProject = createProjectWithBlueSprite("blueProject");
		BaseActivityTestRule.launchActivity(null);
		//for some fucked up reason this isnt working
		//byte[] testPixels1 = ScreenUtils.getFrameBufferPixels(100,100,1,1,true);

		//so might aswell go use the one from stagelistener (that actually does same thing as above)
		//byte[] testPixels = StageActivity.stageListener.getPixels(100, 100, 1, 1);

		byte[] blue = { 0, (byte) 162, (byte) 232, (byte) 255 };

		//color matcher only accepts a GL20View, this can be aquired by getting the only focusable element in the stage
		onView(isFocusable()).check(matches(StageMatchers.isColorAtPx(blue, 1, 1)));

		onView(isRoot()).perform(CustomActions.wait(5000));
	}

	//how to create a project with a sprite
	public Project createProjectWithBlueSprite(String projectName){
		ScreenValues.SCREEN_HEIGHT = PROJECT_HEIGHT;
		ScreenValues.SCREEN_WIDTH = PROJECT_WIDTH;

		Project project = new Project(null, projectName);

		// blue Sprite
		Sprite blueSprite = new SingleSprite("blueSprite");
		StartScript blueStartScript = new StartScript();
		LookData blueLookData = new LookData();
		String blueImageName = "blue_image.bmp";

		blueLookData.setLookName(blueImageName);

		blueSprite.getLookDataList().add(blueLookData);

		blueStartScript.addBrick(new PlaceAtBrick(0, 0));
		blueStartScript.addBrick(new SetSizeToBrick(5000));
		blueSprite.addScript(blueStartScript);

		project.getDefaultScene().addSprite(blueSprite);

		StorageHandler.getInstance().saveProject(project);
		File blueImageFile = UiTestUtils.saveFileToProject(project.getName(), project.getDefaultScene().getName(),
				blueImageName,
				org.catrobat.catroid.test.R.raw.blue_image, InstrumentationRegistry.getContext(),
				UiTestUtils.FileTypes.IMAGE);

		blueLookData.setLookFilename(blueImageFile.getName());

		StorageHandler.getInstance().saveProject(project);
		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(blueSprite);
		UtilUi.updateScreenWidthAndHeight(InstrumentationRegistry.getContext());

		return project;
	}

}

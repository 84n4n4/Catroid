package org.catrobat.catroid.uiespresso.util;

import android.app.Activity;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.stage.StageListener;
import org.catrobat.catroid.test.utils.Reflection;

import java.io.File;

public class BaseActivityInstrumentationRule<T extends Activity> extends ActivityTestRule<T>
{
	private SystemAnimations systemAnimations;
	private Class clazz;

	public BaseActivityInstrumentationRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity){
		super(activityClass, initialTouchMode, launchActivity);
		clazz = activityClass;
	}

	public BaseActivityInstrumentationRule(Class<T> activityClass, boolean initialTouchMode){
		super(activityClass, initialTouchMode);
		clazz = activityClass;
	}

	public BaseActivityInstrumentationRule(Class<T> activityClass){
		super(activityClass);
		clazz = activityClass;
	}

	@Override
	protected void beforeActivityLaunched() {
		super.beforeActivityLaunched();
		Reflection.setPrivateField(StageListener.class, "checkIfAutomaticScreenshotShouldBeTaken", false);
		Reflection.setPrivateField(Constants.class, "DEFAULT_ROOT", Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/Pocket Code uiTest");
		File uiTestFolder = new File(Constants.DEFAULT_ROOT);
		if (uiTestFolder.exists()) {deleteRecursive(uiTestFolder);}
	}

	@Override
	protected void afterActivityLaunched() {
		systemAnimations = new SystemAnimations(InstrumentationRegistry.getTargetContext());
		systemAnimations.disableAll();
		super.afterActivityLaunched();
	}

	@Override
	protected void afterActivityFinished() {
		systemAnimations.enableAll();
		super.afterActivityFinished();
	}

	void deleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);
		fileOrDirectory.delete();
	}
}

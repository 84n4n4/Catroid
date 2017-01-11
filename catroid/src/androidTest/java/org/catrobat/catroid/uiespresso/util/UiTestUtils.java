package org.catrobat.catroid.uiespresso.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.formulaeditor.DataContainer;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.runner.lifecycle.Stage.RESUMED;

public class UiTestUtils {
	private static final String TAG = UiTestUtils.class.getSimpleName();

	@Nullable
	public static Activity getCurrentActivity() {
		final Activity[] currentActivity = {null};
		getInstrumentation().runOnMainSync(new Runnable() {
			public void run()
			{
				Collection<Activity> resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(RESUMED);
				if (resumedActivities.iterator().hasNext()) {
					currentActivity[0] = resumedActivities.iterator().next();
				}
			}
		});
		return currentActivity[0];
	}

	public static Project createProject(String projectName) {
		Project project = new Project(null, projectName);
		Sprite sprite = new Sprite("testSprite");
		Script script = new StartScript();

		SetVariableBrick setVariableBrick = new SetVariableBrick();
		DataContainer dataContainer = project.getDefaultScene().getDataContainer();
		UserVariable userVariable = dataContainer.addProjectUserVariable("Global1");
		setVariableBrick.setUserVariable(userVariable);

		script.addBrick(setVariableBrick);
		sprite.addScript(script);
		project.getDefaultScene().addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);

		return project;
	}

	public static Project createEmptyProject(String projectName) {
		Project project = new Project(null, projectName);
		Sprite sprite = new Sprite("testSprite");
		Script script = new StartScript();
		sprite.addScript(script);
		project.getDefaultScene().addSprite(sprite);
		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		return project;
	}

	/**
	 * saves a file into the project folder
	 * if project == null or "" file will be saved into Catroid folder
	 *
	 * @param project Folder where the file will be saved, this folder should exist
	 * @param name    Name of the file
	 * @param fileID  the id of the file --> needs the right context
	 * @param context
	 * @param type    type of the file: 0 = imagefile, 1 = soundfile
	 * @return the file
	 * @throws IOException
	 */
	public static File saveFileToProject(String project, String sceneName, String name, int fileID, Context context, FileTypes type) {

		boolean withChecksum = true;
		String filePath;
		String defaultRoot = Constants.DEFAULT_ROOT;
		//Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pocket Code uiTest";
		if (project == null || project.equalsIgnoreCase("")) {
			filePath =defaultRoot + "/";
		} else {
			switch (type) {
				case IMAGE:
					filePath = defaultRoot+ "/" + project + "/" + sceneName + "/" + Constants.IMAGE_DIRECTORY + "/";
					break;
				case SOUND:
					filePath = defaultRoot + "/" + project + "/" + sceneName + "/" + Constants.SOUND_DIRECTORY + "/";
					break;
				case SCREENSHOT:
					filePath = defaultRoot + "/" + project + "/" + sceneName + "/";
					withChecksum = false;
					break;
				case ROOT:
					filePath = defaultRoot + "/" + project + "/";
					withChecksum = false;
					break;
				default:
					filePath = defaultRoot + "/";
					break;
			}
		}
		BufferedInputStream in = new BufferedInputStream(context.getResources().openRawResource(fileID),
				Constants.BUFFER_8K);

		try {
			File file = new File(filePath + name);
			file.getParentFile().mkdirs();
			file.createNewFile();

			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file), Constants.BUFFER_8K);
			byte[] buffer = new byte[Constants.BUFFER_8K];
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.flush();
			out.close();

			String checksum;
			if (withChecksum) {
				checksum = Utils.md5Checksum(file) + "_";
			} else {
				checksum = "";
			}

			File tempFile = new File(filePath + checksum + name);
			file.renameTo(tempFile);

			return tempFile;
		} catch (IOException e) {
			Log.e(TAG, "File handling error", e);
			return null;
		}
	}

	public static enum FileTypes {
		IMAGE, SOUND, ROOT, SCREENSHOT
	}

	public static boolean comparePixelRgbaArrays(byte[] firstArray, byte[] secondArray) {
		Log.d(TAG, "first= " + Integer.toString(firstArray[0] & 0xFF) + " " + Integer.toString(firstArray[1] & 0xFF) + " " +
				Integer.toString(firstArray[2] & 0xFF) + " " + Integer.toString(firstArray[3] & 0xFF));
		Log.d(TAG, "second= " + Integer.toString(secondArray[0] & 0xFF) + " " + Integer.toString(secondArray[1] &
				0xFF) +" " + Integer.toString(secondArray[2] & 0xFF) + " " + Integer.toString(secondArray[3] & 0xFF));
		if (firstArray == null || secondArray == null || firstArray.length != 4 || secondArray.length != 4) {
			return false;
		}
		for (int i = 0; i < 4; i++) {
			if (Math.abs((firstArray[i] & 0xFF) - (secondArray[i] & 0xFF)) > 10) {
				return false;
			}
		}
		return true;
	}
}

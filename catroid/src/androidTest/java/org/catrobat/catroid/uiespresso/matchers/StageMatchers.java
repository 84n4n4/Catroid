package org.catrobat.catroid.uiespresso.matchers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.badlogic.gdx.backends.android.surfaceview.GLSurfaceView20;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;

import org.catrobat.catroid.stage.StageActivity;
import org.catrobat.catroid.stage.StageListener;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class StageMatchers {
	private static final String TAG = StageMatchers.class.getSimpleName();

	public static Matcher<View> isColorAtPx(final byte[] color, final int x, final int y){
		return new BoundedMatcher<View, GLSurfaceView20>(GLSurfaceView20.class) {

			@Override
			protected boolean matchesSafely(GLSurfaceView20 view) {
				byte[] testPixels = StageActivity.stageListener.getPixels(x, y, 1, 1);
				return comparePixelRgbaArrays(testPixels, color);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("Look if pixel at y=" + Integer.toString(x) +
						" y=" + Integer.toString(y) + " is white");
			}
		};
	}

	private static boolean comparePixelRgbaArrays(byte[] firstArray, byte[] secondArray) {
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

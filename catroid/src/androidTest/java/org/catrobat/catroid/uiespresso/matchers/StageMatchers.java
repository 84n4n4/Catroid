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
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
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
				return UiTestUtils.comparePixelRgbaArrays(testPixels, color);
			}

			@Override
			public void describeTo(Description description) {
				description.appendText("Look if pixel at y=" + Integer.toString(x) +
						" y=" + Integer.toString(y) + " is white");
			}
		};
	}




}

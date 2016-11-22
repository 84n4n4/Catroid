package org.catrobat.catroid.utils.idlingResources;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;


import java.util.concurrent.atomic.AtomicBoolean;

// https://github.com/googlesamples/android-testing/tree/master/ui/espresso/IdlingResourceSample
public class BooleanInitiallyBusyIdlingResource implements IdlingResource {
	@Nullable
	private volatile ResourceCallback mCallback;
	private AtomicBoolean mIsIdleNow = new AtomicBoolean(false);

	@Override
	public String getName() {
		return BooleanInitiallyBusyIdlingResource.class.getName();
	}

	@Override
	public boolean isIdleNow() {
		return mIsIdleNow.get();
	}

	@Override
	public void registerIdleTransitionCallback(ResourceCallback callback) {
		mCallback = callback;
	}

	public void setIdleState(boolean isIdleNow) {
		mIsIdleNow.set(isIdleNow);
		if (isIdleNow && mCallback != null) {
			mCallback.onTransitionToIdle();
		}
	}

}
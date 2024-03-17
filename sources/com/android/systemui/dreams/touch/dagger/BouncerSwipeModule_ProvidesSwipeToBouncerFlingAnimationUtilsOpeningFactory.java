package com.android.systemui.dreams.touch.dagger;

import com.android.wm.shell.animation.FlingAnimationUtils;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class BouncerSwipeModule_ProvidesSwipeToBouncerFlingAnimationUtilsOpeningFactory implements Factory<FlingAnimationUtils> {
    public static FlingAnimationUtils providesSwipeToBouncerFlingAnimationUtilsOpening(Provider<FlingAnimationUtils.Builder> provider) {
        return (FlingAnimationUtils) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesSwipeToBouncerFlingAnimationUtilsOpening(provider));
    }
}

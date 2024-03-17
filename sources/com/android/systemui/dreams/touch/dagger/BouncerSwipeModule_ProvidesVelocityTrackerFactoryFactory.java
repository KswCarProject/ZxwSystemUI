package com.android.systemui.dreams.touch.dagger;

import com.android.systemui.dreams.touch.BouncerSwipeTouchHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class BouncerSwipeModule_ProvidesVelocityTrackerFactoryFactory implements Factory<BouncerSwipeTouchHandler.VelocityTrackerFactory> {
    public static BouncerSwipeTouchHandler.VelocityTrackerFactory providesVelocityTrackerFactory() {
        return (BouncerSwipeTouchHandler.VelocityTrackerFactory) Preconditions.checkNotNullFromProvides(BouncerSwipeModule.providesVelocityTrackerFactory());
    }
}

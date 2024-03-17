package com.android.systemui.statusbar.dagger;

import com.android.systemui.animation.ActivityLaunchAnimator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory implements Factory<ActivityLaunchAnimator> {

    public static final class InstanceHolder {
        public static final CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory INSTANCE = new CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory();
    }

    public ActivityLaunchAnimator get() {
        return provideActivityLaunchAnimator();
    }

    public static CentralSurfacesDependenciesModule_ProvideActivityLaunchAnimatorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ActivityLaunchAnimator provideActivityLaunchAnimator() {
        return (ActivityLaunchAnimator) Preconditions.checkNotNullFromProvides(CentralSurfacesDependenciesModule.provideActivityLaunchAnimator());
    }
}

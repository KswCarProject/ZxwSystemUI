package com.android.systemui.dagger;

import android.app.ActivityTaskManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideActivityTaskManagerFactory implements Factory<ActivityTaskManager> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideActivityTaskManagerFactory INSTANCE = new FrameworkServicesModule_ProvideActivityTaskManagerFactory();
    }

    public ActivityTaskManager get() {
        return provideActivityTaskManager();
    }

    public static FrameworkServicesModule_ProvideActivityTaskManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static ActivityTaskManager provideActivityTaskManager() {
        return (ActivityTaskManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideActivityTaskManager());
    }
}

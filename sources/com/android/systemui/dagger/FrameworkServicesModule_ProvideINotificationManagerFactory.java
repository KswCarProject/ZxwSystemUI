package com.android.systemui.dagger;

import android.app.INotificationManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideINotificationManagerFactory implements Factory<INotificationManager> {
    public final FrameworkServicesModule module;

    public FrameworkServicesModule_ProvideINotificationManagerFactory(FrameworkServicesModule frameworkServicesModule) {
        this.module = frameworkServicesModule;
    }

    public INotificationManager get() {
        return provideINotificationManager(this.module);
    }

    public static FrameworkServicesModule_ProvideINotificationManagerFactory create(FrameworkServicesModule frameworkServicesModule) {
        return new FrameworkServicesModule_ProvideINotificationManagerFactory(frameworkServicesModule);
    }

    public static INotificationManager provideINotificationManager(FrameworkServicesModule frameworkServicesModule) {
        return (INotificationManager) Preconditions.checkNotNullFromProvides(frameworkServicesModule.provideINotificationManager());
    }
}

package com.android.systemui.dagger;

import com.android.internal.statusbar.IStatusBarService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideIStatusBarServiceFactory implements Factory<IStatusBarService> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIStatusBarServiceFactory INSTANCE = new FrameworkServicesModule_ProvideIStatusBarServiceFactory();
    }

    public IStatusBarService get() {
        return provideIStatusBarService();
    }

    public static FrameworkServicesModule_ProvideIStatusBarServiceFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IStatusBarService provideIStatusBarService() {
        return (IStatusBarService) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIStatusBarService());
    }
}

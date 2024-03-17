package com.android.systemui.dagger;

import android.view.IWindowManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideIWindowManagerFactory implements Factory<IWindowManager> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIWindowManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIWindowManagerFactory();
    }

    public IWindowManager get() {
        return provideIWindowManager();
    }

    public static FrameworkServicesModule_ProvideIWindowManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IWindowManager provideIWindowManager() {
        return (IWindowManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIWindowManager());
    }
}

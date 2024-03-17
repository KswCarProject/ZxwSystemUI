package com.android.systemui.dagger;

import android.content.pm.IPackageManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideIPackageManagerFactory implements Factory<IPackageManager> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideIPackageManagerFactory INSTANCE = new FrameworkServicesModule_ProvideIPackageManagerFactory();
    }

    public IPackageManager get() {
        return provideIPackageManager();
    }

    public static FrameworkServicesModule_ProvideIPackageManagerFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static IPackageManager provideIPackageManager() {
        return (IPackageManager) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideIPackageManager());
    }
}

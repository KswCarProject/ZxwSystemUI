package com.android.systemui.dagger;

import com.android.systemui.shared.system.PackageManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvidePackageManagerWrapperFactory implements Factory<PackageManagerWrapper> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvidePackageManagerWrapperFactory INSTANCE = new FrameworkServicesModule_ProvidePackageManagerWrapperFactory();
    }

    public PackageManagerWrapper get() {
        return providePackageManagerWrapper();
    }

    public static FrameworkServicesModule_ProvidePackageManagerWrapperFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static PackageManagerWrapper providePackageManagerWrapper() {
        return (PackageManagerWrapper) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.providePackageManagerWrapper());
    }
}

package com.android.systemui.dagger;

import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory implements Factory<DevicePolicyManagerWrapper> {
    public final SharedLibraryModule module;

    public SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory(SharedLibraryModule sharedLibraryModule) {
        this.module = sharedLibraryModule;
    }

    public DevicePolicyManagerWrapper get() {
        return provideDevicePolicyManagerWrapper(this.module);
    }

    public static SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory create(SharedLibraryModule sharedLibraryModule) {
        return new SharedLibraryModule_ProvideDevicePolicyManagerWrapperFactory(sharedLibraryModule);
    }

    public static DevicePolicyManagerWrapper provideDevicePolicyManagerWrapper(SharedLibraryModule sharedLibraryModule) {
        return (DevicePolicyManagerWrapper) Preconditions.checkNotNullFromProvides(sharedLibraryModule.provideDevicePolicyManagerWrapper());
    }
}

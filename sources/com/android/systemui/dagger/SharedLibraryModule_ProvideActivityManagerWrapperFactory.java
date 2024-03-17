package com.android.systemui.dagger;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SharedLibraryModule_ProvideActivityManagerWrapperFactory implements Factory<ActivityManagerWrapper> {
    public final SharedLibraryModule module;

    public SharedLibraryModule_ProvideActivityManagerWrapperFactory(SharedLibraryModule sharedLibraryModule) {
        this.module = sharedLibraryModule;
    }

    public ActivityManagerWrapper get() {
        return provideActivityManagerWrapper(this.module);
    }

    public static SharedLibraryModule_ProvideActivityManagerWrapperFactory create(SharedLibraryModule sharedLibraryModule) {
        return new SharedLibraryModule_ProvideActivityManagerWrapperFactory(sharedLibraryModule);
    }

    public static ActivityManagerWrapper provideActivityManagerWrapper(SharedLibraryModule sharedLibraryModule) {
        return (ActivityManagerWrapper) Preconditions.checkNotNullFromProvides(sharedLibraryModule.provideActivityManagerWrapper());
    }
}

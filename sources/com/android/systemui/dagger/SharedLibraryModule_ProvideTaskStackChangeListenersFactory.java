package com.android.systemui.dagger;

import com.android.systemui.shared.system.TaskStackChangeListeners;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SharedLibraryModule_ProvideTaskStackChangeListenersFactory implements Factory<TaskStackChangeListeners> {
    public final SharedLibraryModule module;

    public SharedLibraryModule_ProvideTaskStackChangeListenersFactory(SharedLibraryModule sharedLibraryModule) {
        this.module = sharedLibraryModule;
    }

    public TaskStackChangeListeners get() {
        return provideTaskStackChangeListeners(this.module);
    }

    public static SharedLibraryModule_ProvideTaskStackChangeListenersFactory create(SharedLibraryModule sharedLibraryModule) {
        return new SharedLibraryModule_ProvideTaskStackChangeListenersFactory(sharedLibraryModule);
    }

    public static TaskStackChangeListeners provideTaskStackChangeListeners(SharedLibraryModule sharedLibraryModule) {
        return (TaskStackChangeListeners) Preconditions.checkNotNullFromProvides(sharedLibraryModule.provideTaskStackChangeListeners());
    }
}

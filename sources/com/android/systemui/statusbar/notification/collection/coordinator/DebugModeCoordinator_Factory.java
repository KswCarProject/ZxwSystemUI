package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class DebugModeCoordinator_Factory implements Factory<DebugModeCoordinator> {
    public final Provider<DebugModeFilterProvider> debugModeFilterProvider;

    public DebugModeCoordinator_Factory(Provider<DebugModeFilterProvider> provider) {
        this.debugModeFilterProvider = provider;
    }

    public DebugModeCoordinator get() {
        return newInstance(this.debugModeFilterProvider.get());
    }

    public static DebugModeCoordinator_Factory create(Provider<DebugModeFilterProvider> provider) {
        return new DebugModeCoordinator_Factory(provider);
    }

    public static DebugModeCoordinator newInstance(DebugModeFilterProvider debugModeFilterProvider2) {
        return new DebugModeCoordinator(debugModeFilterProvider2);
    }
}

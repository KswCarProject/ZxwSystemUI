package com.android.systemui.media.nearby;

import com.android.systemui.statusbar.CommandQueue;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NearbyMediaDevicesManager_Factory implements Factory<NearbyMediaDevicesManager> {
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<NearbyMediaDevicesLogger> loggerProvider;

    public NearbyMediaDevicesManager_Factory(Provider<CommandQueue> provider, Provider<NearbyMediaDevicesLogger> provider2) {
        this.commandQueueProvider = provider;
        this.loggerProvider = provider2;
    }

    public NearbyMediaDevicesManager get() {
        return newInstance(this.commandQueueProvider.get(), this.loggerProvider.get());
    }

    public static NearbyMediaDevicesManager_Factory create(Provider<CommandQueue> provider, Provider<NearbyMediaDevicesLogger> provider2) {
        return new NearbyMediaDevicesManager_Factory(provider, provider2);
    }

    public static NearbyMediaDevicesManager newInstance(CommandQueue commandQueue, NearbyMediaDevicesLogger nearbyMediaDevicesLogger) {
        return new NearbyMediaDevicesManager(commandQueue, nearbyMediaDevicesLogger);
    }
}

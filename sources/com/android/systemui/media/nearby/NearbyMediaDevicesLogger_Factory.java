package com.android.systemui.media.nearby;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NearbyMediaDevicesLogger_Factory implements Factory<NearbyMediaDevicesLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public NearbyMediaDevicesLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public NearbyMediaDevicesLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static NearbyMediaDevicesLogger_Factory create(Provider<LogBuffer> provider) {
        return new NearbyMediaDevicesLogger_Factory(provider);
    }

    public static NearbyMediaDevicesLogger newInstance(LogBuffer logBuffer) {
        return new NearbyMediaDevicesLogger(logBuffer);
    }
}

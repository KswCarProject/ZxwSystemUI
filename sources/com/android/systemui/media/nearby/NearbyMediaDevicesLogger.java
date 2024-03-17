package com.android.systemui.media.nearby;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NearbyMediaDevicesLogger.kt */
public final class NearbyMediaDevicesLogger {
    @NotNull
    public final LogBuffer buffer;

    public NearbyMediaDevicesLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logProviderRegistered(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NearbyMediaDevices", LogLevel.DEBUG, NearbyMediaDevicesLogger$logProviderRegistered$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logProviderUnregistered(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NearbyMediaDevices", LogLevel.DEBUG, NearbyMediaDevicesLogger$logProviderUnregistered$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logProviderBinderDied(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NearbyMediaDevices", LogLevel.DEBUG, NearbyMediaDevicesLogger$logProviderBinderDied$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }
}

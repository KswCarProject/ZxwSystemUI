package com.android.systemui.media.muteawait;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaMuteAwaitLogger.kt */
public final class MediaMuteAwaitLogger {
    @NotNull
    public final LogBuffer buffer;

    public MediaMuteAwaitLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logMutedDeviceAdded(@NotNull String str, @NotNull String str2, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaMuteAwait", LogLevel.DEBUG, MediaMuteAwaitLogger$logMutedDeviceAdded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logMutedDeviceRemoved(@NotNull String str, @NotNull String str2, boolean z, boolean z2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaMuteAwait", LogLevel.DEBUG, MediaMuteAwaitLogger$logMutedDeviceRemoved$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setBool1(z);
        obtain.setBool2(z2);
        logBuffer.commit(obtain);
    }
}

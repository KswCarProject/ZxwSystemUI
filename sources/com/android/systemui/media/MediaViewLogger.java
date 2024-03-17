package com.android.systemui.media;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaViewLogger.kt */
public final class MediaViewLogger {
    @NotNull
    public final LogBuffer buffer;

    public MediaViewLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logMediaSize(@NotNull String str, int i, int i2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaView", LogLevel.DEBUG, MediaViewLogger$logMediaSize$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setInt2(i2);
        logBuffer.commit(obtain);
    }

    public final void logMediaLocation(@NotNull String str, int i, int i2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaView", LogLevel.DEBUG, MediaViewLogger$logMediaLocation$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setInt2(i2);
        logBuffer.commit(obtain);
    }
}

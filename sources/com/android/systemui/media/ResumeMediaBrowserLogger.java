package com.android.systemui.media;

import android.content.ComponentName;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ResumeMediaBrowserLogger.kt */
public final class ResumeMediaBrowserLogger {
    @NotNull
    public final LogBuffer buffer;

    public ResumeMediaBrowserLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logConnection(@NotNull ComponentName componentName, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaBrowser", LogLevel.DEBUG, ResumeMediaBrowserLogger$logConnection$2.INSTANCE);
        obtain.setStr1(componentName.toShortString());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logDisconnect(@NotNull ComponentName componentName) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaBrowser", LogLevel.DEBUG, ResumeMediaBrowserLogger$logDisconnect$2.INSTANCE);
        obtain.setStr1(componentName.toShortString());
        logBuffer.commit(obtain);
    }

    public final void logSessionDestroyed(boolean z, @NotNull ComponentName componentName) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaBrowser", LogLevel.DEBUG, ResumeMediaBrowserLogger$logSessionDestroyed$2.INSTANCE);
        obtain.setBool1(z);
        obtain.setStr1(componentName.toShortString());
        logBuffer.commit(obtain);
    }
}

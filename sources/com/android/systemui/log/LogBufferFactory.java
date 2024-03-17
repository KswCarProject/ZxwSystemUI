package com.android.systemui.log;

import android.app.ActivityManager;
import com.android.systemui.dump.DumpManager;
import org.jetbrains.annotations.NotNull;

/* compiled from: LogBufferFactory.kt */
public final class LogBufferFactory {
    @NotNull
    public final DumpManager dumpManager;
    @NotNull
    public final LogcatEchoTracker logcatEchoTracker;

    @NotNull
    public final LogBuffer create(@NotNull String str, int i) {
        return create$default(this, str, i, false, 4, (Object) null);
    }

    public LogBufferFactory(@NotNull DumpManager dumpManager2, @NotNull LogcatEchoTracker logcatEchoTracker2) {
        this.dumpManager = dumpManager2;
        this.logcatEchoTracker = logcatEchoTracker2;
    }

    public final int adjustMaxSize(int i) {
        return ActivityManager.isLowRamDeviceStatic() ? Math.min(i, 20) : i;
    }

    public static /* synthetic */ LogBuffer create$default(LogBufferFactory logBufferFactory, String str, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = true;
        }
        return logBufferFactory.create(str, i, z);
    }

    @NotNull
    public final LogBuffer create(@NotNull String str, int i, boolean z) {
        LogBuffer logBuffer = new LogBuffer(str, adjustMaxSize(i), this.logcatEchoTracker, z);
        this.dumpManager.registerBuffer(str, logBuffer);
        return logBuffer;
    }
}

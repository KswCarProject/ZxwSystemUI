package com.android.systemui.media.taptotransfer.common;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaTttLogger.kt */
public final class MediaTttLogger {
    @NotNull
    public final LogBuffer buffer;
    @NotNull
    public final String deviceTypeTag;

    public MediaTttLogger(@NotNull String str, @NotNull LogBuffer logBuffer) {
        this.deviceTypeTag = str;
        this.buffer = logBuffer;
    }

    public final void logStateChange(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain(Intrinsics.stringPlus("MediaTtt", this.deviceTypeTag), LogLevel.DEBUG, MediaTttLogger$logStateChange$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logChipRemoval(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain(Intrinsics.stringPlus("MediaTtt", this.deviceTypeTag), LogLevel.DEBUG, MediaTttLogger$logChipRemoval$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}

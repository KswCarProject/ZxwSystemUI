package com.android.systemui.media;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: MediaCarouselControllerLogger.kt */
public final class MediaCarouselControllerLogger {
    @NotNull
    public final LogBuffer buffer;

    public MediaCarouselControllerLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logPotentialMemoryLeak(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("MediaCarouselCtlrLog", LogLevel.DEBUG, MediaCarouselControllerLogger$logPotentialMemoryLeak$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}

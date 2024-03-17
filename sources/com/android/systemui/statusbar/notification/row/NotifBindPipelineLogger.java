package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifBindPipelineLogger.kt */
public final class NotifBindPipelineLogger {
    @NotNull
    public final LogBuffer buffer;

    public NotifBindPipelineLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logStageSet(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logStageSet$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logManagedRow(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logManagedRow$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logRequestPipelineRun(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logRequestPipelineRun$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logRequestPipelineRowNotSet(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logRequestPipelineRowNotSet$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logStartPipeline(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logStartPipeline$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logFinishedPipeline(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifBindPipeline", LogLevel.INFO, NotifBindPipelineLogger$logFinishedPipeline$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }
}

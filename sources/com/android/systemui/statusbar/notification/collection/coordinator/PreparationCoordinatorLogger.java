package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: PreparationCoordinatorLogger.kt */
public final class PreparationCoordinatorLogger {
    @NotNull
    public final LogBuffer buffer;

    public PreparationCoordinatorLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logNotifInflated(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", LogLevel.DEBUG, PreparationCoordinatorLogger$logNotifInflated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logInflationAborted(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", LogLevel.DEBUG, PreparationCoordinatorLogger$logInflationAborted$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logDoneWaitingForGroupInflation(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", LogLevel.DEBUG, PreparationCoordinatorLogger$logDoneWaitingForGroupInflation$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logGroupInflationTookTooLong(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", LogLevel.WARNING, PreparationCoordinatorLogger$logGroupInflationTookTooLong$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDelayingGroupRelease(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("PreparationCoordinator", LogLevel.DEBUG, PreparationCoordinatorLogger$logDelayingGroupRelease$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }
}

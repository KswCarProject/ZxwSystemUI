package com.android.systemui.statusbar.notification.collection.coalescer;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: GroupCoalescerLogger.kt */
public final class GroupCoalescerLogger {
    @NotNull
    public final LogBuffer buffer;

    public GroupCoalescerLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logEventCoalesced(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", LogLevel.INFO, GroupCoalescerLogger$logEventCoalesced$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logEmitBatch(@NotNull String str, int i, long j) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", LogLevel.DEBUG, GroupCoalescerLogger$logEmitBatch$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        obtain.setLong1(j);
        logBuffer.commit(obtain);
    }

    public final void logEarlyEmit(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", LogLevel.DEBUG, GroupCoalescerLogger$logEarlyEmit$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logMaxBatchTimeout(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", LogLevel.INFO, GroupCoalescerLogger$logMaxBatchTimeout$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logMissingRanking(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("GroupCoalescer", LogLevel.WARNING, GroupCoalescerLogger$logMissingRanking$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}

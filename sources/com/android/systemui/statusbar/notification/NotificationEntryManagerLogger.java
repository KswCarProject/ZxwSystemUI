package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationEntryManagerLogger.kt */
public final class NotificationEntryManagerLogger {
    @NotNull
    public final LogBuffer buffer;

    public NotificationEntryManagerLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logNotifAdded(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logNotifAdded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifUpdated(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logNotifUpdated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logInflationAborted(@NotNull String str, @NotNull String str2, @NotNull String str3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.DEBUG, NotificationEntryManagerLogger$logInflationAborted$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNotifInflated(@NotNull String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.DEBUG, NotificationEntryManagerLogger$logNotifInflated$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logRemovalIntercepted(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logRemovalIntercepted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logLifetimeExtended(@NotNull String str, @NotNull String str2, @NotNull String str3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logLifetimeExtended$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNotifRemoved(@NotNull String str, boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logNotifRemoved$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logFilterAndSort(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.INFO, NotificationEntryManagerLogger$logFilterAndSort$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logUseWhileNewPipelineActive(@NotNull String str, @NotNull String str2) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationEntryMgr", LogLevel.WARNING, NotificationEntryManagerLogger$logUseWhileNewPipelineActive$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }
}

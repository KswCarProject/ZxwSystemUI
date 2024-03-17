package com.android.systemui.statusbar.notification;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationClickerLogger.kt */
public final class NotificationClickerLogger {
    @NotNull
    public final LogBuffer buffer;

    public NotificationClickerLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logOnClick(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logOnClick$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        obtain.setStr2(notificationEntry.getRanking().getChannel().getId());
        logBuffer.commit(obtain);
    }

    public final void logMenuVisible(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logMenuVisible$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logParentMenuVisible(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logParentMenuVisible$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logChildrenExpanded(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logChildrenExpanded$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logGutsExposed(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotificationClicker", LogLevel.DEBUG, NotificationClickerLogger$logGutsExposed$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }
}

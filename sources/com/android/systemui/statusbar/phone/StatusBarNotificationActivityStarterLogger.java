package com.android.systemui.statusbar.phone;

import android.app.PendingIntent;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.NotificationUtilsKt;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
public final class StatusBarNotificationActivityStarterLogger {
    @NotNull
    public final LogBuffer buffer;

    public StatusBarNotificationActivityStarterLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logStartingActivityFromClick(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logStartingActivityFromClick$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logHandleClickAfterKeyguardDismissed(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logHandleClickAfterKeyguardDismissed$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logHandleClickAfterPanelCollapsed(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logHandleClickAfterPanelCollapsed$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logStartNotificationIntent(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.INFO, StatusBarNotificationActivityStarterLogger$logStartNotificationIntent$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logSendPendingIntent(@NotNull NotificationEntry notificationEntry, @NotNull PendingIntent pendingIntent, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.INFO, StatusBarNotificationActivityStarterLogger$logSendPendingIntent$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        obtain.setStr2(pendingIntent.getIntent().toString());
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logExpandingBubble(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logExpandingBubble$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logSendingIntentFailed(@NotNull Exception exc) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.WARNING, StatusBarNotificationActivityStarterLogger$logSendingIntentFailed$2.INSTANCE);
        obtain.setStr1(exc.toString());
        logBuffer.commit(obtain);
    }

    public final void logNonClickableNotification(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.ERROR, StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logFullScreenIntentSuppressedByDnD(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logFullScreenIntentSuppressedByDnD$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logFullScreenIntentNotImportantEnough(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.DEBUG, StatusBarNotificationActivityStarterLogger$logFullScreenIntentNotImportantEnough$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }

    public final void logSendingFullScreenIntent(@NotNull NotificationEntry notificationEntry, @NotNull PendingIntent pendingIntent) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifActivityStarter", LogLevel.INFO, StatusBarNotificationActivityStarterLogger$logSendingFullScreenIntent$2.INSTANCE);
        obtain.setStr1(NotificationUtilsKt.getLogKey(notificationEntry));
        obtain.setStr2(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }
}

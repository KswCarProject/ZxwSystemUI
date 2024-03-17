package com.android.systemui.statusbar.notification.interruption;

import android.service.notification.StatusBarNotification;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationInterruptLogger.kt */
public final class NotificationInterruptLogger {
    @NotNull
    public final LogBuffer hunBuffer;
    @NotNull
    public final LogBuffer notifBuffer;

    public NotificationInterruptLogger(@NotNull LogBuffer logBuffer, @NotNull LogBuffer logBuffer2) {
        this.notifBuffer = logBuffer;
        this.hunBuffer = logBuffer2;
    }

    public final void logHeadsUpFeatureChanged(boolean z) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.INFO, NotificationInterruptLogger$logHeadsUpFeatureChanged$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logWillDismissAll() {
        LogBuffer logBuffer = this.hunBuffer;
        logBuffer.commit(logBuffer.obtain("InterruptionStateProvider", LogLevel.INFO, NotificationInterruptLogger$logWillDismissAll$2.INSTANCE));
    }

    public final void logNoBubbleNotAllowed(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.notifBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoBubbleNotAllowed$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoBubbleNoMetadata(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.notifBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoBubbleNoMetadata$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpFeatureDisabled() {
        LogBuffer logBuffer = this.hunBuffer;
        logBuffer.commit(logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpFeatureDisabled$2.INSTANCE));
    }

    public final void logNoHeadsUpPackageSnoozed(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpPackageSnoozed$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpAlreadyBubbled(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpAlreadyBubbled$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpSuppressedByDnd(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpSuppressedByDnd$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpNotImportant(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpNotImportant$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpNotInUse(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpNotInUse$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoHeadsUpSuppressedBy(@NotNull StatusBarNotification statusBarNotification, @NotNull NotificationInterruptSuppressor notificationInterruptSuppressor) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoHeadsUpSuppressedBy$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        obtain.setStr2(notificationInterruptSuppressor.getName());
        logBuffer.commit(obtain);
    }

    public final void logHeadsUp(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logHeadsUp$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingFilteredOut(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingFilteredOut$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingGroupAlertBehavior(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingGroupAlertBehavior$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingSuppressedBy(@NotNull StatusBarNotification statusBarNotification, @NotNull NotificationInterruptSuppressor notificationInterruptSuppressor, boolean z) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingSuppressedBy$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        obtain.setStr2(notificationInterruptSuppressor.getName());
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logNoAlertingRecentFullscreen(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoAlertingRecentFullscreen$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingSettingDisabled(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingSettingDisabled$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingBatteryDisabled(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingBatteryDisabled$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingNoAlert(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingNoAlert$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingNoAmbientEffect(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingNoAmbientEffect$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNoPulsingNotImportant(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logNoPulsingNotImportant$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void logPulsing(@NotNull StatusBarNotification statusBarNotification) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$logPulsing$2.INSTANCE);
        obtain.setStr1(statusBarNotification.getKey());
        logBuffer.commit(obtain);
    }

    public final void keyguardHideNotification(@NotNull String str) {
        LogBuffer logBuffer = this.hunBuffer;
        LogMessageImpl obtain = logBuffer.obtain("InterruptionStateProvider", LogLevel.DEBUG, NotificationInterruptLogger$keyguardHideNotification$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}

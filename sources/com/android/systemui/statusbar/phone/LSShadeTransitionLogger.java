package com.android.systemui.statusbar.phone;

import android.util.DisplayMetrics;
import android.view.View;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LSShadeTransitionLogger.kt */
public final class LSShadeTransitionLogger {
    @NotNull
    public final LogBuffer buffer;
    @NotNull
    public final DisplayMetrics displayMetrics;
    @NotNull
    public final LockscreenGestureLogger lockscreenGestureLogger;

    public LSShadeTransitionLogger(@NotNull LogBuffer logBuffer, @NotNull LockscreenGestureLogger lockscreenGestureLogger2, @NotNull DisplayMetrics displayMetrics2) {
        this.buffer = logBuffer;
        this.lockscreenGestureLogger = lockscreenGestureLogger2;
        this.displayMetrics = displayMetrics2;
    }

    public final void logUnSuccessfulDragDown(@Nullable View view) {
        String key;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logUnSuccessfulDragDown$2.INSTANCE);
        String str = "no entry";
        if (!(notificationEntry == null || (key = notificationEntry.getKey()) == null)) {
            str = key;
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDragDownAborted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDragDownAborted$2.INSTANCE));
    }

    public final void logDragDownStarted(@Nullable ExpandableView expandableView) {
        String key;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = expandableView instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) expandableView : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDragDownStarted$2.INSTANCE);
        String str = "no entry";
        if (!(notificationEntry == null || (key = notificationEntry.getKey()) == null)) {
            str = key;
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDraggedDownLockDownShade(@Nullable View view) {
        String key;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDraggedDownLockDownShade$2.INSTANCE);
        String str = "no entry";
        if (!(notificationEntry == null || (key = notificationEntry.getKey()) == null)) {
            str = key;
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logDraggedDown(@Nullable View view, int i) {
        String key;
        NotificationEntry notificationEntry = null;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        if (expandableNotificationRow != null) {
            notificationEntry = expandableNotificationRow.getEntry();
        }
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logDraggedDown$2.INSTANCE);
        String str = "no entry";
        if (!(notificationEntry == null || (key = notificationEntry.getKey()) == null)) {
            str = key;
        }
        obtain.setStr1(str);
        logBuffer.commit(obtain);
        this.lockscreenGestureLogger.write(187, (int) (((float) i) / this.displayMetrics.density), 0);
        this.lockscreenGestureLogger.log(LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_PULL_SHADE_OPEN);
    }

    public final void logDragDownAmountReset() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logDragDownAmountReset$2.INSTANCE));
    }

    public final void logDefaultGoToFullShadeAnimation(long j) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logDefaultGoToFullShadeAnimation$2.INSTANCE);
        obtain.setLong1(j);
        logBuffer.commit(obtain);
    }

    public final void logTryGoToLockedShade(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logTryGoToLockedShade$2.INSTANCE);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logShadeDisabledOnGoToLockedShade() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.WARNING, LSShadeTransitionLogger$logShadeDisabledOnGoToLockedShade$2.INSTANCE));
    }

    public final void logShowBouncerOnGoToLockedShade() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logShowBouncerOnGoToLockedShade$2.INSTANCE));
    }

    public final void logGoingToLockedShade(boolean z) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, new LSShadeTransitionLogger$logGoingToLockedShade$2(z));
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    public final void logOnHideKeyguard() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logOnHideKeyguard$2.INSTANCE));
    }

    public final void logPulseExpansionStarted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logPulseExpansionStarted$2.INSTANCE));
    }

    public final void logPulseExpansionFinished(boolean z) {
        if (z) {
            LogBuffer logBuffer = this.buffer;
            logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logPulseExpansionFinished$2.INSTANCE));
            return;
        }
        LogBuffer logBuffer2 = this.buffer;
        logBuffer2.commit(logBuffer2.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logPulseExpansionFinished$4.INSTANCE));
    }

    public final void logDragDownAnimation(float f) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logDragDownAnimation$2.INSTANCE);
        obtain.setDouble1((double) f);
        logBuffer.commit(obtain);
    }

    public final void logAnimationCancelled(boolean z) {
        if (z) {
            LogBuffer logBuffer = this.buffer;
            logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logAnimationCancelled$2.INSTANCE));
            return;
        }
        LogBuffer logBuffer2 = this.buffer;
        logBuffer2.commit(logBuffer2.obtain("LockscreenShadeTransitionController", LogLevel.DEBUG, LSShadeTransitionLogger$logAnimationCancelled$4.INSTANCE));
    }

    public final void logDragDownAmountResetWhenFullyCollapsed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.WARNING, LSShadeTransitionLogger$logDragDownAmountResetWhenFullyCollapsed$2.INSTANCE));
    }

    public final void logPulseHeightNotResetWhenFullyCollapsed() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.WARNING, LSShadeTransitionLogger$logPulseHeightNotResetWhenFullyCollapsed$2.INSTANCE));
    }

    public final void logGoingToLockedShadeAborted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("LockscreenShadeTransitionController", LogLevel.INFO, LSShadeTransitionLogger$logGoingToLockedShadeAborted$2.INSTANCE));
    }
}

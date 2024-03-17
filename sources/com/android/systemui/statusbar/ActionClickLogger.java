package com.android.systemui.statusbar;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ActionClickLogger.kt */
public final class ActionClickLogger {
    @NotNull
    public final LogBuffer buffer;

    public ActionClickLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logInitialClick(@Nullable NotificationEntry notificationEntry, @NotNull PendingIntent pendingIntent) {
        NotificationListenerService.Ranking ranking;
        NotificationChannel channel;
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logInitialClick$2.INSTANCE);
        String str = null;
        obtain.setStr1(notificationEntry == null ? null : notificationEntry.getKey());
        if (!(notificationEntry == null || (ranking = notificationEntry.getRanking()) == null || (channel = ranking.getChannel()) == null)) {
            str = channel.getId();
        }
        obtain.setStr2(str);
        obtain.setStr3(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }

    public final void logRemoteInputWasHandled(@Nullable NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logRemoteInputWasHandled$2.INSTANCE);
        obtain.setStr1(notificationEntry == null ? null : notificationEntry.getKey());
        logBuffer.commit(obtain);
    }

    public final void logStartingIntentWithDefaultHandler(@Nullable NotificationEntry notificationEntry, @NotNull PendingIntent pendingIntent) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logStartingIntentWithDefaultHandler$2.INSTANCE);
        obtain.setStr1(notificationEntry == null ? null : notificationEntry.getKey());
        obtain.setStr2(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }

    public final void logWaitingToCloseKeyguard(@NotNull PendingIntent pendingIntent) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logWaitingToCloseKeyguard$2.INSTANCE);
        obtain.setStr1(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }

    public final void logKeyguardGone(@NotNull PendingIntent pendingIntent) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ActionClickLogger", LogLevel.DEBUG, ActionClickLogger$logKeyguardGone$2.INSTANCE);
        obtain.setStr1(pendingIntent.getIntent().toString());
        logBuffer.commit(obtain);
    }
}

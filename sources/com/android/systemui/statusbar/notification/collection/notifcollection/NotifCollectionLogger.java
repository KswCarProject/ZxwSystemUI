package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.os.RemoteException;
import android.service.notification.NotificationListenerService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.statusbar.notification.NotificationUtilsKt;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifCollectionLogger.kt */
public final class NotifCollectionLogger {
    @NotNull
    public final LogBuffer buffer;

    public NotifCollectionLogger(@NotNull LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logNotifPosted(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifPosted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifGroupPosted(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifGroupPosted$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifUpdated(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifUpdated$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifRemoved(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifRemoved$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logNotifReleased(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifReleased$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifDismissed(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifDismissed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNonExistentNotifDismissed(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNonExistentNotifDismissed$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logChildDismissed(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logChildDismissed$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        logBuffer.commit(obtain);
    }

    public final void logDismissAll(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logDismissAll$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logDismissOnAlreadyCanceledEntry(@NotNull NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logDismissOnAlreadyCanceledEntry$2.INSTANCE);
        obtain.setStr1(notificationEntry.getKey());
        logBuffer.commit(obtain);
    }

    public final void logNotifDismissedIntercepted(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifDismissedIntercepted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifClearAllDismissalIntercepted(@NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifClearAllDismissalIntercepted$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logNotifInternalUpdate(@NotNull String str, @NotNull String str2, @NotNull String str3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifInternalUpdate$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNotifInternalUpdateFailed(@NotNull String str, @NotNull String str2, @NotNull String str3) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logNotifInternalUpdateFailed$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logNoNotificationToRemoveWithKey(@NotNull String str, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.ERROR, NotifCollectionLogger$logNoNotificationToRemoveWithKey$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logRankingMissing(@NotNull String str, @NotNull NotificationListenerService.RankingMap rankingMap) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logRankingMissing$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
        LogBuffer logBuffer2 = this.buffer;
        logBuffer2.commit(logBuffer2.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logRankingMissing$4.INSTANCE));
        String[] orderedKeys = rankingMap.getOrderedKeys();
        int length = orderedKeys.length;
        int i = 0;
        while (i < length) {
            String str2 = orderedKeys[i];
            i++;
            LogBuffer logBuffer3 = this.buffer;
            LogMessageImpl obtain2 = logBuffer3.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logRankingMissing$6.INSTANCE);
            obtain2.setStr1(str2);
            logBuffer3.commit(obtain2);
        }
    }

    public final void logRemoteExceptionOnNotificationClear(@NotNull String str, @NotNull RemoteException remoteException) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.WTF, NotifCollectionLogger$logRemoteExceptionOnNotificationClear$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(remoteException.toString());
        logBuffer.commit(obtain);
    }

    public final void logRemoteExceptionOnClearAllNotifications(@NotNull RemoteException remoteException) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.WTF, NotifCollectionLogger$logRemoteExceptionOnClearAllNotifications$2.INSTANCE);
        obtain.setStr1(remoteException.toString());
        logBuffer.commit(obtain);
    }

    public final void logLifetimeExtended(@NotNull String str, @NotNull NotifLifetimeExtender notifLifetimeExtender) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logLifetimeExtended$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(notifLifetimeExtender.getName());
        logBuffer.commit(obtain);
    }

    public final void logLifetimeExtensionEnded(@NotNull String str, @NotNull NotifLifetimeExtender notifLifetimeExtender, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logLifetimeExtensionEnded$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(notifLifetimeExtender.getName());
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalReused(@NotNull NotifCollection.FutureDismissal futureDismissal) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.INFO, NotifCollectionLogger$logFutureDismissalReused$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalRegistered(@NotNull NotifCollection.FutureDismissal futureDismissal) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalRegistered$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalDoubleCancelledByServer(@NotNull NotifCollection.FutureDismissal futureDismissal) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logFutureDismissalDoubleCancelledByServer$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalDoubleRun(@NotNull NotifCollection.FutureDismissal futureDismissal) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logFutureDismissalDoubleRun$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalAlreadyCancelledByServer(@NotNull NotifCollection.FutureDismissal futureDismissal) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalAlreadyCancelledByServer$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalGotSystemServerCancel(@NotNull NotifCollection.FutureDismissal futureDismissal, int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalGotSystemServerCancel$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalDismissing(@NotNull NotifCollection.FutureDismissal futureDismissal, @NotNull String str) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.DEBUG, NotifCollectionLogger$logFutureDismissalDismissing$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        obtain.setStr2(str);
        logBuffer.commit(obtain);
    }

    public final void logFutureDismissalMismatchedEntry(@NotNull NotifCollection.FutureDismissal futureDismissal, @NotNull String str, @Nullable NotificationEntry notificationEntry) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("NotifCollection", LogLevel.WARNING, NotifCollectionLogger$logFutureDismissalMismatchedEntry$2.INSTANCE);
        obtain.setStr1(futureDismissal.getLabel());
        obtain.setStr2(str);
        obtain.setStr3(NotificationUtilsKt.getLogKey(notificationEntry));
        logBuffer.commit(obtain);
    }
}

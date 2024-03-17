package com.android.systemui.statusbar.notification.collection;

import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: TargetSdkResolver.kt */
public final class TargetSdkResolver$initialize$1 implements NotifCollectionListener {
    public final /* synthetic */ TargetSdkResolver this$0;

    public TargetSdkResolver$initialize$1(TargetSdkResolver targetSdkResolver) {
        this.this$0 = targetSdkResolver;
    }

    public void onEntryBind(@NotNull NotificationEntry notificationEntry, @NotNull StatusBarNotification statusBarNotification) {
        notificationEntry.targetSdk = this.this$0.resolveNotificationSdk(statusBarNotification);
    }
}

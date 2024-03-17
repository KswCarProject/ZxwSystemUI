package com.android.systemui.statusbar.phone.ongoingcall;

import android.app.PendingIntent;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: OngoingCallController.kt */
public final class OngoingCallController$notifListener$1 implements NotifCollectionListener {
    public final /* synthetic */ OngoingCallController this$0;

    public OngoingCallController$notifListener$1(OngoingCallController ongoingCallController) {
        this.this$0 = ongoingCallController;
    }

    public void onEntryAdded(@NotNull NotificationEntry notificationEntry) {
        onEntryUpdated(notificationEntry, true);
    }

    public void onEntryUpdated(@NotNull NotificationEntry notificationEntry) {
        if (this.this$0.callNotificationInfo != null || !OngoingCallControllerKt.isCallNotification(notificationEntry)) {
            String key = notificationEntry.getSbn().getKey();
            OngoingCallController.CallNotificationInfo access$getCallNotificationInfo$p = this.this$0.callNotificationInfo;
            if (!Intrinsics.areEqual((Object) key, (Object) access$getCallNotificationInfo$p == null ? null : access$getCallNotificationInfo$p.getKey())) {
                return;
            }
        }
        String key2 = notificationEntry.getSbn().getKey();
        long j = notificationEntry.getSbn().getNotification().when;
        PendingIntent pendingIntent = notificationEntry.getSbn().getNotification().contentIntent;
        int uid = notificationEntry.getSbn().getUid();
        boolean z = notificationEntry.getSbn().getNotification().extras.getInt("android.callType", -1) == 2;
        OngoingCallController.CallNotificationInfo access$getCallNotificationInfo$p2 = this.this$0.callNotificationInfo;
        OngoingCallController.CallNotificationInfo callNotificationInfo = new OngoingCallController.CallNotificationInfo(key2, j, pendingIntent, uid, z, access$getCallNotificationInfo$p2 == null ? false : access$getCallNotificationInfo$p2.getStatusBarSwipedAway());
        if (!Intrinsics.areEqual((Object) callNotificationInfo, (Object) this.this$0.callNotificationInfo)) {
            this.this$0.callNotificationInfo = callNotificationInfo;
            if (callNotificationInfo.isOngoing()) {
                this.this$0.updateChip();
            } else {
                this.this$0.removeChip();
            }
        }
    }

    public void onEntryRemoved(@NotNull NotificationEntry notificationEntry, int i) {
        String key = notificationEntry.getSbn().getKey();
        OngoingCallController.CallNotificationInfo access$getCallNotificationInfo$p = this.this$0.callNotificationInfo;
        if (Intrinsics.areEqual((Object) key, (Object) access$getCallNotificationInfo$p == null ? null : access$getCallNotificationInfo$p.getKey())) {
            this.this$0.removeChip();
        }
    }
}

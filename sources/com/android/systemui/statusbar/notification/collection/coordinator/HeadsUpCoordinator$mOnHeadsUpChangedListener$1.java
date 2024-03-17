package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$mOnHeadsUpChangedListener$1 implements OnHeadsUpChangedListener {
    public final /* synthetic */ HeadsUpCoordinator this$0;

    public HeadsUpCoordinator$mOnHeadsUpChangedListener$1(HeadsUpCoordinator headsUpCoordinator) {
        this.this$0 = headsUpCoordinator;
    }

    public void onHeadsUpStateChanged(@NotNull NotificationEntry notificationEntry, boolean z) {
        if (!z) {
            this.this$0.mHeadsUpViewBinder.unbindHeadsUpView(notificationEntry);
            this.this$0.endNotifLifetimeExtensionIfExtended(notificationEntry);
        }
    }
}

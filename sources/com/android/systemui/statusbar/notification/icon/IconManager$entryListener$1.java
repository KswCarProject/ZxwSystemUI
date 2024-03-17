package com.android.systemui.statusbar.notification.icon;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import org.jetbrains.annotations.NotNull;

/* compiled from: IconManager.kt */
public final class IconManager$entryListener$1 implements NotifCollectionListener {
    public final /* synthetic */ IconManager this$0;

    public IconManager$entryListener$1(IconManager iconManager) {
        this.this$0 = iconManager;
    }

    public void onEntryInit(@NotNull NotificationEntry notificationEntry) {
        notificationEntry.addOnSensitivityChangedListener(this.this$0.sensitivityListener);
    }

    public void onEntryCleanUp(@NotNull NotificationEntry notificationEntry) {
        notificationEntry.removeOnSensitivityChangedListener(this.this$0.sensitivityListener);
    }

    public void onRankingApplied() {
        this.this$0.recalculateForImportantConversationChange();
    }
}

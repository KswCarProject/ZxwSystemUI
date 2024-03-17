package com.android.systemui.statusbar.notification.icon;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: IconManager.kt */
public final class IconManager$sensitivityListener$1 implements NotificationEntry.OnSensitivityChangedListener {
    public final /* synthetic */ IconManager this$0;

    public IconManager$sensitivityListener$1(IconManager iconManager) {
        this.this$0 = iconManager;
    }

    public final void onSensitivityChanged(@NotNull NotificationEntry notificationEntry) {
        this.this$0.updateIconsSafe(notificationEntry);
    }
}

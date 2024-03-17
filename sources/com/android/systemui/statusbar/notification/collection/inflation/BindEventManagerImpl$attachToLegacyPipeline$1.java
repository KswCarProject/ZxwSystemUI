package com.android.systemui.statusbar.notification.collection.inflation;

import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import org.jetbrains.annotations.NotNull;

/* compiled from: BindEventManagerImpl.kt */
public final class BindEventManagerImpl$attachToLegacyPipeline$1 implements NotificationEntryListener {
    public final /* synthetic */ BindEventManagerImpl this$0;

    public BindEventManagerImpl$attachToLegacyPipeline$1(BindEventManagerImpl bindEventManagerImpl) {
        this.this$0 = bindEventManagerImpl;
    }

    public void onEntryInflated(@NotNull NotificationEntry notificationEntry) {
        this.this$0.notifyViewBound(notificationEntry);
    }

    public void onEntryReinflated(@NotNull NotificationEntry notificationEntry) {
        this.this$0.notifyViewBound(notificationEntry);
    }
}

package com.android.systemui.statusbar.notification.collection;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotificationRankingManager.kt */
public /* synthetic */ class NotificationRankingManager$filterAndSortLocked$filtered$1 extends FunctionReferenceImpl implements Function1<NotificationEntry, Boolean> {
    public NotificationRankingManager$filterAndSortLocked$filtered$1(Object obj) {
        super(1, obj, NotificationRankingManager.class, "filter", "filter(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Z", 0);
    }

    @NotNull
    public final Boolean invoke(@NotNull NotificationEntry notificationEntry) {
        return Boolean.valueOf(((NotificationRankingManager) this.receiver).filter(notificationEntry));
    }
}

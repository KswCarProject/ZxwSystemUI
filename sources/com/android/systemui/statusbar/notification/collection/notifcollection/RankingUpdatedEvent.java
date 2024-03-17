package com.android.systemui.statusbar.notification.collection.notifcollection;

import android.service.notification.NotificationListenerService;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifEvent.kt */
public final class RankingUpdatedEvent extends NotifEvent {
    @NotNull
    public final NotificationListenerService.RankingMap rankingMap;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof RankingUpdatedEvent) && Intrinsics.areEqual((Object) this.rankingMap, (Object) ((RankingUpdatedEvent) obj).rankingMap);
    }

    public int hashCode() {
        return this.rankingMap.hashCode();
    }

    @NotNull
    public String toString() {
        return "RankingUpdatedEvent(rankingMap=" + this.rankingMap + ')';
    }

    public RankingUpdatedEvent(@NotNull NotificationListenerService.RankingMap rankingMap2) {
        super((DefaultConstructorMarker) null);
        this.rankingMap = rankingMap2;
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onRankingUpdate(this.rankingMap);
    }
}

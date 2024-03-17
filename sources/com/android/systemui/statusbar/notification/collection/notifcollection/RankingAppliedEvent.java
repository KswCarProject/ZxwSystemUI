package com.android.systemui.statusbar.notification.collection.notifcollection;

import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: NotifEvent.kt */
public final class RankingAppliedEvent extends NotifEvent {
    public RankingAppliedEvent() {
        super((DefaultConstructorMarker) null);
    }

    public void dispatchToListener(@NotNull NotifCollectionListener notifCollectionListener) {
        notifCollectionListener.onRankingApplied();
    }
}

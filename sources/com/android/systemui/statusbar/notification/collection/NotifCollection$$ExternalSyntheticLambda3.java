package com.android.systemui.statusbar.notification.collection;

import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotifCollection$$ExternalSyntheticLambda3 implements Predicate {
    public final boolean test(Object obj) {
        return ((NotificationEntry) obj).getSbn().getNotification().isGroupSummary();
    }
}

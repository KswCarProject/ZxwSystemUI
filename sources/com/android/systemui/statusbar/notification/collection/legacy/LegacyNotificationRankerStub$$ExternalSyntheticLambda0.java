package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.ToLongFunction;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LegacyNotificationRankerStub$$ExternalSyntheticLambda0 implements ToLongFunction {
    public final long applyAsLong(Object obj) {
        return ((NotificationEntry) obj).getSbn().getNotification().when;
    }
}

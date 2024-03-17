package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: PeopleNotificationIdentifier.kt */
public final class PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1 extends Lambda implements Function1<NotificationEntry, Integer> {
    public final /* synthetic */ PeopleNotificationIdentifierImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PeopleNotificationIdentifierImpl$getPeopleTypeOfSummary$childTypes$1(PeopleNotificationIdentifierImpl peopleNotificationIdentifierImpl) {
        super(1);
        this.this$0 = peopleNotificationIdentifierImpl;
    }

    @NotNull
    public final Integer invoke(NotificationEntry notificationEntry) {
        return Integer.valueOf(this.this$0.getPeopleNotificationType(notificationEntry));
    }
}

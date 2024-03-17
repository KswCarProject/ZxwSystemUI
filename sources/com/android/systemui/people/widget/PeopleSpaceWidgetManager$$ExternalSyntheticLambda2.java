package com.android.systemui.people.widget;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.function.Predicate;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PeopleSpaceWidgetManager$$ExternalSyntheticLambda2 implements Predicate {
    public final /* synthetic */ PeopleSpaceWidgetManager f$0;

    public /* synthetic */ PeopleSpaceWidgetManager$$ExternalSyntheticLambda2(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.f$0 = peopleSpaceWidgetManager;
    }

    public final boolean test(Object obj) {
        return this.f$0.lambda$groupConversationNotifications$4((NotificationEntry) obj);
    }
}

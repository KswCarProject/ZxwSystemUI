package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.icon.ConversationIconManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator implements Coordinator {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final ConversationIconManager conversationIconManager;
    @NotNull
    public final ConversationCoordinator$notificationPromoter$1 notificationPromoter = new ConversationCoordinator$notificationPromoter$1(this);
    @NotNull
    public final OnBeforeRenderListListener onBeforeRenderListListener = new ConversationCoordinator$onBeforeRenderListListener$1(this);
    @NotNull
    public final PeopleNotificationIdentifier peopleNotificationIdentifier;
    @NotNull
    public final Map<NotificationEntry, NotificationEntry> promotedEntriesToSummaryOfSameChannel = new LinkedHashMap();
    @NotNull
    public final NotifSectioner sectioner;

    public ConversationCoordinator(@NotNull PeopleNotificationIdentifier peopleNotificationIdentifier2, @NotNull ConversationIconManager conversationIconManager2, @NotNull NodeController nodeController) {
        this.peopleNotificationIdentifier = peopleNotificationIdentifier2;
        this.conversationIconManager = conversationIconManager2;
        this.sectioner = new ConversationCoordinator$sectioner$1(this, nodeController);
    }

    @NotNull
    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    public void attach(@NotNull NotifPipeline notifPipeline) {
        notifPipeline.addPromoter(this.notificationPromoter);
        notifPipeline.addOnBeforeRenderListListener(this.onBeforeRenderListListener);
    }

    public final boolean isConversation(ListEntry listEntry) {
        return getPeopleType(listEntry) != 0;
    }

    public final int getPeopleType(ListEntry listEntry) {
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        if (representativeEntry == null) {
            return 0;
        }
        return this.peopleNotificationIdentifier.getPeopleNotificationType(representativeEntry);
    }

    /* compiled from: ConversationCoordinator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}

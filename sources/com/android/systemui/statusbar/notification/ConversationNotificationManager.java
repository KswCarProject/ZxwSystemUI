package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.view.View;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import kotlin.Function;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsKt;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ConversationNotifications.kt */
public final class ConversationNotificationManager {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public final BindEventManager bindEventManager;
    @NotNull
    public final Context context;
    @NotNull
    public final NotifPipelineFlags featureFlags;
    @NotNull
    public final Handler mainHandler;
    @NotNull
    public final CommonNotifCollection notifCollection;
    public boolean notifPanelCollapsed = true;
    @NotNull
    public final NotificationGroupManagerLegacy notificationGroupManager;
    @NotNull
    public final ConcurrentHashMap<String, ConversationState> states = new ConcurrentHashMap<>();

    public ConversationNotificationManager(@NotNull BindEventManager bindEventManager2, @NotNull NotificationGroupManagerLegacy notificationGroupManagerLegacy, @NotNull Context context2, @NotNull CommonNotifCollection commonNotifCollection, @NotNull NotifPipelineFlags notifPipelineFlags, @NotNull Handler handler) {
        this.bindEventManager = bindEventManager2;
        this.notificationGroupManager = notificationGroupManagerLegacy;
        this.context = context2;
        this.notifCollection = commonNotifCollection;
        this.featureFlags = notifPipelineFlags;
        this.mainHandler = handler;
        commonNotifCollection.addCollectionListener(new NotifCollectionListener(this) {
            public final /* synthetic */ ConversationNotificationManager this$0;

            {
                this.this$0 = r1;
            }

            public void onRankingUpdate(@NotNull NotificationListenerService.RankingMap rankingMap) {
                this.this$0.updateNotificationRanking(rankingMap);
            }

            public void onEntryRemoved(@NotNull NotificationEntry notificationEntry, int i) {
                this.this$0.removeTrackedEntry(notificationEntry);
            }
        });
        bindEventManager2.addListener(new Object() {
            public final boolean equals(@Nullable Object obj) {
                if (!(obj instanceof BindEventManager.Listener) || !(obj instanceof FunctionAdapter)) {
                    return false;
                }
                return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
            }

            @NotNull
            public final Function<?> getFunctionDelegate() {
                return new FunctionReferenceImpl(1, ConversationNotificationManager.this, ConversationNotificationManager.class, "onEntryViewBound", "onEntryViewBound(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)V", 0);
            }

            public final int hashCode() {
                return getFunctionDelegate().hashCode();
            }

            public final void onViewBound(@NotNull NotificationEntry notificationEntry) {
                ConversationNotificationManager.this.onEntryViewBound(notificationEntry);
            }
        });
    }

    public static final Sequence<View> updateNotificationRanking$getLayouts(NotificationContentView notificationContentView) {
        return SequencesKt__SequencesKt.sequenceOf(notificationContentView.getContractedChild(), notificationContentView.getExpandedChild(), notificationContentView.getHeadsUpChild());
    }

    public final void updateNotificationRanking(NotificationListenerService.RankingMap rankingMap) {
        NotificationContentView[] layouts;
        Sequence asSequence;
        Sequence flatMap;
        Sequence mapNotNull;
        Sequence<ConversationLayout> filterNot;
        NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
        for (NotificationEntry notificationEntry : SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(this.states.keySet()), new ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1(this))) {
            if (rankingMap.getRanking(notificationEntry.getSbn().getKey(), ranking) && ranking.isConversation()) {
                boolean isImportantConversation = ranking.getChannel().isImportantConversation();
                ExpandableNotificationRow row = notificationEntry.getRow();
                boolean z = false;
                if (!(row == null || (layouts = row.getLayouts()) == null || (asSequence = ArraysKt___ArraysKt.asSequence(layouts)) == null || (flatMap = SequencesKt___SequencesKt.flatMap(asSequence, ConversationNotificationManager$updateNotificationRanking$1.INSTANCE)) == null || (mapNotNull = SequencesKt___SequencesKt.mapNotNull(flatMap, ConversationNotificationManager$updateNotificationRanking$2.INSTANCE)) == null || (filterNot = SequencesKt___SequencesKt.filterNot(mapNotNull, new ConversationNotificationManager$updateNotificationRanking$3(isImportantConversation))) == null)) {
                    boolean z2 = false;
                    for (ConversationLayout conversationLayout : filterNot) {
                        if (!isImportantConversation || !notificationEntry.isMarkedForUserTriggeredMovement()) {
                            conversationLayout.setIsImportantConversation(isImportantConversation, false);
                        } else {
                            this.mainHandler.postDelayed(new ConversationNotificationManager$updateNotificationRanking$4$1(conversationLayout, isImportantConversation), 960);
                        }
                        z2 = true;
                    }
                    z = z2;
                }
                if (z && !this.featureFlags.isNewPipelineEnabled()) {
                    this.notificationGroupManager.updateIsolation(notificationEntry);
                }
            }
        }
    }

    public final void onEntryViewBound(@NotNull NotificationEntry notificationEntry) {
        if (notificationEntry.getRanking().isConversation()) {
            ExpandableNotificationRow row = notificationEntry.getRow();
            if (row != null) {
                row.setOnExpansionChangedListener(new ConversationNotificationManager$onEntryViewBound$1(notificationEntry, this));
            }
            ExpandableNotificationRow row2 = notificationEntry.getRow();
            boolean z = false;
            if (row2 != null && row2.isExpanded()) {
                z = true;
            }
            onEntryViewBound$updateCount(this, notificationEntry, z);
        }
    }

    public static final void onEntryViewBound$updateCount(ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry, boolean z) {
        if (!z) {
            return;
        }
        if (!conversationNotificationManager.notifPanelCollapsed || notificationEntry.isPinnedAndExpanded()) {
            conversationNotificationManager.resetCount(notificationEntry.getKey());
            ExpandableNotificationRow row = notificationEntry.getRow();
            if (row != null) {
                conversationNotificationManager.resetBadgeUi(row);
            }
        }
    }

    public final boolean shouldIncrementUnread(ConversationState conversationState, Notification.Builder builder) {
        if ((conversationState.getNotification().flags & 8) != 0) {
            return false;
        }
        return Notification.areStyledNotificationsVisiblyDifferent(Notification.Builder.recoverBuilder(this.context, conversationState.getNotification()), builder);
    }

    public final int getUnreadCount(@NotNull NotificationEntry notificationEntry, @NotNull Notification.Builder builder) {
        ConversationState compute = this.states.compute(notificationEntry.getKey(), new ConversationNotificationManager$getUnreadCount$1(notificationEntry, this, builder));
        Intrinsics.checkNotNull(compute);
        return compute.getUnreadCount();
    }

    public final void onNotificationPanelExpandStateChanged(boolean z) {
        this.notifPanelCollapsed = z;
        if (!z) {
            Map<K, V> map = MapsKt__MapsKt.toMap(SequencesKt___SequencesKt.mapNotNull(MapsKt___MapsKt.asSequence(this.states), new ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1(this)));
            this.states.replaceAll(new ConversationNotificationManager$onNotificationPanelExpandStateChanged$1(map));
            for (ExpandableNotificationRow resetBadgeUi : SequencesKt___SequencesKt.mapNotNull(CollectionsKt___CollectionsKt.asSequence(map.values()), ConversationNotificationManager$onNotificationPanelExpandStateChanged$2.INSTANCE)) {
                resetBadgeUi(resetBadgeUi);
            }
        }
    }

    public final void resetCount(String str) {
        this.states.compute(str, ConversationNotificationManager$resetCount$1.INSTANCE);
    }

    public final void removeTrackedEntry(NotificationEntry notificationEntry) {
        this.states.remove(notificationEntry.getKey());
    }

    public final void resetBadgeUi(ExpandableNotificationRow expandableNotificationRow) {
        NotificationContentView[] layouts = expandableNotificationRow.getLayouts();
        Sequence asSequence = layouts == null ? null : ArraysKt___ArraysKt.asSequence(layouts);
        if (asSequence == null) {
            asSequence = SequencesKt__SequencesKt.emptySequence();
        }
        for (ConversationLayout unreadCount : SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(asSequence, ConversationNotificationManager$resetBadgeUi$1.INSTANCE), ConversationNotificationManager$resetBadgeUi$2.INSTANCE)) {
            unreadCount.setUnreadCount(0);
        }
    }

    /* compiled from: ConversationNotifications.kt */
    public static final class ConversationState {
        @NotNull
        public final Notification notification;
        public final int unreadCount;

        public static /* synthetic */ ConversationState copy$default(ConversationState conversationState, int i, Notification notification2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                i = conversationState.unreadCount;
            }
            if ((i2 & 2) != 0) {
                notification2 = conversationState.notification;
            }
            return conversationState.copy(i, notification2);
        }

        @NotNull
        public final ConversationState copy(int i, @NotNull Notification notification2) {
            return new ConversationState(i, notification2);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConversationState)) {
                return false;
            }
            ConversationState conversationState = (ConversationState) obj;
            return this.unreadCount == conversationState.unreadCount && Intrinsics.areEqual((Object) this.notification, (Object) conversationState.notification);
        }

        public int hashCode() {
            return (Integer.hashCode(this.unreadCount) * 31) + this.notification.hashCode();
        }

        @NotNull
        public String toString() {
            return "ConversationState(unreadCount=" + this.unreadCount + ", notification=" + this.notification + ')';
        }

        public ConversationState(int i, @NotNull Notification notification2) {
            this.unreadCount = i;
            this.notification = notification2;
        }

        @NotNull
        public final Notification getNotification() {
            return this.notification;
        }

        public final int getUnreadCount() {
            return this.unreadCount;
        }
    }

    /* compiled from: ConversationNotifications.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}

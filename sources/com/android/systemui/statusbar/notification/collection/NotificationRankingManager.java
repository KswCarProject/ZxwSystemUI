package com.android.systemui.statusbar.notification.collection;

import android.service.notification.NotificationListenerService;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotificationRankingManager.kt */
public class NotificationRankingManager implements LegacyNotificationRanker {
    @NotNull
    public final NotificationGroupManagerLegacy groupManager;
    @NotNull
    public final HeadsUpManager headsUpManager;
    @NotNull
    public final HighPriorityProvider highPriorityProvider;
    @NotNull
    public final NotificationEntryManager.KeyguardEnvironment keyguardEnvironment;
    @NotNull
    public final NotificationEntryManagerLogger logger;
    @NotNull
    public final Lazy mediaManager$delegate = LazyKt__LazyJVMKt.lazy(new NotificationRankingManager$mediaManager$2(this));
    @NotNull
    public final dagger.Lazy<NotificationMediaManager> mediaManagerLazy;
    @NotNull
    public final NotificationFilter notifFilter;
    @NotNull
    public final PeopleNotificationIdentifier peopleNotificationIdentifier;
    @NotNull
    public final Comparator<NotificationEntry> rankingComparator = new NotificationRankingManager$rankingComparator$1(this);
    @Nullable
    public NotificationListenerService.RankingMap rankingMap;
    @NotNull
    public final NotificationSectionsFeatureManager sectionsFeatureManager;

    public NotificationRankingManager(@NotNull dagger.Lazy<NotificationMediaManager> lazy, @NotNull NotificationGroupManagerLegacy notificationGroupManagerLegacy, @NotNull HeadsUpManager headsUpManager2, @NotNull NotificationFilter notificationFilter, @NotNull NotificationEntryManagerLogger notificationEntryManagerLogger, @NotNull NotificationSectionsFeatureManager notificationSectionsFeatureManager, @NotNull PeopleNotificationIdentifier peopleNotificationIdentifier2, @NotNull HighPriorityProvider highPriorityProvider2, @NotNull NotificationEntryManager.KeyguardEnvironment keyguardEnvironment2) {
        this.mediaManagerLazy = lazy;
        this.groupManager = notificationGroupManagerLegacy;
        this.headsUpManager = headsUpManager2;
        this.notifFilter = notificationFilter;
        this.logger = notificationEntryManagerLogger;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.peopleNotificationIdentifier = peopleNotificationIdentifier2;
        this.highPriorityProvider = highPriorityProvider2;
        this.keyguardEnvironment = keyguardEnvironment2;
    }

    @Nullable
    public NotificationListenerService.RankingMap getRankingMap() {
        return this.rankingMap;
    }

    public void setRankingMap(@Nullable NotificationListenerService.RankingMap rankingMap2) {
        this.rankingMap = rankingMap2;
    }

    public final NotificationMediaManager getMediaManager() {
        return (NotificationMediaManager) this.mediaManager$delegate.getValue();
    }

    public final boolean getUsePeopleFiltering() {
        return this.sectionsFeatureManager.isFilteringEnabled();
    }

    @NotNull
    public List<NotificationEntry> updateRanking(@Nullable NotificationListenerService.RankingMap rankingMap2, @NotNull Collection<NotificationEntry> collection, @NotNull String str) {
        List<NotificationEntry> filterAndSortLocked;
        if (rankingMap2 != null) {
            setRankingMap(rankingMap2);
            updateRankingForEntries(collection);
        }
        synchronized (this) {
            filterAndSortLocked = filterAndSortLocked(collection, str);
        }
        return filterAndSortLocked;
    }

    public boolean isNotificationForCurrentProfiles(@NotNull NotificationEntry notificationEntry) {
        return this.keyguardEnvironment.isNotificationForCurrentProfiles(notificationEntry.getSbn());
    }

    public final List<NotificationEntry> filterAndSortLocked(Collection<NotificationEntry> collection, String str) {
        this.logger.logFilterAndSort(str);
        Iterable<NotificationEntry> iterable = collection;
        List<NotificationEntry> list = SequencesKt___SequencesKt.toList(SequencesKt___SequencesKt.sortedWith(SequencesKt___SequencesKt.filterNot(CollectionsKt___CollectionsKt.asSequence(iterable), new NotificationRankingManager$filterAndSortLocked$filtered$1(this)), this.rankingComparator));
        for (NotificationEntry notificationEntry : iterable) {
            notificationEntry.setBucket(getBucketForEntry(notificationEntry));
        }
        return list;
    }

    public final boolean filter(NotificationEntry notificationEntry) {
        boolean shouldFilterOut = this.notifFilter.shouldFilterOut(notificationEntry);
        if (shouldFilterOut) {
            notificationEntry.resetInitializationTime();
        }
        return shouldFilterOut;
    }

    public final int getBucketForEntry(NotificationEntry notificationEntry) {
        boolean access$isImportantCall = NotificationRankingManagerKt.isImportantCall(notificationEntry);
        boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
        boolean isImportantMedia = isImportantMedia(notificationEntry);
        boolean access$isSystemMax = NotificationRankingManagerKt.isSystemMax(notificationEntry);
        if (NotificationRankingManagerKt.isColorizedForegroundService(notificationEntry) || access$isImportantCall) {
            return 3;
        }
        if (!getUsePeopleFiltering() || !isConversation(notificationEntry)) {
            return (isRowHeadsUp || isImportantMedia || access$isSystemMax || isHighPriority(notificationEntry)) ? 5 : 6;
        }
        return 4;
    }

    public final void updateRankingForEntries(Iterable<NotificationEntry> iterable) {
        NotificationListenerService.RankingMap rankingMap2 = getRankingMap();
        if (rankingMap2 != null) {
            synchronized (iterable) {
                for (NotificationEntry next : iterable) {
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    if (rankingMap2.getRanking(next.getKey(), ranking)) {
                        next.setRanking(ranking);
                        String overrideGroupKey = ranking.getOverrideGroupKey();
                        if (!Objects.equals(next.getSbn().getOverrideGroupKey(), overrideGroupKey)) {
                            String groupKey = next.getSbn().getGroupKey();
                            boolean isGroup = next.getSbn().isGroup();
                            boolean isGroupSummary = next.getSbn().getNotification().isGroupSummary();
                            next.getSbn().setOverrideGroupKey(overrideGroupKey);
                            this.groupManager.onEntryUpdated(next, groupKey, isGroup, isGroupSummary);
                        }
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
        }
    }

    public final boolean isImportantMedia(NotificationEntry notificationEntry) {
        return Intrinsics.areEqual((Object) notificationEntry.getKey(), (Object) getMediaManager().getMediaNotificationKey()) && notificationEntry.getImportance() > 1;
    }

    public final boolean isConversation(NotificationEntry notificationEntry) {
        return getPeopleNotificationType(notificationEntry) != 0;
    }

    public final int getPeopleNotificationType(NotificationEntry notificationEntry) {
        return this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry);
    }

    public final boolean isHighPriority(NotificationEntry notificationEntry) {
        return this.highPriorityProvider.isHighPriority(notificationEntry);
    }
}

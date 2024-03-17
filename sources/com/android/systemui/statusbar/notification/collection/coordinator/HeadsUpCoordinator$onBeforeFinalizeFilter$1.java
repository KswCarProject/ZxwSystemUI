package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: HeadsUpCoordinator.kt */
public final class HeadsUpCoordinator$onBeforeFinalizeFilter$1 extends Lambda implements Function1<HunMutator, Unit> {
    public final /* synthetic */ List<ListEntry> $list;
    public final /* synthetic */ HeadsUpCoordinator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public HeadsUpCoordinator$onBeforeFinalizeFilter$1(HeadsUpCoordinator headsUpCoordinator, List<? extends ListEntry> list) {
        super(1);
        this.this$0 = headsUpCoordinator;
        this.$list = list;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((HunMutator) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull HunMutator hunMutator) {
        Object obj;
        boolean z;
        HunMutator hunMutator2 = hunMutator;
        if (!this.this$0.mPostedEntries.isEmpty()) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            for (Object next : this.this$0.mPostedEntries.values()) {
                String groupKey = ((HeadsUpCoordinator.PostedEntry) next).getEntry().getSbn().getGroupKey();
                Object obj2 = linkedHashMap.get(groupKey);
                if (obj2 == null) {
                    obj2 = new ArrayList();
                    linkedHashMap.put(groupKey, obj2);
                }
                ((List) obj2).add(next);
            }
            NotifPipeline access$getMNotifPipeline$p = this.this$0.mNotifPipeline;
            if (access$getMNotifPipeline$p == null) {
                access$getMNotifPipeline$p = null;
            }
            Sequence filter = SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(access$getMNotifPipeline$p.getAllNotifs()), new HeadsUpCoordinator$onBeforeFinalizeFilter$1$logicalMembersByGroup$1(linkedHashMap));
            LinkedHashMap linkedHashMap2 = new LinkedHashMap();
            for (Object next2 : filter) {
                String groupKey2 = ((NotificationEntry) next2).getSbn().getGroupKey();
                Object obj3 = linkedHashMap2.get(groupKey2);
                if (obj3 == null) {
                    obj3 = new ArrayList();
                    linkedHashMap2.put(groupKey2, obj3);
                }
                ((List) obj3).add(next2);
            }
            Lazy lazy = LazyKt__LazyJVMKt.lazy(new HeadsUpCoordinator$onBeforeFinalizeFilter$1$groupLocationsByKey$2(this.this$0, this.$list));
            this.this$0.mLogger.logEvaluatingGroups(linkedHashMap.size());
            HeadsUpCoordinator headsUpCoordinator = this.this$0;
            for (Map.Entry entry : linkedHashMap.entrySet()) {
                String str = (String) entry.getKey();
                List<HeadsUpCoordinator.PostedEntry> list = (List) entry.getValue();
                List list2 = (List) linkedHashMap2.get(str);
                if (list2 == null) {
                    list2 = CollectionsKt__CollectionsKt.emptyList();
                }
                Iterator it = list2.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        obj = null;
                        break;
                    }
                    obj = it.next();
                    if (((NotificationEntry) obj).getSbn().getNotification().isGroupSummary()) {
                        break;
                    }
                }
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                headsUpCoordinator.mLogger.logEvaluatingGroup(str, list.size(), list2.size());
                if (notificationEntry == null) {
                    for (HeadsUpCoordinator.PostedEntry access$handlePostedEntry : list) {
                        headsUpCoordinator.handlePostedEntry(access$handlePostedEntry, hunMutator2, "logical-summary-missing");
                    }
                } else if (!headsUpCoordinator.isGoingToShowHunStrict(notificationEntry)) {
                    for (HeadsUpCoordinator.PostedEntry access$handlePostedEntry2 : list) {
                        headsUpCoordinator.handlePostedEntry(access$handlePostedEntry2, hunMutator2, "logical-summary-not-alerting");
                    }
                } else {
                    NotificationEntry access$findAlertOverride = headsUpCoordinator.findAlertOverride(list, new HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$3(m2349invoke$lambda2(lazy)));
                    String str2 = access$findAlertOverride != null ? "alertOverride" : "undefined";
                    boolean containsKey = m2349invoke$lambda2(lazy).containsKey(notificationEntry.getKey());
                    if (!containsKey && access$findAlertOverride == null && (access$findAlertOverride = headsUpCoordinator.findBestTransferChild(list2, new HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$4(m2349invoke$lambda2(lazy)))) != null) {
                        str2 = "bestChild";
                    }
                    if (access$findAlertOverride == null) {
                        for (HeadsUpCoordinator.PostedEntry access$handlePostedEntry3 : list) {
                            headsUpCoordinator.handlePostedEntry(access$handlePostedEntry3, hunMutator2, "no-transfer-target");
                        }
                    } else {
                        HeadsUpCoordinator.PostedEntry postedEntry = (HeadsUpCoordinator.PostedEntry) headsUpCoordinator.mPostedEntries.get(notificationEntry.getKey());
                        if (!containsKey) {
                            if (postedEntry == null) {
                                postedEntry = null;
                            } else {
                                postedEntry.setShouldHeadsUpEver(false);
                            }
                            if (postedEntry == null) {
                                z = false;
                                postedEntry = new HeadsUpCoordinator.PostedEntry(notificationEntry, false, false, false, false, headsUpCoordinator.mHeadsUpManager.isAlerting(notificationEntry.getKey()), headsUpCoordinator.isEntryBinding(notificationEntry));
                            } else {
                                z = false;
                            }
                            headsUpCoordinator.handlePostedEntry(postedEntry, hunMutator2, "detached-summary-remove-alert");
                        } else {
                            z = false;
                            if (postedEntry != null) {
                                headsUpCoordinator.mLogger.logPostedEntryWillNotEvaluate(postedEntry, "attached-summary-transferred");
                            }
                        }
                        boolean z2 = z;
                        for (HeadsUpCoordinator.PostedEntry postedEntry2 : SequencesKt___SequencesKt.filter(CollectionsKt___CollectionsKt.asSequence(list), new HeadsUpCoordinator$onBeforeFinalizeFilter$1$1$6(notificationEntry))) {
                            if (Intrinsics.areEqual((Object) access$findAlertOverride.getKey(), (Object) postedEntry2.getKey())) {
                                postedEntry2.setShouldHeadsUpEver(true);
                                postedEntry2.setShouldHeadsUpAgain(true);
                                headsUpCoordinator.handlePostedEntry(postedEntry2, hunMutator2, Intrinsics.stringPlus("child-alert-transfer-target-", str2));
                                z2 = true;
                            } else {
                                headsUpCoordinator.handlePostedEntry(postedEntry2, hunMutator2, "child-alert-non-target");
                            }
                        }
                        if (!z2) {
                            headsUpCoordinator.handlePostedEntry(new HeadsUpCoordinator.PostedEntry(access$findAlertOverride, false, false, true, true, headsUpCoordinator.mHeadsUpManager.isAlerting(access$findAlertOverride.getKey()), headsUpCoordinator.isEntryBinding(access$findAlertOverride)), hunMutator2, Intrinsics.stringPlus("non-posted-child-alert-transfer-target-", str2));
                        }
                    }
                }
            }
            this.this$0.mPostedEntries.clear();
        }
    }

    /* renamed from: invoke$lambda-2  reason: not valid java name */
    public static final Map<String, GroupLocation> m2349invoke$lambda2(Lazy<? extends Map<String, ? extends GroupLocation>> lazy) {
        return (Map) lazy.getValue();
    }
}

package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.util.Preconditions;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationInteractionTracker;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeSortListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeTransformGroupsListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.PipelineState;
import com.android.systemui.statusbar.notification.collection.listbuilder.ShadeListBuilderLogger;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.DefaultNotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifComparator;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;
import com.android.systemui.statusbar.notification.collection.notifcollection.CollectionReadyForBuildListener;
import com.android.systemui.util.Assert;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ShadeListBuilder implements Dumpable {
    public static final NotifSectioner DEFAULT_SECTIONER = new NotifSectioner("UnknownSection", 0) {
        public boolean isInSection(ListEntry listEntry) {
            return true;
        }
    };
    public Collection<NotificationEntry> mAllEntries = Collections.emptyList();
    public final boolean mAlwaysLogList;
    public final NotifPipelineChoreographer mChoreographer;
    public final DumpManager mDumpManager;
    public boolean mForceReorderable = false;
    public final Comparator<ListEntry> mGroupChildrenComparator = new ShadeListBuilder$$ExternalSyntheticLambda1(this);
    public final Map<String, GroupEntry> mGroups = new ArrayMap();
    public final NotificationInteractionTracker mInteractionTracker;
    public int mIterationCount = 0;
    public final ShadeListBuilderLogger mLogger;
    public List<ListEntry> mNewNotifList = new ArrayList();
    public final List<NotifComparator> mNotifComparators = new ArrayList();
    public final List<NotifFilter> mNotifFinalizeFilters = new ArrayList();
    public List<ListEntry> mNotifList = new ArrayList();
    public final List<NotifFilter> mNotifPreGroupFilters = new ArrayList();
    public final List<NotifPromoter> mNotifPromoters = new ArrayList();
    public final List<NotifSection> mNotifSections = new ArrayList();
    public NotifStabilityManager mNotifStabilityManager;
    public final List<OnBeforeFinalizeFilterListener> mOnBeforeFinalizeFilterListeners = new ArrayList();
    public final List<OnBeforeRenderListListener> mOnBeforeRenderListListeners = new ArrayList();
    public final List<OnBeforeSortListener> mOnBeforeSortListeners = new ArrayList();
    public final List<OnBeforeTransformGroupsListener> mOnBeforeTransformGroupsListeners = new ArrayList();
    public OnRenderListListener mOnRenderListListener;
    public final PipelineState mPipelineState = new PipelineState();
    public List<ListEntry> mReadOnlyNewNotifList = Collections.unmodifiableList(this.mNewNotifList);
    public List<ListEntry> mReadOnlyNotifList = Collections.unmodifiableList(this.mNotifList);
    public final CollectionReadyForBuildListener mReadyForBuildListener = new CollectionReadyForBuildListener() {
        public void onBuildList(Collection<NotificationEntry> collection) {
            Assert.isMainThread();
            ShadeListBuilder.this.mPipelineState.requireIsBefore(1);
            ShadeListBuilder.this.mLogger.logOnBuildList();
            ShadeListBuilder.this.mAllEntries = collection;
            ShadeListBuilder.this.mChoreographer.schedule();
        }
    };
    public final SystemClock mSystemClock;
    public final ArrayList<ListEntry> mTempSectionMembers = new ArrayList<>();
    public final Comparator<ListEntry> mTopLevelComparator = new ShadeListBuilder$$ExternalSyntheticLambda0(this);

    public interface OnRenderListListener {
        void onRenderList(List<ListEntry> list);
    }

    public ShadeListBuilder(DumpManager dumpManager, NotifPipelineChoreographer notifPipelineChoreographer, NotifPipelineFlags notifPipelineFlags, NotificationInteractionTracker notificationInteractionTracker, ShadeListBuilderLogger shadeListBuilderLogger, SystemClock systemClock) {
        this.mSystemClock = systemClock;
        this.mLogger = shadeListBuilderLogger;
        this.mAlwaysLogList = notifPipelineFlags.isDevLoggingEnabled();
        this.mInteractionTracker = notificationInteractionTracker;
        this.mChoreographer = notifPipelineChoreographer;
        this.mDumpManager = dumpManager;
        setSectioners(Collections.emptyList());
    }

    public void attach(NotifCollection notifCollection) {
        Assert.isMainThread();
        this.mDumpManager.registerDumpable("ShadeListBuilder", this);
        notifCollection.addCollectionListener(this.mInteractionTracker);
        notifCollection.setBuildListener(this.mReadyForBuildListener);
        this.mChoreographer.addOnEvalListener(new ShadeListBuilder$$ExternalSyntheticLambda6(this));
    }

    public void setOnRenderListListener(OnRenderListListener onRenderListListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnRenderListListener = onRenderListListener;
    }

    public void addOnBeforeTransformGroupsListener(OnBeforeTransformGroupsListener onBeforeTransformGroupsListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnBeforeTransformGroupsListeners.add(onBeforeTransformGroupsListener);
    }

    public void addOnBeforeFinalizeFilterListener(OnBeforeFinalizeFilterListener onBeforeFinalizeFilterListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnBeforeFinalizeFilterListeners.add(onBeforeFinalizeFilterListener);
    }

    public void addOnBeforeRenderListListener(OnBeforeRenderListListener onBeforeRenderListListener) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mOnBeforeRenderListListeners.add(onBeforeRenderListListener);
    }

    public void addPreRenderInvalidator(Invalidator invalidator) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        invalidator.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda5(this));
    }

    public void addPreGroupFilter(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifPreGroupFilters.add(notifFilter);
        notifFilter.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda8(this));
    }

    public void addFinalizeFilter(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifFinalizeFilters.add(notifFilter);
        notifFilter.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda9(this));
    }

    public void addPromoter(NotifPromoter notifPromoter) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        this.mNotifPromoters.add(notifPromoter);
        notifPromoter.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda7(this));
    }

    public void setSectioners(List<NotifSectioner> list) {
        Assert.isMainThread();
        int i = 0;
        this.mPipelineState.requireState(0);
        this.mNotifSections.clear();
        for (NotifSectioner next : list) {
            NotifSection notifSection = new NotifSection(next, this.mNotifSections.size());
            NotifComparator comparator = notifSection.getComparator();
            this.mNotifSections.add(notifSection);
            next.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda2(this));
            if (comparator != null) {
                comparator.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda3(this));
            }
        }
        List<NotifSection> list2 = this.mNotifSections;
        list2.add(new NotifSection(DEFAULT_SECTIONER, list2.size()));
        ArraySet arraySet = new ArraySet();
        if (this.mNotifSections.size() > 0) {
            i = this.mNotifSections.get(0).getBucket();
        }
        for (NotifSection next2 : this.mNotifSections) {
            if (i == next2.getBucket() || !arraySet.contains(Integer.valueOf(next2.getBucket()))) {
                i = next2.getBucket();
                arraySet.add(Integer.valueOf(i));
            } else {
                throw new IllegalStateException("setSectioners with non contiguous sections " + next2.getLabel() + " has an already seen bucket");
            }
        }
    }

    public void setNotifStabilityManager(NotifStabilityManager notifStabilityManager) {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        if (this.mNotifStabilityManager == null) {
            this.mNotifStabilityManager = notifStabilityManager;
            notifStabilityManager.setInvalidationListener(new ShadeListBuilder$$ExternalSyntheticLambda4(this));
            return;
        }
        throw new IllegalStateException("Attempting to set the NotifStabilityManager more than once. There should only be one visual stability manager. Manager is being set by " + this.mNotifStabilityManager.getName() + " and " + notifStabilityManager.getName());
    }

    public final NotifStabilityManager getStabilityManager() {
        NotifStabilityManager notifStabilityManager = this.mNotifStabilityManager;
        return notifStabilityManager == null ? DefaultNotifStabilityManager.INSTANCE : notifStabilityManager;
    }

    public List<ListEntry> getShadeList() {
        Assert.isMainThread();
        this.mPipelineState.requireState(0);
        return this.mReadOnlyNotifList;
    }

    public final void onPreRenderInvalidated(Invalidator invalidator) {
        Assert.isMainThread();
        this.mLogger.logPreRenderInvalidated(invalidator.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(9);
    }

    public final void onPreGroupFilterInvalidated(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mLogger.logPreGroupFilterInvalidated(notifFilter.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(3);
    }

    public final void onReorderingAllowedInvalidated(NotifStabilityManager notifStabilityManager) {
        Assert.isMainThread();
        this.mLogger.logReorderingAllowedInvalidated(notifStabilityManager.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(4);
    }

    public final void onPromoterInvalidated(NotifPromoter notifPromoter) {
        Assert.isMainThread();
        this.mLogger.logPromoterInvalidated(notifPromoter.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(5);
    }

    public final void onNotifSectionInvalidated(NotifSectioner notifSectioner) {
        Assert.isMainThread();
        this.mLogger.logNotifSectionInvalidated(notifSectioner.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(7);
    }

    public final void onFinalizeFilterInvalidated(NotifFilter notifFilter) {
        Assert.isMainThread();
        this.mLogger.logFinalizeFilterInvalidated(notifFilter.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(8);
    }

    public final void onNotifComparatorInvalidated(NotifComparator notifComparator) {
        Assert.isMainThread();
        this.mLogger.logNotifComparatorInvalidated(notifComparator.getName(), this.mPipelineState.getState());
        rebuildListIfBefore(7);
    }

    public final void buildList() {
        Trace.beginSection("ShadeListBuilder.buildList");
        this.mPipelineState.requireIsBefore(1);
        if (!this.mNotifStabilityManager.isPipelineRunAllowed()) {
            this.mLogger.logPipelineRunSuppressed();
            Trace.endSection();
            return;
        }
        this.mPipelineState.setState(1);
        this.mPipelineState.incrementTo(2);
        resetNotifs();
        onBeginRun();
        this.mPipelineState.incrementTo(3);
        filterNotifs(this.mAllEntries, this.mNotifList, this.mNotifPreGroupFilters);
        this.mPipelineState.incrementTo(4);
        groupNotifs(this.mNotifList, this.mNewNotifList);
        applyNewNotifList();
        pruneIncompleteGroups(this.mNotifList);
        dispatchOnBeforeTransformGroups(this.mReadOnlyNotifList);
        this.mPipelineState.incrementTo(5);
        promoteNotifs(this.mNotifList);
        pruneIncompleteGroups(this.mNotifList);
        this.mPipelineState.incrementTo(6);
        stabilizeGroupingNotifs(this.mNotifList);
        dispatchOnBeforeSort(this.mReadOnlyNotifList);
        this.mPipelineState.incrementTo(7);
        assignSections();
        notifySectionEntriesUpdated();
        sortListAndGroups();
        dispatchOnBeforeFinalizeFilter(this.mReadOnlyNotifList);
        this.mPipelineState.incrementTo(8);
        filterNotifs(this.mNotifList, this.mNewNotifList, this.mNotifFinalizeFilters);
        applyNewNotifList();
        pruneIncompleteGroups(this.mNotifList);
        this.mPipelineState.incrementTo(9);
        logChanges();
        freeEmptyGroups();
        cleanupPluggables();
        dispatchOnBeforeRenderList(this.mReadOnlyNotifList);
        Trace.beginSection("ShadeListBuilder.onRenderList");
        OnRenderListListener onRenderListListener = this.mOnRenderListListener;
        if (onRenderListListener != null) {
            onRenderListListener.onRenderList(this.mReadOnlyNotifList);
        }
        Trace.endSection();
        Trace.beginSection("ShadeListBuilder.logEndBuildList");
        this.mLogger.logEndBuildList(this.mIterationCount, this.mReadOnlyNotifList.size(), countChildren(this.mReadOnlyNotifList));
        if (this.mAlwaysLogList || this.mIterationCount % 10 == 0) {
            Trace.beginSection("ShadeListBuilder.logFinalList");
            this.mLogger.logFinalList(this.mNotifList);
            Trace.endSection();
        }
        Trace.endSection();
        this.mPipelineState.setState(0);
        this.mIterationCount++;
        Trace.endSection();
    }

    public final void notifySectionEntriesUpdated() {
        Trace.beginSection("ShadeListBuilder.notifySectionEntriesUpdated");
        this.mTempSectionMembers.clear();
        for (NotifSection next : this.mNotifSections) {
            for (ListEntry next2 : this.mNotifList) {
                if (next == next2.getSection()) {
                    this.mTempSectionMembers.add(next2);
                }
            }
            next.getSectioner().onEntriesUpdated(this.mTempSectionMembers);
            this.mTempSectionMembers.clear();
        }
        Trace.endSection();
    }

    public final void applyNewNotifList() {
        this.mNotifList.clear();
        List<ListEntry> list = this.mNotifList;
        this.mNotifList = this.mNewNotifList;
        this.mNewNotifList = list;
        List<ListEntry> list2 = this.mReadOnlyNotifList;
        this.mReadOnlyNotifList = this.mReadOnlyNewNotifList;
        this.mReadOnlyNewNotifList = list2;
    }

    public final void resetNotifs() {
        for (GroupEntry next : this.mGroups.values()) {
            next.beginNewAttachState();
            next.clearChildren();
            next.setSummary((NotificationEntry) null);
        }
        for (NotificationEntry beginNewAttachState : this.mAllEntries) {
            beginNewAttachState.beginNewAttachState();
        }
        this.mNotifList.clear();
    }

    public final void filterNotifs(Collection<? extends ListEntry> collection, List<ListEntry> list, List<NotifFilter> list2) {
        Trace.beginSection("ShadeListBuilder.filterNotifs");
        long uptimeMillis = this.mSystemClock.uptimeMillis();
        for (ListEntry listEntry : collection) {
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry representativeEntry = groupEntry.getRepresentativeEntry();
                if (applyFilters(representativeEntry, uptimeMillis, list2)) {
                    groupEntry.setSummary((NotificationEntry) null);
                    annulAddition(representativeEntry);
                }
                List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
                for (int size = rawChildren.size() - 1; size >= 0; size--) {
                    NotificationEntry notificationEntry = rawChildren.get(size);
                    if (applyFilters(notificationEntry, uptimeMillis, list2)) {
                        rawChildren.remove(notificationEntry);
                        annulAddition(notificationEntry);
                    }
                }
                list.add(groupEntry);
            } else if (applyFilters((NotificationEntry) listEntry, uptimeMillis, list2)) {
                annulAddition(listEntry);
            } else {
                list.add(listEntry);
            }
        }
        Trace.endSection();
    }

    public final void groupNotifs(List<ListEntry> list, List<ListEntry> list2) {
        Trace.beginSection("ShadeListBuilder.groupNotifs");
        Iterator<ListEntry> it = list.iterator();
        while (it.hasNext()) {
            NotificationEntry notificationEntry = (NotificationEntry) it.next();
            if (notificationEntry.getSbn().isGroup()) {
                String groupKey = notificationEntry.getSbn().getGroupKey();
                GroupEntry groupEntry = this.mGroups.get(groupKey);
                if (groupEntry == null) {
                    groupEntry = new GroupEntry(groupKey, this.mSystemClock.uptimeMillis());
                    this.mGroups.put(groupKey, groupEntry);
                }
                if (groupEntry.getParent() == null) {
                    groupEntry.setParent(GroupEntry.ROOT_ENTRY);
                    list2.add(groupEntry);
                }
                notificationEntry.setParent(groupEntry);
                if (notificationEntry.getSbn().getNotification().isGroupSummary()) {
                    NotificationEntry summary = groupEntry.getSummary();
                    if (summary == null) {
                        groupEntry.setSummary(notificationEntry);
                    } else {
                        this.mLogger.logDuplicateSummary(this.mIterationCount, groupEntry.getKey(), summary.getKey(), notificationEntry.getKey());
                        if (notificationEntry.getSbn().getPostTime() > summary.getSbn().getPostTime()) {
                            groupEntry.setSummary(notificationEntry);
                            annulAddition(summary, list2);
                        } else {
                            annulAddition(notificationEntry, list2);
                        }
                    }
                } else {
                    groupEntry.addChild(notificationEntry);
                }
            } else {
                String key = notificationEntry.getKey();
                if (this.mGroups.containsKey(key)) {
                    this.mLogger.logDuplicateTopLevelKey(this.mIterationCount, key);
                } else {
                    notificationEntry.setParent(GroupEntry.ROOT_ENTRY);
                    list2.add(notificationEntry);
                }
            }
        }
        Trace.endSection();
    }

    public final void stabilizeGroupingNotifs(List<ListEntry> list) {
        if (!getStabilityManager().isEveryChangeAllowed()) {
            Trace.beginSection("ShadeListBuilder.stabilizeGroupingNotifs");
            int i = 0;
            while (i < list.size()) {
                ListEntry listEntry = list.get(i);
                if (listEntry instanceof GroupEntry) {
                    GroupEntry groupEntry = (GroupEntry) listEntry;
                    List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
                    int i2 = 0;
                    while (i2 < groupEntry.getChildren().size()) {
                        if (maybeSuppressGroupChange(rawChildren.get(i2), list)) {
                            rawChildren.remove(i2);
                            i2--;
                        }
                        i2++;
                    }
                } else if (maybeSuppressGroupChange(listEntry.getRepresentativeEntry(), list)) {
                    list.remove(i);
                    i--;
                }
                i++;
            }
            Trace.endSection();
        }
    }

    public final boolean maybeSuppressGroupChange(NotificationEntry notificationEntry, List<ListEntry> list) {
        GroupEntry parent;
        GroupEntry parent2 = notificationEntry.getPreviousAttachState().getParent();
        if (parent2 == null || parent2 == (parent = notificationEntry.getParent())) {
            return false;
        }
        GroupEntry groupEntry = GroupEntry.ROOT_ENTRY;
        if ((parent2 != groupEntry && parent2.getParent() == null) || getStabilityManager().isGroupChangeAllowed(notificationEntry.getRepresentativeEntry())) {
            return false;
        }
        notificationEntry.getAttachState().getSuppressedChanges().setParent(parent);
        notificationEntry.setParent(parent2);
        if (parent2 == groupEntry) {
            list.add(notificationEntry);
            return true;
        }
        parent2.addChild(notificationEntry);
        if (this.mGroups.containsKey(parent2.getKey())) {
            return true;
        }
        this.mGroups.put(parent2.getKey(), parent2);
        return true;
    }

    public final void promoteNotifs(List<ListEntry> list) {
        Trace.beginSection("ShadeListBuilder.promoteNotifs");
        for (int i = 0; i < list.size(); i++) {
            ListEntry listEntry = list.get(i);
            if (listEntry instanceof GroupEntry) {
                ((GroupEntry) listEntry).getRawChildren().removeIf(new ShadeListBuilder$$ExternalSyntheticLambda11(this, list));
            }
        }
        Trace.endSection();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$promoteNotifs$0(List list, NotificationEntry notificationEntry) {
        boolean applyTopLevelPromoters = applyTopLevelPromoters(notificationEntry);
        if (applyTopLevelPromoters) {
            notificationEntry.setParent(GroupEntry.ROOT_ENTRY);
            list.add(notificationEntry);
        }
        return applyTopLevelPromoters;
    }

    public final void pruneIncompleteGroups(List<ListEntry> list) {
        Trace.beginSection("ShadeListBuilder.pruneIncompleteGroups");
        Set<String> groupsWithChildrenLostToStability = getGroupsWithChildrenLostToStability(list);
        ArraySet arraySet = new ArraySet(groupsWithChildrenLostToStability);
        addGroupsWithChildrenLostToFiltering(arraySet);
        addGroupsWithChildrenLostToPromotion(list, arraySet);
        for (int size = list.size() - 1; size >= 0; size--) {
            ListEntry listEntry = list.get(size);
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
                boolean z = groupEntry.getSummary() != null;
                if (!z || rawChildren.size() != 0) {
                    if (!z) {
                        pruneGroupAtIndexAndPromoteAnyChildren(list, groupEntry, size);
                    } else if (rawChildren.size() < 2) {
                        Preconditions.checkState(z, "group must have summary at this point");
                        Preconditions.checkState(!rawChildren.isEmpty(), "empty group should have been promoted");
                        if (groupsWithChildrenLostToStability.contains(groupEntry.getKey())) {
                            groupEntry.getAttachState().getSuppressedChanges().setWasPruneSuppressed(true);
                        } else if (!groupEntry.wasAttachedInPreviousPass() || getStabilityManager().isGroupPruneAllowed(groupEntry)) {
                            pruneGroupAtIndexAndPromoteAnyChildren(list, groupEntry, size);
                        } else {
                            Preconditions.checkState(!rawChildren.isEmpty(), "empty group should have been pruned");
                            groupEntry.getAttachState().getSuppressedChanges().setWasPruneSuppressed(true);
                        }
                    }
                } else if (arraySet.contains(groupEntry.getKey())) {
                    pruneGroupAtIndexAndPromoteAnyChildren(list, groupEntry, size);
                } else {
                    pruneGroupAtIndexAndPromoteSummary(list, groupEntry, size);
                }
            }
        }
        Trace.endSection();
    }

    public final void pruneGroupAtIndexAndPromoteSummary(List<ListEntry> list, GroupEntry groupEntry, int i) {
        Preconditions.checkArgument(groupEntry.getChildren().isEmpty(), "group should have no children");
        NotificationEntry summary = groupEntry.getSummary();
        summary.setParent(GroupEntry.ROOT_ENTRY);
        Preconditions.checkState(list.set(i, summary) == groupEntry);
        groupEntry.setSummary((NotificationEntry) null);
        annulAddition(groupEntry, list);
        ListAttachState attachState = summary.getAttachState();
        attachState.setGroupPruneReason("SUMMARY with no children @ " + this.mPipelineState.getStateName());
    }

    public final void pruneGroupAtIndexAndPromoteAnyChildren(List<ListEntry> list, GroupEntry groupEntry, int i) {
        String str;
        Preconditions.checkState(list.remove(i) == groupEntry);
        List<NotificationEntry> rawChildren = groupEntry.getRawChildren();
        boolean z = groupEntry.getSummary() != null;
        if (z) {
            NotificationEntry summary = groupEntry.getSummary();
            groupEntry.setSummary((NotificationEntry) null);
            annulAddition(summary, list);
            summary.getAttachState().setGroupPruneReason("SUMMARY with too few children @ " + this.mPipelineState.getStateName());
        }
        if (!rawChildren.isEmpty()) {
            if (z) {
                str = "CHILD with " + (rawChildren.size() - 1) + " siblings @ " + this.mPipelineState.getStateName();
            } else {
                str = "CHILD with no summary @ " + this.mPipelineState.getStateName();
            }
            for (int i2 = 0; i2 < rawChildren.size(); i2++) {
                NotificationEntry notificationEntry = rawChildren.get(i2);
                notificationEntry.setParent(GroupEntry.ROOT_ENTRY);
                ListAttachState attachState = notificationEntry.getAttachState();
                Objects.requireNonNull(str);
                attachState.setGroupPruneReason(str);
            }
            list.addAll(i, rawChildren);
            rawChildren.clear();
        }
        annulAddition(groupEntry, list);
    }

    public final Set<String> getGroupsWithChildrenLostToStability(List<ListEntry> list) {
        if (getStabilityManager().isEveryChangeAllowed()) {
            return Collections.emptySet();
        }
        ArraySet arraySet = new ArraySet();
        for (int i = 0; i < list.size(); i++) {
            GroupEntry parent = list.get(i).getAttachState().getSuppressedChanges().getParent();
            if (parent != null) {
                arraySet.add(parent.getKey());
            }
        }
        return arraySet;
    }

    public final void addGroupsWithChildrenLostToPromotion(List<ListEntry> list, Set<String> set) {
        for (int i = 0; i < list.size(); i++) {
            ListEntry listEntry = list.get(i);
            if (listEntry.getAttachState().getPromoter() != null) {
                set.add(listEntry.getRepresentativeEntry().getSbn().getGroupKey());
            }
        }
    }

    public final void addGroupsWithChildrenLostToFiltering(Set<String> set) {
        for (ListEntry next : this.mAllEntries) {
            StatusBarNotification sbn = next.getRepresentativeEntry().getSbn();
            if (sbn.isGroup() && !sbn.getNotification().isGroupSummary() && next.getAttachState().getExcludingFilter() != null) {
                set.add(sbn.getGroupKey());
            }
        }
    }

    public final void annulAddition(ListEntry listEntry, List<ListEntry> list) {
        if (listEntry.getParent() == null) {
            throw new IllegalStateException("Cannot nullify addition of " + listEntry.getKey() + ": no parent.");
        } else if (listEntry.getParent() != GroupEntry.ROOT_ENTRY || !list.contains(listEntry)) {
            if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                if (groupEntry.getSummary() != null) {
                    throw new IllegalStateException("Cannot nullify group " + groupEntry.getKey() + ": summary is not null");
                } else if (!groupEntry.getChildren().isEmpty()) {
                    throw new IllegalStateException("Cannot nullify group " + groupEntry.getKey() + ": still has children");
                }
            } else if ((listEntry instanceof NotificationEntry) && (listEntry == listEntry.getParent().getSummary() || listEntry.getParent().getChildren().contains(listEntry))) {
                throw new IllegalStateException("Cannot nullify addition of child " + listEntry.getKey() + ": it's still attached to its parent.");
            }
            annulAddition(listEntry);
        } else {
            throw new IllegalStateException("Cannot nullify addition of " + listEntry.getKey() + ": it's still in the shade list.");
        }
    }

    public final void annulAddition(ListEntry listEntry) {
        listEntry.setParent((GroupEntry) null);
        listEntry.getAttachState().setSection((NotifSection) null);
        listEntry.getAttachState().setPromoter((NotifPromoter) null);
    }

    public final void assignSections() {
        Trace.beginSection("ShadeListBuilder.assignSections");
        for (ListEntry next : this.mNotifList) {
            NotifSection applySections = applySections(next);
            if (next instanceof GroupEntry) {
                for (NotificationEntry entrySection : ((GroupEntry) next).getChildren()) {
                    setEntrySection(entrySection, applySections);
                }
            }
        }
        Trace.endSection();
    }

    public final void sortListAndGroups() {
        Trace.beginSection("ShadeListBuilder.sortListAndGroups");
        for (ListEntry next : this.mNotifList) {
            if (next instanceof GroupEntry) {
                ((GroupEntry) next).sortChildren(this.mGroupChildrenComparator);
            }
        }
        this.mNotifList.sort(this.mTopLevelComparator);
        assignIndexes(this.mNotifList);
        if (!getStabilityManager().isEveryChangeAllowed()) {
            this.mForceReorderable = true;
            boolean isSorted = isSorted(this.mNotifList, this.mTopLevelComparator);
            this.mForceReorderable = false;
            if (!isSorted) {
                getStabilityManager().onEntryReorderSuppressed();
            }
        }
        Trace.endSection();
    }

    public static <T> boolean isSorted(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) {
            return true;
        }
        Iterator<T> it = list.iterator();
        T next = it.next();
        while (it.hasNext()) {
            T next2 = it.next();
            if (comparator.compare(next, next2) > 0) {
                return false;
            }
            next = next2;
        }
        return true;
    }

    public static void assignIndexes(List<ListEntry> list) {
        if (list.size() != 0) {
            NotifSection section = list.get(0).getSection();
            Objects.requireNonNull(section);
            int i = 0;
            for (int i2 = 0; i2 < list.size(); i2++) {
                ListEntry listEntry = list.get(i2);
                NotifSection section2 = listEntry.getSection();
                Objects.requireNonNull(section2);
                if (section2.getIndex() != section.getIndex()) {
                    i = 0;
                    section = section2;
                }
                listEntry.getAttachState().setStableIndex(i);
                if (listEntry instanceof GroupEntry) {
                    GroupEntry groupEntry = (GroupEntry) listEntry;
                    for (int i3 = 0; i3 < groupEntry.getChildren().size(); i3++) {
                        groupEntry.getChildren().get(i3).getAttachState().setStableIndex(i);
                        i++;
                    }
                }
                i++;
            }
        }
    }

    public final void freeEmptyGroups() {
        Trace.beginSection("ShadeListBuilder.freeEmptyGroups");
        this.mGroups.values().removeIf(new ShadeListBuilder$$ExternalSyntheticLambda10());
        Trace.endSection();
    }

    public static /* synthetic */ boolean lambda$freeEmptyGroups$1(GroupEntry groupEntry) {
        return groupEntry.getSummary() == null && groupEntry.getChildren().isEmpty();
    }

    public final void logChanges() {
        Trace.beginSection("ShadeListBuilder.logChanges");
        for (NotificationEntry logAttachStateChanges : this.mAllEntries) {
            logAttachStateChanges(logAttachStateChanges);
        }
        for (GroupEntry logAttachStateChanges2 : this.mGroups.values()) {
            logAttachStateChanges(logAttachStateChanges2);
        }
        Trace.endSection();
    }

    public final void logAttachStateChanges(ListEntry listEntry) {
        ListAttachState attachState = listEntry.getAttachState();
        ListAttachState previousAttachState = listEntry.getPreviousAttachState();
        if (!Objects.equals(attachState, previousAttachState)) {
            this.mLogger.logEntryAttachStateChanged(this.mIterationCount, listEntry.getKey(), previousAttachState.getParent(), attachState.getParent());
            if (attachState.getParent() != previousAttachState.getParent()) {
                this.mLogger.logParentChanged(this.mIterationCount, previousAttachState.getParent(), attachState.getParent());
            }
            if (attachState.getSuppressedChanges().getParent() != null) {
                this.mLogger.logParentChangeSuppressed(this.mIterationCount, attachState.getSuppressedChanges().getParent(), attachState.getParent());
            }
            if (attachState.getSuppressedChanges().getSection() != null) {
                this.mLogger.logSectionChangeSuppressed(this.mIterationCount, attachState.getSuppressedChanges().getSection(), attachState.getSection());
            }
            if (attachState.getSuppressedChanges().getWasPruneSuppressed()) {
                this.mLogger.logGroupPruningSuppressed(this.mIterationCount, attachState.getParent());
            }
            if (!Objects.equals(attachState.getGroupPruneReason(), previousAttachState.getGroupPruneReason())) {
                this.mLogger.logPrunedReasonChanged(this.mIterationCount, previousAttachState.getGroupPruneReason(), attachState.getGroupPruneReason());
            }
            if (attachState.getExcludingFilter() != previousAttachState.getExcludingFilter()) {
                this.mLogger.logFilterChanged(this.mIterationCount, previousAttachState.getExcludingFilter(), attachState.getExcludingFilter());
            }
            boolean z = attachState.getParent() == null && previousAttachState.getParent() != null;
            if (!z && attachState.getPromoter() != previousAttachState.getPromoter()) {
                this.mLogger.logPromoterChanged(this.mIterationCount, previousAttachState.getPromoter(), attachState.getPromoter());
            }
            if (!z && attachState.getSection() != previousAttachState.getSection()) {
                this.mLogger.logSectionChanged(this.mIterationCount, previousAttachState.getSection(), attachState.getSection());
            }
        }
    }

    public final void onBeginRun() {
        getStabilityManager().onBeginRun();
    }

    public final void cleanupPluggables() {
        Trace.beginSection("ShadeListBuilder.cleanupPluggables");
        callOnCleanup(this.mNotifPreGroupFilters);
        callOnCleanup(this.mNotifPromoters);
        callOnCleanup(this.mNotifFinalizeFilters);
        callOnCleanup(this.mNotifComparators);
        for (int i = 0; i < this.mNotifSections.size(); i++) {
            NotifSection notifSection = this.mNotifSections.get(i);
            notifSection.getSectioner().onCleanup();
            NotifComparator comparator = notifSection.getComparator();
            if (comparator != null) {
                comparator.onCleanup();
            }
        }
        callOnCleanup(List.of(getStabilityManager()));
        Trace.endSection();
    }

    public final void callOnCleanup(List<? extends Pluggable<?>> list) {
        for (int i = 0; i < list.size(); i++) {
            ((Pluggable) list.get(i)).onCleanup();
        }
    }

    public final NotifComparator getSectionComparator(ListEntry listEntry, ListEntry listEntry2) {
        NotifSection section = listEntry.getSection();
        if (section != listEntry2.getSection()) {
            throw new RuntimeException("Entry ordering should only be done within sections");
        } else if (section != null) {
            return section.getComparator();
        } else {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$new$2(ListEntry listEntry, ListEntry listEntry2) {
        int compare;
        int compare2 = Integer.compare(listEntry.getSectionIndex(), listEntry2.getSectionIndex());
        if (compare2 != 0) {
            return compare2;
        }
        int i = -1;
        int stableIndex = canReorder(listEntry) ? -1 : listEntry.getPreviousAttachState().getStableIndex();
        if (!canReorder(listEntry2)) {
            i = listEntry2.getPreviousAttachState().getStableIndex();
        }
        int compare3 = Integer.compare(stableIndex, i);
        if (compare3 != 0) {
            return compare3;
        }
        NotifComparator sectionComparator = getSectionComparator(listEntry, listEntry2);
        if (sectionComparator != null && (compare = sectionComparator.compare(listEntry, listEntry2)) != 0) {
            return compare;
        }
        for (int i2 = 0; i2 < this.mNotifComparators.size(); i2++) {
            int compare4 = this.mNotifComparators.get(i2).compare(listEntry, listEntry2);
            if (compare4 != 0) {
                return compare4;
            }
        }
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        NotificationEntry representativeEntry2 = listEntry2.getRepresentativeEntry();
        int rank = representativeEntry.getRanking().getRank() - representativeEntry2.getRanking().getRank();
        if (rank != 0) {
            return rank;
        }
        return Long.compare(representativeEntry2.getSbn().getNotification().when, representativeEntry.getSbn().getNotification().when);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ int lambda$new$3(ListEntry listEntry, ListEntry listEntry2) {
        int i = -1;
        int stableIndex = canReorder(listEntry) ? -1 : listEntry.getPreviousAttachState().getStableIndex();
        if (!canReorder(listEntry2)) {
            i = listEntry2.getPreviousAttachState().getStableIndex();
        }
        int compare = Integer.compare(stableIndex, i);
        if (compare != 0) {
            return compare;
        }
        int rank = listEntry.getRepresentativeEntry().getRanking().getRank() - listEntry2.getRepresentativeEntry().getRanking().getRank();
        if (rank != 0) {
            return rank;
        }
        return Long.compare(listEntry2.getRepresentativeEntry().getSbn().getNotification().when, listEntry.getRepresentativeEntry().getSbn().getNotification().when);
    }

    public final boolean canReorder(ListEntry listEntry) {
        return this.mForceReorderable || getStabilityManager().isEntryReorderingAllowed(listEntry);
    }

    public final boolean applyFilters(NotificationEntry notificationEntry, long j, List<NotifFilter> list) {
        NotifFilter findRejectingFilter = findRejectingFilter(notificationEntry, j, list);
        notificationEntry.getAttachState().setExcludingFilter(findRejectingFilter);
        if (findRejectingFilter != null) {
            notificationEntry.resetInitializationTime();
        }
        return findRejectingFilter != null;
    }

    public static NotifFilter findRejectingFilter(NotificationEntry notificationEntry, long j, List<NotifFilter> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            NotifFilter notifFilter = list.get(i);
            if (notifFilter.shouldFilterOut(notificationEntry, j)) {
                return notifFilter;
            }
        }
        return null;
    }

    public final boolean applyTopLevelPromoters(NotificationEntry notificationEntry) {
        NotifPromoter findPromoter = findPromoter(notificationEntry);
        notificationEntry.getAttachState().setPromoter(findPromoter);
        return findPromoter != null;
    }

    public final NotifPromoter findPromoter(NotificationEntry notificationEntry) {
        for (int i = 0; i < this.mNotifPromoters.size(); i++) {
            NotifPromoter notifPromoter = this.mNotifPromoters.get(i);
            if (notifPromoter.shouldPromoteToTopLevel(notificationEntry)) {
                return notifPromoter;
            }
        }
        return null;
    }

    public final NotifSection applySections(ListEntry listEntry) {
        NotifSection findSection = findSection(listEntry);
        ListAttachState previousAttachState = listEntry.getPreviousAttachState();
        if (listEntry.wasAttachedInPreviousPass() && findSection != previousAttachState.getSection() && !getStabilityManager().isSectionChangeAllowed(listEntry.getRepresentativeEntry())) {
            listEntry.getAttachState().getSuppressedChanges().setSection(findSection);
            findSection = previousAttachState.getSection();
        }
        setEntrySection(listEntry, findSection);
        return findSection;
    }

    public final void setEntrySection(ListEntry listEntry, NotifSection notifSection) {
        listEntry.getAttachState().setSection(notifSection);
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        if (representativeEntry != null) {
            representativeEntry.getAttachState().setSection(notifSection);
            if (notifSection != null) {
                representativeEntry.setBucket(notifSection.getBucket());
            }
        }
    }

    public final NotifSection findSection(ListEntry listEntry) {
        for (int i = 0; i < this.mNotifSections.size(); i++) {
            NotifSection notifSection = this.mNotifSections.get(i);
            if (notifSection.getSectioner().isInSection(listEntry)) {
                return notifSection;
            }
        }
        throw new RuntimeException("Missing default sectioner!");
    }

    public final void rebuildListIfBefore(int i) {
        this.mPipelineState.requireIsBefore(i);
        if (this.mPipelineState.is(0)) {
            this.mChoreographer.schedule();
        }
    }

    public static int countChildren(List<ListEntry> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            ListEntry listEntry = list.get(i2);
            if (listEntry instanceof GroupEntry) {
                i += ((GroupEntry) listEntry).getChildren().size();
            }
        }
        return i;
    }

    public final void dispatchOnBeforeTransformGroups(List<ListEntry> list) {
        Trace.beginSection("ShadeListBuilder.dispatchOnBeforeTransformGroups");
        for (int i = 0; i < this.mOnBeforeTransformGroupsListeners.size(); i++) {
            this.mOnBeforeTransformGroupsListeners.get(i).onBeforeTransformGroups(list);
        }
        Trace.endSection();
    }

    public final void dispatchOnBeforeSort(List<ListEntry> list) {
        Trace.beginSection("ShadeListBuilder.dispatchOnBeforeSort");
        for (int i = 0; i < this.mOnBeforeSortListeners.size(); i++) {
            this.mOnBeforeSortListeners.get(i).onBeforeSort(list);
        }
        Trace.endSection();
    }

    public final void dispatchOnBeforeFinalizeFilter(List<ListEntry> list) {
        Trace.beginSection("ShadeListBuilder.dispatchOnBeforeFinalizeFilter");
        for (int i = 0; i < this.mOnBeforeFinalizeFilterListeners.size(); i++) {
            this.mOnBeforeFinalizeFilterListeners.get(i).onBeforeFinalizeFilter(list);
        }
        Trace.endSection();
    }

    public final void dispatchOnBeforeRenderList(List<ListEntry> list) {
        Trace.beginSection("ShadeListBuilder.dispatchOnBeforeRenderList");
        for (int i = 0; i < this.mOnBeforeRenderListListeners.size(); i++) {
            this.mOnBeforeRenderListListeners.get(i).onBeforeRenderList(list);
        }
        Trace.endSection();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("\tShadeListBuilder shade notifications:");
        if (getShadeList().size() == 0) {
            printWriter.println("\t\t None");
        }
        printWriter.println(ListDumper.dumpTree(getShadeList(), this.mInteractionTracker, true, "\t\t"));
    }
}

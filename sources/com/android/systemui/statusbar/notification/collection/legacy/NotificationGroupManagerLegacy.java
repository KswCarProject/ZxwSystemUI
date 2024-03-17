package com.android.systemui.statusbar.notification.collection.legacy;

import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.wm.shell.bubbles.Bubbles;
import dagger.Lazy;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Function;

public class NotificationGroupManagerLegacy implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, GroupMembershipManager, GroupExpansionManager, Dumpable {
    public static final boolean DEBUG = Log.isLoggable("LegacyNotifGroupManager", 3);
    public static final boolean SPEW = Log.isLoggable("LegacyNotifGroupManager", 2);
    public int mBarState;
    public final Optional<Bubbles> mBubblesOptional;
    public final GroupEventDispatcher mEventDispatcher;
    public final ArraySet<GroupExpansionManager.OnGroupExpansionChangeListener> mExpansionChangeListeners = new ArraySet<>();
    public final HashMap<String, NotificationGroup> mGroupMap;
    public HeadsUpManager mHeadsUpManager;
    public boolean mIsUpdatingUnchangedGroup;
    public HashMap<String, StatusBarNotification> mIsolatedEntries;
    public final Lazy<PeopleNotificationIdentifier> mPeopleNotificationIdentifier;

    public interface OnGroupChangeListener {
        void onGroupAlertOverrideChanged(NotificationGroup notificationGroup, NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        }

        void onGroupCreated(NotificationGroup notificationGroup, String str) {
        }

        void onGroupRemoved(NotificationGroup notificationGroup, String str) {
        }

        void onGroupSuppressionChanged(NotificationGroup notificationGroup, boolean z) {
        }

        void onGroupsChanged() {
        }
    }

    public NotificationGroupManagerLegacy(StatusBarStateController statusBarStateController, Lazy<PeopleNotificationIdentifier> lazy, Optional<Bubbles> optional, DumpManager dumpManager) {
        HashMap<String, NotificationGroup> hashMap = new HashMap<>();
        this.mGroupMap = hashMap;
        Objects.requireNonNull(hashMap);
        this.mEventDispatcher = new GroupEventDispatcher(new NotificationGroupManagerLegacy$$ExternalSyntheticLambda0(hashMap));
        this.mBarState = -1;
        this.mIsolatedEntries = new HashMap<>();
        statusBarStateController.addCallback(this);
        this.mPeopleNotificationIdentifier = lazy;
        this.mBubblesOptional = optional;
        dumpManager.registerDumpable(this);
    }

    public void registerGroupChangeListener(OnGroupChangeListener onGroupChangeListener) {
        this.mEventDispatcher.registerGroupChangeListener(onGroupChangeListener);
    }

    public void registerGroupExpansionChangeListener(GroupExpansionManager.OnGroupExpansionChangeListener onGroupExpansionChangeListener) {
        this.mExpansionChangeListeners.add(onGroupExpansionChangeListener);
    }

    public boolean isGroupExpanded(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup == null) {
            return false;
        }
        return notificationGroup.expanded;
    }

    public boolean isLogicalGroupExpanded(StatusBarNotification statusBarNotification) {
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        if (notificationGroup == null) {
            return false;
        }
        return notificationGroup.expanded;
    }

    public void setGroupExpanded(NotificationEntry notificationEntry, boolean z) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup != null) {
            setGroupExpanded(notificationGroup, z);
        }
    }

    public final void setGroupExpanded(NotificationGroup notificationGroup, boolean z) {
        notificationGroup.expanded = z;
        if (notificationGroup.summary != null) {
            Iterator<GroupExpansionManager.OnGroupExpansionChangeListener> it = this.mExpansionChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupExpansionChange(notificationGroup.summary.getRow(), z);
            }
        }
    }

    public void onEntryRemoved(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d("LegacyNotifGroupManager", "onEntryRemoved: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        this.mEventDispatcher.openBufferScope();
        onEntryRemovedInternal(notificationEntry, notificationEntry.getSbn());
        StatusBarNotification remove = this.mIsolatedEntries.remove(notificationEntry.getKey());
        if (remove != null) {
            updateSuppression(this.mGroupMap.get(remove.getGroupKey()));
        }
        this.mEventDispatcher.closeBufferScope();
    }

    public final void onEntryRemovedInternal(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        onEntryRemovedInternal(notificationEntry, statusBarNotification.getGroupKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    public final void onEntryRemovedInternal(NotificationEntry notificationEntry, String str, boolean z, boolean z2) {
        String groupKey = getGroupKey(notificationEntry.getKey(), str);
        NotificationGroup notificationGroup = this.mGroupMap.get(groupKey);
        if (notificationGroup != null) {
            if (SPEW) {
                Log.d("LegacyNotifGroupManager", "onEntryRemovedInternal: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " group=" + logGroupKey(notificationGroup));
            }
            if (isGroupChild(notificationEntry.getKey(), z, z2)) {
                notificationGroup.children.remove(notificationEntry.getKey());
            } else {
                notificationGroup.summary = null;
            }
            updateSuppression(notificationGroup);
            if (notificationGroup.children.isEmpty() && notificationGroup.summary == null) {
                this.mGroupMap.remove(groupKey);
                this.mEventDispatcher.notifyGroupRemoved(notificationGroup);
            }
        }
    }

    public void onEntryAdded(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d("LegacyNotifGroupManager", "onEntryAdded: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        this.mEventDispatcher.openBufferScope();
        updateIsolation(notificationEntry);
        onEntryAddedInternal(notificationEntry);
        this.mEventDispatcher.closeBufferScope();
    }

    public final void onEntryAddedInternal(NotificationEntry notificationEntry) {
        String str;
        if (notificationEntry.isRowRemoved()) {
            notificationEntry.setDebugThrowable(new Throwable());
        }
        StatusBarNotification sbn = notificationEntry.getSbn();
        boolean isGroupChild = isGroupChild(sbn);
        String groupKey = getGroupKey(sbn);
        NotificationGroup notificationGroup = this.mGroupMap.get(groupKey);
        if (notificationGroup == null) {
            notificationGroup = new NotificationGroup(groupKey);
            this.mGroupMap.put(groupKey, notificationGroup);
            this.mEventDispatcher.notifyGroupCreated(notificationGroup);
        }
        if (SPEW) {
            Log.d("LegacyNotifGroupManager", "onEntryAddedInternal: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " group=" + logGroupKey(notificationGroup));
        }
        if (isGroupChild) {
            NotificationEntry notificationEntry2 = notificationGroup.children.get(notificationEntry.getKey());
            if (!(notificationEntry2 == null || notificationEntry2 == notificationEntry)) {
                Throwable debugThrowable = notificationEntry2.getDebugThrowable();
                StringBuilder sb = new StringBuilder();
                sb.append("Inconsistent entries found with the same key ");
                sb.append(NotificationUtils.logKey((ListEntry) notificationEntry));
                sb.append("existing removed: ");
                sb.append(notificationEntry2.isRowRemoved());
                if (debugThrowable != null) {
                    str = Log.getStackTraceString(debugThrowable) + "\n";
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append(" added removed");
                sb.append(notificationEntry.isRowRemoved());
                Log.wtf("LegacyNotifGroupManager", sb.toString(), new Throwable());
            }
            notificationGroup.children.put(notificationEntry.getKey(), notificationEntry);
            addToPostBatchHistory(notificationGroup, notificationEntry);
            updateSuppression(notificationGroup);
            return;
        }
        notificationGroup.summary = notificationEntry;
        addToPostBatchHistory(notificationGroup, notificationEntry);
        notificationGroup.expanded = notificationEntry.areChildrenExpanded();
        updateSuppression(notificationGroup);
        if (!notificationGroup.children.isEmpty()) {
            Iterator it = new ArrayList(notificationGroup.children.values()).iterator();
            while (it.hasNext()) {
                onEntryBecomingChild((NotificationEntry) it.next());
            }
            this.mEventDispatcher.notifyGroupsChanged();
        }
    }

    public final void addToPostBatchHistory(NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        if (notificationEntry != null && notificationGroup.postBatchHistory.add(new PostRecord(notificationEntry))) {
            trimPostBatchHistory(notificationGroup.postBatchHistory);
        }
    }

    public final void trimPostBatchHistory(TreeSet<PostRecord> treeSet) {
        if (treeSet.size() > 1) {
            long j = treeSet.last().postTime - 5000;
            while (!treeSet.isEmpty() && treeSet.first().postTime < j) {
                treeSet.pollFirst();
            }
        }
    }

    public final void onEntryBecomingChild(NotificationEntry notificationEntry) {
        updateIsolation(notificationEntry);
    }

    public final void updateSuppression(NotificationGroup notificationGroup) {
        if (notificationGroup != null) {
            NotificationEntry notificationEntry = notificationGroup.alertOverride;
            notificationGroup.alertOverride = getPriorityConversationAlertOverride(notificationGroup);
            boolean z = false;
            int i = 0;
            boolean z2 = false;
            for (NotificationEntry next : notificationGroup.children.values()) {
                if (!this.mBubblesOptional.isPresent() || !this.mBubblesOptional.get().isBubbleNotificationSuppressedFromShade(next.getKey(), next.getSbn().getGroupKey())) {
                    i++;
                } else {
                    z2 = true;
                }
            }
            boolean z3 = notificationGroup.suppressed;
            NotificationEntry notificationEntry2 = notificationGroup.summary;
            boolean z4 = notificationEntry2 != null && !notificationGroup.expanded && (i == 1 || (i == 0 && notificationEntry2.getSbn().getNotification().isGroupSummary() && (hasIsolatedChildren(notificationGroup) || z2)));
            notificationGroup.suppressed = z4;
            boolean z5 = notificationEntry != notificationGroup.alertOverride;
            if (z3 != z4) {
                z = true;
            }
            if (z5 || z) {
                if (DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("updateSuppression: willNotifyListeners=");
                    sb.append(!this.mIsUpdatingUnchangedGroup);
                    sb.append(" changes for group:\n");
                    sb.append(notificationGroup);
                    Log.d("LegacyNotifGroupManager", sb.toString());
                    if (z5) {
                        Log.d("LegacyNotifGroupManager", "updateSuppression: alertOverride was=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " now=" + NotificationUtils.logKey((ListEntry) notificationGroup.alertOverride));
                    }
                    if (z) {
                        Log.d("LegacyNotifGroupManager", "updateSuppression: suppressed changed to " + notificationGroup.suppressed);
                    }
                }
                if (z5) {
                    this.mEventDispatcher.notifyAlertOverrideChanged(notificationGroup, notificationEntry);
                }
                if (z) {
                    this.mEventDispatcher.notifySuppressedChanged(notificationGroup);
                }
                if (!this.mIsUpdatingUnchangedGroup) {
                    this.mEventDispatcher.notifyGroupsChanged();
                }
            }
        }
    }

    public final NotificationEntry getPriorityConversationAlertOverride(NotificationGroup notificationGroup) {
        NotificationEntry notificationEntry;
        if (notificationGroup == null || (notificationEntry = notificationGroup.summary) == null) {
            if (SPEW) {
                Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: null group or summary group=" + logGroupKey(notificationGroup));
            }
            return null;
        } else if (isIsolated(notificationEntry.getKey())) {
            if (SPEW) {
                Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: isolated group group=" + logGroupKey(notificationGroup));
            }
            return null;
        } else if (notificationGroup.summary.getSbn().getNotification().getGroupAlertBehavior() == 2) {
            if (SPEW) {
                Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: summary == GROUP_ALERT_CHILDREN group=" + logGroupKey(notificationGroup));
            }
            return null;
        } else {
            HashMap<String, NotificationEntry> importantConversations = getImportantConversations(notificationGroup);
            if (importantConversations == null || importantConversations.isEmpty()) {
                if (SPEW) {
                    Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: no important conversations group=" + logGroupKey(notificationGroup));
                }
                return null;
            }
            HashSet hashSet = new HashSet(importantConversations.keySet());
            importantConversations.putAll(notificationGroup.children);
            for (NotificationEntry sbn : importantConversations.values()) {
                if (sbn.getSbn().getNotification().getGroupAlertBehavior() != 1) {
                    if (SPEW) {
                        Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: child != GROUP_ALERT_SUMMARY group=" + logGroupKey(notificationGroup));
                    }
                    return null;
                }
            }
            TreeSet treeSet = new TreeSet(notificationGroup.postBatchHistory);
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                treeSet.addAll(this.mGroupMap.get((String) it.next()).postBatchHistory);
            }
            trimPostBatchHistory(treeSet);
            HashSet hashSet2 = new HashSet();
            long j = -1;
            NotificationEntry notificationEntry2 = null;
            for (PostRecord postRecord : treeSet.descendingSet()) {
                if (hashSet2.contains(postRecord.key)) {
                    break;
                }
                hashSet2.add(postRecord.key);
                NotificationEntry notificationEntry3 = importantConversations.get(postRecord.key);
                if (notificationEntry3 != null) {
                    long j2 = notificationEntry3.getSbn().getNotification().when;
                    if (notificationEntry2 == null || j2 > j) {
                        notificationEntry2 = notificationEntry3;
                        j = j2;
                    }
                }
            }
            if (notificationEntry2 == null || !hashSet.contains(notificationEntry2.getKey())) {
                if (SPEW) {
                    Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: result=null newestChild=" + NotificationUtils.logKey((ListEntry) notificationEntry2) + " group=" + logGroupKey(notificationGroup));
                }
                return null;
            }
            if (SPEW) {
                Log.d("LegacyNotifGroupManager", "getPriorityConversationAlertOverride: result=" + NotificationUtils.logKey((ListEntry) notificationEntry2) + " group=" + logGroupKey(notificationGroup));
            }
            return notificationEntry2;
        }
    }

    public final boolean hasIsolatedChildren(NotificationGroup notificationGroup) {
        return getNumberOfIsolatedChildren(notificationGroup.summary.getSbn().getGroupKey()) != 0;
    }

    public final int getNumberOfIsolatedChildren(String str) {
        int i = 0;
        for (StatusBarNotification next : this.mIsolatedEntries.values()) {
            if (next.getGroupKey().equals(str) && isIsolated(next.getKey())) {
                i++;
            }
        }
        return i;
    }

    public final HashMap<String, NotificationEntry> getImportantConversations(NotificationGroup notificationGroup) {
        String groupKey = notificationGroup.summary.getSbn().getGroupKey();
        HashMap<String, NotificationEntry> hashMap = null;
        for (StatusBarNotification next : this.mIsolatedEntries.values()) {
            if (next.getGroupKey().equals(groupKey)) {
                NotificationEntry notificationEntry = this.mGroupMap.get(next.getKey()).summary;
                if (isImportantConversation(notificationEntry)) {
                    if (hashMap == null) {
                        hashMap = new HashMap<>();
                    }
                    hashMap.put(next.getKey(), notificationEntry);
                }
            }
        }
        return hashMap;
    }

    public void onEntryUpdated(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
        if (SPEW) {
            Log.d("LegacyNotifGroupManager", "onEntryUpdated: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        onEntryUpdated(notificationEntry, statusBarNotification.getGroupKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    public void onEntryUpdated(NotificationEntry notificationEntry, String str, boolean z, boolean z2) {
        String groupKey = notificationEntry.getSbn().getGroupKey();
        boolean z3 = true;
        boolean z4 = !str.equals(groupKey);
        boolean isGroupChild = isGroupChild(notificationEntry.getKey(), z, z2);
        boolean isGroupChild2 = isGroupChild(notificationEntry.getSbn());
        this.mEventDispatcher.openBufferScope();
        if (z4 || isGroupChild != isGroupChild2) {
            z3 = false;
        }
        this.mIsUpdatingUnchangedGroup = z3;
        if (this.mGroupMap.get(getGroupKey(notificationEntry.getKey(), str)) != null) {
            onEntryRemovedInternal(notificationEntry, str, z, z2);
        }
        onEntryAddedInternal(notificationEntry);
        this.mIsUpdatingUnchangedGroup = false;
        if (isIsolated(notificationEntry.getSbn().getKey())) {
            this.mIsolatedEntries.put(notificationEntry.getKey(), notificationEntry.getSbn());
            if (z4) {
                updateSuppression(this.mGroupMap.get(str));
            }
            updateSuppression(this.mGroupMap.get(groupKey));
        } else if (!isGroupChild && isGroupChild2) {
            onEntryBecomingChild(notificationEntry);
        }
        this.mEventDispatcher.closeBufferScope();
    }

    public boolean isSummaryOfSuppressedGroup(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().isGroupSummary() && isGroupSuppressed(getGroupKey(statusBarNotification));
    }

    public NotificationGroup getGroupForSummary(StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getNotification().isGroupSummary()) {
            return this.mGroupMap.get(getGroupKey(statusBarNotification));
        }
        return null;
    }

    public final boolean isOnlyChild(StatusBarNotification statusBarNotification) {
        if (statusBarNotification.getNotification().isGroupSummary() || getTotalNumberOfChildren(statusBarNotification) != 1) {
            return false;
        }
        return true;
    }

    public boolean isOnlyChildInGroup(NotificationEntry notificationEntry) {
        NotificationEntry logicalGroupSummary;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (isOnlyChild(sbn) && (logicalGroupSummary = getLogicalGroupSummary(notificationEntry)) != null && !logicalGroupSummary.getSbn().equals(sbn)) {
            return true;
        }
        return false;
    }

    public final int getTotalNumberOfChildren(StatusBarNotification statusBarNotification) {
        int numberOfIsolatedChildren = getNumberOfIsolatedChildren(statusBarNotification.getGroupKey());
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        return numberOfIsolatedChildren + (notificationGroup != null ? notificationGroup.children.size() : 0);
    }

    public final boolean isGroupSuppressed(String str) {
        NotificationGroup notificationGroup = this.mGroupMap.get(str);
        return notificationGroup != null && notificationGroup.suppressed;
    }

    public final void setStatusBarState(int i) {
        this.mBarState = i;
        if (i == 1) {
            collapseGroups();
        }
    }

    public void collapseGroups() {
        ArrayList arrayList = new ArrayList(this.mGroupMap.values());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            NotificationGroup notificationGroup = (NotificationGroup) arrayList.get(i);
            if (notificationGroup.expanded) {
                setGroupExpanded(notificationGroup, false);
            }
            updateSuppression(notificationGroup);
        }
    }

    public boolean isChildInGroup(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (isGroupChild(sbn) && (notificationGroup = this.mGroupMap.get(getGroupKey(sbn))) != null && notificationGroup.summary != null && !notificationGroup.suppressed && !notificationGroup.children.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isGroupSummary(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup;
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (isGroupSummary(sbn) && (notificationGroup = this.mGroupMap.get(getGroupKey(sbn))) != null && notificationGroup.summary != null && !notificationGroup.children.isEmpty() && Objects.equals(notificationGroup.summary.getSbn(), sbn)) {
            return true;
        }
        return false;
    }

    public NotificationEntry getGroupSummary(NotificationEntry notificationEntry) {
        return getGroupSummary(getGroupKey(notificationEntry.getSbn()));
    }

    public NotificationEntry getLogicalGroupSummary(NotificationEntry notificationEntry) {
        return getGroupSummary(notificationEntry.getSbn().getGroupKey());
    }

    public final NotificationEntry getGroupSummary(String str) {
        NotificationGroup notificationGroup = this.mGroupMap.get(str);
        if (notificationGroup == null) {
            return null;
        }
        return notificationGroup.summary;
    }

    public ArrayList<NotificationEntry> getLogicalChildren(StatusBarNotification statusBarNotification) {
        NotificationGroup notificationGroup = this.mGroupMap.get(statusBarNotification.getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        ArrayList<NotificationEntry> arrayList = new ArrayList<>(notificationGroup.children.values());
        for (StatusBarNotification next : this.mIsolatedEntries.values()) {
            if (next.getGroupKey().equals(statusBarNotification.getGroupKey())) {
                arrayList.add(this.mGroupMap.get(next.getKey()).summary);
            }
        }
        return arrayList;
    }

    public List<NotificationEntry> getChildren(ListEntry listEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(listEntry.getRepresentativeEntry().getSbn().getGroupKey());
        if (notificationGroup == null) {
            return null;
        }
        return new ArrayList(notificationGroup.children.values());
    }

    public void updateSuppression(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup != null) {
            updateSuppression(notificationGroup);
        }
    }

    public String getGroupKey(StatusBarNotification statusBarNotification) {
        return getGroupKey(statusBarNotification.getKey(), statusBarNotification.getGroupKey());
    }

    public final String getGroupKey(String str, String str2) {
        return isIsolated(str) ? str : str2;
    }

    public boolean toggleGroupExpansion(NotificationEntry notificationEntry) {
        NotificationGroup notificationGroup = this.mGroupMap.get(getGroupKey(notificationEntry.getSbn()));
        if (notificationGroup == null) {
            return false;
        }
        setGroupExpanded(notificationGroup, !notificationGroup.expanded);
        return notificationGroup.expanded;
    }

    public final boolean isIsolated(String str) {
        return this.mIsolatedEntries.containsKey(str);
    }

    public boolean isGroupSummary(StatusBarNotification statusBarNotification) {
        if (isIsolated(statusBarNotification.getKey())) {
            return true;
        }
        return statusBarNotification.getNotification().isGroupSummary();
    }

    public boolean isGroupChild(StatusBarNotification statusBarNotification) {
        return isGroupChild(statusBarNotification.getKey(), statusBarNotification.isGroup(), statusBarNotification.getNotification().isGroupSummary());
    }

    public final boolean isGroupChild(String str, boolean z, boolean z2) {
        return !isIsolated(str) && z && !z2;
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        updateIsolation(notificationEntry);
    }

    public final boolean shouldIsolate(NotificationEntry notificationEntry) {
        StatusBarNotification sbn = notificationEntry.getSbn();
        if (!sbn.isGroup() || sbn.getNotification().isGroupSummary()) {
            return false;
        }
        if (isImportantConversation(notificationEntry)) {
            return true;
        }
        HeadsUpManager headsUpManager = this.mHeadsUpManager;
        if (headsUpManager != null && !headsUpManager.isAlerting(notificationEntry.getKey())) {
            return false;
        }
        NotificationGroup notificationGroup = this.mGroupMap.get(sbn.getGroupKey());
        if (sbn.getNotification().fullScreenIntent != null || notificationGroup == null || !notificationGroup.expanded || isGroupNotFullyVisible(notificationGroup)) {
            return true;
        }
        return false;
    }

    public final boolean isImportantConversation(NotificationEntry notificationEntry) {
        return this.mPeopleNotificationIdentifier.get().getPeopleNotificationType(notificationEntry) == 3;
    }

    public final void isolateNotification(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d("LegacyNotifGroupManager", "isolateNotification: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        onEntryRemovedInternal(notificationEntry, notificationEntry.getSbn());
        this.mIsolatedEntries.put(notificationEntry.getKey(), notificationEntry.getSbn());
        onEntryAddedInternal(notificationEntry);
        updateSuppression(this.mGroupMap.get(notificationEntry.getSbn().getGroupKey()));
        this.mEventDispatcher.notifyGroupsChanged();
    }

    public void updateIsolation(NotificationEntry notificationEntry) {
        this.mEventDispatcher.openBufferScope();
        boolean isIsolated = isIsolated(notificationEntry.getSbn().getKey());
        if (shouldIsolate(notificationEntry)) {
            if (!isIsolated) {
                isolateNotification(notificationEntry);
            }
        } else if (isIsolated) {
            stopIsolatingNotification(notificationEntry);
        }
        this.mEventDispatcher.closeBufferScope();
    }

    public final void stopIsolatingNotification(NotificationEntry notificationEntry) {
        if (SPEW) {
            Log.d("LegacyNotifGroupManager", "stopIsolatingNotification: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        onEntryRemovedInternal(notificationEntry, notificationEntry.getSbn());
        this.mIsolatedEntries.remove(notificationEntry.getKey());
        onEntryAddedInternal(notificationEntry);
        this.mEventDispatcher.notifyGroupsChanged();
    }

    public final boolean isGroupNotFullyVisible(NotificationGroup notificationGroup) {
        NotificationEntry notificationEntry = notificationGroup.summary;
        return notificationEntry == null || notificationEntry.isGroupNotFullyVisible();
    }

    public void setHeadsUpManager(HeadsUpManager headsUpManager) {
        this.mHeadsUpManager = headsUpManager;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("GroupManagerLegacy state:");
        printWriter.println("  number of groups: " + this.mGroupMap.size());
        for (Map.Entry next : this.mGroupMap.entrySet()) {
            printWriter.println("\n    key: " + NotificationUtils.logKey((String) next.getKey()));
            printWriter.println(next.getValue());
        }
        printWriter.println("\n    isolated entries: " + this.mIsolatedEntries.size());
        for (Map.Entry next2 : this.mIsolatedEntries.entrySet()) {
            printWriter.print("      ");
            printWriter.print(NotificationUtils.logKey((String) next2.getKey()));
            printWriter.print(", ");
            printWriter.println(next2.getValue());
        }
    }

    public void onStateChanged(int i) {
        setStatusBarState(i);
    }

    public static String logGroupKey(NotificationGroup notificationGroup) {
        return notificationGroup == null ? "null" : NotificationUtils.logKey(notificationGroup.groupKey);
    }

    public static class PostRecord implements Comparable<PostRecord> {
        public final String key;
        public final long postTime;

        public PostRecord(NotificationEntry notificationEntry) {
            this.postTime = notificationEntry.getSbn().getPostTime();
            this.key = notificationEntry.getKey();
        }

        public int compareTo(PostRecord postRecord) {
            int compare = Long.compare(this.postTime, postRecord.postTime);
            return compare == 0 ? String.CASE_INSENSITIVE_ORDER.compare(this.key, postRecord.key) : compare;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PostRecord postRecord = (PostRecord) obj;
            if (this.postTime != postRecord.postTime || !this.key.equals(postRecord.key)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Long.valueOf(this.postTime), this.key});
        }
    }

    public static class NotificationGroup {
        public NotificationEntry alertOverride;
        public final HashMap<String, NotificationEntry> children = new HashMap<>();
        public boolean expanded;
        public final String groupKey;
        public final TreeSet<PostRecord> postBatchHistory = new TreeSet<>();
        public NotificationEntry summary;
        public boolean suppressed;

        public NotificationGroup(String str) {
            this.groupKey = str;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("    groupKey: ");
            sb.append(this.groupKey);
            sb.append("\n    summary:");
            appendEntry(sb, this.summary);
            sb.append("\n    children size: ");
            sb.append(this.children.size());
            for (NotificationEntry appendEntry : this.children.values()) {
                appendEntry(sb, appendEntry);
            }
            sb.append("\n    alertOverride:");
            appendEntry(sb, this.alertOverride);
            sb.append("\n    summary suppressed: ");
            sb.append(this.suppressed);
            return sb.toString();
        }

        public final void appendEntry(StringBuilder sb, NotificationEntry notificationEntry) {
            sb.append("\n      ");
            sb.append(notificationEntry != null ? notificationEntry.getSbn() : "null");
            if (notificationEntry != null && notificationEntry.getDebugThrowable() != null) {
                sb.append(Log.getStackTraceString(notificationEntry.getDebugThrowable()));
            }
        }
    }

    public static class GroupEventDispatcher {
        public int mBufferScopeDepth = 0;
        public boolean mDidGroupsChange = false;
        public final ArraySet<OnGroupChangeListener> mGroupChangeListeners = new ArraySet<>();
        public final Function<String, NotificationGroup> mGroupMapGetter;
        public final HashMap<String, NotificationEntry> mOldAlertOverrideByGroup = new HashMap<>();
        public final HashMap<String, Boolean> mOldSuppressedByGroup = new HashMap<>();

        public GroupEventDispatcher(Function<String, NotificationGroup> function) {
            Objects.requireNonNull(function);
            this.mGroupMapGetter = function;
        }

        public void registerGroupChangeListener(OnGroupChangeListener onGroupChangeListener) {
            this.mGroupChangeListeners.add(onGroupChangeListener);
        }

        public final boolean isBuffering() {
            return this.mBufferScopeDepth > 0;
        }

        public void notifyAlertOverrideChanged(NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
            if (!isBuffering()) {
                Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
                while (it.hasNext()) {
                    it.next().onGroupAlertOverrideChanged(notificationGroup, notificationEntry, notificationGroup.alertOverride);
                }
            } else if (!this.mOldAlertOverrideByGroup.containsKey(notificationGroup.groupKey)) {
                this.mOldAlertOverrideByGroup.put(notificationGroup.groupKey, notificationEntry);
            }
        }

        public void notifySuppressedChanged(NotificationGroup notificationGroup) {
            if (isBuffering()) {
                this.mOldSuppressedByGroup.putIfAbsent(notificationGroup.groupKey, Boolean.valueOf(!notificationGroup.suppressed));
                return;
            }
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupSuppressionChanged(notificationGroup, notificationGroup.suppressed);
            }
        }

        public void notifyGroupsChanged() {
            if (isBuffering()) {
                this.mDidGroupsChange = true;
                return;
            }
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupsChanged();
            }
        }

        public void notifyGroupCreated(NotificationGroup notificationGroup) {
            String str = notificationGroup.groupKey;
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupCreated(notificationGroup, str);
            }
        }

        public void notifyGroupRemoved(NotificationGroup notificationGroup) {
            String str = notificationGroup.groupKey;
            Iterator<OnGroupChangeListener> it = this.mGroupChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onGroupRemoved(notificationGroup, str);
            }
        }

        public void openBufferScope() {
            this.mBufferScopeDepth++;
            if (NotificationGroupManagerLegacy.SPEW) {
                Log.d("LegacyNotifGroupManager", "openBufferScope: scopeDepth=" + this.mBufferScopeDepth);
            }
        }

        public void closeBufferScope() {
            this.mBufferScopeDepth--;
            if (NotificationGroupManagerLegacy.SPEW) {
                Log.d("LegacyNotifGroupManager", "closeBufferScope: scopeDepth=" + this.mBufferScopeDepth);
            }
            if (!isBuffering()) {
                flushBuffer();
            }
        }

        public final void flushBuffer() {
            if (NotificationGroupManagerLegacy.SPEW) {
                Log.d("LegacyNotifGroupManager", "flushBuffer:  suppressed.size=" + this.mOldSuppressedByGroup.size() + " alertOverride.size=" + this.mOldAlertOverrideByGroup.size() + " mDidGroupsChange=" + this.mDidGroupsChange);
            }
            for (Map.Entry next : this.mOldSuppressedByGroup.entrySet()) {
                NotificationGroup apply = this.mGroupMapGetter.apply((String) next.getKey());
                if (apply != null) {
                    if (apply.suppressed != ((Boolean) next.getValue()).booleanValue()) {
                        notifySuppressedChanged(apply);
                    } else if (NotificationGroupManagerLegacy.SPEW) {
                        Log.d("LegacyNotifGroupManager", "flushBuffer: suppressed: did not change for group: " + NotificationUtils.logKey((String) next.getKey()));
                    }
                } else if (NotificationGroupManagerLegacy.SPEW) {
                    Log.d("LegacyNotifGroupManager", "flushBuffer: suppressed: cannot report for removed group: " + NotificationUtils.logKey((String) next.getKey()));
                }
            }
            this.mOldSuppressedByGroup.clear();
            for (Map.Entry next2 : this.mOldAlertOverrideByGroup.entrySet()) {
                NotificationGroup apply2 = this.mGroupMapGetter.apply((String) next2.getKey());
                if (apply2 != null) {
                    NotificationEntry notificationEntry = (NotificationEntry) next2.getValue();
                    if (apply2.alertOverride != notificationEntry) {
                        notifyAlertOverrideChanged(apply2, notificationEntry);
                    } else if (NotificationGroupManagerLegacy.SPEW) {
                        Log.d("LegacyNotifGroupManager", "flushBuffer: alertOverride: did not change for group: " + NotificationUtils.logKey((String) next2.getKey()));
                    }
                } else if (NotificationGroupManagerLegacy.SPEW) {
                    Log.d("LegacyNotifGroupManager", "flushBuffer: alertOverride: cannot report for removed group: " + ((String) next2.getKey()));
                }
            }
            this.mOldAlertOverrideByGroup.clear();
            if (this.mDidGroupsChange) {
                notifyGroupsChanged();
                this.mDidGroupsChange = false;
            }
        }
    }
}

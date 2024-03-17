package com.android.systemui.statusbar.phone;

import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotificationGroupAlertTransferHelper implements OnHeadsUpChangedListener, StatusBarStateController.StateListener {
    public static final boolean DEBUG = Log.isLoggable("NotifGroupAlertTransfer", 3);
    public static final boolean SPEW = Log.isLoggable("NotifGroupAlertTransfer", 2);
    public NotificationEntryManager mEntryManager;
    public final ArrayMap<String, GroupAlertEntry> mGroupAlertEntries = new ArrayMap<>();
    public final NotificationGroupManagerLegacy mGroupManager;
    public HeadsUpManager mHeadsUpManager;
    public boolean mIsDozing;
    public final NotificationEntryListener mNotificationEntryListener = new NotificationEntryListener() {
        public void onPendingEntryAdded(NotificationEntry notificationEntry) {
            if (NotificationGroupAlertTransferHelper.DEBUG) {
                Log.d("NotifGroupAlertTransfer", "!! onPendingEntryAdded: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            GroupAlertEntry groupAlertEntry = (GroupAlertEntry) NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.get(NotificationGroupAlertTransferHelper.this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
            if (groupAlertEntry != null && groupAlertEntry.mGroup.alertOverride == null) {
                NotificationGroupAlertTransferHelper.this.checkShouldTransferBack(groupAlertEntry);
            }
        }

        public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
            NotificationGroupAlertTransferHelper.this.mPendingAlerts.remove(notificationEntry.getKey());
        }
    };
    public final NotificationGroupManagerLegacy.OnGroupChangeListener mOnGroupChangeListener = new NotificationGroupManagerLegacy.OnGroupChangeListener() {
        public void onGroupCreated(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.put(str, new GroupAlertEntry(notificationGroup));
        }

        public void onGroupRemoved(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, String str) {
            NotificationGroupAlertTransferHelper.this.mGroupAlertEntries.remove(str);
        }

        public void onGroupSuppressionChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, boolean z) {
            if (NotificationGroupAlertTransferHelper.DEBUG) {
                Log.d("NotifGroupAlertTransfer", "!! onGroupSuppressionChanged: group=" + NotificationGroupManagerLegacy.logGroupKey(notificationGroup) + " group.summary=" + NotificationUtils.logKey((ListEntry) notificationGroup.summary) + " suppressed=" + z);
            }
            NotificationGroupAlertTransferHelper.this.onGroupChanged(notificationGroup, notificationGroup.alertOverride);
        }

        public void onGroupAlertOverrideChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
            if (NotificationGroupAlertTransferHelper.DEBUG) {
                Log.d("NotifGroupAlertTransfer", "!! onGroupAlertOverrideChanged: group=" + NotificationGroupManagerLegacy.logGroupKey(notificationGroup) + " group.summary=" + NotificationUtils.logKey((ListEntry) notificationGroup.summary) + " oldAlertOverride=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " newAlertOverride=" + NotificationUtils.logKey((ListEntry) notificationEntry2));
            }
            NotificationGroupAlertTransferHelper.this.onGroupChanged(notificationGroup, notificationEntry);
        }
    };
    public final ArrayMap<String, PendingAlertInfo> mPendingAlerts = new ArrayMap<>();
    public final RowContentBindStage mRowContentBindStage;

    public void onStateChanged(int i) {
    }

    public NotificationGroupAlertTransferHelper(RowContentBindStage rowContentBindStage, StatusBarStateController statusBarStateController, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        this.mRowContentBindStage = rowContentBindStage;
        this.mGroupManager = notificationGroupManagerLegacy;
        statusBarStateController.addCallback(this);
    }

    public void bind(NotificationEntryManager notificationEntryManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy) {
        if (this.mEntryManager == null) {
            this.mEntryManager = notificationEntryManager;
            notificationEntryManager.addNotificationEntryListener(this.mNotificationEntryListener);
            notificationGroupManagerLegacy.registerGroupChangeListener(this.mOnGroupChangeListener);
            return;
        }
        throw new IllegalStateException("Already bound.");
    }

    public void setHeadsUpManager(HeadsUpManager headsUpManager) {
        this.mHeadsUpManager = headsUpManager;
    }

    public void onDozingChanged(boolean z) {
        if (this.mIsDozing != z) {
            for (GroupAlertEntry next : this.mGroupAlertEntries.values()) {
                next.mLastAlertTransferTime = 0;
                next.mAlertSummaryOnNextAddition = false;
            }
        }
        this.mIsDozing = z;
    }

    public final void onGroupChanged(NotificationGroupManagerLegacy.NotificationGroup notificationGroup, NotificationEntry notificationEntry) {
        NotificationEntry notificationEntry2 = notificationGroup.summary;
        if (notificationEntry2 == null) {
            if (DEBUG) {
                Log.d("NotifGroupAlertTransfer", "onGroupChanged: summary is null");
            }
        } else if (notificationGroup.suppressed || notificationGroup.alertOverride != null) {
            checkForForwardAlertTransfer(notificationEntry2, notificationEntry);
        } else {
            if (DEBUG) {
                Log.d("NotifGroupAlertTransfer", "onGroupChanged: maybe transfer back");
            }
            GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationGroup.summary.getSbn()));
            if (groupAlertEntry.mAlertSummaryOnNextAddition) {
                if (!this.mHeadsUpManager.isAlerting(notificationGroup.summary.getKey())) {
                    alertNotificationWhenPossible(notificationGroup.summary);
                }
                groupAlertEntry.mAlertSummaryOnNextAddition = false;
                return;
            }
            checkShouldTransferBack(groupAlertEntry);
        }
    }

    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        if (DEBUG) {
            Log.d("NotifGroupAlertTransfer", "!! onHeadsUpStateChanged: entry=" + NotificationUtils.logKey((ListEntry) notificationEntry) + " isHeadsUp=" + z);
        }
        if (z && notificationEntry.getSbn().getNotification().isGroupSummary()) {
            checkForForwardAlertTransfer(notificationEntry, (NotificationEntry) null);
        }
    }

    public final void checkForForwardAlertTransfer(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        boolean z = DEBUG;
        if (z) {
            Log.d("NotifGroupAlertTransfer", "checkForForwardAlertTransfer: enter");
        }
        NotificationGroupManagerLegacy.NotificationGroup groupForSummary = this.mGroupManager.getGroupForSummary(notificationEntry.getSbn());
        if (groupForSummary != null && groupForSummary.alertOverride != null) {
            handleOverriddenSummaryAlerted(notificationEntry);
        } else if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.getSbn())) {
            handleSuppressedSummaryAlerted(notificationEntry, notificationEntry2);
        }
        if (z) {
            Log.d("NotifGroupAlertTransfer", "checkForForwardAlertTransfer: done");
        }
    }

    public final int getPendingChildrenNotAlerting(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationEntryManager notificationEntryManager = this.mEntryManager;
        int i = 0;
        if (notificationEntryManager == null) {
            return 0;
        }
        for (NotificationEntry next : notificationEntryManager.getPendingNotificationsIterator()) {
            if (isPendingNotificationInGroup(next, notificationGroup) && onlySummaryAlerts(next)) {
                i++;
            }
        }
        return i;
    }

    public final boolean pendingInflationsWillAddChildren(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        NotificationEntryManager notificationEntryManager = this.mEntryManager;
        if (notificationEntryManager == null) {
            return false;
        }
        for (NotificationEntry isPendingNotificationInGroup : notificationEntryManager.getPendingNotificationsIterator()) {
            if (isPendingNotificationInGroup(isPendingNotificationInGroup, notificationGroup)) {
                return true;
            }
        }
        return false;
    }

    public final boolean isPendingNotificationInGroup(NotificationEntry notificationEntry, NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
        return this.mGroupManager.isGroupChild(notificationEntry.getSbn()) && Objects.equals(this.mGroupManager.getGroupKey(notificationEntry.getSbn()), this.mGroupManager.getGroupKey(notificationGroup.summary.getSbn())) && !notificationGroup.children.containsKey(notificationEntry.getKey());
    }

    public final void handleSuppressedSummaryAlerted(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        boolean z = DEBUG;
        if (z) {
            Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: summary=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
        if (this.mGroupManager.isSummaryOfSuppressedGroup(notificationEntry.getSbn()) && groupAlertEntry != null) {
            boolean isAlerting = this.mHeadsUpManager.isAlerting(notificationEntry.getKey());
            boolean z2 = notificationEntry2 != null && this.mHeadsUpManager.isAlerting(notificationEntry2.getKey());
            if (isAlerting || z2) {
                if (!pendingInflationsWillAddChildren(groupAlertEntry.mGroup)) {
                    NotificationEntry next = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn()).iterator().next();
                    if (isAlerting) {
                        if (z) {
                            Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: transfer summary -> child");
                        }
                        tryTransferAlertState(notificationEntry, notificationEntry, next, groupAlertEntry);
                    } else if (canStillTransferBack(groupAlertEntry)) {
                        if (z) {
                            Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: transfer override -> child");
                        }
                        tryTransferAlertState(notificationEntry, notificationEntry2, next, groupAlertEntry);
                    } else if (z) {
                        Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: transfer from override: too late");
                    }
                } else if (z) {
                    Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: pending inflations");
                }
            } else if (z) {
                Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: no summary or override alerting");
            }
        } else if (z) {
            Log.d("NotifGroupAlertTransfer", "handleSuppressedSummaryAlerted: invalid state");
        }
    }

    public final void handleOverriddenSummaryAlerted(NotificationEntry notificationEntry) {
        boolean z = DEBUG;
        if (z) {
            Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: summary=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        GroupAlertEntry groupAlertEntry = this.mGroupAlertEntries.get(this.mGroupManager.getGroupKey(notificationEntry.getSbn()));
        NotificationGroupManagerLegacy.NotificationGroup groupForSummary = this.mGroupManager.getGroupForSummary(notificationEntry.getSbn());
        if (groupForSummary == null || groupForSummary.alertOverride == null || groupAlertEntry == null) {
            if (z) {
                Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: invalid state");
            }
        } else if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            if (z) {
                Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: transfer summary -> override");
            }
            tryTransferAlertState(notificationEntry, notificationEntry, groupForSummary.alertOverride, groupAlertEntry);
        } else if (canStillTransferBack(groupAlertEntry)) {
            ArrayList<NotificationEntry> logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn());
            if (logicalChildren != null) {
                logicalChildren.remove(groupForSummary.alertOverride);
                if (releaseChildAlerts(logicalChildren)) {
                    if (z) {
                        Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: transfer child -> override");
                    }
                    tryTransferAlertState(notificationEntry, (NotificationEntry) null, groupForSummary.alertOverride, groupAlertEntry);
                } else if (z) {
                    Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: no child alert released");
                }
            } else if (z) {
                Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: no children");
            }
        } else if (z) {
            Log.d("NotifGroupAlertTransfer", "handleOverriddenSummaryAlerted: transfer from child: too late");
        }
    }

    public final void tryTransferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2, NotificationEntry notificationEntry3, GroupAlertEntry groupAlertEntry) {
        if (notificationEntry3 != null && !notificationEntry3.getRow().keepInParent() && !notificationEntry3.isRowRemoved() && !notificationEntry3.isRowDismissed()) {
            if (!this.mHeadsUpManager.isAlerting(notificationEntry3.getKey()) && onlySummaryAlerts(notificationEntry)) {
                groupAlertEntry.mLastAlertTransferTime = SystemClock.elapsedRealtime();
            }
            if (DEBUG) {
                Log.d("NotifGroupAlertTransfer", "transferAlertState: fromEntry=" + NotificationUtils.logKey((ListEntry) notificationEntry2) + " toEntry=" + NotificationUtils.logKey((ListEntry) notificationEntry3));
            }
            transferAlertState(notificationEntry2, notificationEntry3);
        }
    }

    public final void transferAlertState(NotificationEntry notificationEntry, NotificationEntry notificationEntry2) {
        if (notificationEntry != null) {
            this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
        }
        alertNotificationWhenPossible(notificationEntry2);
    }

    public final void checkShouldTransferBack(GroupAlertEntry groupAlertEntry) {
        if (canStillTransferBack(groupAlertEntry)) {
            NotificationEntry notificationEntry = groupAlertEntry.mGroup.summary;
            if (onlySummaryAlerts(notificationEntry)) {
                ArrayList<NotificationEntry> logicalChildren = this.mGroupManager.getLogicalChildren(notificationEntry.getSbn());
                int size = logicalChildren.size();
                if (getPendingChildrenNotAlerting(groupAlertEntry.mGroup) + size > 1 && releaseChildAlerts(logicalChildren) && !this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                    if (size > 1) {
                        alertNotificationWhenPossible(notificationEntry);
                    } else {
                        groupAlertEntry.mAlertSummaryOnNextAddition = true;
                    }
                    groupAlertEntry.mLastAlertTransferTime = 0;
                }
            }
        }
    }

    public final boolean canStillTransferBack(GroupAlertEntry groupAlertEntry) {
        return SystemClock.elapsedRealtime() - groupAlertEntry.mLastAlertTransferTime < 300;
    }

    public final boolean releaseChildAlerts(List<NotificationEntry> list) {
        if (SPEW) {
            Log.d("NotifGroupAlertTransfer", "releaseChildAlerts: numChildren=" + list.size());
        }
        boolean z = false;
        for (int i = 0; i < list.size(); i++) {
            NotificationEntry notificationEntry = list.get(i);
            if (SPEW) {
                Log.d("NotifGroupAlertTransfer", "releaseChildAlerts: checking i=" + i + " entry=" + notificationEntry + " onlySummaryAlerts=" + onlySummaryAlerts(notificationEntry) + " isAlerting=" + this.mHeadsUpManager.isAlerting(notificationEntry.getKey()) + " isPendingAlert=" + this.mPendingAlerts.containsKey(notificationEntry.getKey()));
            }
            if (onlySummaryAlerts(notificationEntry) && this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
                this.mHeadsUpManager.removeNotification(notificationEntry.getKey(), true);
                z = true;
            }
            if (this.mPendingAlerts.containsKey(notificationEntry.getKey())) {
                this.mPendingAlerts.get(notificationEntry.getKey()).mAbortOnInflation = true;
                z = true;
            }
        }
        if (SPEW) {
            Log.d("NotifGroupAlertTransfer", "releaseChildAlerts: didRelease=" + z);
        }
        return z;
    }

    public final void alertNotificationWhenPossible(NotificationEntry notificationEntry) {
        int contentFlag = this.mHeadsUpManager.getContentFlag();
        RowContentBindParams rowContentBindParams = (RowContentBindParams) this.mRowContentBindStage.getStageParams(notificationEntry);
        if ((rowContentBindParams.getContentViews() & contentFlag) == 0) {
            if (DEBUG) {
                Log.d("NotifGroupAlertTransfer", "alertNotificationWhenPossible: async requestRebind entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            this.mPendingAlerts.put(notificationEntry.getKey(), new PendingAlertInfo(notificationEntry));
            rowContentBindParams.requireContentViews(contentFlag);
            this.mRowContentBindStage.requestRebind(notificationEntry, new NotificationGroupAlertTransferHelper$$ExternalSyntheticLambda0(this, notificationEntry, contentFlag));
        } else if (this.mHeadsUpManager.isAlerting(notificationEntry.getKey())) {
            if (DEBUG) {
                Log.d("NotifGroupAlertTransfer", "alertNotificationWhenPossible: continue alerting entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            this.mHeadsUpManager.updateNotification(notificationEntry.getKey(), true);
        } else {
            if (DEBUG) {
                Log.d("NotifGroupAlertTransfer", "alertNotificationWhenPossible: start alerting entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
            }
            this.mHeadsUpManager.showNotification(notificationEntry);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$alertNotificationWhenPossible$0(NotificationEntry notificationEntry, int i, NotificationEntry notificationEntry2) {
        PendingAlertInfo remove = this.mPendingAlerts.remove(notificationEntry.getKey());
        if (remove == null) {
            return;
        }
        if (remove.isStillValid()) {
            alertNotificationWhenPossible(notificationEntry);
            return;
        }
        if (DEBUG) {
            Log.d("NotifGroupAlertTransfer", "alertNotificationWhenPossible: markContentViewsFreeable entry=" + NotificationUtils.logKey((ListEntry) notificationEntry));
        }
        ((RowContentBindParams) this.mRowContentBindStage.getStageParams(notificationEntry)).markContentViewsFreeable(i);
        this.mRowContentBindStage.requestRebind(notificationEntry, (NotifBindPipeline.BindCallback) null);
    }

    public final boolean onlySummaryAlerts(NotificationEntry notificationEntry) {
        return notificationEntry.getSbn().getNotification().getGroupAlertBehavior() == 1;
    }

    public class PendingAlertInfo {
        public boolean mAbortOnInflation;
        public final NotificationEntry mEntry;
        public final StatusBarNotification mOriginalNotification;

        public PendingAlertInfo(NotificationEntry notificationEntry) {
            this.mOriginalNotification = notificationEntry.getSbn();
            this.mEntry = notificationEntry;
        }

        public final boolean isStillValid() {
            if (!this.mAbortOnInflation && this.mEntry.getSbn().getGroupKey().equals(this.mOriginalNotification.getGroupKey()) && this.mEntry.getSbn().getNotification().isGroupSummary() == this.mOriginalNotification.getNotification().isGroupSummary()) {
                return true;
            }
            return false;
        }
    }

    public static class GroupAlertEntry {
        public boolean mAlertSummaryOnNextAddition;
        public final NotificationGroupManagerLegacy.NotificationGroup mGroup;
        public long mLastAlertTransferTime;

        public GroupAlertEntry(NotificationGroupManagerLegacy.NotificationGroup notificationGroup) {
            this.mGroup = notificationGroup;
        }
    }
}

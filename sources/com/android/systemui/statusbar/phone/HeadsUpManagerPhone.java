package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.Region;
import android.util.Pools;
import androidx.collection.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.AlertingNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.OnReorderingAllowedListener;
import com.android.systemui.statusbar.notification.collection.provider.VisualStabilityProvider;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class HeadsUpManagerPhone extends HeadsUpManager implements Dumpable, OnHeadsUpChangedListener {
    public AnimationStateHandler mAnimationStateHandler;
    public final KeyguardBypassController mBypassController;
    public final HashSet<NotificationEntry> mEntriesToRemoveAfterExpand = new HashSet<>();
    public final ArraySet<NotificationEntry> mEntriesToRemoveWhenReorderingAllowed = new ArraySet<>();
    public final Pools.Pool<HeadsUpEntryPhone> mEntryPool = new Pools.Pool<HeadsUpEntryPhone>() {
        public Stack<HeadsUpEntryPhone> mPoolObjects = new Stack<>();

        public HeadsUpEntryPhone acquire() {
            if (!this.mPoolObjects.isEmpty()) {
                return this.mPoolObjects.pop();
            }
            return new HeadsUpEntryPhone();
        }

        public boolean release(HeadsUpEntryPhone headsUpEntryPhone) {
            this.mPoolObjects.push(headsUpEntryPhone);
            return true;
        }
    };
    @VisibleForTesting
    public final int mExtensionTime;
    public final GroupMembershipManager mGroupMembershipManager;
    public boolean mHeadsUpGoingAway;
    public int mHeadsUpInset;
    public final List<OnHeadsUpPhoneListenerChange> mHeadsUpPhoneListeners = new ArrayList();
    public boolean mIsExpanded;
    public final OnReorderingAllowedListener mOnReorderingAllowedListener = new HeadsUpManagerPhone$$ExternalSyntheticLambda0(this);
    public boolean mReleaseOnExpandFinish;
    public int mStatusBarState;
    public final StatusBarStateController.StateListener mStatusBarStateListener;
    public final HashSet<String> mSwipedOutKeys = new HashSet<>();
    public final Region mTouchableRegion = new Region();
    public boolean mTrackingHeadsUp;
    public final VisualStabilityProvider mVisualStabilityProvider;

    public interface AnimationStateHandler {
        void setHeadsUpGoingAwayAnimationsAllowed(boolean z);
    }

    public interface OnHeadsUpPhoneListenerChange {
        void onHeadsUpGoingAwayStateChanged(boolean z);
    }

    public HeadsUpManagerPhone(Context context, HeadsUpManagerLogger headsUpManagerLogger, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, VisualStabilityProvider visualStabilityProvider, ConfigurationController configurationController) {
        super(context, headsUpManagerLogger);
        AnonymousClass3 r2 = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                boolean z = false;
                boolean z2 = HeadsUpManagerPhone.this.mStatusBarState == 1;
                if (i == 1) {
                    z = true;
                }
                HeadsUpManagerPhone.this.mStatusBarState = i;
                if (z2 && !z && HeadsUpManagerPhone.this.mBypassController.getBypassEnabled()) {
                    ArrayList arrayList = new ArrayList();
                    for (AlertingNotificationManager.AlertEntry alertEntry : HeadsUpManagerPhone.this.mAlertEntries.values()) {
                        NotificationEntry notificationEntry = alertEntry.mEntry;
                        if (notificationEntry != null && notificationEntry.isBubble() && !alertEntry.isSticky()) {
                            arrayList.add(alertEntry.mEntry.getKey());
                        }
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        HeadsUpManagerPhone.this.removeAlertEntry((String) it.next());
                    }
                }
            }

            public void onDozingChanged(boolean z) {
                if (!z) {
                    for (AlertingNotificationManager.AlertEntry updateEntry : HeadsUpManagerPhone.this.mAlertEntries.values()) {
                        updateEntry.updateEntry(true);
                    }
                }
            }
        };
        this.mStatusBarStateListener = r2;
        this.mExtensionTime = this.mContext.getResources().getInteger(R$integer.ambient_notification_extension_time);
        statusBarStateController.addCallback(r2);
        this.mBypassController = keyguardBypassController;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mVisualStabilityProvider = visualStabilityProvider;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() {
            public void onDensityOrFontScaleChanged() {
                HeadsUpManagerPhone.this.updateResources();
            }

            public void onThemeChanged() {
                HeadsUpManagerPhone.this.updateResources();
            }
        });
    }

    public void setAnimationStateHandler(AnimationStateHandler animationStateHandler) {
        this.mAnimationStateHandler = animationStateHandler;
    }

    public final void updateResources() {
        this.mHeadsUpInset = SystemBarUtils.getStatusBarHeight(this.mContext) + this.mContext.getResources().getDimensionPixelSize(R$dimen.heads_up_status_bar_padding);
    }

    public void addHeadsUpPhoneListener(OnHeadsUpPhoneListenerChange onHeadsUpPhoneListenerChange) {
        this.mHeadsUpPhoneListeners.add(onHeadsUpPhoneListenerChange);
    }

    public Region getTouchableRegion() {
        NotificationEntry groupSummary;
        NotificationEntry topEntry = getTopEntry();
        if (!hasPinnedHeadsUp() || topEntry == null) {
            return null;
        }
        if (topEntry.isChildInGroup() && (groupSummary = this.mGroupMembershipManager.getGroupSummary(topEntry)) != null) {
            topEntry = groupSummary;
        }
        ExpandableNotificationRow row = topEntry.getRow();
        int[] iArr = new int[2];
        row.getLocationOnScreen(iArr);
        int i = iArr[0];
        int intrinsicHeight = row.getIntrinsicHeight();
        this.mTouchableRegion.set(i, 0, row.getWidth() + i, this.mHeadsUpInset + intrinsicHeight);
        return this.mTouchableRegion;
    }

    public boolean shouldSwallowClick(String str) {
        HeadsUpManager.HeadsUpEntry headsUpEntry = getHeadsUpEntry(str);
        return headsUpEntry != null && this.mClock.currentTimeMillis() < headsUpEntry.mPostTime;
    }

    public void onExpandingFinished() {
        if (this.mReleaseOnExpandFinish) {
            releaseAllImmediately();
            this.mReleaseOnExpandFinish = false;
        } else {
            Iterator<NotificationEntry> it = this.mEntriesToRemoveAfterExpand.iterator();
            while (it.hasNext()) {
                NotificationEntry next = it.next();
                if (isAlerting(next.getKey())) {
                    removeAlertEntry(next.getKey());
                }
            }
        }
        this.mEntriesToRemoveAfterExpand.clear();
    }

    public void setTrackingHeadsUp(boolean z) {
        this.mTrackingHeadsUp = z;
    }

    public void setIsPanelExpanded(boolean z) {
        if (z != this.mIsExpanded) {
            this.mIsExpanded = z;
            if (z) {
                this.mHeadsUpGoingAway = false;
            }
        }
    }

    public void setHeadsUpGoingAway(boolean z) {
        if (z != this.mHeadsUpGoingAway) {
            this.mHeadsUpGoingAway = z;
            for (OnHeadsUpPhoneListenerChange onHeadsUpGoingAwayStateChanged : this.mHeadsUpPhoneListeners) {
                onHeadsUpGoingAwayStateChanged.onHeadsUpGoingAwayStateChanged(z);
            }
        }
    }

    public boolean isHeadsUpGoingAway() {
        return this.mHeadsUpGoingAway;
    }

    public void setRemoteInputActive(NotificationEntry notificationEntry, boolean z) {
        HeadsUpEntryPhone headsUpEntryPhone = getHeadsUpEntryPhone(notificationEntry.getKey());
        if (headsUpEntryPhone != null && headsUpEntryPhone.remoteInputActive != z) {
            headsUpEntryPhone.remoteInputActive = z;
            if (z) {
                headsUpEntryPhone.removeAutoRemovalCallbacks();
            } else {
                headsUpEntryPhone.updateEntry(false);
            }
        }
    }

    public void setMenuShown(NotificationEntry notificationEntry, boolean z) {
        HeadsUpManager.HeadsUpEntry headsUpEntry = getHeadsUpEntry(notificationEntry.getKey());
        if ((headsUpEntry instanceof HeadsUpEntryPhone) && notificationEntry.isRowPinned()) {
            ((HeadsUpEntryPhone) headsUpEntry).setMenuShownPinned(z);
        }
    }

    public void extendHeadsUp() {
        HeadsUpEntryPhone topHeadsUpEntryPhone = getTopHeadsUpEntryPhone();
        if (topHeadsUpEntryPhone != null) {
            topHeadsUpEntryPhone.extendPulse();
        }
    }

    public boolean isTrackingHeadsUp() {
        return this.mTrackingHeadsUp;
    }

    public void snooze() {
        super.snooze();
        this.mReleaseOnExpandFinish = true;
    }

    public void addSwipedOutNotification(String str) {
        this.mSwipedOutKeys.add(str);
    }

    public boolean removeNotification(String str, boolean z, boolean z2) {
        if (z2) {
            return removeNotification(str, z);
        }
        this.mAnimationStateHandler.setHeadsUpGoingAwayAnimationsAllowed(false);
        boolean removeNotification = removeNotification(str, z);
        this.mAnimationStateHandler.setHeadsUpGoingAwayAnimationsAllowed(true);
        return removeNotification;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("HeadsUpManagerPhone state:");
        dumpInternal(printWriter, strArr);
    }

    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return this.mVisualStabilityProvider.isReorderingAllowed() && super.shouldExtendLifetime(notificationEntry);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mAnimationStateHandler.setHeadsUpGoingAwayAnimationsAllowed(false);
        Iterator<NotificationEntry> it = this.mEntriesToRemoveWhenReorderingAllowed.iterator();
        while (it.hasNext()) {
            NotificationEntry next = it.next();
            if (isAlerting(next.getKey())) {
                removeAlertEntry(next.getKey());
            }
        }
        this.mEntriesToRemoveWhenReorderingAllowed.clear();
        this.mAnimationStateHandler.setHeadsUpGoingAwayAnimationsAllowed(true);
    }

    public HeadsUpManager.HeadsUpEntry createAlertEntry() {
        return (HeadsUpManager.HeadsUpEntry) this.mEntryPool.acquire();
    }

    public void onAlertEntryRemoved(AlertingNotificationManager.AlertEntry alertEntry) {
        super.onAlertEntryRemoved(alertEntry);
        this.mEntryPool.release((HeadsUpEntryPhone) alertEntry);
    }

    public boolean shouldHeadsUpBecomePinned(NotificationEntry notificationEntry) {
        boolean z = this.mStatusBarState == 0 && !this.mIsExpanded;
        if (this.mBypassController.getBypassEnabled()) {
            z |= this.mStatusBarState == 1;
        }
        if (z || super.shouldHeadsUpBecomePinned(notificationEntry)) {
            return true;
        }
        return false;
    }

    public void dumpInternal(PrintWriter printWriter, String[] strArr) {
        super.dumpInternal(printWriter, strArr);
        printWriter.print("  mBarState=");
        printWriter.println(this.mStatusBarState);
        printWriter.print("  mTouchableRegion=");
        printWriter.println(this.mTouchableRegion);
    }

    public final HeadsUpEntryPhone getHeadsUpEntryPhone(String str) {
        return (HeadsUpEntryPhone) this.mAlertEntries.get(str);
    }

    public final HeadsUpEntryPhone getTopHeadsUpEntryPhone() {
        return (HeadsUpEntryPhone) getTopHeadsUpEntry();
    }

    public boolean canRemoveImmediately(String str) {
        if (this.mSwipedOutKeys.contains(str)) {
            this.mSwipedOutKeys.remove(str);
            return true;
        }
        HeadsUpEntryPhone headsUpEntryPhone = getHeadsUpEntryPhone(str);
        HeadsUpEntryPhone topHeadsUpEntryPhone = getTopHeadsUpEntryPhone();
        if (headsUpEntryPhone == null || headsUpEntryPhone != topHeadsUpEntryPhone || super.canRemoveImmediately(str)) {
            return true;
        }
        return false;
    }

    public class HeadsUpEntryPhone extends HeadsUpManager.HeadsUpEntry {
        public boolean extended;
        public boolean mMenuShownPinned;

        public HeadsUpEntryPhone() {
            super();
        }

        public boolean isSticky() {
            return super.isSticky() || this.mMenuShownPinned;
        }

        public void setEntry(NotificationEntry notificationEntry) {
            setEntry(notificationEntry, new HeadsUpManagerPhone$HeadsUpEntryPhone$$ExternalSyntheticLambda0(this, notificationEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setEntry$0(NotificationEntry notificationEntry) {
            if (!HeadsUpManagerPhone.this.mVisualStabilityProvider.isReorderingAllowed() && !notificationEntry.showingPulsing()) {
                HeadsUpManagerPhone.this.mEntriesToRemoveWhenReorderingAllowed.add(notificationEntry);
                HeadsUpManagerPhone.this.mVisualStabilityProvider.addTemporaryReorderingAllowedListener(HeadsUpManagerPhone.this.mOnReorderingAllowedListener);
            } else if (HeadsUpManagerPhone.this.mTrackingHeadsUp) {
                HeadsUpManagerPhone.this.mEntriesToRemoveAfterExpand.add(notificationEntry);
            } else {
                HeadsUpManagerPhone.this.removeAlertEntry(notificationEntry.getKey());
            }
        }

        public void updateEntry(boolean z) {
            super.updateEntry(z);
            if (HeadsUpManagerPhone.this.mEntriesToRemoveAfterExpand.contains(this.mEntry)) {
                HeadsUpManagerPhone.this.mEntriesToRemoveAfterExpand.remove(this.mEntry);
            }
            if (HeadsUpManagerPhone.this.mEntriesToRemoveWhenReorderingAllowed.contains(this.mEntry)) {
                HeadsUpManagerPhone.this.mEntriesToRemoveWhenReorderingAllowed.remove(this.mEntry);
            }
        }

        public void setExpanded(boolean z) {
            if (this.expanded != z) {
                this.expanded = z;
                if (z) {
                    removeAutoRemovalCallbacks();
                } else {
                    updateEntry(false);
                }
            }
        }

        public void setMenuShownPinned(boolean z) {
            if (this.mMenuShownPinned != z) {
                this.mMenuShownPinned = z;
                if (z) {
                    removeAutoRemovalCallbacks();
                } else {
                    updateEntry(false);
                }
            }
        }

        public void reset() {
            super.reset();
            this.mMenuShownPinned = false;
            this.extended = false;
        }

        public final void extendPulse() {
            if (!this.extended) {
                this.extended = true;
                updateEntry(false);
            }
        }

        public long calculateFinishTime() {
            return super.calculateFinishTime() + ((long) (this.extended ? HeadsUpManagerPhone.this.mExtensionTime : 0));
        }
    }
}

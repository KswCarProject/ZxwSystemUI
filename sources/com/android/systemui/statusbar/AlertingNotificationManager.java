package com.android.systemui.statusbar;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.HeadsUpManagerLogger;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class AlertingNotificationManager implements NotificationLifetimeExtender {
    public final ArrayMap<String, AlertEntry> mAlertEntries = new ArrayMap<>();
    public int mAutoDismissNotificationDecay;
    public final Clock mClock = new Clock();
    public final ArraySet<NotificationEntry> mExtendedLifetimeAlertEntries = new ArraySet<>();
    @VisibleForTesting
    public Handler mHandler = new Handler(Looper.getMainLooper());
    public final HeadsUpManagerLogger mLogger;
    public int mMinimumDisplayTime;
    public NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;

    public abstract void onAlertEntryAdded(AlertEntry alertEntry);

    public abstract void onAlertEntryRemoved(AlertEntry alertEntry);

    public AlertingNotificationManager(HeadsUpManagerLogger headsUpManagerLogger) {
        this.mLogger = headsUpManagerLogger;
    }

    public void showNotification(NotificationEntry notificationEntry) {
        this.mLogger.logShowNotification(notificationEntry.getKey());
        addAlertEntry(notificationEntry);
        updateNotification(notificationEntry.getKey(), true);
        notificationEntry.setInterruption();
    }

    public boolean removeNotification(String str, boolean z) {
        this.mLogger.logRemoveNotification(str, z);
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry == null) {
            return true;
        }
        if (z || canRemoveImmediately(str)) {
            removeAlertEntry(str);
            return true;
        }
        alertEntry.removeAsSoonAsPossible();
        return false;
    }

    public void updateNotification(String str, boolean z) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        this.mLogger.logUpdateNotification(str, z, alertEntry != null);
        if (alertEntry != null) {
            alertEntry.mEntry.sendAccessibilityEvent(2048);
            if (z) {
                alertEntry.updateEntry(true);
            }
        }
    }

    public void releaseAllImmediately() {
        this.mLogger.logReleaseAllImmediately();
        Iterator it = new ArraySet(this.mAlertEntries.keySet()).iterator();
        while (it.hasNext()) {
            removeAlertEntry((String) it.next());
        }
    }

    public Stream<NotificationEntry> getAllEntries() {
        return this.mAlertEntries.values().stream().map(new AlertingNotificationManager$$ExternalSyntheticLambda0());
    }

    public boolean hasNotifications() {
        return !this.mAlertEntries.isEmpty();
    }

    public boolean isAlerting(String str) {
        return this.mAlertEntries.containsKey(str);
    }

    public final void addAlertEntry(NotificationEntry notificationEntry) {
        AlertEntry createAlertEntry = createAlertEntry();
        createAlertEntry.setEntry(notificationEntry);
        this.mAlertEntries.put(notificationEntry.getKey(), createAlertEntry);
        onAlertEntryAdded(createAlertEntry);
        notificationEntry.sendAccessibilityEvent(2048);
        notificationEntry.setIsAlerting(true);
    }

    public final void removeAlertEntry(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            NotificationEntry notificationEntry = alertEntry.mEntry;
            if (notificationEntry == null || !notificationEntry.isExpandAnimationRunning()) {
                this.mAlertEntries.remove(str);
                onAlertEntryRemoved(alertEntry);
                notificationEntry.sendAccessibilityEvent(2048);
                alertEntry.reset();
                if (this.mExtendedLifetimeAlertEntries.contains(notificationEntry)) {
                    NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
                    if (notificationSafeToRemoveCallback != null) {
                        notificationSafeToRemoveCallback.onSafeToRemove(str);
                    }
                    this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
                }
            }
        }
    }

    public AlertEntry createAlertEntry() {
        return new AlertEntry();
    }

    public boolean canRemoveImmediately(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        return alertEntry == null || alertEntry.wasShownLongEnough() || alertEntry.mEntry.isRowDismissed();
    }

    public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
        this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
    }

    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return !canRemoveImmediately(notificationEntry.getKey());
    }

    public boolean isSticky(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            return alertEntry.isSticky();
        }
        return false;
    }

    public long getEarliestRemovalTime(String str) {
        AlertEntry alertEntry = this.mAlertEntries.get(str);
        if (alertEntry != null) {
            return Math.max(0, alertEntry.mEarliestRemovaltime - this.mClock.currentTimeMillis());
        }
        return 0;
    }

    public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mExtendedLifetimeAlertEntries.add(notificationEntry);
            this.mAlertEntries.get(notificationEntry.getKey()).removeAsSoonAsPossible();
            return;
        }
        this.mExtendedLifetimeAlertEntries.remove(notificationEntry);
    }

    public class AlertEntry implements Comparable<AlertEntry> {
        public long mEarliestRemovaltime;
        public NotificationEntry mEntry;
        public long mPostTime;
        public Runnable mRemoveAlertRunnable;

        public boolean isSticky() {
            return false;
        }

        public AlertEntry() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setEntry$0(NotificationEntry notificationEntry) {
            AlertingNotificationManager.this.removeAlertEntry(notificationEntry.getKey());
        }

        public void setEntry(NotificationEntry notificationEntry) {
            setEntry(notificationEntry, new AlertingNotificationManager$AlertEntry$$ExternalSyntheticLambda0(this, notificationEntry));
        }

        public void setEntry(NotificationEntry notificationEntry, Runnable runnable) {
            this.mEntry = notificationEntry;
            this.mRemoveAlertRunnable = runnable;
            this.mPostTime = calculatePostTime();
            updateEntry(true);
        }

        public void updateEntry(boolean z) {
            AlertingNotificationManager.this.mLogger.logUpdateEntry(this.mEntry.getKey(), z);
            long currentTimeMillis = AlertingNotificationManager.this.mClock.currentTimeMillis();
            this.mEarliestRemovaltime = ((long) AlertingNotificationManager.this.mMinimumDisplayTime) + currentTimeMillis;
            if (z) {
                this.mPostTime = Math.max(this.mPostTime, currentTimeMillis);
            }
            removeAutoRemovalCallbacks();
            if (!isSticky()) {
                AlertingNotificationManager.this.mHandler.postDelayed(this.mRemoveAlertRunnable, Math.max(calculateFinishTime() - currentTimeMillis, (long) AlertingNotificationManager.this.mMinimumDisplayTime));
            }
        }

        public boolean wasShownLongEnough() {
            return this.mEarliestRemovaltime < AlertingNotificationManager.this.mClock.currentTimeMillis();
        }

        public int compareTo(AlertEntry alertEntry) {
            long j = this.mPostTime;
            long j2 = alertEntry.mPostTime;
            if (j < j2) {
                return 1;
            }
            if (j == j2) {
                return this.mEntry.getKey().compareTo(alertEntry.mEntry.getKey());
            }
            return -1;
        }

        public void reset() {
            this.mEntry = null;
            removeAutoRemovalCallbacks();
            this.mRemoveAlertRunnable = null;
        }

        public void removeAutoRemovalCallbacks() {
            Runnable runnable = this.mRemoveAlertRunnable;
            if (runnable != null) {
                AlertingNotificationManager.this.mHandler.removeCallbacks(runnable);
            }
        }

        public void removeAsSoonAsPossible() {
            if (this.mRemoveAlertRunnable != null) {
                removeAutoRemovalCallbacks();
                AlertingNotificationManager alertingNotificationManager = AlertingNotificationManager.this;
                alertingNotificationManager.mHandler.postDelayed(this.mRemoveAlertRunnable, this.mEarliestRemovaltime - alertingNotificationManager.mClock.currentTimeMillis());
            }
        }

        public long calculatePostTime() {
            return AlertingNotificationManager.this.mClock.currentTimeMillis();
        }

        public long calculateFinishTime() {
            return this.mPostTime + ((long) AlertingNotificationManager.this.mAutoDismissNotificationDecay);
        }
    }

    public static final class Clock {
        public long currentTimeMillis() {
            return SystemClock.elapsedRealtime();
        }
    }
}

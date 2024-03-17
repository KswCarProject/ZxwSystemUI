package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.smartspace.SmartspaceTarget;
import android.os.Parcelable;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/* compiled from: SmartspaceDedupingCoordinator.kt */
public final class SmartspaceDedupingCoordinator implements Coordinator {
    @NotNull
    public final SystemClock clock;
    @NotNull
    public final SmartspaceDedupingCoordinator$collectionListener$1 collectionListener = new SmartspaceDedupingCoordinator$collectionListener$1(this);
    @NotNull
    public final DelayableExecutor executor;
    @NotNull
    public final SmartspaceDedupingCoordinator$filter$1 filter = new SmartspaceDedupingCoordinator$filter$1(this);
    public boolean isOnLockscreen;
    @NotNull
    public final NotifPipeline notifPipeline;
    @NotNull
    public final NotificationEntryManager notificationEntryManager;
    @NotNull
    public final NotificationLockscreenUserManager notificationLockscreenUserManager;
    @NotNull
    public final LockscreenSmartspaceController smartspaceController;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    @NotNull
    public final SmartspaceDedupingCoordinator$statusBarStateListener$1 statusBarStateListener = new SmartspaceDedupingCoordinator$statusBarStateListener$1(this);
    @NotNull
    public Map<String, TrackedSmartspaceTarget> trackedSmartspaceTargets = new LinkedHashMap();

    public SmartspaceDedupingCoordinator(@NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull LockscreenSmartspaceController lockscreenSmartspaceController, @NotNull NotificationEntryManager notificationEntryManager2, @NotNull NotificationLockscreenUserManager notificationLockscreenUserManager2, @NotNull NotifPipeline notifPipeline2, @NotNull DelayableExecutor delayableExecutor, @NotNull SystemClock systemClock) {
        this.statusBarStateController = sysuiStatusBarStateController;
        this.smartspaceController = lockscreenSmartspaceController;
        this.notificationEntryManager = notificationEntryManager2;
        this.notificationLockscreenUserManager = notificationLockscreenUserManager2;
        this.notifPipeline = notifPipeline2;
        this.executor = delayableExecutor;
        this.clock = systemClock;
    }

    public void attach(@NotNull NotifPipeline notifPipeline2) {
        notifPipeline2.addPreGroupFilter(this.filter);
        notifPipeline2.addCollectionListener(this.collectionListener);
        this.statusBarStateController.addCallback(this.statusBarStateListener);
        this.smartspaceController.addListener(new SmartspaceDedupingCoordinator$attach$1(this));
        if (!notifPipeline2.isNewPipelineEnabled()) {
            this.notificationLockscreenUserManager.addKeyguardNotificationSuppressor(new SmartspaceDedupingCoordinator$attach$2(this));
        }
        recordStatusBarState(this.statusBarStateController.getState());
    }

    public final boolean isDupedWithSmartspaceContent(NotificationEntry notificationEntry) {
        TrackedSmartspaceTarget trackedSmartspaceTarget = this.trackedSmartspaceTargets.get(notificationEntry.getKey());
        if (trackedSmartspaceTarget == null) {
            return false;
        }
        return trackedSmartspaceTarget.getShouldFilter();
    }

    public final void onNewSmartspaceTargets(List<? extends Parcelable> list) {
        Runnable cancelTimeoutRunnable;
        String sourceNotificationKey;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Map<String, TrackedSmartspaceTarget> map = this.trackedSmartspaceTargets;
        Iterator<? extends Parcelable> it = list.iterator();
        boolean z = false;
        if (it.hasNext()) {
            SmartspaceTarget smartspaceTarget = (Parcelable) it.next();
            SmartspaceTarget smartspaceTarget2 = smartspaceTarget instanceof SmartspaceTarget ? smartspaceTarget : null;
            if (!(smartspaceTarget2 == null || (sourceNotificationKey = smartspaceTarget2.getSourceNotificationKey()) == null)) {
                TrackedSmartspaceTarget trackedSmartspaceTarget = map.get(sourceNotificationKey);
                if (trackedSmartspaceTarget == null) {
                    trackedSmartspaceTarget = new TrackedSmartspaceTarget(sourceNotificationKey);
                }
                TrackedSmartspaceTarget trackedSmartspaceTarget2 = trackedSmartspaceTarget;
                linkedHashMap.put(sourceNotificationKey, trackedSmartspaceTarget2);
                z = updateFilterStatus(trackedSmartspaceTarget2);
            }
        }
        for (String next : map.keySet()) {
            if (!linkedHashMap.containsKey(next)) {
                TrackedSmartspaceTarget trackedSmartspaceTarget3 = map.get(next);
                if (!(trackedSmartspaceTarget3 == null || (cancelTimeoutRunnable = trackedSmartspaceTarget3.getCancelTimeoutRunnable()) == null)) {
                    cancelTimeoutRunnable.run();
                }
                z = true;
            }
        }
        if (z) {
            this.filter.invalidateList();
            this.notificationEntryManager.updateNotifications("Smartspace targets changed");
        }
        this.trackedSmartspaceTargets = linkedHashMap;
    }

    public final boolean updateFilterStatus(TrackedSmartspaceTarget trackedSmartspaceTarget) {
        boolean shouldFilter = trackedSmartspaceTarget.getShouldFilter();
        NotificationEntry entry = this.notifPipeline.getEntry(trackedSmartspaceTarget.getKey());
        if (entry != null) {
            updateAlertException(trackedSmartspaceTarget, entry);
            trackedSmartspaceTarget.setShouldFilter(!hasRecentlyAlerted(entry));
        }
        if (trackedSmartspaceTarget.getShouldFilter() == shouldFilter || !this.isOnLockscreen) {
            return false;
        }
        return true;
    }

    public final void updateAlertException(TrackedSmartspaceTarget trackedSmartspaceTarget, NotificationEntry notificationEntry) {
        long currentTimeMillis = this.clock.currentTimeMillis();
        long lastAudiblyAlertedMillis = notificationEntry.getRanking().getLastAudiblyAlertedMillis() + SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
        if (lastAudiblyAlertedMillis != trackedSmartspaceTarget.getAlertExceptionExpires() && lastAudiblyAlertedMillis > currentTimeMillis) {
            Runnable cancelTimeoutRunnable = trackedSmartspaceTarget.getCancelTimeoutRunnable();
            if (cancelTimeoutRunnable != null) {
                cancelTimeoutRunnable.run();
            }
            trackedSmartspaceTarget.setAlertExceptionExpires(lastAudiblyAlertedMillis);
            trackedSmartspaceTarget.setCancelTimeoutRunnable(this.executor.executeDelayed(new SmartspaceDedupingCoordinator$updateAlertException$1(trackedSmartspaceTarget, this), lastAudiblyAlertedMillis - currentTimeMillis));
        }
    }

    public final void cancelExceptionTimeout(TrackedSmartspaceTarget trackedSmartspaceTarget) {
        Runnable cancelTimeoutRunnable = trackedSmartspaceTarget.getCancelTimeoutRunnable();
        if (cancelTimeoutRunnable != null) {
            cancelTimeoutRunnable.run();
        }
        trackedSmartspaceTarget.setCancelTimeoutRunnable((Runnable) null);
        trackedSmartspaceTarget.setAlertExceptionExpires(0);
    }

    public final void recordStatusBarState(int i) {
        boolean z = this.isOnLockscreen;
        boolean z2 = true;
        if (i != 1) {
            z2 = false;
        }
        this.isOnLockscreen = z2;
        if (z2 != z) {
            this.filter.invalidateList();
        }
    }

    public final boolean hasRecentlyAlerted(NotificationEntry notificationEntry) {
        return this.clock.currentTimeMillis() - notificationEntry.getRanking().getLastAudiblyAlertedMillis() <= SmartspaceDedupingCoordinatorKt.ALERT_WINDOW;
    }
}

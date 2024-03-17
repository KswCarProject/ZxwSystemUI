package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifDismissInterceptor;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubbles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BubbleCoordinator implements Coordinator {
    public final Optional<BubblesManager> mBubblesManagerOptional;
    public final Optional<Bubbles> mBubblesOptional;
    public final NotifDismissInterceptor mDismissInterceptor = new NotifDismissInterceptor() {
        public String getName() {
            return "BubbleCoordinator";
        }

        public void setCallback(NotifDismissInterceptor.OnEndDismissInterception onEndDismissInterception) {
            BubbleCoordinator.this.mOnEndDismissInterception = onEndDismissInterception;
        }

        public boolean shouldInterceptDismissal(NotificationEntry notificationEntry) {
            if (!BubbleCoordinator.this.mBubblesManagerOptional.isPresent() || !((BubblesManager) BubbleCoordinator.this.mBubblesManagerOptional.get()).handleDismissalInterception(notificationEntry)) {
                BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
                return false;
            }
            BubbleCoordinator.this.mInterceptedDismissalEntries.add(notificationEntry.getKey());
            return true;
        }

        public void cancelDismissInterception(NotificationEntry notificationEntry) {
            BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
        }
    };
    public final Set<String> mInterceptedDismissalEntries = new HashSet();
    public final BubblesManager.NotifCallback mNotifCallback = new BubblesManager.NotifCallback() {
        public void maybeCancelSummary(NotificationEntry notificationEntry) {
        }

        public void removeNotification(NotificationEntry notificationEntry, DismissedByUserStats dismissedByUserStats, int i) {
            NotificationEntry entry;
            if (!BubbleCoordinator.this.mNotifPipeline.isNewPipelineEnabled() && (entry = BubbleCoordinator.this.mNotifPipeline.getEntry(notificationEntry.getKey())) != null) {
                notificationEntry = entry;
            }
            if (BubbleCoordinator.this.isInterceptingDismissal(notificationEntry)) {
                BubbleCoordinator.this.mInterceptedDismissalEntries.remove(notificationEntry.getKey());
                BubbleCoordinator.this.mOnEndDismissInterception.onEndDismissInterception(BubbleCoordinator.this.mDismissInterceptor, notificationEntry, dismissedByUserStats);
            } else if (BubbleCoordinator.this.mNotifPipeline.getEntry(notificationEntry.getKey()) != null) {
                BubbleCoordinator.this.mNotifCollection.dismissNotification(notificationEntry, dismissedByUserStats);
            }
        }

        public void invalidateNotifications(String str) {
            BubbleCoordinator.this.mNotifFilter.invalidateList();
        }
    };
    public final NotifCollection mNotifCollection;
    public final NotifFilter mNotifFilter = new NotifFilter("BubbleCoordinator") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return BubbleCoordinator.this.mBubblesOptional.isPresent() && ((Bubbles) BubbleCoordinator.this.mBubblesOptional.get()).isBubbleNotificationSuppressedFromShade(notificationEntry.getKey(), notificationEntry.getSbn().getGroupKey());
        }
    };
    public NotifPipeline mNotifPipeline;
    public NotifDismissInterceptor.OnEndDismissInterception mOnEndDismissInterception;

    public BubbleCoordinator(Optional<BubblesManager> optional, Optional<Bubbles> optional2, NotifCollection notifCollection) {
        this.mBubblesManagerOptional = optional;
        this.mBubblesOptional = optional2;
        this.mNotifCollection = notifCollection;
    }

    public void attach(NotifPipeline notifPipeline) {
        this.mNotifPipeline = notifPipeline;
        notifPipeline.addNotificationDismissInterceptor(this.mDismissInterceptor);
        this.mNotifPipeline.addPreGroupFilter(this.mNotifFilter);
        this.mBubblesManagerOptional.ifPresent(new BubbleCoordinator$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attach$0(BubblesManager bubblesManager) {
        bubblesManager.addNotifCallback(this.mNotifCallback);
    }

    public final boolean isInterceptingDismissal(NotificationEntry notificationEntry) {
        return this.mInterceptedDismissalEntries.contains(notificationEntry.getKey());
    }
}

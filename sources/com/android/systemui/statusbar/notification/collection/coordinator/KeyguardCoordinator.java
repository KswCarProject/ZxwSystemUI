package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProvider;

public class KeyguardCoordinator implements Coordinator {
    public final HighPriorityProvider mHighPriorityProvider;
    public final KeyguardNotificationVisibilityProvider mKeyguardNotificationVisibilityProvider;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final SharedCoordinatorLogger mLogger;
    public final NotifFilter mNotifFilter = new NotifFilter("KeyguardCoordinator") {
        public boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            return KeyguardCoordinator.this.mKeyguardNotificationVisibilityProvider.shouldHideNotification(notificationEntry);
        }
    };
    public final SectionHeaderVisibilityProvider mSectionHeaderVisibilityProvider;
    public final StatusBarStateController mStatusBarStateController;

    public final void setupInvalidateNotifListCallbacks() {
    }

    public KeyguardCoordinator(StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, HighPriorityProvider highPriorityProvider, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider, KeyguardNotificationVisibilityProvider keyguardNotificationVisibilityProvider, SharedCoordinatorLogger sharedCoordinatorLogger) {
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mSectionHeaderVisibilityProvider = sectionHeaderVisibilityProvider;
        this.mKeyguardNotificationVisibilityProvider = keyguardNotificationVisibilityProvider;
        this.mLogger = sharedCoordinatorLogger;
    }

    public void attach(NotifPipeline notifPipeline) {
        setupInvalidateNotifListCallbacks();
        notifPipeline.addFinalizeFilter(this.mNotifFilter);
        this.mKeyguardNotificationVisibilityProvider.addOnStateChangedListener(new KeyguardCoordinator$$ExternalSyntheticLambda0(this));
        updateSectionHeadersVisibility();
    }

    public final void invalidateListFromFilter(String str) {
        this.mLogger.logKeyguardCoordinatorInvalidated(str);
        updateSectionHeadersVisibility();
        this.mNotifFilter.invalidateList();
    }

    public final void updateSectionHeadersVisibility() {
        boolean z = false;
        boolean z2 = this.mStatusBarStateController.getState() == 1;
        boolean neverShowSectionHeaders = this.mSectionHeaderVisibilityProvider.getNeverShowSectionHeaders();
        if (!z2 && !neverShowSectionHeaders) {
            z = true;
        }
        this.mSectionHeaderVisibilityProvider.setSectionHeadersVisible(z);
    }
}

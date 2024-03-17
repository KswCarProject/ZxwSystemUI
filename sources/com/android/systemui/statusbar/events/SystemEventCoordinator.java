package com.android.systemui.statusbar.events;

import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.time.SystemClock;
import org.jetbrains.annotations.NotNull;

/* compiled from: SystemEventCoordinator.kt */
public final class SystemEventCoordinator {
    @NotNull
    public final BatteryController batteryController;
    @NotNull
    public final SystemEventCoordinator$batteryStateListener$1 batteryStateListener = new SystemEventCoordinator$batteryStateListener$1(this);
    @NotNull
    public final PrivacyItemController privacyController;
    @NotNull
    public final SystemEventCoordinator$privacyStateListener$1 privacyStateListener = new SystemEventCoordinator$privacyStateListener$1(this);
    public SystemStatusAnimationScheduler scheduler;
    @NotNull
    public final SystemClock systemClock;

    public SystemEventCoordinator(@NotNull SystemClock systemClock2, @NotNull BatteryController batteryController2, @NotNull PrivacyItemController privacyItemController) {
        this.systemClock = systemClock2;
        this.batteryController = batteryController2;
        this.privacyController = privacyItemController;
    }

    public final void startObserving() {
        this.privacyController.addCallback((PrivacyItemController.Callback) this.privacyStateListener);
    }

    public final void stopObserving() {
        this.privacyController.removeCallback((PrivacyItemController.Callback) this.privacyStateListener);
    }

    public final void attachScheduler(@NotNull SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.scheduler = systemStatusAnimationScheduler;
    }

    public final void notifyPluggedIn() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler == null) {
            systemStatusAnimationScheduler = null;
        }
        systemStatusAnimationScheduler.onStatusEvent(new BatteryEvent());
    }

    public final void notifyPrivacyItemsEmpty() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler == null) {
            systemStatusAnimationScheduler = null;
        }
        systemStatusAnimationScheduler.setShouldShowPersistentPrivacyIndicator(false);
    }

    public final void notifyPrivacyItemsChanged(boolean z) {
        PrivacyEvent privacyEvent = new PrivacyEvent(z);
        privacyEvent.setPrivacyItems(this.privacyStateListener.getCurrentPrivacyItems());
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.scheduler;
        if (systemStatusAnimationScheduler == null) {
            systemStatusAnimationScheduler = null;
        }
        systemStatusAnimationScheduler.onStatusEvent(privacyEvent);
    }
}

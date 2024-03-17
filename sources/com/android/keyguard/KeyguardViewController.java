package com.android.keyguard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewRootImpl;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;

public interface KeyguardViewController {
    void blockPanelExpansionFromCurrentTouch();

    boolean bouncerIsOrWillBeShowing();

    void dismissAndCollapse();

    ViewRootImpl getViewRootImpl();

    void hide(long j, long j2);

    boolean isBouncerShowing();

    boolean isGoingToNotificationShade();

    boolean isShowing();

    boolean isUnlockWithWallpaper();

    void keyguardGoingAway();

    void notifyKeyguardAuthenticated(boolean z);

    void onCancelClicked();

    void onFinishedGoingToSleep() {
    }

    void onStartedGoingToSleep() {
    }

    void onStartedWakingUp() {
    }

    void registerCentralSurfaces(CentralSurfaces centralSurfaces, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, BiometricUnlockController biometricUnlockController, View view, KeyguardBypassController keyguardBypassController);

    void reset(boolean z);

    void resetAlternateAuth(boolean z);

    void setKeyguardGoingAwayState(boolean z);

    void setNeedsInput(boolean z);

    void setOccluded(boolean z, boolean z2);

    boolean shouldDisableWindowAnimationsForUnlock();

    boolean shouldSubtleWindowAnimationsForUnlock();

    void show(Bundle bundle);

    void showBouncer(boolean z);

    void startPreHideAnimation(Runnable runnable);
}

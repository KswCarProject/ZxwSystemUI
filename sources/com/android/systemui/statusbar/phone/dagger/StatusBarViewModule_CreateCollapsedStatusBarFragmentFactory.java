package com.android.systemui.statusbar.phone.dagger;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;

public final class StatusBarViewModule_CreateCollapsedStatusBarFragmentFactory implements Factory<CollapsedStatusBarFragment> {
    public static CollapsedStatusBarFragment createCollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CarrierConfigTracker carrierConfigTracker, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2, SecureSettings secureSettings, Executor executor) {
        return (CollapsedStatusBarFragment) Preconditions.checkNotNullFromProvides(StatusBarViewModule.createCollapsedStatusBarFragment(factory, ongoingCallController, systemStatusAnimationScheduler, statusBarLocationPublisher, notificationIconAreaController, panelExpansionStateManager, featureFlags, statusBarIconController, statusBarHideIconsForBouncerManager, keyguardStateController, notificationPanelViewController, networkController, statusBarStateController, commandQueue, carrierConfigTracker, collapsedStatusBarFragmentLogger, factory2, secureSettings, executor));
    }
}

package com.android.systemui.statusbar.phone.dagger;

import android.content.ContentResolver;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import com.android.keyguard.LockIconView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.biometrics.AuthRippleView;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.notification.row.dagger.NotificationShelfComponent;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragmentLogger;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.util.settings.SecureSettings;
import java.util.concurrent.Executor;

public abstract class StatusBarViewModule {
    public static NotificationShadeWindowView providesNotificationShadeWindowView(LayoutInflater layoutInflater) {
        NotificationShadeWindowView notificationShadeWindowView = (NotificationShadeWindowView) layoutInflater.inflate(R$layout.super_notification_shade, (ViewGroup) null);
        if (notificationShadeWindowView != null) {
            return notificationShadeWindowView;
        }
        throw new IllegalStateException("R.layout.super_notification_shade could not be properly inflated");
    }

    public static NotificationStackScrollLayout providesNotificationStackScrollLayout(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationStackScrollLayout) notificationShadeWindowView.findViewById(R$id.notification_stack_scroller);
    }

    public static NotificationShelf providesNotificationShelf(LayoutInflater layoutInflater, NotificationStackScrollLayout notificationStackScrollLayout) {
        NotificationShelf notificationShelf = (NotificationShelf) layoutInflater.inflate(R$layout.status_bar_notification_shelf, notificationStackScrollLayout, false);
        if (notificationShelf != null) {
            return notificationShelf;
        }
        throw new IllegalStateException("R.layout.status_bar_notification_shelf could not be properly inflated");
    }

    public static NotificationShelfController providesStatusBarWindowView(NotificationShelfComponent.Builder builder, NotificationShelf notificationShelf) {
        NotificationShelfController notificationShelfController = builder.notificationShelf(notificationShelf).build().getNotificationShelfController();
        notificationShelfController.init();
        return notificationShelfController;
    }

    public static NotificationPanelView getNotificationPanelView(NotificationShadeWindowView notificationShadeWindowView) {
        return notificationShadeWindowView.getNotificationPanelView();
    }

    public static LockIconView getLockIconView(NotificationShadeWindowView notificationShadeWindowView) {
        return (LockIconView) notificationShadeWindowView.findViewById(R$id.lock_icon_view);
    }

    public static AuthRippleView getAuthRippleView(NotificationShadeWindowView notificationShadeWindowView) {
        return (AuthRippleView) notificationShadeWindowView.findViewById(R$id.auth_ripple);
    }

    public static View getLargeScreenShadeHeaderBarView(NotificationShadeWindowView notificationShadeWindowView, FeatureFlags featureFlags) {
        int i;
        ViewStub viewStub = (ViewStub) notificationShadeWindowView.findViewById(R$id.qs_header_stub);
        if (featureFlags.isEnabled(Flags.COMBINED_QS_HEADERS)) {
            i = R$layout.combined_qs_header;
        } else {
            i = R$layout.large_screen_shade_header;
        }
        viewStub.setLayoutResource(i);
        return viewStub.inflate();
    }

    public static OngoingPrivacyChip getSplitShadeOngoingPrivacyChip(View view) {
        return (OngoingPrivacyChip) view.findViewById(R$id.privacy_chip);
    }

    public static StatusIconContainer providesStatusIconContainer(View view) {
        return (StatusIconContainer) view.findViewById(R$id.statusIcons);
    }

    public static BatteryMeterView getBatteryMeterView(View view) {
        return (BatteryMeterView) view.findViewById(R$id.batteryRemainingIcon);
    }

    public static BatteryMeterViewController getBatteryMeterViewController(BatteryMeterView batteryMeterView, ConfigurationController configurationController, TunerService tunerService, BroadcastDispatcher broadcastDispatcher, Handler handler, ContentResolver contentResolver, BatteryController batteryController) {
        return new BatteryMeterViewController(batteryMeterView, configurationController, tunerService, broadcastDispatcher, handler, contentResolver, batteryController);
    }

    public static TapAgainView getTapAgainView(NotificationPanelView notificationPanelView) {
        return notificationPanelView.getTapAgainView();
    }

    public static NotificationsQuickSettingsContainer getNotificationsQuickSettingsContainer(NotificationShadeWindowView notificationShadeWindowView) {
        return (NotificationsQuickSettingsContainer) notificationShadeWindowView.findViewById(R$id.notification_container_parent);
    }

    public static CollapsedStatusBarFragment createCollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CarrierConfigTracker carrierConfigTracker, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2, SecureSettings secureSettings, Executor executor) {
        return new CollapsedStatusBarFragment(factory, ongoingCallController, systemStatusAnimationScheduler, statusBarLocationPublisher, notificationIconAreaController, panelExpansionStateManager, featureFlags, statusBarIconController, statusBarHideIconsForBouncerManager, keyguardStateController, notificationPanelViewController, networkController, statusBarStateController, commandQueue, carrierConfigTracker, collapsedStatusBarFragmentLogger, factory2, secureSettings, executor);
    }
}

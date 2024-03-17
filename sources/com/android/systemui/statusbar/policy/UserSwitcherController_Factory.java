package com.android.systemui.statusbar.policy;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class UserSwitcherController_Factory implements Factory<UserSwitcherController> {
    public final Provider<IActivityManager> activityManagerProvider;
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<InteractionJankMonitor> interactionJankMonitorProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<Executor> longRunningExecutorProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<Executor> uiExecutorProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public UserSwitcherController_Factory(Provider<Context> provider, Provider<IActivityManager> provider2, Provider<UserManager> provider3, Provider<UserTracker> provider4, Provider<KeyguardStateController> provider5, Provider<DeviceProvisionedController> provider6, Provider<DevicePolicyManager> provider7, Provider<Handler> provider8, Provider<ActivityStarter> provider9, Provider<BroadcastDispatcher> provider10, Provider<BroadcastSender> provider11, Provider<UiEventLogger> provider12, Provider<FalsingManager> provider13, Provider<TelephonyListenerManager> provider14, Provider<SecureSettings> provider15, Provider<GlobalSettings> provider16, Provider<Executor> provider17, Provider<Executor> provider18, Provider<Executor> provider19, Provider<InteractionJankMonitor> provider20, Provider<LatencyTracker> provider21, Provider<DumpManager> provider22, Provider<DialogLaunchAnimator> provider23) {
        this.contextProvider = provider;
        this.activityManagerProvider = provider2;
        this.userManagerProvider = provider3;
        this.userTrackerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.deviceProvisionedControllerProvider = provider6;
        this.devicePolicyManagerProvider = provider7;
        this.handlerProvider = provider8;
        this.activityStarterProvider = provider9;
        this.broadcastDispatcherProvider = provider10;
        this.broadcastSenderProvider = provider11;
        this.uiEventLoggerProvider = provider12;
        this.falsingManagerProvider = provider13;
        this.telephonyListenerManagerProvider = provider14;
        this.secureSettingsProvider = provider15;
        this.globalSettingsProvider = provider16;
        this.bgExecutorProvider = provider17;
        this.longRunningExecutorProvider = provider18;
        this.uiExecutorProvider = provider19;
        this.interactionJankMonitorProvider = provider20;
        this.latencyTrackerProvider = provider21;
        this.dumpManagerProvider = provider22;
        this.dialogLaunchAnimatorProvider = provider23;
    }

    public UserSwitcherController get() {
        return newInstance(this.contextProvider.get(), this.activityManagerProvider.get(), this.userManagerProvider.get(), this.userTrackerProvider.get(), this.keyguardStateControllerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.devicePolicyManagerProvider.get(), this.handlerProvider.get(), this.activityStarterProvider.get(), this.broadcastDispatcherProvider.get(), this.broadcastSenderProvider.get(), this.uiEventLoggerProvider.get(), this.falsingManagerProvider.get(), this.telephonyListenerManagerProvider.get(), this.secureSettingsProvider.get(), this.globalSettingsProvider.get(), this.bgExecutorProvider.get(), this.longRunningExecutorProvider.get(), this.uiExecutorProvider.get(), this.interactionJankMonitorProvider.get(), this.latencyTrackerProvider.get(), this.dumpManagerProvider.get(), this.dialogLaunchAnimatorProvider.get());
    }

    public static UserSwitcherController_Factory create(Provider<Context> provider, Provider<IActivityManager> provider2, Provider<UserManager> provider3, Provider<UserTracker> provider4, Provider<KeyguardStateController> provider5, Provider<DeviceProvisionedController> provider6, Provider<DevicePolicyManager> provider7, Provider<Handler> provider8, Provider<ActivityStarter> provider9, Provider<BroadcastDispatcher> provider10, Provider<BroadcastSender> provider11, Provider<UiEventLogger> provider12, Provider<FalsingManager> provider13, Provider<TelephonyListenerManager> provider14, Provider<SecureSettings> provider15, Provider<GlobalSettings> provider16, Provider<Executor> provider17, Provider<Executor> provider18, Provider<Executor> provider19, Provider<InteractionJankMonitor> provider20, Provider<LatencyTracker> provider21, Provider<DumpManager> provider22, Provider<DialogLaunchAnimator> provider23) {
        return new UserSwitcherController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23);
    }

    public static UserSwitcherController newInstance(Context context, IActivityManager iActivityManager, UserManager userManager, UserTracker userTracker, KeyguardStateController keyguardStateController, DeviceProvisionedController deviceProvisionedController, DevicePolicyManager devicePolicyManager, Handler handler, ActivityStarter activityStarter, BroadcastDispatcher broadcastDispatcher, BroadcastSender broadcastSender, UiEventLogger uiEventLogger, FalsingManager falsingManager, TelephonyListenerManager telephonyListenerManager, SecureSettings secureSettings, GlobalSettings globalSettings, Executor executor, Executor executor2, Executor executor3, InteractionJankMonitor interactionJankMonitor, LatencyTracker latencyTracker, DumpManager dumpManager, DialogLaunchAnimator dialogLaunchAnimator) {
        return new UserSwitcherController(context, iActivityManager, userManager, userTracker, keyguardStateController, deviceProvisionedController, devicePolicyManager, handler, activityStarter, broadcastDispatcher, broadcastSender, uiEventLogger, falsingManager, telephonyListenerManager, secureSettings, globalSettings, executor, executor2, executor3, interactionJankMonitor, latencyTracker, dumpManager, dialogLaunchAnimator);
    }
}

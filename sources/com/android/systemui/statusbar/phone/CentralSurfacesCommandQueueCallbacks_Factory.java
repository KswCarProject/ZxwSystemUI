package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.os.PowerManager;
import android.os.Vibrator;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.DisableFlagsLogger;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class CentralSurfacesCommandQueueCallbacks_Factory implements Factory<CentralSurfacesCommandQueueCallbacks> {
    public final Provider<AssistManager> assistManagerProvider;
    public final Provider<CentralSurfaces> centralSurfacesProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<DisableFlagsLogger> disableFlagsLoggerProvider;
    public final Provider<Integer> displayIdProvider;
    public final Provider<DozeServiceHost> dozeServiceHostProvider;
    public final Provider<HeadsUpManager> headsUpManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LightBarController> lightBarControllerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    public final Provider<NotificationShadeWindowView> notificationShadeWindowViewProvider;
    public final Provider<NotificationStackScrollLayoutController> notificationStackScrollLayoutControllerProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<RemoteInputQuickSettingsDisabler> remoteInputQuickSettingsDisablerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<StatusBarHideIconsForBouncerManager> statusBarHideIconsForBouncerManagerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VibratorHelper> vibratorHelperProvider;
    public final Provider<Optional<Vibrator>> vibratorOptionalProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public CentralSurfacesCommandQueueCallbacks_Factory(Provider<CentralSurfaces> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<ShadeController> provider4, Provider<CommandQueue> provider5, Provider<NotificationPanelViewController> provider6, Provider<RemoteInputQuickSettingsDisabler> provider7, Provider<MetricsLogger> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<KeyguardStateController> provider10, Provider<HeadsUpManager> provider11, Provider<WakefulnessLifecycle> provider12, Provider<DeviceProvisionedController> provider13, Provider<StatusBarKeyguardViewManager> provider14, Provider<AssistManager> provider15, Provider<DozeServiceHost> provider16, Provider<SysuiStatusBarStateController> provider17, Provider<NotificationShadeWindowView> provider18, Provider<NotificationStackScrollLayoutController> provider19, Provider<StatusBarHideIconsForBouncerManager> provider20, Provider<PowerManager> provider21, Provider<VibratorHelper> provider22, Provider<Optional<Vibrator>> provider23, Provider<LightBarController> provider24, Provider<DisableFlagsLogger> provider25, Provider<Integer> provider26) {
        this.centralSurfacesProvider = provider;
        this.contextProvider = provider2;
        this.resourcesProvider = provider3;
        this.shadeControllerProvider = provider4;
        this.commandQueueProvider = provider5;
        this.notificationPanelViewControllerProvider = provider6;
        this.remoteInputQuickSettingsDisablerProvider = provider7;
        this.metricsLoggerProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.keyguardStateControllerProvider = provider10;
        this.headsUpManagerProvider = provider11;
        this.wakefulnessLifecycleProvider = provider12;
        this.deviceProvisionedControllerProvider = provider13;
        this.statusBarKeyguardViewManagerProvider = provider14;
        this.assistManagerProvider = provider15;
        this.dozeServiceHostProvider = provider16;
        this.statusBarStateControllerProvider = provider17;
        this.notificationShadeWindowViewProvider = provider18;
        this.notificationStackScrollLayoutControllerProvider = provider19;
        this.statusBarHideIconsForBouncerManagerProvider = provider20;
        this.powerManagerProvider = provider21;
        this.vibratorHelperProvider = provider22;
        this.vibratorOptionalProvider = provider23;
        this.lightBarControllerProvider = provider24;
        this.disableFlagsLoggerProvider = provider25;
        this.displayIdProvider = provider26;
    }

    public CentralSurfacesCommandQueueCallbacks get() {
        return newInstance(this.centralSurfacesProvider.get(), this.contextProvider.get(), this.resourcesProvider.get(), this.shadeControllerProvider.get(), this.commandQueueProvider.get(), this.notificationPanelViewControllerProvider.get(), this.remoteInputQuickSettingsDisablerProvider.get(), this.metricsLoggerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.keyguardStateControllerProvider.get(), this.headsUpManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.deviceProvisionedControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.assistManagerProvider.get(), this.dozeServiceHostProvider.get(), this.statusBarStateControllerProvider.get(), this.notificationShadeWindowViewProvider.get(), this.notificationStackScrollLayoutControllerProvider.get(), this.statusBarHideIconsForBouncerManagerProvider.get(), this.powerManagerProvider.get(), this.vibratorHelperProvider.get(), this.vibratorOptionalProvider.get(), this.lightBarControllerProvider.get(), this.disableFlagsLoggerProvider.get(), this.displayIdProvider.get().intValue());
    }

    public static CentralSurfacesCommandQueueCallbacks_Factory create(Provider<CentralSurfaces> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<ShadeController> provider4, Provider<CommandQueue> provider5, Provider<NotificationPanelViewController> provider6, Provider<RemoteInputQuickSettingsDisabler> provider7, Provider<MetricsLogger> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<KeyguardStateController> provider10, Provider<HeadsUpManager> provider11, Provider<WakefulnessLifecycle> provider12, Provider<DeviceProvisionedController> provider13, Provider<StatusBarKeyguardViewManager> provider14, Provider<AssistManager> provider15, Provider<DozeServiceHost> provider16, Provider<SysuiStatusBarStateController> provider17, Provider<NotificationShadeWindowView> provider18, Provider<NotificationStackScrollLayoutController> provider19, Provider<StatusBarHideIconsForBouncerManager> provider20, Provider<PowerManager> provider21, Provider<VibratorHelper> provider22, Provider<Optional<Vibrator>> provider23, Provider<LightBarController> provider24, Provider<DisableFlagsLogger> provider25, Provider<Integer> provider26) {
        return new CentralSurfacesCommandQueueCallbacks_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26);
    }

    public static CentralSurfacesCommandQueueCallbacks newInstance(CentralSurfaces centralSurfaces, Context context, Resources resources, ShadeController shadeController, CommandQueue commandQueue, NotificationPanelViewController notificationPanelViewController, RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, MetricsLogger metricsLogger, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, HeadsUpManager headsUpManager, WakefulnessLifecycle wakefulnessLifecycle, DeviceProvisionedController deviceProvisionedController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, AssistManager assistManager, DozeServiceHost dozeServiceHost, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowView notificationShadeWindowView, NotificationStackScrollLayoutController notificationStackScrollLayoutController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, PowerManager powerManager, VibratorHelper vibratorHelper, Optional<Vibrator> optional, LightBarController lightBarController, DisableFlagsLogger disableFlagsLogger, int i) {
        return new CentralSurfacesCommandQueueCallbacks(centralSurfaces, context, resources, shadeController, commandQueue, notificationPanelViewController, remoteInputQuickSettingsDisabler, metricsLogger, keyguardUpdateMonitor, keyguardStateController, headsUpManager, wakefulnessLifecycle, deviceProvisionedController, statusBarKeyguardViewManager, assistManager, dozeServiceHost, sysuiStatusBarStateController, notificationShadeWindowView, notificationStackScrollLayoutController, statusBarHideIconsForBouncerManager, powerManager, vibratorHelper, optional, lightBarController, disableFlagsLogger, i);
    }
}

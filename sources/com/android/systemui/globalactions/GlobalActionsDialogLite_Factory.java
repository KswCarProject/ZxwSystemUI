package com.android.systemui.globalactions;

import android.app.IActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.trust.TrustManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.os.UserManager;
import android.service.dreams.IDreamManager;
import android.telecom.TelecomManager;
import android.view.IWindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.GlobalActions;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.systemui.util.RingerModeTracker;
import com.android.systemui.util.settings.GlobalSettings;
import com.android.systemui.util.settings.SecureSettings;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class GlobalActionsDialogLite_Factory implements Factory<GlobalActionsDialogLite> {
    public final Provider<AudioManager> audioManagerProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalProvider;
    public final Provider<SysuiColorExtractor> colorExtractorProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<DialogLaunchAnimator> dialogLaunchAnimatorProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<IActivityManager> iActivityManagerProvider;
    public final Provider<IDreamManager> iDreamManagerProvider;
    public final Provider<IWindowManager> iWindowManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<RingerModeTracker> ringerModeTrackerProvider;
    public final Provider<SecureSettings> secureSettingsProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<TelecomManager> telecomManagerProvider;
    public final Provider<TelephonyListenerManager> telephonyListenerManagerProvider;
    public final Provider<TrustManager> trustManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<VibratorHelper> vibratorProvider;
    public final Provider<GlobalActions.GlobalActionsManager> windowManagerFuncsProvider;

    public GlobalActionsDialogLite_Factory(Provider<Context> provider, Provider<GlobalActions.GlobalActionsManager> provider2, Provider<AudioManager> provider3, Provider<IDreamManager> provider4, Provider<DevicePolicyManager> provider5, Provider<LockPatternUtils> provider6, Provider<BroadcastDispatcher> provider7, Provider<TelephonyListenerManager> provider8, Provider<GlobalSettings> provider9, Provider<SecureSettings> provider10, Provider<VibratorHelper> provider11, Provider<Resources> provider12, Provider<ConfigurationController> provider13, Provider<KeyguardStateController> provider14, Provider<UserManager> provider15, Provider<TrustManager> provider16, Provider<IActivityManager> provider17, Provider<TelecomManager> provider18, Provider<MetricsLogger> provider19, Provider<SysuiColorExtractor> provider20, Provider<IStatusBarService> provider21, Provider<NotificationShadeWindowController> provider22, Provider<IWindowManager> provider23, Provider<Executor> provider24, Provider<UiEventLogger> provider25, Provider<RingerModeTracker> provider26, Provider<Handler> provider27, Provider<PackageManager> provider28, Provider<Optional<CentralSurfaces>> provider29, Provider<KeyguardUpdateMonitor> provider30, Provider<DialogLaunchAnimator> provider31) {
        this.contextProvider = provider;
        this.windowManagerFuncsProvider = provider2;
        this.audioManagerProvider = provider3;
        this.iDreamManagerProvider = provider4;
        this.devicePolicyManagerProvider = provider5;
        this.lockPatternUtilsProvider = provider6;
        this.broadcastDispatcherProvider = provider7;
        this.telephonyListenerManagerProvider = provider8;
        this.globalSettingsProvider = provider9;
        this.secureSettingsProvider = provider10;
        this.vibratorProvider = provider11;
        this.resourcesProvider = provider12;
        this.configurationControllerProvider = provider13;
        this.keyguardStateControllerProvider = provider14;
        this.userManagerProvider = provider15;
        this.trustManagerProvider = provider16;
        this.iActivityManagerProvider = provider17;
        this.telecomManagerProvider = provider18;
        this.metricsLoggerProvider = provider19;
        this.colorExtractorProvider = provider20;
        this.statusBarServiceProvider = provider21;
        this.notificationShadeWindowControllerProvider = provider22;
        this.iWindowManagerProvider = provider23;
        this.backgroundExecutorProvider = provider24;
        this.uiEventLoggerProvider = provider25;
        this.ringerModeTrackerProvider = provider26;
        this.handlerProvider = provider27;
        this.packageManagerProvider = provider28;
        this.centralSurfacesOptionalProvider = provider29;
        this.keyguardUpdateMonitorProvider = provider30;
        this.dialogLaunchAnimatorProvider = provider31;
    }

    public GlobalActionsDialogLite get() {
        return newInstance(this.contextProvider.get(), this.windowManagerFuncsProvider.get(), this.audioManagerProvider.get(), this.iDreamManagerProvider.get(), this.devicePolicyManagerProvider.get(), this.lockPatternUtilsProvider.get(), this.broadcastDispatcherProvider.get(), this.telephonyListenerManagerProvider.get(), this.globalSettingsProvider.get(), this.secureSettingsProvider.get(), this.vibratorProvider.get(), this.resourcesProvider.get(), this.configurationControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.userManagerProvider.get(), this.trustManagerProvider.get(), this.iActivityManagerProvider.get(), this.telecomManagerProvider.get(), this.metricsLoggerProvider.get(), this.colorExtractorProvider.get(), this.statusBarServiceProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.iWindowManagerProvider.get(), this.backgroundExecutorProvider.get(), this.uiEventLoggerProvider.get(), this.ringerModeTrackerProvider.get(), this.handlerProvider.get(), this.packageManagerProvider.get(), this.centralSurfacesOptionalProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dialogLaunchAnimatorProvider.get());
    }

    public static GlobalActionsDialogLite_Factory create(Provider<Context> provider, Provider<GlobalActions.GlobalActionsManager> provider2, Provider<AudioManager> provider3, Provider<IDreamManager> provider4, Provider<DevicePolicyManager> provider5, Provider<LockPatternUtils> provider6, Provider<BroadcastDispatcher> provider7, Provider<TelephonyListenerManager> provider8, Provider<GlobalSettings> provider9, Provider<SecureSettings> provider10, Provider<VibratorHelper> provider11, Provider<Resources> provider12, Provider<ConfigurationController> provider13, Provider<KeyguardStateController> provider14, Provider<UserManager> provider15, Provider<TrustManager> provider16, Provider<IActivityManager> provider17, Provider<TelecomManager> provider18, Provider<MetricsLogger> provider19, Provider<SysuiColorExtractor> provider20, Provider<IStatusBarService> provider21, Provider<NotificationShadeWindowController> provider22, Provider<IWindowManager> provider23, Provider<Executor> provider24, Provider<UiEventLogger> provider25, Provider<RingerModeTracker> provider26, Provider<Handler> provider27, Provider<PackageManager> provider28, Provider<Optional<CentralSurfaces>> provider29, Provider<KeyguardUpdateMonitor> provider30, Provider<DialogLaunchAnimator> provider31) {
        return new GlobalActionsDialogLite_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31);
    }

    public static GlobalActionsDialogLite newInstance(Context context, GlobalActions.GlobalActionsManager globalActionsManager, AudioManager audioManager, IDreamManager iDreamManager, DevicePolicyManager devicePolicyManager, LockPatternUtils lockPatternUtils, BroadcastDispatcher broadcastDispatcher, TelephonyListenerManager telephonyListenerManager, GlobalSettings globalSettings, SecureSettings secureSettings, VibratorHelper vibratorHelper, Resources resources, ConfigurationController configurationController, KeyguardStateController keyguardStateController, UserManager userManager, TrustManager trustManager, IActivityManager iActivityManager, TelecomManager telecomManager, MetricsLogger metricsLogger, SysuiColorExtractor sysuiColorExtractor, IStatusBarService iStatusBarService, NotificationShadeWindowController notificationShadeWindowController, IWindowManager iWindowManager, Executor executor, UiEventLogger uiEventLogger, RingerModeTracker ringerModeTracker, Handler handler, PackageManager packageManager, Optional<CentralSurfaces> optional, KeyguardUpdateMonitor keyguardUpdateMonitor, DialogLaunchAnimator dialogLaunchAnimator) {
        return new GlobalActionsDialogLite(context, globalActionsManager, audioManager, iDreamManager, devicePolicyManager, lockPatternUtils, broadcastDispatcher, telephonyListenerManager, globalSettings, secureSettings, vibratorHelper, resources, configurationController, keyguardStateController, userManager, trustManager, iActivityManager, telecomManager, metricsLogger, sysuiColorExtractor, iStatusBarService, notificationShadeWindowController, iWindowManager, executor, uiEventLogger, ringerModeTracker, handler, packageManager, optional, keyguardUpdateMonitor, dialogLaunchAnimator);
    }
}

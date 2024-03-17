package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.os.Handler;
import android.os.PowerManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BiometricUnlockController_Factory implements Factory<BiometricUnlockController> {
    public final Provider<AuthController> authControllerProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<DozeScrimController> dozeScrimControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<KeyguardBypassController> keyguardBypassControllerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NotificationMediaManager> notificationMediaManagerProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<ScrimController> scrimControllerProvider;
    public final Provider<SessionTracker> sessionTrackerProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<WakefulnessLifecycle> wakefulnessLifecycleProvider;

    public BiometricUnlockController_Factory(Provider<DozeScrimController> provider, Provider<KeyguardViewMediator> provider2, Provider<ScrimController> provider3, Provider<ShadeController> provider4, Provider<NotificationShadeWindowController> provider5, Provider<KeyguardStateController> provider6, Provider<Handler> provider7, Provider<KeyguardUpdateMonitor> provider8, Provider<Resources> provider9, Provider<KeyguardBypassController> provider10, Provider<DozeParameters> provider11, Provider<MetricsLogger> provider12, Provider<DumpManager> provider13, Provider<PowerManager> provider14, Provider<NotificationMediaManager> provider15, Provider<WakefulnessLifecycle> provider16, Provider<ScreenLifecycle> provider17, Provider<AuthController> provider18, Provider<StatusBarStateController> provider19, Provider<KeyguardUnlockAnimationController> provider20, Provider<SessionTracker> provider21, Provider<LatencyTracker> provider22, Provider<ScreenOffAnimationController> provider23) {
        this.dozeScrimControllerProvider = provider;
        this.keyguardViewMediatorProvider = provider2;
        this.scrimControllerProvider = provider3;
        this.shadeControllerProvider = provider4;
        this.notificationShadeWindowControllerProvider = provider5;
        this.keyguardStateControllerProvider = provider6;
        this.handlerProvider = provider7;
        this.keyguardUpdateMonitorProvider = provider8;
        this.resourcesProvider = provider9;
        this.keyguardBypassControllerProvider = provider10;
        this.dozeParametersProvider = provider11;
        this.metricsLoggerProvider = provider12;
        this.dumpManagerProvider = provider13;
        this.powerManagerProvider = provider14;
        this.notificationMediaManagerProvider = provider15;
        this.wakefulnessLifecycleProvider = provider16;
        this.screenLifecycleProvider = provider17;
        this.authControllerProvider = provider18;
        this.statusBarStateControllerProvider = provider19;
        this.keyguardUnlockAnimationControllerProvider = provider20;
        this.sessionTrackerProvider = provider21;
        this.latencyTrackerProvider = provider22;
        this.screenOffAnimationControllerProvider = provider23;
    }

    public BiometricUnlockController get() {
        return newInstance(this.dozeScrimControllerProvider.get(), this.keyguardViewMediatorProvider.get(), this.scrimControllerProvider.get(), this.shadeControllerProvider.get(), this.notificationShadeWindowControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.handlerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.resourcesProvider.get(), this.keyguardBypassControllerProvider.get(), this.dozeParametersProvider.get(), this.metricsLoggerProvider.get(), this.dumpManagerProvider.get(), this.powerManagerProvider.get(), this.notificationMediaManagerProvider.get(), this.wakefulnessLifecycleProvider.get(), this.screenLifecycleProvider.get(), this.authControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUnlockAnimationControllerProvider.get(), this.sessionTrackerProvider.get(), this.latencyTrackerProvider.get(), this.screenOffAnimationControllerProvider.get());
    }

    public static BiometricUnlockController_Factory create(Provider<DozeScrimController> provider, Provider<KeyguardViewMediator> provider2, Provider<ScrimController> provider3, Provider<ShadeController> provider4, Provider<NotificationShadeWindowController> provider5, Provider<KeyguardStateController> provider6, Provider<Handler> provider7, Provider<KeyguardUpdateMonitor> provider8, Provider<Resources> provider9, Provider<KeyguardBypassController> provider10, Provider<DozeParameters> provider11, Provider<MetricsLogger> provider12, Provider<DumpManager> provider13, Provider<PowerManager> provider14, Provider<NotificationMediaManager> provider15, Provider<WakefulnessLifecycle> provider16, Provider<ScreenLifecycle> provider17, Provider<AuthController> provider18, Provider<StatusBarStateController> provider19, Provider<KeyguardUnlockAnimationController> provider20, Provider<SessionTracker> provider21, Provider<LatencyTracker> provider22, Provider<ScreenOffAnimationController> provider23) {
        return new BiometricUnlockController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23);
    }

    public static BiometricUnlockController newInstance(DozeScrimController dozeScrimController, KeyguardViewMediator keyguardViewMediator, ScrimController scrimController, ShadeController shadeController, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, Resources resources, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, MetricsLogger metricsLogger, DumpManager dumpManager, PowerManager powerManager, NotificationMediaManager notificationMediaManager, WakefulnessLifecycle wakefulnessLifecycle, ScreenLifecycle screenLifecycle, AuthController authController, StatusBarStateController statusBarStateController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, SessionTracker sessionTracker, LatencyTracker latencyTracker, ScreenOffAnimationController screenOffAnimationController) {
        return new BiometricUnlockController(dozeScrimController, keyguardViewMediator, scrimController, shadeController, notificationShadeWindowController, keyguardStateController, handler, keyguardUpdateMonitor, resources, keyguardBypassController, dozeParameters, metricsLogger, dumpManager, powerManager, notificationMediaManager, wakefulnessLifecycle, screenLifecycle, authController, statusBarStateController, keyguardUnlockAnimationController, sessionTracker, latencyTracker, screenOffAnimationController);
    }
}

package com.android.systemui.statusbar.phone;

import android.app.AlarmManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.wakelock.DelayedWakeLock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ScrimController_Factory implements Factory<ScrimController> {
    public final Provider<AlarmManager> alarmManagerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DelayedWakeLock.Builder> delayedWakeLockBuilderProvider;
    public final Provider<DockManager> dockManagerProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<Handler> handlerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUnlockAnimationController> keyguardUnlockAnimationControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LightBarController> lightBarControllerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<PanelExpansionStateManager> panelExpansionStateManagerProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;

    public ScrimController_Factory(Provider<LightBarController> provider, Provider<DozeParameters> provider2, Provider<AlarmManager> provider3, Provider<KeyguardStateController> provider4, Provider<DelayedWakeLock.Builder> provider5, Provider<Handler> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DockManager> provider8, Provider<ConfigurationController> provider9, Provider<Executor> provider10, Provider<ScreenOffAnimationController> provider11, Provider<PanelExpansionStateManager> provider12, Provider<KeyguardUnlockAnimationController> provider13, Provider<StatusBarKeyguardViewManager> provider14) {
        this.lightBarControllerProvider = provider;
        this.dozeParametersProvider = provider2;
        this.alarmManagerProvider = provider3;
        this.keyguardStateControllerProvider = provider4;
        this.delayedWakeLockBuilderProvider = provider5;
        this.handlerProvider = provider6;
        this.keyguardUpdateMonitorProvider = provider7;
        this.dockManagerProvider = provider8;
        this.configurationControllerProvider = provider9;
        this.mainExecutorProvider = provider10;
        this.screenOffAnimationControllerProvider = provider11;
        this.panelExpansionStateManagerProvider = provider12;
        this.keyguardUnlockAnimationControllerProvider = provider13;
        this.statusBarKeyguardViewManagerProvider = provider14;
    }

    public ScrimController get() {
        return newInstance(this.lightBarControllerProvider.get(), this.dozeParametersProvider.get(), this.alarmManagerProvider.get(), this.keyguardStateControllerProvider.get(), this.delayedWakeLockBuilderProvider.get(), this.handlerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dockManagerProvider.get(), this.configurationControllerProvider.get(), this.mainExecutorProvider.get(), this.screenOffAnimationControllerProvider.get(), this.panelExpansionStateManagerProvider.get(), this.keyguardUnlockAnimationControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get());
    }

    public static ScrimController_Factory create(Provider<LightBarController> provider, Provider<DozeParameters> provider2, Provider<AlarmManager> provider3, Provider<KeyguardStateController> provider4, Provider<DelayedWakeLock.Builder> provider5, Provider<Handler> provider6, Provider<KeyguardUpdateMonitor> provider7, Provider<DockManager> provider8, Provider<ConfigurationController> provider9, Provider<Executor> provider10, Provider<ScreenOffAnimationController> provider11, Provider<PanelExpansionStateManager> provider12, Provider<KeyguardUnlockAnimationController> provider13, Provider<StatusBarKeyguardViewManager> provider14) {
        return new ScrimController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static ScrimController newInstance(LightBarController lightBarController, DozeParameters dozeParameters, AlarmManager alarmManager, KeyguardStateController keyguardStateController, DelayedWakeLock.Builder builder, Handler handler, KeyguardUpdateMonitor keyguardUpdateMonitor, DockManager dockManager, ConfigurationController configurationController, Executor executor, ScreenOffAnimationController screenOffAnimationController, PanelExpansionStateManager panelExpansionStateManager, KeyguardUnlockAnimationController keyguardUnlockAnimationController, StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        return new ScrimController(lightBarController, dozeParameters, alarmManager, keyguardStateController, builder, handler, keyguardUpdateMonitor, dockManager, configurationController, executor, screenOffAnimationController, panelExpansionStateManager, keyguardUnlockAnimationController, statusBarKeyguardViewManager);
    }
}

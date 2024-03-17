package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardUserSwitcherController_Factory implements Factory<KeyguardUserSwitcherController> {
    public final Provider<Context> contextProvider;
    public final Provider<DozeParameters> dozeParametersProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<KeyguardUserSwitcherView> keyguardUserSwitcherViewProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<ScreenOffAnimationController> screenOffAnimationControllerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;

    public KeyguardUserSwitcherController_Factory(Provider<KeyguardUserSwitcherView> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<LayoutInflater> provider4, Provider<ScreenLifecycle> provider5, Provider<UserSwitcherController> provider6, Provider<KeyguardStateController> provider7, Provider<SysuiStatusBarStateController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<DozeParameters> provider10, Provider<ScreenOffAnimationController> provider11) {
        this.keyguardUserSwitcherViewProvider = provider;
        this.contextProvider = provider2;
        this.resourcesProvider = provider3;
        this.layoutInflaterProvider = provider4;
        this.screenLifecycleProvider = provider5;
        this.userSwitcherControllerProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.statusBarStateControllerProvider = provider8;
        this.keyguardUpdateMonitorProvider = provider9;
        this.dozeParametersProvider = provider10;
        this.screenOffAnimationControllerProvider = provider11;
    }

    public KeyguardUserSwitcherController get() {
        return newInstance(this.keyguardUserSwitcherViewProvider.get(), this.contextProvider.get(), this.resourcesProvider.get(), this.layoutInflaterProvider.get(), this.screenLifecycleProvider.get(), this.userSwitcherControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dozeParametersProvider.get(), this.screenOffAnimationControllerProvider.get());
    }

    public static KeyguardUserSwitcherController_Factory create(Provider<KeyguardUserSwitcherView> provider, Provider<Context> provider2, Provider<Resources> provider3, Provider<LayoutInflater> provider4, Provider<ScreenLifecycle> provider5, Provider<UserSwitcherController> provider6, Provider<KeyguardStateController> provider7, Provider<SysuiStatusBarStateController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<DozeParameters> provider10, Provider<ScreenOffAnimationController> provider11) {
        return new KeyguardUserSwitcherController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public static KeyguardUserSwitcherController newInstance(KeyguardUserSwitcherView keyguardUserSwitcherView, Context context, Resources resources, LayoutInflater layoutInflater, ScreenLifecycle screenLifecycle, UserSwitcherController userSwitcherController, KeyguardStateController keyguardStateController, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController) {
        return new KeyguardUserSwitcherController(keyguardUserSwitcherView, context, resources, layoutInflater, screenLifecycle, userSwitcherController, keyguardStateController, sysuiStatusBarStateController, keyguardUpdateMonitor, dozeParameters, screenOffAnimationController);
    }
}

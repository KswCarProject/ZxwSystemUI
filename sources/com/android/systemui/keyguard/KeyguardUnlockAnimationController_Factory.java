package com.android.systemui.keyguard;

import android.content.Context;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardUnlockAnimationController_Factory implements Factory<KeyguardUnlockAnimationController> {
    public final Provider<BiometricUnlockController> biometricUnlockControllerLazyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardViewController> keyguardViewControllerProvider;
    public final Provider<KeyguardViewMediator> keyguardViewMediatorProvider;
    public final Provider<NotificationShadeWindowController> notificationShadeWindowControllerProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public KeyguardUnlockAnimationController_Factory(Provider<Context> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardViewMediator> provider3, Provider<KeyguardViewController> provider4, Provider<FeatureFlags> provider5, Provider<BiometricUnlockController> provider6, Provider<SysuiStatusBarStateController> provider7, Provider<NotificationShadeWindowController> provider8) {
        this.contextProvider = provider;
        this.keyguardStateControllerProvider = provider2;
        this.keyguardViewMediatorProvider = provider3;
        this.keyguardViewControllerProvider = provider4;
        this.featureFlagsProvider = provider5;
        this.biometricUnlockControllerLazyProvider = provider6;
        this.statusBarStateControllerProvider = provider7;
        this.notificationShadeWindowControllerProvider = provider8;
    }

    public KeyguardUnlockAnimationController get() {
        return newInstance(this.contextProvider.get(), this.keyguardStateControllerProvider.get(), DoubleCheck.lazy(this.keyguardViewMediatorProvider), this.keyguardViewControllerProvider.get(), this.featureFlagsProvider.get(), DoubleCheck.lazy(this.biometricUnlockControllerLazyProvider), this.statusBarStateControllerProvider.get(), this.notificationShadeWindowControllerProvider.get());
    }

    public static KeyguardUnlockAnimationController_Factory create(Provider<Context> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardViewMediator> provider3, Provider<KeyguardViewController> provider4, Provider<FeatureFlags> provider5, Provider<BiometricUnlockController> provider6, Provider<SysuiStatusBarStateController> provider7, Provider<NotificationShadeWindowController> provider8) {
        return new KeyguardUnlockAnimationController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    public static KeyguardUnlockAnimationController newInstance(Context context, KeyguardStateController keyguardStateController, Lazy<KeyguardViewMediator> lazy, KeyguardViewController keyguardViewController, FeatureFlags featureFlags, Lazy<BiometricUnlockController> lazy2, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowController notificationShadeWindowController) {
        return new KeyguardUnlockAnimationController(context, keyguardStateController, lazy, keyguardViewController, featureFlags, lazy2, sysuiStatusBarStateController, notificationShadeWindowController);
    }
}

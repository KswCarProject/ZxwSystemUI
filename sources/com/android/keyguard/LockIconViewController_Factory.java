package com.android.keyguard;

import android.content.res.Resources;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.AuthRippleController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LockIconViewController_Factory implements Factory<LockIconViewController> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AuthController> authControllerProvider;
    public final Provider<AuthRippleController> authRippleControllerProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<KeyguardViewController> keyguardViewControllerProvider;
    public final Provider<Resources> resourcesProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<VibratorHelper> vibratorProvider;
    public final Provider<LockIconView> viewProvider;

    public LockIconViewController_Factory(Provider<LockIconView> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<AuthController> provider7, Provider<DumpManager> provider8, Provider<AccessibilityManager> provider9, Provider<ConfigurationController> provider10, Provider<DelayableExecutor> provider11, Provider<VibratorHelper> provider12, Provider<AuthRippleController> provider13, Provider<Resources> provider14) {
        this.viewProvider = provider;
        this.statusBarStateControllerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.keyguardViewControllerProvider = provider4;
        this.keyguardStateControllerProvider = provider5;
        this.falsingManagerProvider = provider6;
        this.authControllerProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.accessibilityManagerProvider = provider9;
        this.configurationControllerProvider = provider10;
        this.executorProvider = provider11;
        this.vibratorProvider = provider12;
        this.authRippleControllerProvider = provider13;
        this.resourcesProvider = provider14;
    }

    public LockIconViewController get() {
        return newInstance(this.viewProvider.get(), this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.keyguardViewControllerProvider.get(), this.keyguardStateControllerProvider.get(), this.falsingManagerProvider.get(), this.authControllerProvider.get(), this.dumpManagerProvider.get(), this.accessibilityManagerProvider.get(), this.configurationControllerProvider.get(), this.executorProvider.get(), this.vibratorProvider.get(), this.authRippleControllerProvider.get(), this.resourcesProvider.get());
    }

    public static LockIconViewController_Factory create(Provider<LockIconView> provider, Provider<StatusBarStateController> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<KeyguardViewController> provider4, Provider<KeyguardStateController> provider5, Provider<FalsingManager> provider6, Provider<AuthController> provider7, Provider<DumpManager> provider8, Provider<AccessibilityManager> provider9, Provider<ConfigurationController> provider10, Provider<DelayableExecutor> provider11, Provider<VibratorHelper> provider12, Provider<AuthRippleController> provider13, Provider<Resources> provider14) {
        return new LockIconViewController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14);
    }

    public static LockIconViewController newInstance(LockIconView lockIconView, StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardViewController keyguardViewController, KeyguardStateController keyguardStateController, FalsingManager falsingManager, AuthController authController, DumpManager dumpManager, AccessibilityManager accessibilityManager, ConfigurationController configurationController, DelayableExecutor delayableExecutor, VibratorHelper vibratorHelper, AuthRippleController authRippleController, Resources resources) {
        return new LockIconViewController(lockIconView, statusBarStateController, keyguardUpdateMonitor, keyguardViewController, keyguardStateController, falsingManager, authController, dumpManager, accessibilityManager, configurationController, delayableExecutor, vibratorHelper, authRippleController, resources);
    }
}

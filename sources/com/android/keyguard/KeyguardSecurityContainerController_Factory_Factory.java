package com.android.keyguard;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardSecurityContainerController_Factory_Factory implements Factory<KeyguardSecurityContainerController.Factory> {
    public final Provider<AdminSecondaryLockScreenController.Factory> adminSecondaryLockScreenControllerFactoryProvider;
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<FalsingCollector> falsingCollectorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<GlobalSettings> globalSettingsProvider;
    public final Provider<KeyguardSecurityModel> keyguardSecurityModelProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<LockPatternUtils> lockPatternUtilsProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<KeyguardSecurityViewFlipperController> securityViewFlipperControllerProvider;
    public final Provider<SessionTracker> sessionTrackerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserSwitcherController> userSwitcherControllerProvider;
    public final Provider<KeyguardSecurityContainer> viewProvider;

    public KeyguardSecurityContainerController_Factory_Factory(Provider<KeyguardSecurityContainer> provider, Provider<AdminSecondaryLockScreenController.Factory> provider2, Provider<LockPatternUtils> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardSecurityModel> provider5, Provider<MetricsLogger> provider6, Provider<UiEventLogger> provider7, Provider<KeyguardStateController> provider8, Provider<KeyguardSecurityViewFlipperController> provider9, Provider<ConfigurationController> provider10, Provider<FalsingCollector> provider11, Provider<FalsingManager> provider12, Provider<UserSwitcherController> provider13, Provider<FeatureFlags> provider14, Provider<GlobalSettings> provider15, Provider<SessionTracker> provider16) {
        this.viewProvider = provider;
        this.adminSecondaryLockScreenControllerFactoryProvider = provider2;
        this.lockPatternUtilsProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
        this.keyguardSecurityModelProvider = provider5;
        this.metricsLoggerProvider = provider6;
        this.uiEventLoggerProvider = provider7;
        this.keyguardStateControllerProvider = provider8;
        this.securityViewFlipperControllerProvider = provider9;
        this.configurationControllerProvider = provider10;
        this.falsingCollectorProvider = provider11;
        this.falsingManagerProvider = provider12;
        this.userSwitcherControllerProvider = provider13;
        this.featureFlagsProvider = provider14;
        this.globalSettingsProvider = provider15;
        this.sessionTrackerProvider = provider16;
    }

    public KeyguardSecurityContainerController.Factory get() {
        return newInstance(this.viewProvider.get(), this.adminSecondaryLockScreenControllerFactoryProvider.get(), this.lockPatternUtilsProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.keyguardSecurityModelProvider.get(), this.metricsLoggerProvider.get(), this.uiEventLoggerProvider.get(), this.keyguardStateControllerProvider.get(), this.securityViewFlipperControllerProvider.get(), this.configurationControllerProvider.get(), this.falsingCollectorProvider.get(), this.falsingManagerProvider.get(), this.userSwitcherControllerProvider.get(), this.featureFlagsProvider.get(), this.globalSettingsProvider.get(), this.sessionTrackerProvider.get());
    }

    public static KeyguardSecurityContainerController_Factory_Factory create(Provider<KeyguardSecurityContainer> provider, Provider<AdminSecondaryLockScreenController.Factory> provider2, Provider<LockPatternUtils> provider3, Provider<KeyguardUpdateMonitor> provider4, Provider<KeyguardSecurityModel> provider5, Provider<MetricsLogger> provider6, Provider<UiEventLogger> provider7, Provider<KeyguardStateController> provider8, Provider<KeyguardSecurityViewFlipperController> provider9, Provider<ConfigurationController> provider10, Provider<FalsingCollector> provider11, Provider<FalsingManager> provider12, Provider<UserSwitcherController> provider13, Provider<FeatureFlags> provider14, Provider<GlobalSettings> provider15, Provider<SessionTracker> provider16) {
        return new KeyguardSecurityContainerController_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16);
    }

    public static KeyguardSecurityContainerController.Factory newInstance(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController, FalsingCollector falsingCollector, FalsingManager falsingManager, UserSwitcherController userSwitcherController, FeatureFlags featureFlags, GlobalSettings globalSettings, SessionTracker sessionTracker) {
        return new KeyguardSecurityContainerController.Factory(keyguardSecurityContainer, factory, lockPatternUtils, keyguardUpdateMonitor, keyguardSecurityModel, metricsLogger, uiEventLogger, keyguardStateController, keyguardSecurityViewFlipperController, configurationController, falsingCollector, falsingManager, userSwitcherController, featureFlags, globalSettings, sessionTracker);
    }
}

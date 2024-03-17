package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.interruption.KeyguardNotificationVisibilityProvider;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class KeyguardCoordinator_Factory implements Factory<KeyguardCoordinator> {
    public final Provider<HighPriorityProvider> highPriorityProvider;
    public final Provider<KeyguardNotificationVisibilityProvider> keyguardNotificationVisibilityProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<SharedCoordinatorLogger> loggerProvider;
    public final Provider<SectionHeaderVisibilityProvider> sectionHeaderVisibilityProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;

    public KeyguardCoordinator_Factory(Provider<StatusBarStateController> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<HighPriorityProvider> provider3, Provider<SectionHeaderVisibilityProvider> provider4, Provider<KeyguardNotificationVisibilityProvider> provider5, Provider<SharedCoordinatorLogger> provider6) {
        this.statusBarStateControllerProvider = provider;
        this.keyguardUpdateMonitorProvider = provider2;
        this.highPriorityProvider = provider3;
        this.sectionHeaderVisibilityProvider = provider4;
        this.keyguardNotificationVisibilityProvider = provider5;
        this.loggerProvider = provider6;
    }

    public KeyguardCoordinator get() {
        return newInstance(this.statusBarStateControllerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.highPriorityProvider.get(), this.sectionHeaderVisibilityProvider.get(), this.keyguardNotificationVisibilityProvider.get(), this.loggerProvider.get());
    }

    public static KeyguardCoordinator_Factory create(Provider<StatusBarStateController> provider, Provider<KeyguardUpdateMonitor> provider2, Provider<HighPriorityProvider> provider3, Provider<SectionHeaderVisibilityProvider> provider4, Provider<KeyguardNotificationVisibilityProvider> provider5, Provider<SharedCoordinatorLogger> provider6) {
        return new KeyguardCoordinator_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static KeyguardCoordinator newInstance(StatusBarStateController statusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, HighPriorityProvider highPriorityProvider2, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider2, KeyguardNotificationVisibilityProvider keyguardNotificationVisibilityProvider2, SharedCoordinatorLogger sharedCoordinatorLogger) {
        return new KeyguardCoordinator(statusBarStateController, keyguardUpdateMonitor, highPriorityProvider2, sectionHeaderVisibilityProvider2, keyguardNotificationVisibilityProvider2, sharedCoordinatorLogger);
    }
}

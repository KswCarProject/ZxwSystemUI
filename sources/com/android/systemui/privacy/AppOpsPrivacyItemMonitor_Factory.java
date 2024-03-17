package com.android.systemui.privacy;

import com.android.systemui.appops.AppOpsController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AppOpsPrivacyItemMonitor_Factory implements Factory<AppOpsPrivacyItemMonitor> {
    public final Provider<AppOpsController> appOpsControllerProvider;
    public final Provider<DelayableExecutor> bgExecutorProvider;
    public final Provider<PrivacyLogger> loggerProvider;
    public final Provider<PrivacyConfig> privacyConfigProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public AppOpsPrivacyItemMonitor_Factory(Provider<AppOpsController> provider, Provider<UserTracker> provider2, Provider<PrivacyConfig> provider3, Provider<DelayableExecutor> provider4, Provider<PrivacyLogger> provider5) {
        this.appOpsControllerProvider = provider;
        this.userTrackerProvider = provider2;
        this.privacyConfigProvider = provider3;
        this.bgExecutorProvider = provider4;
        this.loggerProvider = provider5;
    }

    public AppOpsPrivacyItemMonitor get() {
        return newInstance(this.appOpsControllerProvider.get(), this.userTrackerProvider.get(), this.privacyConfigProvider.get(), this.bgExecutorProvider.get(), this.loggerProvider.get());
    }

    public static AppOpsPrivacyItemMonitor_Factory create(Provider<AppOpsController> provider, Provider<UserTracker> provider2, Provider<PrivacyConfig> provider3, Provider<DelayableExecutor> provider4, Provider<PrivacyLogger> provider5) {
        return new AppOpsPrivacyItemMonitor_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static AppOpsPrivacyItemMonitor newInstance(AppOpsController appOpsController, UserTracker userTracker, PrivacyConfig privacyConfig, DelayableExecutor delayableExecutor, PrivacyLogger privacyLogger) {
        return new AppOpsPrivacyItemMonitor(appOpsController, userTracker, privacyConfig, delayableExecutor, privacyLogger);
    }
}

package com.android.systemui.statusbar.phone;

import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationsQSContainerController_Factory implements Factory<NotificationsQSContainerController> {
    public final Provider<DelayableExecutor> delayableExecutorProvider;
    public final Provider<FeatureFlags> featureFlagsProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<NotificationsQuickSettingsContainer> viewProvider;

    public NotificationsQSContainerController_Factory(Provider<NotificationsQuickSettingsContainer> provider, Provider<NavigationModeController> provider2, Provider<OverviewProxyService> provider3, Provider<FeatureFlags> provider4, Provider<DelayableExecutor> provider5) {
        this.viewProvider = provider;
        this.navigationModeControllerProvider = provider2;
        this.overviewProxyServiceProvider = provider3;
        this.featureFlagsProvider = provider4;
        this.delayableExecutorProvider = provider5;
    }

    public NotificationsQSContainerController get() {
        return newInstance(this.viewProvider.get(), this.navigationModeControllerProvider.get(), this.overviewProxyServiceProvider.get(), this.featureFlagsProvider.get(), this.delayableExecutorProvider.get());
    }

    public static NotificationsQSContainerController_Factory create(Provider<NotificationsQuickSettingsContainer> provider, Provider<NavigationModeController> provider2, Provider<OverviewProxyService> provider3, Provider<FeatureFlags> provider4, Provider<DelayableExecutor> provider5) {
        return new NotificationsQSContainerController_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static NotificationsQSContainerController newInstance(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer, NavigationModeController navigationModeController, OverviewProxyService overviewProxyService, FeatureFlags featureFlags, DelayableExecutor delayableExecutor) {
        return new NotificationsQSContainerController(notificationsQuickSettingsContainer, navigationModeController, overviewProxyService, featureFlags, delayableExecutor);
    }
}

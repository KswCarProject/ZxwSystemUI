package com.android.systemui.statusbar;

import android.content.Context;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.SingleShadeLockScreenOverScroller_Factory  reason: case insensitive filesystem */
public final class C0002SingleShadeLockScreenOverScroller_Factory {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Context> contextProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;

    public C0002SingleShadeLockScreenOverScroller_Factory(Provider<ConfigurationController> provider, Provider<Context> provider2, Provider<SysuiStatusBarStateController> provider3) {
        this.configurationControllerProvider = provider;
        this.contextProvider = provider2;
        this.statusBarStateControllerProvider = provider3;
    }

    public SingleShadeLockScreenOverScroller get(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return newInstance(this.configurationControllerProvider.get(), this.contextProvider.get(), this.statusBarStateControllerProvider.get(), notificationStackScrollLayoutController);
    }

    public static C0002SingleShadeLockScreenOverScroller_Factory create(Provider<ConfigurationController> provider, Provider<Context> provider2, Provider<SysuiStatusBarStateController> provider3) {
        return new C0002SingleShadeLockScreenOverScroller_Factory(provider, provider2, provider3);
    }

    public static SingleShadeLockScreenOverScroller newInstance(ConfigurationController configurationController, Context context, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return new SingleShadeLockScreenOverScroller(configurationController, context, sysuiStatusBarStateController, notificationStackScrollLayoutController);
    }
}

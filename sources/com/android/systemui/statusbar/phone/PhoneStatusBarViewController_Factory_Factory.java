package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.util.view.ViewUtil;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class PhoneStatusBarViewController_Factory_Factory implements Factory<PhoneStatusBarViewController.Factory> {
    public final Provider<ConfigurationController> configurationControllerProvider;
    public final Provider<Optional<ScopedUnfoldTransitionProgressProvider>> progressProvider;
    public final Provider<Optional<SysUIUnfoldComponent>> unfoldComponentProvider;
    public final Provider<StatusBarUserSwitcherController> userSwitcherControllerProvider;
    public final Provider<ViewUtil> viewUtilProvider;

    public PhoneStatusBarViewController_Factory_Factory(Provider<Optional<SysUIUnfoldComponent>> provider, Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provider2, Provider<StatusBarUserSwitcherController> provider3, Provider<ViewUtil> provider4, Provider<ConfigurationController> provider5) {
        this.unfoldComponentProvider = provider;
        this.progressProvider = provider2;
        this.userSwitcherControllerProvider = provider3;
        this.viewUtilProvider = provider4;
        this.configurationControllerProvider = provider5;
    }

    public PhoneStatusBarViewController.Factory get() {
        return newInstance(this.unfoldComponentProvider.get(), this.progressProvider.get(), this.userSwitcherControllerProvider.get(), this.viewUtilProvider.get(), this.configurationControllerProvider.get());
    }

    public static PhoneStatusBarViewController_Factory_Factory create(Provider<Optional<SysUIUnfoldComponent>> provider, Provider<Optional<ScopedUnfoldTransitionProgressProvider>> provider2, Provider<StatusBarUserSwitcherController> provider3, Provider<ViewUtil> provider4, Provider<ConfigurationController> provider5) {
        return new PhoneStatusBarViewController_Factory_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static PhoneStatusBarViewController.Factory newInstance(Optional<SysUIUnfoldComponent> optional, Optional<ScopedUnfoldTransitionProgressProvider> optional2, StatusBarUserSwitcherController statusBarUserSwitcherController, ViewUtil viewUtil, ConfigurationController configurationController) {
        return new PhoneStatusBarViewController.Factory(optional, optional2, statusBarUserSwitcherController, viewUtil, configurationController);
    }
}

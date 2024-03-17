package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory implements Factory<PhoneStatusBarViewController> {
    public final Provider<NotificationPanelViewController> notificationPanelViewControllerProvider;
    public final Provider<PhoneStatusBarViewController.Factory> phoneStatusBarViewControllerFactoryProvider;
    public final Provider<PhoneStatusBarView> phoneStatusBarViewProvider;

    public StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory(Provider<PhoneStatusBarViewController.Factory> provider, Provider<PhoneStatusBarView> provider2, Provider<NotificationPanelViewController> provider3) {
        this.phoneStatusBarViewControllerFactoryProvider = provider;
        this.phoneStatusBarViewProvider = provider2;
        this.notificationPanelViewControllerProvider = provider3;
    }

    public PhoneStatusBarViewController get() {
        return providePhoneStatusBarViewController(this.phoneStatusBarViewControllerFactoryProvider.get(), this.phoneStatusBarViewProvider.get(), this.notificationPanelViewControllerProvider.get());
    }

    public static StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory create(Provider<PhoneStatusBarViewController.Factory> provider, Provider<PhoneStatusBarView> provider2, Provider<NotificationPanelViewController> provider3) {
        return new StatusBarFragmentModule_ProvidePhoneStatusBarViewControllerFactory(provider, provider2, provider3);
    }

    public static PhoneStatusBarViewController providePhoneStatusBarViewController(PhoneStatusBarViewController.Factory factory, PhoneStatusBarView phoneStatusBarView, NotificationPanelViewController notificationPanelViewController) {
        return (PhoneStatusBarViewController) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.providePhoneStatusBarViewController(factory, phoneStatusBarView, notificationPanelViewController));
    }
}

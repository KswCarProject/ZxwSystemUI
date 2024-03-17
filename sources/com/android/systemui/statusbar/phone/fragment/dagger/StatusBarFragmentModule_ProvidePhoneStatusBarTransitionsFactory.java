package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.PhoneStatusBarTransitions;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory implements Factory<PhoneStatusBarTransitions> {
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory(Provider<PhoneStatusBarView> provider, Provider<StatusBarWindowController> provider2) {
        this.viewProvider = provider;
        this.statusBarWindowControllerProvider = provider2;
    }

    public PhoneStatusBarTransitions get() {
        return providePhoneStatusBarTransitions(this.viewProvider.get(), this.statusBarWindowControllerProvider.get());
    }

    public static StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory create(Provider<PhoneStatusBarView> provider, Provider<StatusBarWindowController> provider2) {
        return new StatusBarFragmentModule_ProvidePhoneStatusBarTransitionsFactory(provider, provider2);
    }

    public static PhoneStatusBarTransitions providePhoneStatusBarTransitions(PhoneStatusBarView phoneStatusBarView, StatusBarWindowController statusBarWindowController) {
        return (PhoneStatusBarTransitions) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.providePhoneStatusBarTransitions(phoneStatusBarView, statusBarWindowController));
    }
}

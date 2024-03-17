package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory implements Factory<StatusBarUserSwitcherContainer> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public StatusBarUserSwitcherContainer get() {
        return provideStatusBarUserSwitcherContainer(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvideStatusBarUserSwitcherContainerFactory(provider);
    }

    public static StatusBarUserSwitcherContainer provideStatusBarUserSwitcherContainer(PhoneStatusBarView phoneStatusBarView) {
        return (StatusBarUserSwitcherContainer) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideStatusBarUserSwitcherContainer(phoneStatusBarView));
    }
}

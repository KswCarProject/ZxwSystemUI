package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.policy.Clock;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvideClockFactory implements Factory<Clock> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvideClockFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public Clock get() {
        return provideClock(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvideClockFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvideClockFactory(provider);
    }

    public static Clock provideClock(PhoneStatusBarView phoneStatusBarView) {
        return (Clock) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideClock(phoneStatusBarView));
    }
}

package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory implements Factory<PhoneStatusBarView> {
    public final Provider<CollapsedStatusBarFragment> collapsedStatusBarFragmentProvider;

    public StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory(Provider<CollapsedStatusBarFragment> provider) {
        this.collapsedStatusBarFragmentProvider = provider;
    }

    public PhoneStatusBarView get() {
        return providePhoneStatusBarView(this.collapsedStatusBarFragmentProvider.get());
    }

    public static StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory create(Provider<CollapsedStatusBarFragment> provider) {
        return new StatusBarFragmentModule_ProvidePhoneStatusBarViewFactory(provider);
    }

    public static PhoneStatusBarView providePhoneStatusBarView(CollapsedStatusBarFragment collapsedStatusBarFragment) {
        return (PhoneStatusBarView) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.providePhoneStatusBarView(collapsedStatusBarFragment));
    }
}

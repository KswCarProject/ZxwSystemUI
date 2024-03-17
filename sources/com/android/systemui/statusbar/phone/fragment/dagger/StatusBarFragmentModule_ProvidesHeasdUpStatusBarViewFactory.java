package com.android.systemui.statusbar.phone.fragment.dagger;

import com.android.systemui.statusbar.HeadsUpStatusBarView;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory implements Factory<HeadsUpStatusBarView> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public HeadsUpStatusBarView get() {
        return providesHeasdUpStatusBarView(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvidesHeasdUpStatusBarViewFactory(provider);
    }

    public static HeadsUpStatusBarView providesHeasdUpStatusBarView(PhoneStatusBarView phoneStatusBarView) {
        return (HeadsUpStatusBarView) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.providesHeasdUpStatusBarView(phoneStatusBarView));
    }
}

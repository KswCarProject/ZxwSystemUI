package com.android.systemui.statusbar.phone.fragment.dagger;

import android.view.View;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvideLightsOutNotifViewFactory implements Factory<View> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvideLightsOutNotifViewFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public View get() {
        return provideLightsOutNotifView(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvideLightsOutNotifViewFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvideLightsOutNotifViewFactory(provider);
    }

    public static View provideLightsOutNotifView(PhoneStatusBarView phoneStatusBarView) {
        return (View) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideLightsOutNotifView(phoneStatusBarView));
    }
}

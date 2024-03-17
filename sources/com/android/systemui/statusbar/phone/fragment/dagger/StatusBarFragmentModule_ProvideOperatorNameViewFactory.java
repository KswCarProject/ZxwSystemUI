package com.android.systemui.statusbar.phone.fragment.dagger;

import android.view.View;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvideOperatorNameViewFactory implements Factory<View> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvideOperatorNameViewFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public View get() {
        return provideOperatorNameView(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvideOperatorNameViewFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvideOperatorNameViewFactory(provider);
    }

    public static View provideOperatorNameView(PhoneStatusBarView phoneStatusBarView) {
        return (View) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideOperatorNameView(phoneStatusBarView));
    }
}

package com.android.systemui.statusbar.phone.fragment.dagger;

import android.view.View;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory implements Factory<Optional<View>> {
    public final Provider<PhoneStatusBarView> viewProvider;

    public StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory(Provider<PhoneStatusBarView> provider) {
        this.viewProvider = provider;
    }

    public Optional<View> get() {
        return provideOperatorFrameNameView(this.viewProvider.get());
    }

    public static StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory create(Provider<PhoneStatusBarView> provider) {
        return new StatusBarFragmentModule_ProvideOperatorFrameNameViewFactory(provider);
    }

    public static Optional<View> provideOperatorFrameNameView(PhoneStatusBarView phoneStatusBarView) {
        return (Optional) Preconditions.checkNotNullFromProvides(StatusBarFragmentModule.provideOperatorFrameNameView(phoneStatusBarView));
    }
}

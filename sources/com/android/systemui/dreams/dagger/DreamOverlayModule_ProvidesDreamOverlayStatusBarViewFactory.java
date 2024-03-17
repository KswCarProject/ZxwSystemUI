package com.android.systemui.dreams.dagger;

import com.android.systemui.dreams.DreamOverlayContainerView;
import com.android.systemui.dreams.DreamOverlayStatusBarView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory implements Factory<DreamOverlayStatusBarView> {
    public final Provider<DreamOverlayContainerView> viewProvider;

    public DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory(Provider<DreamOverlayContainerView> provider) {
        this.viewProvider = provider;
    }

    public DreamOverlayStatusBarView get() {
        return providesDreamOverlayStatusBarView(this.viewProvider.get());
    }

    public static DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory create(Provider<DreamOverlayContainerView> provider) {
        return new DreamOverlayModule_ProvidesDreamOverlayStatusBarViewFactory(provider);
    }

    public static DreamOverlayStatusBarView providesDreamOverlayStatusBarView(DreamOverlayContainerView dreamOverlayContainerView) {
        return (DreamOverlayStatusBarView) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesDreamOverlayStatusBarView(dreamOverlayContainerView));
    }
}

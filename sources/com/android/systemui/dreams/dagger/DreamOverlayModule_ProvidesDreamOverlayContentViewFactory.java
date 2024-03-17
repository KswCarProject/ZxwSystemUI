package com.android.systemui.dreams.dagger;

import android.view.ViewGroup;
import com.android.systemui.dreams.DreamOverlayContainerView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesDreamOverlayContentViewFactory implements Factory<ViewGroup> {
    public final Provider<DreamOverlayContainerView> viewProvider;

    public DreamOverlayModule_ProvidesDreamOverlayContentViewFactory(Provider<DreamOverlayContainerView> provider) {
        this.viewProvider = provider;
    }

    public ViewGroup get() {
        return providesDreamOverlayContentView(this.viewProvider.get());
    }

    public static DreamOverlayModule_ProvidesDreamOverlayContentViewFactory create(Provider<DreamOverlayContainerView> provider) {
        return new DreamOverlayModule_ProvidesDreamOverlayContentViewFactory(provider);
    }

    public static ViewGroup providesDreamOverlayContentView(DreamOverlayContainerView dreamOverlayContainerView) {
        return (ViewGroup) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesDreamOverlayContentView(dreamOverlayContainerView));
    }
}

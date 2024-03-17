package com.android.systemui.dreams.dagger;

import android.view.LayoutInflater;
import com.android.systemui.dreams.DreamOverlayContainerView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory implements Factory<DreamOverlayContainerView> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public DreamOverlayContainerView get() {
        return providesDreamOverlayContainerView(this.layoutInflaterProvider.get());
    }

    public static DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory create(Provider<LayoutInflater> provider) {
        return new DreamOverlayModule_ProvidesDreamOverlayContainerViewFactory(provider);
    }

    public static DreamOverlayContainerView providesDreamOverlayContainerView(LayoutInflater layoutInflater) {
        return (DreamOverlayContainerView) Preconditions.checkNotNullFromProvides(DreamOverlayModule.providesDreamOverlayContainerView(layoutInflater));
    }
}

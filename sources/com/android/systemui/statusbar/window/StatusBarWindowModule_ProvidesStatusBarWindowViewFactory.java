package com.android.systemui.statusbar.window;

import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class StatusBarWindowModule_ProvidesStatusBarWindowViewFactory implements Factory<StatusBarWindowView> {
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public StatusBarWindowModule_ProvidesStatusBarWindowViewFactory(Provider<LayoutInflater> provider) {
        this.layoutInflaterProvider = provider;
    }

    public StatusBarWindowView get() {
        return providesStatusBarWindowView(this.layoutInflaterProvider.get());
    }

    public static StatusBarWindowModule_ProvidesStatusBarWindowViewFactory create(Provider<LayoutInflater> provider) {
        return new StatusBarWindowModule_ProvidesStatusBarWindowViewFactory(provider);
    }

    public static StatusBarWindowView providesStatusBarWindowView(LayoutInflater layoutInflater) {
        return (StatusBarWindowView) Preconditions.checkNotNullFromProvides(StatusBarWindowModule.providesStatusBarWindowView(layoutInflater));
    }
}

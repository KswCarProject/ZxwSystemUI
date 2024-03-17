package com.android.keyguard.dagger;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardHostView;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class KeyguardBouncerModule_ProvidesKeyguardHostViewFactory implements Factory<KeyguardHostView> {
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<ViewGroup> rootViewProvider;

    public KeyguardBouncerModule_ProvidesKeyguardHostViewFactory(Provider<ViewGroup> provider, Provider<LayoutInflater> provider2) {
        this.rootViewProvider = provider;
        this.layoutInflaterProvider = provider2;
    }

    public KeyguardHostView get() {
        return providesKeyguardHostView(this.rootViewProvider.get(), this.layoutInflaterProvider.get());
    }

    public static KeyguardBouncerModule_ProvidesKeyguardHostViewFactory create(Provider<ViewGroup> provider, Provider<LayoutInflater> provider2) {
        return new KeyguardBouncerModule_ProvidesKeyguardHostViewFactory(provider, provider2);
    }

    public static KeyguardHostView providesKeyguardHostView(ViewGroup viewGroup, LayoutInflater layoutInflater) {
        return (KeyguardHostView) Preconditions.checkNotNullFromProvides(KeyguardBouncerModule.providesKeyguardHostView(viewGroup, layoutInflater));
    }
}

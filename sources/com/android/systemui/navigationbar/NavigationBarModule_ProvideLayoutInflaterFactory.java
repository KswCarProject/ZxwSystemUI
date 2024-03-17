package com.android.systemui.navigationbar;

import android.content.Context;
import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NavigationBarModule_ProvideLayoutInflaterFactory implements Factory<LayoutInflater> {
    public final Provider<Context> contextProvider;

    public NavigationBarModule_ProvideLayoutInflaterFactory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    public LayoutInflater get() {
        return provideLayoutInflater(this.contextProvider.get());
    }

    public static NavigationBarModule_ProvideLayoutInflaterFactory create(Provider<Context> provider) {
        return new NavigationBarModule_ProvideLayoutInflaterFactory(provider);
    }

    public static LayoutInflater provideLayoutInflater(Context context) {
        return (LayoutInflater) Preconditions.checkNotNullFromProvides(NavigationBarModule.provideLayoutInflater(context));
    }
}

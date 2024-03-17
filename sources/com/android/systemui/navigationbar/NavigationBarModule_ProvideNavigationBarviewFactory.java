package com.android.systemui.navigationbar;

import android.view.LayoutInflater;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class NavigationBarModule_ProvideNavigationBarviewFactory implements Factory<NavigationBarView> {
    public final Provider<NavigationBarFrame> frameProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;

    public NavigationBarModule_ProvideNavigationBarviewFactory(Provider<LayoutInflater> provider, Provider<NavigationBarFrame> provider2) {
        this.layoutInflaterProvider = provider;
        this.frameProvider = provider2;
    }

    public NavigationBarView get() {
        return provideNavigationBarview(this.layoutInflaterProvider.get(), this.frameProvider.get());
    }

    public static NavigationBarModule_ProvideNavigationBarviewFactory create(Provider<LayoutInflater> provider, Provider<NavigationBarFrame> provider2) {
        return new NavigationBarModule_ProvideNavigationBarviewFactory(provider, provider2);
    }

    public static NavigationBarView provideNavigationBarview(LayoutInflater layoutInflater, NavigationBarFrame navigationBarFrame) {
        return (NavigationBarView) Preconditions.checkNotNullFromProvides(NavigationBarModule.provideNavigationBarview(layoutInflater, navigationBarFrame));
    }
}

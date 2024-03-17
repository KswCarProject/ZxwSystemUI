package com.android.systemui.theme;

import android.content.res.Resources;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ThemeModule_ProvideLauncherPackageFactory implements Factory<String> {
    public final Provider<Resources> resourcesProvider;

    public ThemeModule_ProvideLauncherPackageFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public String get() {
        return provideLauncherPackage(this.resourcesProvider.get());
    }

    public static ThemeModule_ProvideLauncherPackageFactory create(Provider<Resources> provider) {
        return new ThemeModule_ProvideLauncherPackageFactory(provider);
    }

    public static String provideLauncherPackage(Resources resources) {
        return (String) Preconditions.checkNotNullFromProvides(ThemeModule.provideLauncherPackage(resources));
    }
}

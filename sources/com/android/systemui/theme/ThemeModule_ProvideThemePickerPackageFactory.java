package com.android.systemui.theme;

import android.content.res.Resources;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class ThemeModule_ProvideThemePickerPackageFactory implements Factory<String> {
    public final Provider<Resources> resourcesProvider;

    public ThemeModule_ProvideThemePickerPackageFactory(Provider<Resources> provider) {
        this.resourcesProvider = provider;
    }

    public String get() {
        return provideThemePickerPackage(this.resourcesProvider.get());
    }

    public static ThemeModule_ProvideThemePickerPackageFactory create(Provider<Resources> provider) {
        return new ThemeModule_ProvideThemePickerPackageFactory(provider);
    }

    public static String provideThemePickerPackage(Resources resources) {
        return (String) Preconditions.checkNotNullFromProvides(ThemeModule.provideThemePickerPackage(resources));
    }
}

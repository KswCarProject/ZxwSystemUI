package com.android.systemui.unfold;

import com.android.systemui.util.WallpaperController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UnfoldTransitionWallpaperController_Factory implements Factory<UnfoldTransitionWallpaperController> {
    public final Provider<UnfoldTransitionProgressProvider> unfoldTransitionProgressProvider;
    public final Provider<WallpaperController> wallpaperControllerProvider;

    public UnfoldTransitionWallpaperController_Factory(Provider<UnfoldTransitionProgressProvider> provider, Provider<WallpaperController> provider2) {
        this.unfoldTransitionProgressProvider = provider;
        this.wallpaperControllerProvider = provider2;
    }

    public UnfoldTransitionWallpaperController get() {
        return newInstance(this.unfoldTransitionProgressProvider.get(), this.wallpaperControllerProvider.get());
    }

    public static UnfoldTransitionWallpaperController_Factory create(Provider<UnfoldTransitionProgressProvider> provider, Provider<WallpaperController> provider2) {
        return new UnfoldTransitionWallpaperController_Factory(provider, provider2);
    }

    public static UnfoldTransitionWallpaperController newInstance(UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2, WallpaperController wallpaperController) {
        return new UnfoldTransitionWallpaperController(unfoldTransitionProgressProvider2, wallpaperController);
    }
}

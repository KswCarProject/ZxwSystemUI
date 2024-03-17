package com.android.systemui.util;

import android.app.WallpaperManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class WallpaperController_Factory implements Factory<WallpaperController> {
    public final Provider<WallpaperManager> wallpaperManagerProvider;

    public WallpaperController_Factory(Provider<WallpaperManager> provider) {
        this.wallpaperManagerProvider = provider;
    }

    public WallpaperController get() {
        return newInstance(this.wallpaperManagerProvider.get());
    }

    public static WallpaperController_Factory create(Provider<WallpaperManager> provider) {
        return new WallpaperController_Factory(provider);
    }

    public static WallpaperController newInstance(WallpaperManager wallpaperManager) {
        return new WallpaperController(wallpaperManager);
    }
}

package com.android.systemui.statusbar.phone;

import android.app.IWallpaperManager;
import android.app.WallpaperManager;
import android.os.Handler;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LockscreenWallpaper_Factory implements Factory<LockscreenWallpaper> {
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<IWallpaperManager> iWallpaperManagerProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<NotificationMediaManager> mediaManagerProvider;
    public final Provider<WallpaperManager> wallpaperManagerProvider;

    public LockscreenWallpaper_Factory(Provider<WallpaperManager> provider, Provider<IWallpaperManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<DumpManager> provider4, Provider<NotificationMediaManager> provider5, Provider<Handler> provider6) {
        this.wallpaperManagerProvider = provider;
        this.iWallpaperManagerProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
        this.dumpManagerProvider = provider4;
        this.mediaManagerProvider = provider5;
        this.mainHandlerProvider = provider6;
    }

    public LockscreenWallpaper get() {
        return newInstance(this.wallpaperManagerProvider.get(), this.iWallpaperManagerProvider.get(), this.keyguardUpdateMonitorProvider.get(), this.dumpManagerProvider.get(), this.mediaManagerProvider.get(), this.mainHandlerProvider.get());
    }

    public static LockscreenWallpaper_Factory create(Provider<WallpaperManager> provider, Provider<IWallpaperManager> provider2, Provider<KeyguardUpdateMonitor> provider3, Provider<DumpManager> provider4, Provider<NotificationMediaManager> provider5, Provider<Handler> provider6) {
        return new LockscreenWallpaper_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static LockscreenWallpaper newInstance(WallpaperManager wallpaperManager, IWallpaperManager iWallpaperManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, NotificationMediaManager notificationMediaManager, Handler handler) {
        return new LockscreenWallpaper(wallpaperManager, iWallpaperManager, keyguardUpdateMonitor, dumpManager, notificationMediaManager, handler);
    }
}

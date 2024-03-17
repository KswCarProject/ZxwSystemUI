package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.content.Context;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class WakefulnessLifecycle_Factory implements Factory<WakefulnessLifecycle> {
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<IWallpaperManager> wallpaperManagerServiceProvider;

    public WakefulnessLifecycle_Factory(Provider<Context> provider, Provider<IWallpaperManager> provider2, Provider<DumpManager> provider3) {
        this.contextProvider = provider;
        this.wallpaperManagerServiceProvider = provider2;
        this.dumpManagerProvider = provider3;
    }

    public WakefulnessLifecycle get() {
        return newInstance(this.contextProvider.get(), this.wallpaperManagerServiceProvider.get(), this.dumpManagerProvider.get());
    }

    public static WakefulnessLifecycle_Factory create(Provider<Context> provider, Provider<IWallpaperManager> provider2, Provider<DumpManager> provider3) {
        return new WakefulnessLifecycle_Factory(provider, provider2, provider3);
    }

    public static WakefulnessLifecycle newInstance(Context context, IWallpaperManager iWallpaperManager, DumpManager dumpManager) {
        return new WakefulnessLifecycle(context, iWallpaperManager, dumpManager);
    }
}

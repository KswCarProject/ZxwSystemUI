package com.android.systemui.theme;

import android.content.om.OverlayManager;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ThemeOverlayApplier_Factory implements Factory<ThemeOverlayApplier> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<String> launcherPackageProvider;
    public final Provider<OverlayManager> overlayManagerProvider;
    public final Provider<String> themePickerPackageProvider;

    public ThemeOverlayApplier_Factory(Provider<OverlayManager> provider, Provider<Executor> provider2, Provider<String> provider3, Provider<String> provider4, Provider<DumpManager> provider5) {
        this.overlayManagerProvider = provider;
        this.bgExecutorProvider = provider2;
        this.launcherPackageProvider = provider3;
        this.themePickerPackageProvider = provider4;
        this.dumpManagerProvider = provider5;
    }

    public ThemeOverlayApplier get() {
        return newInstance(this.overlayManagerProvider.get(), this.bgExecutorProvider.get(), this.launcherPackageProvider.get(), this.themePickerPackageProvider.get(), this.dumpManagerProvider.get());
    }

    public static ThemeOverlayApplier_Factory create(Provider<OverlayManager> provider, Provider<Executor> provider2, Provider<String> provider3, Provider<String> provider4, Provider<DumpManager> provider5) {
        return new ThemeOverlayApplier_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ThemeOverlayApplier newInstance(OverlayManager overlayManager, Executor executor, String str, String str2, DumpManager dumpManager) {
        return new ThemeOverlayApplier(overlayManager, executor, str, str2, dumpManager);
    }
}

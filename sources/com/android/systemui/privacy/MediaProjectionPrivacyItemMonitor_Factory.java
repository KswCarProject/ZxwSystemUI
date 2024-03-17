package com.android.systemui.privacy;

import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaProjectionPrivacyItemMonitor_Factory implements Factory<MediaProjectionPrivacyItemMonitor> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<PrivacyLogger> loggerProvider;
    public final Provider<MediaProjectionManager> mediaProjectionManagerProvider;
    public final Provider<PackageManager> packageManagerProvider;
    public final Provider<PrivacyConfig> privacyConfigProvider;
    public final Provider<SystemClock> systemClockProvider;

    public MediaProjectionPrivacyItemMonitor_Factory(Provider<MediaProjectionManager> provider, Provider<PackageManager> provider2, Provider<PrivacyConfig> provider3, Provider<Handler> provider4, Provider<SystemClock> provider5, Provider<PrivacyLogger> provider6) {
        this.mediaProjectionManagerProvider = provider;
        this.packageManagerProvider = provider2;
        this.privacyConfigProvider = provider3;
        this.bgHandlerProvider = provider4;
        this.systemClockProvider = provider5;
        this.loggerProvider = provider6;
    }

    public MediaProjectionPrivacyItemMonitor get() {
        return newInstance(this.mediaProjectionManagerProvider.get(), this.packageManagerProvider.get(), this.privacyConfigProvider.get(), this.bgHandlerProvider.get(), this.systemClockProvider.get(), this.loggerProvider.get());
    }

    public static MediaProjectionPrivacyItemMonitor_Factory create(Provider<MediaProjectionManager> provider, Provider<PackageManager> provider2, Provider<PrivacyConfig> provider3, Provider<Handler> provider4, Provider<SystemClock> provider5, Provider<PrivacyLogger> provider6) {
        return new MediaProjectionPrivacyItemMonitor_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static MediaProjectionPrivacyItemMonitor newInstance(MediaProjectionManager mediaProjectionManager, PackageManager packageManager, PrivacyConfig privacyConfig, Handler handler, SystemClock systemClock, PrivacyLogger privacyLogger) {
        return new MediaProjectionPrivacyItemMonitor(mediaProjectionManager, packageManager, privacyConfig, handler, systemClock, privacyLogger);
    }
}

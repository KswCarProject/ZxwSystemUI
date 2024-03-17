package com.android.systemui.statusbar.phone;

import android.util.DisplayMetrics;
import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class LSShadeTransitionLogger_Factory implements Factory<LSShadeTransitionLogger> {
    public final Provider<LogBuffer> bufferProvider;
    public final Provider<DisplayMetrics> displayMetricsProvider;
    public final Provider<LockscreenGestureLogger> lockscreenGestureLoggerProvider;

    public LSShadeTransitionLogger_Factory(Provider<LogBuffer> provider, Provider<LockscreenGestureLogger> provider2, Provider<DisplayMetrics> provider3) {
        this.bufferProvider = provider;
        this.lockscreenGestureLoggerProvider = provider2;
        this.displayMetricsProvider = provider3;
    }

    public LSShadeTransitionLogger get() {
        return newInstance(this.bufferProvider.get(), this.lockscreenGestureLoggerProvider.get(), this.displayMetricsProvider.get());
    }

    public static LSShadeTransitionLogger_Factory create(Provider<LogBuffer> provider, Provider<LockscreenGestureLogger> provider2, Provider<DisplayMetrics> provider3) {
        return new LSShadeTransitionLogger_Factory(provider, provider2, provider3);
    }

    public static LSShadeTransitionLogger newInstance(LogBuffer logBuffer, LockscreenGestureLogger lockscreenGestureLogger, DisplayMetrics displayMetrics) {
        return new LSShadeTransitionLogger(logBuffer, lockscreenGestureLogger, displayMetrics);
    }
}

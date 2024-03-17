package com.android.systemui.statusbar.gesture;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SwipeStatusBarAwayGestureLogger_Factory implements Factory<SwipeStatusBarAwayGestureLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public SwipeStatusBarAwayGestureLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public SwipeStatusBarAwayGestureLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static SwipeStatusBarAwayGestureLogger_Factory create(Provider<LogBuffer> provider) {
        return new SwipeStatusBarAwayGestureLogger_Factory(provider);
    }

    public static SwipeStatusBarAwayGestureLogger newInstance(LogBuffer logBuffer) {
        return new SwipeStatusBarAwayGestureLogger(logBuffer);
    }
}

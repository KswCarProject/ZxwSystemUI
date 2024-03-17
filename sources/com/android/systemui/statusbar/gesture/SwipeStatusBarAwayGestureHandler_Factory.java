package com.android.systemui.statusbar.gesture;

import android.content.Context;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SwipeStatusBarAwayGestureHandler_Factory implements Factory<SwipeStatusBarAwayGestureHandler> {
    public final Provider<Context> contextProvider;
    public final Provider<SwipeStatusBarAwayGestureLogger> loggerProvider;
    public final Provider<StatusBarWindowController> statusBarWindowControllerProvider;

    public SwipeStatusBarAwayGestureHandler_Factory(Provider<Context> provider, Provider<StatusBarWindowController> provider2, Provider<SwipeStatusBarAwayGestureLogger> provider3) {
        this.contextProvider = provider;
        this.statusBarWindowControllerProvider = provider2;
        this.loggerProvider = provider3;
    }

    public SwipeStatusBarAwayGestureHandler get() {
        return newInstance(this.contextProvider.get(), this.statusBarWindowControllerProvider.get(), this.loggerProvider.get());
    }

    public static SwipeStatusBarAwayGestureHandler_Factory create(Provider<Context> provider, Provider<StatusBarWindowController> provider2, Provider<SwipeStatusBarAwayGestureLogger> provider3) {
        return new SwipeStatusBarAwayGestureHandler_Factory(provider, provider2, provider3);
    }

    public static SwipeStatusBarAwayGestureHandler newInstance(Context context, StatusBarWindowController statusBarWindowController, SwipeStatusBarAwayGestureLogger swipeStatusBarAwayGestureLogger) {
        return new SwipeStatusBarAwayGestureHandler(context, statusBarWindowController, swipeStatusBarAwayGestureLogger);
    }
}

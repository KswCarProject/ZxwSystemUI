package com.android.systemui.screenshot;

import android.content.Context;
import android.view.WindowManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ScreenshotNotificationsController_Factory implements Factory<ScreenshotNotificationsController> {
    public final Provider<Context> contextProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public ScreenshotNotificationsController_Factory(Provider<Context> provider, Provider<WindowManager> provider2) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
    }

    public ScreenshotNotificationsController get() {
        return newInstance(this.contextProvider.get(), this.windowManagerProvider.get());
    }

    public static ScreenshotNotificationsController_Factory create(Provider<Context> provider, Provider<WindowManager> provider2) {
        return new ScreenshotNotificationsController_Factory(provider, provider2);
    }

    public static ScreenshotNotificationsController newInstance(Context context, WindowManager windowManager) {
        return new ScreenshotNotificationsController(context, windowManager);
    }
}

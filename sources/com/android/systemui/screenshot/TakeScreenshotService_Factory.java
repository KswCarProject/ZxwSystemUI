package com.android.systemui.screenshot;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.UserManager;
import com.android.internal.logging.UiEventLogger;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class TakeScreenshotService_Factory implements Factory<TakeScreenshotService> {
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DevicePolicyManager> devicePolicyManagerProvider;
    public final Provider<ScreenshotNotificationsController> notificationsControllerProvider;
    public final Provider<ScreenshotController> screenshotControllerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserManager> userManagerProvider;

    public TakeScreenshotService_Factory(Provider<ScreenshotController> provider, Provider<UserManager> provider2, Provider<DevicePolicyManager> provider3, Provider<UiEventLogger> provider4, Provider<ScreenshotNotificationsController> provider5, Provider<Context> provider6, Provider<Executor> provider7) {
        this.screenshotControllerProvider = provider;
        this.userManagerProvider = provider2;
        this.devicePolicyManagerProvider = provider3;
        this.uiEventLoggerProvider = provider4;
        this.notificationsControllerProvider = provider5;
        this.contextProvider = provider6;
        this.bgExecutorProvider = provider7;
    }

    public TakeScreenshotService get() {
        return newInstance(this.screenshotControllerProvider.get(), this.userManagerProvider.get(), this.devicePolicyManagerProvider.get(), this.uiEventLoggerProvider.get(), this.notificationsControllerProvider.get(), this.contextProvider.get(), this.bgExecutorProvider.get());
    }

    public static TakeScreenshotService_Factory create(Provider<ScreenshotController> provider, Provider<UserManager> provider2, Provider<DevicePolicyManager> provider3, Provider<UiEventLogger> provider4, Provider<ScreenshotNotificationsController> provider5, Provider<Context> provider6, Provider<Executor> provider7) {
        return new TakeScreenshotService_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static TakeScreenshotService newInstance(ScreenshotController screenshotController, UserManager userManager, DevicePolicyManager devicePolicyManager, UiEventLogger uiEventLogger, ScreenshotNotificationsController screenshotNotificationsController, Context context, Executor executor) {
        return new TakeScreenshotService(screenshotController, userManager, devicePolicyManager, uiEventLogger, screenshotNotificationsController, context, executor);
    }
}

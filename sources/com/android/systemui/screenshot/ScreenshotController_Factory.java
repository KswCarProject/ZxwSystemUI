package com.android.systemui.screenshot;

import android.app.ActivityManager;
import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.broadcast.BroadcastSender;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ScreenshotController_Factory implements Factory<ScreenshotController> {
    public final Provider<ActivityManager> activityManagerProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<ImageExporter> imageExporterProvider;
    public final Provider<LongScreenshotData> longScreenshotHolderProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<ScreenshotNotificationsController> screenshotNotificationsControllerProvider;
    public final Provider<ScreenshotSmartActions> screenshotSmartActionsProvider;
    public final Provider<ScrollCaptureClient> scrollCaptureClientProvider;
    public final Provider<ScrollCaptureController> scrollCaptureControllerProvider;
    public final Provider<TimeoutHandler> timeoutHandlerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;

    public ScreenshotController_Factory(Provider<Context> provider, Provider<ScreenshotSmartActions> provider2, Provider<ScreenshotNotificationsController> provider3, Provider<ScrollCaptureClient> provider4, Provider<UiEventLogger> provider5, Provider<ImageExporter> provider6, Provider<Executor> provider7, Provider<ScrollCaptureController> provider8, Provider<LongScreenshotData> provider9, Provider<ActivityManager> provider10, Provider<TimeoutHandler> provider11, Provider<BroadcastSender> provider12) {
        this.contextProvider = provider;
        this.screenshotSmartActionsProvider = provider2;
        this.screenshotNotificationsControllerProvider = provider3;
        this.scrollCaptureClientProvider = provider4;
        this.uiEventLoggerProvider = provider5;
        this.imageExporterProvider = provider6;
        this.mainExecutorProvider = provider7;
        this.scrollCaptureControllerProvider = provider8;
        this.longScreenshotHolderProvider = provider9;
        this.activityManagerProvider = provider10;
        this.timeoutHandlerProvider = provider11;
        this.broadcastSenderProvider = provider12;
    }

    public ScreenshotController get() {
        return newInstance(this.contextProvider.get(), this.screenshotSmartActionsProvider.get(), this.screenshotNotificationsControllerProvider.get(), this.scrollCaptureClientProvider.get(), this.uiEventLoggerProvider.get(), this.imageExporterProvider.get(), this.mainExecutorProvider.get(), this.scrollCaptureControllerProvider.get(), this.longScreenshotHolderProvider.get(), this.activityManagerProvider.get(), this.timeoutHandlerProvider.get(), this.broadcastSenderProvider.get());
    }

    public static ScreenshotController_Factory create(Provider<Context> provider, Provider<ScreenshotSmartActions> provider2, Provider<ScreenshotNotificationsController> provider3, Provider<ScrollCaptureClient> provider4, Provider<UiEventLogger> provider5, Provider<ImageExporter> provider6, Provider<Executor> provider7, Provider<ScrollCaptureController> provider8, Provider<LongScreenshotData> provider9, Provider<ActivityManager> provider10, Provider<TimeoutHandler> provider11, Provider<BroadcastSender> provider12) {
        return new ScreenshotController_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static ScreenshotController newInstance(Context context, ScreenshotSmartActions screenshotSmartActions, ScreenshotNotificationsController screenshotNotificationsController, ScrollCaptureClient scrollCaptureClient, UiEventLogger uiEventLogger, Object obj, Executor executor, ScrollCaptureController scrollCaptureController, LongScreenshotData longScreenshotData, ActivityManager activityManager, TimeoutHandler timeoutHandler, BroadcastSender broadcastSender) {
        return new ScreenshotController(context, screenshotSmartActions, screenshotNotificationsController, scrollCaptureClient, uiEventLogger, (ImageExporter) obj, executor, scrollCaptureController, longScreenshotData, activityManager, timeoutHandler, broadcastSender);
    }
}

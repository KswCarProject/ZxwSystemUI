package com.android.systemui.screenrecord;

import android.app.NotificationManager;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class RecordingService_Factory implements Factory<RecordingService> {
    public final Provider<RecordingController> controllerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    public final Provider<NotificationManager> notificationManagerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserContextProvider> userContextTrackerProvider;

    public RecordingService_Factory(Provider<RecordingController> provider, Provider<Executor> provider2, Provider<UiEventLogger> provider3, Provider<NotificationManager> provider4, Provider<UserContextProvider> provider5, Provider<KeyguardDismissUtil> provider6) {
        this.controllerProvider = provider;
        this.executorProvider = provider2;
        this.uiEventLoggerProvider = provider3;
        this.notificationManagerProvider = provider4;
        this.userContextTrackerProvider = provider5;
        this.keyguardDismissUtilProvider = provider6;
    }

    public RecordingService get() {
        return newInstance(this.controllerProvider.get(), this.executorProvider.get(), this.uiEventLoggerProvider.get(), this.notificationManagerProvider.get(), this.userContextTrackerProvider.get(), this.keyguardDismissUtilProvider.get());
    }

    public static RecordingService_Factory create(Provider<RecordingController> provider, Provider<Executor> provider2, Provider<UiEventLogger> provider3, Provider<NotificationManager> provider4, Provider<UserContextProvider> provider5, Provider<KeyguardDismissUtil> provider6) {
        return new RecordingService_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static RecordingService newInstance(RecordingController recordingController, Executor executor, UiEventLogger uiEventLogger, NotificationManager notificationManager, UserContextProvider userContextProvider, KeyguardDismissUtil keyguardDismissUtil) {
        return new RecordingService(recordingController, executor, uiEventLogger, notificationManager, userContextProvider, keyguardDismissUtil);
    }
}

package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaDataFilter_Factory implements Factory<MediaDataFilter> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<BroadcastSender> broadcastSenderProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<NotificationLockscreenUserManager> lockscreenUserManagerProvider;
    public final Provider<MediaUiEventLogger> loggerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public MediaDataFilter_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<BroadcastSender> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<Executor> provider5, Provider<SystemClock> provider6, Provider<MediaUiEventLogger> provider7) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.broadcastSenderProvider = provider3;
        this.lockscreenUserManagerProvider = provider4;
        this.executorProvider = provider5;
        this.systemClockProvider = provider6;
        this.loggerProvider = provider7;
    }

    public MediaDataFilter get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.broadcastSenderProvider.get(), this.lockscreenUserManagerProvider.get(), this.executorProvider.get(), this.systemClockProvider.get(), this.loggerProvider.get());
    }

    public static MediaDataFilter_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<BroadcastSender> provider3, Provider<NotificationLockscreenUserManager> provider4, Provider<Executor> provider5, Provider<SystemClock> provider6, Provider<MediaUiEventLogger> provider7) {
        return new MediaDataFilter_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static MediaDataFilter newInstance(Context context, BroadcastDispatcher broadcastDispatcher, BroadcastSender broadcastSender, NotificationLockscreenUserManager notificationLockscreenUserManager, Executor executor, SystemClock systemClock, MediaUiEventLogger mediaUiEventLogger) {
        return new MediaDataFilter(context, broadcastDispatcher, broadcastSender, notificationLockscreenUserManager, executor, systemClock, mediaUiEventLogger);
    }
}

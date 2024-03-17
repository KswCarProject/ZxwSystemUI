package com.android.systemui.media;

import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTimeoutListener_Factory implements Factory<MediaTimeoutListener> {
    public final Provider<MediaTimeoutLogger> loggerProvider;
    public final Provider<DelayableExecutor> mainExecutorProvider;
    public final Provider<MediaControllerFactory> mediaControllerFactoryProvider;
    public final Provider<SysuiStatusBarStateController> statusBarStateControllerProvider;
    public final Provider<SystemClock> systemClockProvider;

    public MediaTimeoutListener_Factory(Provider<MediaControllerFactory> provider, Provider<DelayableExecutor> provider2, Provider<MediaTimeoutLogger> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<SystemClock> provider5) {
        this.mediaControllerFactoryProvider = provider;
        this.mainExecutorProvider = provider2;
        this.loggerProvider = provider3;
        this.statusBarStateControllerProvider = provider4;
        this.systemClockProvider = provider5;
    }

    public MediaTimeoutListener get() {
        return newInstance(this.mediaControllerFactoryProvider.get(), this.mainExecutorProvider.get(), this.loggerProvider.get(), this.statusBarStateControllerProvider.get(), this.systemClockProvider.get());
    }

    public static MediaTimeoutListener_Factory create(Provider<MediaControllerFactory> provider, Provider<DelayableExecutor> provider2, Provider<MediaTimeoutLogger> provider3, Provider<SysuiStatusBarStateController> provider4, Provider<SystemClock> provider5) {
        return new MediaTimeoutListener_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static MediaTimeoutListener newInstance(MediaControllerFactory mediaControllerFactory, DelayableExecutor delayableExecutor, MediaTimeoutLogger mediaTimeoutLogger, SysuiStatusBarStateController sysuiStatusBarStateController, SystemClock systemClock) {
        return new MediaTimeoutListener(mediaControllerFactory, delayableExecutor, mediaTimeoutLogger, sysuiStatusBarStateController, systemClock);
    }
}

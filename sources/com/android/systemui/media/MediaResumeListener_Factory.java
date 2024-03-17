package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaResumeListener_Factory implements Factory<MediaResumeListener> {
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<ResumeMediaBrowserFactory> mediaBrowserFactoryProvider;
    public final Provider<SystemClock> systemClockProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public MediaResumeListener_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Executor> provider3, Provider<TunerService> provider4, Provider<ResumeMediaBrowserFactory> provider5, Provider<DumpManager> provider6, Provider<SystemClock> provider7) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.backgroundExecutorProvider = provider3;
        this.tunerServiceProvider = provider4;
        this.mediaBrowserFactoryProvider = provider5;
        this.dumpManagerProvider = provider6;
        this.systemClockProvider = provider7;
    }

    public MediaResumeListener get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.backgroundExecutorProvider.get(), this.tunerServiceProvider.get(), this.mediaBrowserFactoryProvider.get(), this.dumpManagerProvider.get(), this.systemClockProvider.get());
    }

    public static MediaResumeListener_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<Executor> provider3, Provider<TunerService> provider4, Provider<ResumeMediaBrowserFactory> provider5, Provider<DumpManager> provider6, Provider<SystemClock> provider7) {
        return new MediaResumeListener_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static MediaResumeListener newInstance(Context context, BroadcastDispatcher broadcastDispatcher, Executor executor, TunerService tunerService, ResumeMediaBrowserFactory resumeMediaBrowserFactory, DumpManager dumpManager, SystemClock systemClock) {
        return new MediaResumeListener(context, broadcastDispatcher, executor, tunerService, resumeMediaBrowserFactory, dumpManager, systemClock);
    }
}

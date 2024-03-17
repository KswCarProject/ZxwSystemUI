package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaDataManager_Factory implements Factory<MediaDataManager> {
    public final Provider<ActivityStarter> activityStarterProvider;
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<SystemClock> clockProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> foregroundExecutorProvider;
    public final Provider<MediaUiEventLogger> loggerProvider;
    public final Provider<MediaControllerFactory> mediaControllerFactoryProvider;
    public final Provider<MediaDataCombineLatest> mediaDataCombineLatestProvider;
    public final Provider<MediaDataFilter> mediaDataFilterProvider;
    public final Provider<MediaDeviceManager> mediaDeviceManagerProvider;
    public final Provider<MediaFlags> mediaFlagsProvider;
    public final Provider<MediaResumeListener> mediaResumeListenerProvider;
    public final Provider<MediaSessionBasedFilter> mediaSessionBasedFilterProvider;
    public final Provider<MediaTimeoutListener> mediaTimeoutListenerProvider;
    public final Provider<SmartspaceMediaDataProvider> smartspaceMediaDataProvider;
    public final Provider<TunerService> tunerServiceProvider;

    public MediaDataManager_Factory(Provider<Context> provider, Provider<Executor> provider2, Provider<DelayableExecutor> provider3, Provider<MediaControllerFactory> provider4, Provider<DumpManager> provider5, Provider<BroadcastDispatcher> provider6, Provider<MediaTimeoutListener> provider7, Provider<MediaResumeListener> provider8, Provider<MediaSessionBasedFilter> provider9, Provider<MediaDeviceManager> provider10, Provider<MediaDataCombineLatest> provider11, Provider<MediaDataFilter> provider12, Provider<ActivityStarter> provider13, Provider<SmartspaceMediaDataProvider> provider14, Provider<SystemClock> provider15, Provider<TunerService> provider16, Provider<MediaFlags> provider17, Provider<MediaUiEventLogger> provider18) {
        this.contextProvider = provider;
        this.backgroundExecutorProvider = provider2;
        this.foregroundExecutorProvider = provider3;
        this.mediaControllerFactoryProvider = provider4;
        this.dumpManagerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.mediaTimeoutListenerProvider = provider7;
        this.mediaResumeListenerProvider = provider8;
        this.mediaSessionBasedFilterProvider = provider9;
        this.mediaDeviceManagerProvider = provider10;
        this.mediaDataCombineLatestProvider = provider11;
        this.mediaDataFilterProvider = provider12;
        this.activityStarterProvider = provider13;
        this.smartspaceMediaDataProvider = provider14;
        this.clockProvider = provider15;
        this.tunerServiceProvider = provider16;
        this.mediaFlagsProvider = provider17;
        this.loggerProvider = provider18;
    }

    public MediaDataManager get() {
        return newInstance(this.contextProvider.get(), this.backgroundExecutorProvider.get(), this.foregroundExecutorProvider.get(), this.mediaControllerFactoryProvider.get(), this.dumpManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.mediaTimeoutListenerProvider.get(), this.mediaResumeListenerProvider.get(), this.mediaSessionBasedFilterProvider.get(), this.mediaDeviceManagerProvider.get(), this.mediaDataCombineLatestProvider.get(), this.mediaDataFilterProvider.get(), this.activityStarterProvider.get(), this.smartspaceMediaDataProvider.get(), this.clockProvider.get(), this.tunerServiceProvider.get(), this.mediaFlagsProvider.get(), this.loggerProvider.get());
    }

    public static MediaDataManager_Factory create(Provider<Context> provider, Provider<Executor> provider2, Provider<DelayableExecutor> provider3, Provider<MediaControllerFactory> provider4, Provider<DumpManager> provider5, Provider<BroadcastDispatcher> provider6, Provider<MediaTimeoutListener> provider7, Provider<MediaResumeListener> provider8, Provider<MediaSessionBasedFilter> provider9, Provider<MediaDeviceManager> provider10, Provider<MediaDataCombineLatest> provider11, Provider<MediaDataFilter> provider12, Provider<ActivityStarter> provider13, Provider<SmartspaceMediaDataProvider> provider14, Provider<SystemClock> provider15, Provider<TunerService> provider16, Provider<MediaFlags> provider17, Provider<MediaUiEventLogger> provider18) {
        return new MediaDataManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static MediaDataManager newInstance(Context context, Executor executor, DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, MediaTimeoutListener mediaTimeoutListener, MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter, ActivityStarter activityStarter, SmartspaceMediaDataProvider smartspaceMediaDataProvider2, SystemClock systemClock, TunerService tunerService, MediaFlags mediaFlags, MediaUiEventLogger mediaUiEventLogger) {
        return new MediaDataManager(context, executor, delayableExecutor, mediaControllerFactory, dumpManager, broadcastDispatcher, mediaTimeoutListener, mediaResumeListener, mediaSessionBasedFilter, mediaDeviceManager, mediaDataCombineLatest, mediaDataFilter, activityStarter, smartspaceMediaDataProvider2, systemClock, tunerService, mediaFlags, mediaUiEventLogger);
    }
}

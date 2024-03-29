package com.android.systemui.media;

import android.content.Context;
import android.media.session.MediaSessionManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaSessionBasedFilter_Factory implements Factory<MediaSessionBasedFilter> {
    public final Provider<Executor> backgroundExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<Executor> foregroundExecutorProvider;
    public final Provider<MediaSessionManager> sessionManagerProvider;

    public MediaSessionBasedFilter_Factory(Provider<Context> provider, Provider<MediaSessionManager> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        this.contextProvider = provider;
        this.sessionManagerProvider = provider2;
        this.foregroundExecutorProvider = provider3;
        this.backgroundExecutorProvider = provider4;
    }

    public MediaSessionBasedFilter get() {
        return newInstance(this.contextProvider.get(), this.sessionManagerProvider.get(), this.foregroundExecutorProvider.get(), this.backgroundExecutorProvider.get());
    }

    public static MediaSessionBasedFilter_Factory create(Provider<Context> provider, Provider<MediaSessionManager> provider2, Provider<Executor> provider3, Provider<Executor> provider4) {
        return new MediaSessionBasedFilter_Factory(provider, provider2, provider3, provider4);
    }

    public static MediaSessionBasedFilter newInstance(Context context, MediaSessionManager mediaSessionManager, Executor executor, Executor executor2) {
        return new MediaSessionBasedFilter(context, mediaSessionManager, executor, executor2);
    }
}

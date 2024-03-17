package com.android.systemui.media.muteawait;

import android.content.Context;
import com.android.systemui.media.MediaFlags;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class MediaMuteAwaitConnectionManagerFactory_Factory implements Factory<MediaMuteAwaitConnectionManagerFactory> {
    public final Provider<Context> contextProvider;
    public final Provider<MediaMuteAwaitLogger> loggerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<MediaFlags> mediaFlagsProvider;

    public MediaMuteAwaitConnectionManagerFactory_Factory(Provider<MediaFlags> provider, Provider<Context> provider2, Provider<MediaMuteAwaitLogger> provider3, Provider<Executor> provider4) {
        this.mediaFlagsProvider = provider;
        this.contextProvider = provider2;
        this.loggerProvider = provider3;
        this.mainExecutorProvider = provider4;
    }

    public MediaMuteAwaitConnectionManagerFactory get() {
        return newInstance(this.mediaFlagsProvider.get(), this.contextProvider.get(), this.loggerProvider.get(), this.mainExecutorProvider.get());
    }

    public static MediaMuteAwaitConnectionManagerFactory_Factory create(Provider<MediaFlags> provider, Provider<Context> provider2, Provider<MediaMuteAwaitLogger> provider3, Provider<Executor> provider4) {
        return new MediaMuteAwaitConnectionManagerFactory_Factory(provider, provider2, provider3, provider4);
    }

    public static MediaMuteAwaitConnectionManagerFactory newInstance(MediaFlags mediaFlags, Context context, MediaMuteAwaitLogger mediaMuteAwaitLogger, Executor executor) {
        return new MediaMuteAwaitConnectionManagerFactory(mediaFlags, context, mediaMuteAwaitLogger, executor);
    }
}

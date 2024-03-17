package com.android.systemui.media.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class MediaModule_ProvidesMediaTttSenderLoggerFactory implements Factory<MediaTttLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public MediaModule_ProvidesMediaTttSenderLoggerFactory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public MediaTttLogger get() {
        return providesMediaTttSenderLogger(this.bufferProvider.get());
    }

    public static MediaModule_ProvidesMediaTttSenderLoggerFactory create(Provider<LogBuffer> provider) {
        return new MediaModule_ProvidesMediaTttSenderLoggerFactory(provider);
    }

    public static MediaTttLogger providesMediaTttSenderLogger(LogBuffer logBuffer) {
        return (MediaTttLogger) Preconditions.checkNotNullFromProvides(MediaModule.providesMediaTttSenderLogger(logBuffer));
    }
}

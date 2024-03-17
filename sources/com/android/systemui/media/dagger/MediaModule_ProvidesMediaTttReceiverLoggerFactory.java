package com.android.systemui.media.dagger;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.media.taptotransfer.common.MediaTttLogger;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class MediaModule_ProvidesMediaTttReceiverLoggerFactory implements Factory<MediaTttLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public MediaModule_ProvidesMediaTttReceiverLoggerFactory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public MediaTttLogger get() {
        return providesMediaTttReceiverLogger(this.bufferProvider.get());
    }

    public static MediaModule_ProvidesMediaTttReceiverLoggerFactory create(Provider<LogBuffer> provider) {
        return new MediaModule_ProvidesMediaTttReceiverLoggerFactory(provider);
    }

    public static MediaTttLogger providesMediaTttReceiverLogger(LogBuffer logBuffer) {
        return (MediaTttLogger) Preconditions.checkNotNullFromProvides(MediaModule.providesMediaTttReceiverLogger(logBuffer));
    }
}

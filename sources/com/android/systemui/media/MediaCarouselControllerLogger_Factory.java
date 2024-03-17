package com.android.systemui.media;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaCarouselControllerLogger_Factory implements Factory<MediaCarouselControllerLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public MediaCarouselControllerLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public MediaCarouselControllerLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static MediaCarouselControllerLogger_Factory create(Provider<LogBuffer> provider) {
        return new MediaCarouselControllerLogger_Factory(provider);
    }

    public static MediaCarouselControllerLogger newInstance(LogBuffer logBuffer) {
        return new MediaCarouselControllerLogger(logBuffer);
    }
}

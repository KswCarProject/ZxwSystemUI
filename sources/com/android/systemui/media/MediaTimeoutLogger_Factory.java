package com.android.systemui.media;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaTimeoutLogger_Factory implements Factory<MediaTimeoutLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public MediaTimeoutLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public MediaTimeoutLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static MediaTimeoutLogger_Factory create(Provider<LogBuffer> provider) {
        return new MediaTimeoutLogger_Factory(provider);
    }

    public static MediaTimeoutLogger newInstance(LogBuffer logBuffer) {
        return new MediaTimeoutLogger(logBuffer);
    }
}

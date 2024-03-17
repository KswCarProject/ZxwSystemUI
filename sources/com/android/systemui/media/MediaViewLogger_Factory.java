package com.android.systemui.media;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaViewLogger_Factory implements Factory<MediaViewLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public MediaViewLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public MediaViewLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static MediaViewLogger_Factory create(Provider<LogBuffer> provider) {
        return new MediaViewLogger_Factory(provider);
    }

    public static MediaViewLogger newInstance(LogBuffer logBuffer) {
        return new MediaViewLogger(logBuffer);
    }
}

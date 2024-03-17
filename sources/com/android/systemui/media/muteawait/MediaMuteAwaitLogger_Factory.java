package com.android.systemui.media.muteawait;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MediaMuteAwaitLogger_Factory implements Factory<MediaMuteAwaitLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public MediaMuteAwaitLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public MediaMuteAwaitLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static MediaMuteAwaitLogger_Factory create(Provider<LogBuffer> provider) {
        return new MediaMuteAwaitLogger_Factory(provider);
    }

    public static MediaMuteAwaitLogger newInstance(LogBuffer logBuffer) {
        return new MediaMuteAwaitLogger(logBuffer);
    }
}

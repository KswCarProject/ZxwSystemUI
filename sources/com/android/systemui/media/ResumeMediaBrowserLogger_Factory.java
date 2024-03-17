package com.android.systemui.media;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ResumeMediaBrowserLogger_Factory implements Factory<ResumeMediaBrowserLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public ResumeMediaBrowserLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public ResumeMediaBrowserLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static ResumeMediaBrowserLogger_Factory create(Provider<LogBuffer> provider) {
        return new ResumeMediaBrowserLogger_Factory(provider);
    }

    public static ResumeMediaBrowserLogger newInstance(LogBuffer logBuffer) {
        return new ResumeMediaBrowserLogger(logBuffer);
    }
}

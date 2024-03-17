package com.android.systemui.qs.logging;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class QSLogger_Factory implements Factory<QSLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public QSLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public QSLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static QSLogger_Factory create(Provider<LogBuffer> provider) {
        return new QSLogger_Factory(provider);
    }

    public static QSLogger newInstance(LogBuffer logBuffer) {
        return new QSLogger(logBuffer);
    }
}

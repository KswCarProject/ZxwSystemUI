package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class RowContentBindStageLogger_Factory implements Factory<RowContentBindStageLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public RowContentBindStageLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public RowContentBindStageLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static RowContentBindStageLogger_Factory create(Provider<LogBuffer> provider) {
        return new RowContentBindStageLogger_Factory(provider);
    }

    public static RowContentBindStageLogger newInstance(LogBuffer logBuffer) {
        return new RowContentBindStageLogger(logBuffer);
    }
}

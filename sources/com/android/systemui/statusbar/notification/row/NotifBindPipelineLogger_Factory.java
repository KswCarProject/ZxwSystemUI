package com.android.systemui.statusbar.notification.row;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotifBindPipelineLogger_Factory implements Factory<NotifBindPipelineLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public NotifBindPipelineLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public NotifBindPipelineLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static NotifBindPipelineLogger_Factory create(Provider<LogBuffer> provider) {
        return new NotifBindPipelineLogger_Factory(provider);
    }

    public static NotifBindPipelineLogger newInstance(LogBuffer logBuffer) {
        return new NotifBindPipelineLogger(logBuffer);
    }
}

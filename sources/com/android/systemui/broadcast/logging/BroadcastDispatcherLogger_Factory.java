package com.android.systemui.broadcast.logging;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BroadcastDispatcherLogger_Factory implements Factory<BroadcastDispatcherLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public BroadcastDispatcherLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public BroadcastDispatcherLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static BroadcastDispatcherLogger_Factory create(Provider<LogBuffer> provider) {
        return new BroadcastDispatcherLogger_Factory(provider);
    }

    public static BroadcastDispatcherLogger newInstance(LogBuffer logBuffer) {
        return new BroadcastDispatcherLogger(logBuffer);
    }
}

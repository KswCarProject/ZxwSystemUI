package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class StackStateLogger_Factory implements Factory<StackStateLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public StackStateLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public StackStateLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static StackStateLogger_Factory create(Provider<LogBuffer> provider) {
        return new StackStateLogger_Factory(provider);
    }

    public static StackStateLogger newInstance(LogBuffer logBuffer) {
        return new StackStateLogger(logBuffer);
    }
}

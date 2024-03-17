package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationInterruptLogger_Factory implements Factory<NotificationInterruptLogger> {
    public final Provider<LogBuffer> hunBufferProvider;
    public final Provider<LogBuffer> notifBufferProvider;

    public NotificationInterruptLogger_Factory(Provider<LogBuffer> provider, Provider<LogBuffer> provider2) {
        this.notifBufferProvider = provider;
        this.hunBufferProvider = provider2;
    }

    public NotificationInterruptLogger get() {
        return newInstance(this.notifBufferProvider.get(), this.hunBufferProvider.get());
    }

    public static NotificationInterruptLogger_Factory create(Provider<LogBuffer> provider, Provider<LogBuffer> provider2) {
        return new NotificationInterruptLogger_Factory(provider, provider2);
    }

    public static NotificationInterruptLogger newInstance(LogBuffer logBuffer, LogBuffer logBuffer2) {
        return new NotificationInterruptLogger(logBuffer, logBuffer2);
    }
}

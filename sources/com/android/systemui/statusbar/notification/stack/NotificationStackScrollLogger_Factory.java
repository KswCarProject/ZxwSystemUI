package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class NotificationStackScrollLogger_Factory implements Factory<NotificationStackScrollLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public NotificationStackScrollLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public NotificationStackScrollLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static NotificationStackScrollLogger_Factory create(Provider<LogBuffer> provider) {
        return new NotificationStackScrollLogger_Factory(provider);
    }

    public static NotificationStackScrollLogger newInstance(LogBuffer logBuffer) {
        return new NotificationStackScrollLogger(logBuffer);
    }
}

package com.android.systemui.statusbar.notification.interruption;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HeadsUpViewBinderLogger_Factory implements Factory<HeadsUpViewBinderLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public HeadsUpViewBinderLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public HeadsUpViewBinderLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static HeadsUpViewBinderLogger_Factory create(Provider<LogBuffer> provider) {
        return new HeadsUpViewBinderLogger_Factory(provider);
    }

    public static HeadsUpViewBinderLogger newInstance(LogBuffer logBuffer) {
        return new HeadsUpViewBinderLogger(logBuffer);
    }
}

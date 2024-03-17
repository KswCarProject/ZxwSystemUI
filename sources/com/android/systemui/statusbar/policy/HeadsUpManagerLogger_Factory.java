package com.android.systemui.statusbar.policy;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HeadsUpManagerLogger_Factory implements Factory<HeadsUpManagerLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public HeadsUpManagerLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public HeadsUpManagerLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static HeadsUpManagerLogger_Factory create(Provider<LogBuffer> provider) {
        return new HeadsUpManagerLogger_Factory(provider);
    }

    public static HeadsUpManagerLogger newInstance(LogBuffer logBuffer) {
        return new HeadsUpManagerLogger(logBuffer);
    }
}

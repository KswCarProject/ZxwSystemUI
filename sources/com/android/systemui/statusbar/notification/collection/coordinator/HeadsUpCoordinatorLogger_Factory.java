package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class HeadsUpCoordinatorLogger_Factory implements Factory<HeadsUpCoordinatorLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public HeadsUpCoordinatorLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public HeadsUpCoordinatorLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static HeadsUpCoordinatorLogger_Factory create(Provider<LogBuffer> provider) {
        return new HeadsUpCoordinatorLogger_Factory(provider);
    }

    public static HeadsUpCoordinatorLogger newInstance(LogBuffer logBuffer) {
        return new HeadsUpCoordinatorLogger(logBuffer);
    }
}

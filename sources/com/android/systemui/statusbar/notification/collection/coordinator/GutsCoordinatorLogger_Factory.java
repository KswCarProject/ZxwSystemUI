package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class GutsCoordinatorLogger_Factory implements Factory<GutsCoordinatorLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public GutsCoordinatorLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public GutsCoordinatorLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static GutsCoordinatorLogger_Factory create(Provider<LogBuffer> provider) {
        return new GutsCoordinatorLogger_Factory(provider);
    }

    public static GutsCoordinatorLogger newInstance(LogBuffer logBuffer) {
        return new GutsCoordinatorLogger(logBuffer);
    }
}

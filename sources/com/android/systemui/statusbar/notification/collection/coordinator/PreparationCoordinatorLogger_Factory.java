package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class PreparationCoordinatorLogger_Factory implements Factory<PreparationCoordinatorLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public PreparationCoordinatorLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public PreparationCoordinatorLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static PreparationCoordinatorLogger_Factory create(Provider<LogBuffer> provider) {
        return new PreparationCoordinatorLogger_Factory(provider);
    }

    public static PreparationCoordinatorLogger newInstance(LogBuffer logBuffer) {
        return new PreparationCoordinatorLogger(logBuffer);
    }
}

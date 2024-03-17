package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ShadeEventCoordinatorLogger_Factory implements Factory<ShadeEventCoordinatorLogger> {
    public final Provider<LogBuffer> bufferProvider;

    public ShadeEventCoordinatorLogger_Factory(Provider<LogBuffer> provider) {
        this.bufferProvider = provider;
    }

    public ShadeEventCoordinatorLogger get() {
        return newInstance(this.bufferProvider.get());
    }

    public static ShadeEventCoordinatorLogger_Factory create(Provider<LogBuffer> provider) {
        return new ShadeEventCoordinatorLogger_Factory(provider);
    }

    public static ShadeEventCoordinatorLogger newInstance(LogBuffer logBuffer) {
        return new ShadeEventCoordinatorLogger(logBuffer);
    }
}

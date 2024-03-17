package com.android.systemui.dreams;

import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DreamOverlayStateController_Factory implements Factory<DreamOverlayStateController> {
    public final Provider<Executor> executorProvider;

    public DreamOverlayStateController_Factory(Provider<Executor> provider) {
        this.executorProvider = provider;
    }

    public DreamOverlayStateController get() {
        return newInstance(this.executorProvider.get());
    }

    public static DreamOverlayStateController_Factory create(Provider<Executor> provider) {
        return new DreamOverlayStateController_Factory(provider);
    }

    public static DreamOverlayStateController newInstance(Executor executor) {
        return new DreamOverlayStateController(executor);
    }
}

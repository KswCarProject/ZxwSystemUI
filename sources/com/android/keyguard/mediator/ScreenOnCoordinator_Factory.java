package com.android.keyguard.mediator;

import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.util.concurrency.Execution;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ScreenOnCoordinator_Factory implements Factory<ScreenOnCoordinator> {
    public final Provider<Execution> executionProvider;
    public final Provider<ScreenLifecycle> screenLifecycleProvider;
    public final Provider<Optional<SysUIUnfoldComponent>> unfoldComponentProvider;

    public ScreenOnCoordinator_Factory(Provider<ScreenLifecycle> provider, Provider<Optional<SysUIUnfoldComponent>> provider2, Provider<Execution> provider3) {
        this.screenLifecycleProvider = provider;
        this.unfoldComponentProvider = provider2;
        this.executionProvider = provider3;
    }

    public ScreenOnCoordinator get() {
        return newInstance(this.screenLifecycleProvider.get(), this.unfoldComponentProvider.get(), this.executionProvider.get());
    }

    public static ScreenOnCoordinator_Factory create(Provider<ScreenLifecycle> provider, Provider<Optional<SysUIUnfoldComponent>> provider2, Provider<Execution> provider3) {
        return new ScreenOnCoordinator_Factory(provider, provider2, provider3);
    }

    public static ScreenOnCoordinator newInstance(ScreenLifecycle screenLifecycle, Optional<SysUIUnfoldComponent> optional, Execution execution) {
        return new ScreenOnCoordinator(screenLifecycle, optional, execution);
    }
}

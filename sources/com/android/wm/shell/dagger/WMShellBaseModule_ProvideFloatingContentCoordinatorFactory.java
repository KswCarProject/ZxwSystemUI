package com.android.wm.shell.dagger;

import com.android.wm.shell.common.FloatingContentCoordinator;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class WMShellBaseModule_ProvideFloatingContentCoordinatorFactory implements Factory<FloatingContentCoordinator> {

    public static final class InstanceHolder {
        public static final WMShellBaseModule_ProvideFloatingContentCoordinatorFactory INSTANCE = new WMShellBaseModule_ProvideFloatingContentCoordinatorFactory();
    }

    public FloatingContentCoordinator get() {
        return provideFloatingContentCoordinator();
    }

    public static WMShellBaseModule_ProvideFloatingContentCoordinatorFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static FloatingContentCoordinator provideFloatingContentCoordinator() {
        return (FloatingContentCoordinator) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideFloatingContentCoordinator());
    }
}

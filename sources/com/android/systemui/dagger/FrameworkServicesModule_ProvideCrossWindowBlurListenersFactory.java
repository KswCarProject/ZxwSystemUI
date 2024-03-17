package com.android.systemui.dagger;

import android.view.CrossWindowBlurListeners;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory implements Factory<CrossWindowBlurListeners> {

    public static final class InstanceHolder {
        public static final FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory INSTANCE = new FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory();
    }

    public CrossWindowBlurListeners get() {
        return provideCrossWindowBlurListeners();
    }

    public static FrameworkServicesModule_ProvideCrossWindowBlurListenersFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static CrossWindowBlurListeners provideCrossWindowBlurListeners() {
        return (CrossWindowBlurListeners) Preconditions.checkNotNullFromProvides(FrameworkServicesModule.provideCrossWindowBlurListeners());
    }
}

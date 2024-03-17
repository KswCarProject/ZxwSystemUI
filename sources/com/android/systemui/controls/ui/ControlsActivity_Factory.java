package com.android.systemui.controls.ui;

import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ControlsActivity_Factory implements Factory<ControlsActivity> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<ControlsUiController> uiControllerProvider;

    public ControlsActivity_Factory(Provider<ControlsUiController> provider, Provider<BroadcastDispatcher> provider2) {
        this.uiControllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
    }

    public ControlsActivity get() {
        return newInstance(this.uiControllerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static ControlsActivity_Factory create(Provider<ControlsUiController> provider, Provider<BroadcastDispatcher> provider2) {
        return new ControlsActivity_Factory(provider, provider2);
    }

    public static ControlsActivity newInstance(ControlsUiController controlsUiController, BroadcastDispatcher broadcastDispatcher) {
        return new ControlsActivity(controlsUiController, broadcastDispatcher);
    }
}

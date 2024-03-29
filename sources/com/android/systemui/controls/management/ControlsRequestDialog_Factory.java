package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ControlsRequestDialog_Factory implements Factory<ControlsRequestDialog> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<ControlsController> controllerProvider;
    public final Provider<ControlsListingController> controlsListingControllerProvider;

    public ControlsRequestDialog_Factory(Provider<ControlsController> provider, Provider<BroadcastDispatcher> provider2, Provider<ControlsListingController> provider3) {
        this.controllerProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.controlsListingControllerProvider = provider3;
    }

    public ControlsRequestDialog get() {
        return newInstance(this.controllerProvider.get(), this.broadcastDispatcherProvider.get(), this.controlsListingControllerProvider.get());
    }

    public static ControlsRequestDialog_Factory create(Provider<ControlsController> provider, Provider<BroadcastDispatcher> provider2, Provider<ControlsListingController> provider3) {
        return new ControlsRequestDialog_Factory(provider, provider2, provider3);
    }

    public static ControlsRequestDialog newInstance(ControlsController controlsController, BroadcastDispatcher broadcastDispatcher, ControlsListingController controlsListingController) {
        return new ControlsRequestDialog(controlsController, broadcastDispatcher, controlsListingController);
    }
}

package com.android.systemui.controls.management;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.ui.ControlsUiController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class ControlsFavoritingActivity_Factory implements Factory<ControlsFavoritingActivity> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<ControlsControllerImpl> controllerProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<ControlsListingController> listingControllerProvider;
    public final Provider<ControlsUiController> uiControllerProvider;

    public ControlsFavoritingActivity_Factory(Provider<Executor> provider, Provider<ControlsControllerImpl> provider2, Provider<ControlsListingController> provider3, Provider<BroadcastDispatcher> provider4, Provider<ControlsUiController> provider5) {
        this.executorProvider = provider;
        this.controllerProvider = provider2;
        this.listingControllerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.uiControllerProvider = provider5;
    }

    public ControlsFavoritingActivity get() {
        return newInstance(this.executorProvider.get(), this.controllerProvider.get(), this.listingControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.uiControllerProvider.get());
    }

    public static ControlsFavoritingActivity_Factory create(Provider<Executor> provider, Provider<ControlsControllerImpl> provider2, Provider<ControlsListingController> provider3, Provider<BroadcastDispatcher> provider4, Provider<ControlsUiController> provider5) {
        return new ControlsFavoritingActivity_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static ControlsFavoritingActivity newInstance(Executor executor, ControlsControllerImpl controlsControllerImpl, ControlsListingController controlsListingController, BroadcastDispatcher broadcastDispatcher, ControlsUiController controlsUiController) {
        return new ControlsFavoritingActivity(executor, controlsControllerImpl, controlsListingController, broadcastDispatcher, controlsUiController);
    }
}

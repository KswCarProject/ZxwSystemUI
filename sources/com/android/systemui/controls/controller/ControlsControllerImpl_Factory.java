package com.android.systemui.controls.controller;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ControlsControllerImpl_Factory implements Factory<ControlsControllerImpl> {
    public final Provider<ControlsBindingController> bindingControllerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<DelayableExecutor> executorProvider;
    public final Provider<ControlsListingController> listingControllerProvider;
    public final Provider<Optional<ControlsFavoritePersistenceWrapper>> optionalWrapperProvider;
    public final Provider<ControlsUiController> uiControllerProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public ControlsControllerImpl_Factory(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<ControlsUiController> provider3, Provider<ControlsBindingController> provider4, Provider<ControlsListingController> provider5, Provider<BroadcastDispatcher> provider6, Provider<Optional<ControlsFavoritePersistenceWrapper>> provider7, Provider<DumpManager> provider8, Provider<UserTracker> provider9) {
        this.contextProvider = provider;
        this.executorProvider = provider2;
        this.uiControllerProvider = provider3;
        this.bindingControllerProvider = provider4;
        this.listingControllerProvider = provider5;
        this.broadcastDispatcherProvider = provider6;
        this.optionalWrapperProvider = provider7;
        this.dumpManagerProvider = provider8;
        this.userTrackerProvider = provider9;
    }

    public ControlsControllerImpl get() {
        return newInstance(this.contextProvider.get(), this.executorProvider.get(), this.uiControllerProvider.get(), this.bindingControllerProvider.get(), this.listingControllerProvider.get(), this.broadcastDispatcherProvider.get(), this.optionalWrapperProvider.get(), this.dumpManagerProvider.get(), this.userTrackerProvider.get());
    }

    public static ControlsControllerImpl_Factory create(Provider<Context> provider, Provider<DelayableExecutor> provider2, Provider<ControlsUiController> provider3, Provider<ControlsBindingController> provider4, Provider<ControlsListingController> provider5, Provider<BroadcastDispatcher> provider6, Provider<Optional<ControlsFavoritePersistenceWrapper>> provider7, Provider<DumpManager> provider8, Provider<UserTracker> provider9) {
        return new ControlsControllerImpl_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static ControlsControllerImpl newInstance(Context context, DelayableExecutor delayableExecutor, ControlsUiController controlsUiController, ControlsBindingController controlsBindingController, ControlsListingController controlsListingController, BroadcastDispatcher broadcastDispatcher, Optional<ControlsFavoritePersistenceWrapper> optional, DumpManager dumpManager, UserTracker userTracker) {
        return new ControlsControllerImpl(context, delayableExecutor, controlsUiController, controlsBindingController, controlsListingController, broadcastDispatcher, optional, dumpManager, userTracker);
    }
}

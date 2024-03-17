package com.android.systemui.recents;

import android.content.Context;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class ScreenPinningRequest_Factory implements Factory<ScreenPinningRequest> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;

    public ScreenPinningRequest_Factory(Provider<Context> provider, Provider<Optional<CentralSurfaces>> provider2, Provider<NavigationModeController> provider3, Provider<BroadcastDispatcher> provider4) {
        this.contextProvider = provider;
        this.centralSurfacesOptionalLazyProvider = provider2;
        this.navigationModeControllerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
    }

    public ScreenPinningRequest get() {
        return newInstance(this.contextProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.navigationModeControllerProvider.get(), this.broadcastDispatcherProvider.get());
    }

    public static ScreenPinningRequest_Factory create(Provider<Context> provider, Provider<Optional<CentralSurfaces>> provider2, Provider<NavigationModeController> provider3, Provider<BroadcastDispatcher> provider4) {
        return new ScreenPinningRequest_Factory(provider, provider2, provider3, provider4);
    }

    public static ScreenPinningRequest newInstance(Context context, Lazy<Optional<CentralSurfaces>> lazy, NavigationModeController navigationModeController, BroadcastDispatcher broadcastDispatcher) {
        return new ScreenPinningRequest(context, lazy, navigationModeController, broadcastDispatcher);
    }
}

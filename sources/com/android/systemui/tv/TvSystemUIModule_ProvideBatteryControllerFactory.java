package com.android.systemui.tv;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.statusbar.policy.BatteryController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class TvSystemUIModule_ProvideBatteryControllerFactory implements Factory<BatteryController> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DemoModeController> demoModeControllerProvider;
    public final Provider<EnhancedEstimates> enhancedEstimatesProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<PowerManager> powerManagerProvider;

    public TvSystemUIModule_ProvideBatteryControllerFactory(Provider<Context> provider, Provider<EnhancedEstimates> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4, Provider<DemoModeController> provider5, Provider<Handler> provider6, Provider<Handler> provider7) {
        this.contextProvider = provider;
        this.enhancedEstimatesProvider = provider2;
        this.powerManagerProvider = provider3;
        this.broadcastDispatcherProvider = provider4;
        this.demoModeControllerProvider = provider5;
        this.mainHandlerProvider = provider6;
        this.bgHandlerProvider = provider7;
    }

    public BatteryController get() {
        return provideBatteryController(this.contextProvider.get(), this.enhancedEstimatesProvider.get(), this.powerManagerProvider.get(), this.broadcastDispatcherProvider.get(), this.demoModeControllerProvider.get(), this.mainHandlerProvider.get(), this.bgHandlerProvider.get());
    }

    public static TvSystemUIModule_ProvideBatteryControllerFactory create(Provider<Context> provider, Provider<EnhancedEstimates> provider2, Provider<PowerManager> provider3, Provider<BroadcastDispatcher> provider4, Provider<DemoModeController> provider5, Provider<Handler> provider6, Provider<Handler> provider7) {
        return new TvSystemUIModule_ProvideBatteryControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static BatteryController provideBatteryController(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2) {
        return (BatteryController) Preconditions.checkNotNullFromProvides(TvSystemUIModule.provideBatteryController(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2));
    }
}

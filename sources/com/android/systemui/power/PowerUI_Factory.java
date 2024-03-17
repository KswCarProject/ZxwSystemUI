package com.android.systemui.power;

import android.content.Context;
import android.os.PowerManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.power.PowerUI;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;

public final class PowerUI_Factory implements Factory<PowerUI> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<EnhancedEstimates> enhancedEstimatesProvider;
    public final Provider<PowerManager> powerManagerProvider;
    public final Provider<PowerUI.WarningsUI> warningsUIProvider;

    public PowerUI_Factory(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<CommandQueue> provider3, Provider<Optional<CentralSurfaces>> provider4, Provider<PowerUI.WarningsUI> provider5, Provider<EnhancedEstimates> provider6, Provider<PowerManager> provider7) {
        this.contextProvider = provider;
        this.broadcastDispatcherProvider = provider2;
        this.commandQueueProvider = provider3;
        this.centralSurfacesOptionalLazyProvider = provider4;
        this.warningsUIProvider = provider5;
        this.enhancedEstimatesProvider = provider6;
        this.powerManagerProvider = provider7;
    }

    public PowerUI get() {
        return newInstance(this.contextProvider.get(), this.broadcastDispatcherProvider.get(), this.commandQueueProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.warningsUIProvider.get(), this.enhancedEstimatesProvider.get(), this.powerManagerProvider.get());
    }

    public static PowerUI_Factory create(Provider<Context> provider, Provider<BroadcastDispatcher> provider2, Provider<CommandQueue> provider3, Provider<Optional<CentralSurfaces>> provider4, Provider<PowerUI.WarningsUI> provider5, Provider<EnhancedEstimates> provider6, Provider<PowerManager> provider7) {
        return new PowerUI_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7);
    }

    public static PowerUI newInstance(Context context, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Lazy<Optional<CentralSurfaces>> lazy, PowerUI.WarningsUI warningsUI, EnhancedEstimates enhancedEstimates, PowerManager powerManager) {
        return new PowerUI(context, broadcastDispatcher, commandQueue, lazy, warningsUI, enhancedEstimates, powerManager);
    }
}

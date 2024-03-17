package com.android.systemui.navigationbar.gestural;

import android.view.IWindowManager;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.tracing.ProtoTracer;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class EdgeBackGestureHandler_Factory_Factory implements Factory<EdgeBackGestureHandler.Factory> {
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Executor> executorProvider;
    public final Provider<FalsingManager> falsingManagerProvider;
    public final Provider<LatencyTracker> latencyTrackerProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<PluginManager> pluginManagerProvider;
    public final Provider<ProtoTracer> protoTracerProvider;
    public final Provider<SysUiState> sysUiStateProvider;
    public final Provider<ViewConfiguration> viewConfigurationProvider;
    public final Provider<WindowManager> windowManagerProvider;
    public final Provider<IWindowManager> windowManagerServiceProvider;

    public EdgeBackGestureHandler_Factory_Factory(Provider<OverviewProxyService> provider, Provider<SysUiState> provider2, Provider<PluginManager> provider3, Provider<Executor> provider4, Provider<BroadcastDispatcher> provider5, Provider<ProtoTracer> provider6, Provider<NavigationModeController> provider7, Provider<ViewConfiguration> provider8, Provider<WindowManager> provider9, Provider<IWindowManager> provider10, Provider<FalsingManager> provider11, Provider<LatencyTracker> provider12) {
        this.overviewProxyServiceProvider = provider;
        this.sysUiStateProvider = provider2;
        this.pluginManagerProvider = provider3;
        this.executorProvider = provider4;
        this.broadcastDispatcherProvider = provider5;
        this.protoTracerProvider = provider6;
        this.navigationModeControllerProvider = provider7;
        this.viewConfigurationProvider = provider8;
        this.windowManagerProvider = provider9;
        this.windowManagerServiceProvider = provider10;
        this.falsingManagerProvider = provider11;
        this.latencyTrackerProvider = provider12;
    }

    public EdgeBackGestureHandler.Factory get() {
        return newInstance(this.overviewProxyServiceProvider.get(), this.sysUiStateProvider.get(), this.pluginManagerProvider.get(), this.executorProvider.get(), this.broadcastDispatcherProvider.get(), this.protoTracerProvider.get(), this.navigationModeControllerProvider.get(), this.viewConfigurationProvider.get(), this.windowManagerProvider.get(), this.windowManagerServiceProvider.get(), this.falsingManagerProvider.get(), this.latencyTrackerProvider.get());
    }

    public static EdgeBackGestureHandler_Factory_Factory create(Provider<OverviewProxyService> provider, Provider<SysUiState> provider2, Provider<PluginManager> provider3, Provider<Executor> provider4, Provider<BroadcastDispatcher> provider5, Provider<ProtoTracer> provider6, Provider<NavigationModeController> provider7, Provider<ViewConfiguration> provider8, Provider<WindowManager> provider9, Provider<IWindowManager> provider10, Provider<FalsingManager> provider11, Provider<LatencyTracker> provider12) {
        return new EdgeBackGestureHandler_Factory_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12);
    }

    public static EdgeBackGestureHandler.Factory newInstance(OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager, LatencyTracker latencyTracker) {
        return new EdgeBackGestureHandler.Factory(overviewProxyService, sysUiState, pluginManager, executor, broadcastDispatcher, protoTracer, navigationModeController, viewConfiguration, windowManager, iWindowManager, falsingManager, latencyTracker);
    }
}

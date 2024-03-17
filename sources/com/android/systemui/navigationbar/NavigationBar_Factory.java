package com.android.systemui.navigationbar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.pip.Pip;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class NavigationBar_Factory implements Factory<NavigationBar> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<AutoHideController.Factory> autoHideControllerFactoryProvider;
    public final Provider<Optional<BackAnimation>> backAnimationProvider;
    public final Provider<Executor> bgExecutorProvider;
    public final Provider<BroadcastDispatcher> broadcastDispatcherProvider;
    public final Provider<Optional<CentralSurfaces>> centralSurfacesOptionalLazyProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeadZone> deadZoneProvider;
    public final Provider<DeviceConfigProxy> deviceConfigProxyProvider;
    public final Provider<DeviceProvisionedController> deviceProvisionedControllerProvider;
    public final Provider<EdgeBackGestureHandler> edgeBackGestureHandlerProvider;
    public final Provider<InputMethodManager> inputMethodManagerProvider;
    public final Provider<LightBarController.Factory> lightBarControllerFactoryProvider;
    public final Provider<AutoHideController> mainAutoHideControllerProvider;
    public final Provider<Executor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<LightBarController> mainLightBarControllerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<NavBarHelper> navBarHelperProvider;
    public final Provider<NavigationBarFrame> navigationBarFrameProvider;
    public final Provider<NavigationBarTransitions> navigationBarTransitionsProvider;
    public final Provider<NavigationBarView> navigationBarViewProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<NotificationRemoteInputManager> notificationRemoteInputManagerProvider;
    public final Provider<NotificationShadeDepthController> notificationShadeDepthControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<Optional<Pip>> pipOptionalProvider;
    public final Provider<Optional<Recents>> recentsOptionalProvider;
    public final Provider<Bundle> savedStateProvider;
    public final Provider<ShadeController> shadeControllerProvider;
    public final Provider<StatusBarKeyguardViewManager> statusBarKeyguardViewManagerProvider;
    public final Provider<StatusBarStateController> statusBarStateControllerProvider;
    public final Provider<SysUiState> sysUiFlagsContainerProvider;
    public final Provider<Optional<TelecomManager>> telecomManagerOptionalProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserContextProvider> userContextProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public NavigationBar_Factory(Provider<NavigationBarView> provider, Provider<NavigationBarFrame> provider2, Provider<Bundle> provider3, Provider<Context> provider4, Provider<WindowManager> provider5, Provider<AssistManager> provider6, Provider<AccessibilityManager> provider7, Provider<DeviceProvisionedController> provider8, Provider<MetricsLogger> provider9, Provider<OverviewProxyService> provider10, Provider<NavigationModeController> provider11, Provider<StatusBarStateController> provider12, Provider<StatusBarKeyguardViewManager> provider13, Provider<SysUiState> provider14, Provider<BroadcastDispatcher> provider15, Provider<CommandQueue> provider16, Provider<Optional<Pip>> provider17, Provider<Optional<Recents>> provider18, Provider<Optional<CentralSurfaces>> provider19, Provider<ShadeController> provider20, Provider<NotificationRemoteInputManager> provider21, Provider<NotificationShadeDepthController> provider22, Provider<Handler> provider23, Provider<Executor> provider24, Provider<Executor> provider25, Provider<UiEventLogger> provider26, Provider<NavBarHelper> provider27, Provider<LightBarController> provider28, Provider<LightBarController.Factory> provider29, Provider<AutoHideController> provider30, Provider<AutoHideController.Factory> provider31, Provider<Optional<TelecomManager>> provider32, Provider<InputMethodManager> provider33, Provider<DeadZone> provider34, Provider<DeviceConfigProxy> provider35, Provider<NavigationBarTransitions> provider36, Provider<EdgeBackGestureHandler> provider37, Provider<Optional<BackAnimation>> provider38, Provider<UserContextProvider> provider39) {
        this.navigationBarViewProvider = provider;
        this.navigationBarFrameProvider = provider2;
        this.savedStateProvider = provider3;
        this.contextProvider = provider4;
        this.windowManagerProvider = provider5;
        this.assistManagerLazyProvider = provider6;
        this.accessibilityManagerProvider = provider7;
        this.deviceProvisionedControllerProvider = provider8;
        this.metricsLoggerProvider = provider9;
        this.overviewProxyServiceProvider = provider10;
        this.navigationModeControllerProvider = provider11;
        this.statusBarStateControllerProvider = provider12;
        this.statusBarKeyguardViewManagerProvider = provider13;
        this.sysUiFlagsContainerProvider = provider14;
        this.broadcastDispatcherProvider = provider15;
        this.commandQueueProvider = provider16;
        this.pipOptionalProvider = provider17;
        this.recentsOptionalProvider = provider18;
        this.centralSurfacesOptionalLazyProvider = provider19;
        this.shadeControllerProvider = provider20;
        this.notificationRemoteInputManagerProvider = provider21;
        this.notificationShadeDepthControllerProvider = provider22;
        this.mainHandlerProvider = provider23;
        this.mainExecutorProvider = provider24;
        this.bgExecutorProvider = provider25;
        this.uiEventLoggerProvider = provider26;
        this.navBarHelperProvider = provider27;
        this.mainLightBarControllerProvider = provider28;
        this.lightBarControllerFactoryProvider = provider29;
        this.mainAutoHideControllerProvider = provider30;
        this.autoHideControllerFactoryProvider = provider31;
        this.telecomManagerOptionalProvider = provider32;
        this.inputMethodManagerProvider = provider33;
        this.deadZoneProvider = provider34;
        this.deviceConfigProxyProvider = provider35;
        this.navigationBarTransitionsProvider = provider36;
        this.edgeBackGestureHandlerProvider = provider37;
        this.backAnimationProvider = provider38;
        this.userContextProvider = provider39;
    }

    public NavigationBar get() {
        return newInstance(this.navigationBarViewProvider.get(), this.navigationBarFrameProvider.get(), this.savedStateProvider.get(), this.contextProvider.get(), this.windowManagerProvider.get(), DoubleCheck.lazy(this.assistManagerLazyProvider), this.accessibilityManagerProvider.get(), this.deviceProvisionedControllerProvider.get(), this.metricsLoggerProvider.get(), this.overviewProxyServiceProvider.get(), this.navigationModeControllerProvider.get(), this.statusBarStateControllerProvider.get(), this.statusBarKeyguardViewManagerProvider.get(), this.sysUiFlagsContainerProvider.get(), this.broadcastDispatcherProvider.get(), this.commandQueueProvider.get(), this.pipOptionalProvider.get(), this.recentsOptionalProvider.get(), DoubleCheck.lazy(this.centralSurfacesOptionalLazyProvider), this.shadeControllerProvider.get(), this.notificationRemoteInputManagerProvider.get(), this.notificationShadeDepthControllerProvider.get(), this.mainHandlerProvider.get(), this.mainExecutorProvider.get(), this.bgExecutorProvider.get(), this.uiEventLoggerProvider.get(), this.navBarHelperProvider.get(), this.mainLightBarControllerProvider.get(), this.lightBarControllerFactoryProvider.get(), this.mainAutoHideControllerProvider.get(), this.autoHideControllerFactoryProvider.get(), this.telecomManagerOptionalProvider.get(), this.inputMethodManagerProvider.get(), this.deadZoneProvider.get(), this.deviceConfigProxyProvider.get(), this.navigationBarTransitionsProvider.get(), this.edgeBackGestureHandlerProvider.get(), this.backAnimationProvider.get(), this.userContextProvider.get());
    }

    public static NavigationBar_Factory create(Provider<NavigationBarView> provider, Provider<NavigationBarFrame> provider2, Provider<Bundle> provider3, Provider<Context> provider4, Provider<WindowManager> provider5, Provider<AssistManager> provider6, Provider<AccessibilityManager> provider7, Provider<DeviceProvisionedController> provider8, Provider<MetricsLogger> provider9, Provider<OverviewProxyService> provider10, Provider<NavigationModeController> provider11, Provider<StatusBarStateController> provider12, Provider<StatusBarKeyguardViewManager> provider13, Provider<SysUiState> provider14, Provider<BroadcastDispatcher> provider15, Provider<CommandQueue> provider16, Provider<Optional<Pip>> provider17, Provider<Optional<Recents>> provider18, Provider<Optional<CentralSurfaces>> provider19, Provider<ShadeController> provider20, Provider<NotificationRemoteInputManager> provider21, Provider<NotificationShadeDepthController> provider22, Provider<Handler> provider23, Provider<Executor> provider24, Provider<Executor> provider25, Provider<UiEventLogger> provider26, Provider<NavBarHelper> provider27, Provider<LightBarController> provider28, Provider<LightBarController.Factory> provider29, Provider<AutoHideController> provider30, Provider<AutoHideController.Factory> provider31, Provider<Optional<TelecomManager>> provider32, Provider<InputMethodManager> provider33, Provider<DeadZone> provider34, Provider<DeviceConfigProxy> provider35, Provider<NavigationBarTransitions> provider36, Provider<EdgeBackGestureHandler> provider37, Provider<Optional<BackAnimation>> provider38, Provider<UserContextProvider> provider39) {
        return new NavigationBar_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18, provider19, provider20, provider21, provider22, provider23, provider24, provider25, provider26, provider27, provider28, provider29, provider30, provider31, provider32, provider33, provider34, provider35, provider36, provider37, provider38, provider39);
    }

    public static NavigationBar newInstance(NavigationBarView navigationBarView, NavigationBarFrame navigationBarFrame, Bundle bundle, Context context, WindowManager windowManager, Lazy<AssistManager> lazy, AccessibilityManager accessibilityManager, DeviceProvisionedController deviceProvisionedController, MetricsLogger metricsLogger, OverviewProxyService overviewProxyService, NavigationModeController navigationModeController, StatusBarStateController statusBarStateController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, SysUiState sysUiState, BroadcastDispatcher broadcastDispatcher, CommandQueue commandQueue, Optional<Pip> optional, Optional<Recents> optional2, Lazy<Optional<CentralSurfaces>> lazy2, ShadeController shadeController, NotificationRemoteInputManager notificationRemoteInputManager, NotificationShadeDepthController notificationShadeDepthController, Handler handler, Executor executor, Executor executor2, UiEventLogger uiEventLogger, NavBarHelper navBarHelper, LightBarController lightBarController, LightBarController.Factory factory, AutoHideController autoHideController, AutoHideController.Factory factory2, Optional<TelecomManager> optional3, InputMethodManager inputMethodManager, DeadZone deadZone, DeviceConfigProxy deviceConfigProxy, NavigationBarTransitions navigationBarTransitions, EdgeBackGestureHandler edgeBackGestureHandler, Optional<BackAnimation> optional4, UserContextProvider userContextProvider2) {
        return new NavigationBar(navigationBarView, navigationBarFrame, bundle, context, windowManager, lazy, accessibilityManager, deviceProvisionedController, metricsLogger, overviewProxyService, navigationModeController, statusBarStateController, statusBarKeyguardViewManager, sysUiState, broadcastDispatcher, commandQueue, optional, optional2, lazy2, shadeController, notificationRemoteInputManager, notificationShadeDepthController, handler, executor, executor2, uiEventLogger, navBarHelper, lightBarController, factory, autoHideController, factory2, optional3, inputMethodManager, deadZone, deviceConfigProxy, navigationBarTransitions, edgeBackGestureHandler, optional4, userContextProvider2);
    }
}

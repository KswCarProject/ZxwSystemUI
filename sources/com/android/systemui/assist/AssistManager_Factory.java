package com.android.systemui.assist;

import android.content.Context;
import android.os.Handler;
import com.android.internal.app.AssistUtils;
import com.android.systemui.assist.ui.DefaultUiController;
import com.android.systemui.model.SysUiState;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import dagger.Lazy;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AssistManager_Factory implements Factory<AssistManager> {
    public final Provider<AssistLogger> assistLoggerProvider;
    public final Provider<AssistUtils> assistUtilsProvider;
    public final Provider<CommandQueue> commandQueueProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DeviceProvisionedController> controllerProvider;
    public final Provider<DefaultUiController> defaultUiControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<PhoneStateMonitor> phoneStateMonitorProvider;
    public final Provider<SysUiState> sysUiStateProvider;
    public final Provider<Handler> uiHandlerProvider;

    public AssistManager_Factory(Provider<DeviceProvisionedController> provider, Provider<Context> provider2, Provider<AssistUtils> provider3, Provider<CommandQueue> provider4, Provider<PhoneStateMonitor> provider5, Provider<OverviewProxyService> provider6, Provider<SysUiState> provider7, Provider<DefaultUiController> provider8, Provider<AssistLogger> provider9, Provider<Handler> provider10) {
        this.controllerProvider = provider;
        this.contextProvider = provider2;
        this.assistUtilsProvider = provider3;
        this.commandQueueProvider = provider4;
        this.phoneStateMonitorProvider = provider5;
        this.overviewProxyServiceProvider = provider6;
        this.sysUiStateProvider = provider7;
        this.defaultUiControllerProvider = provider8;
        this.assistLoggerProvider = provider9;
        this.uiHandlerProvider = provider10;
    }

    public AssistManager get() {
        return newInstance(this.controllerProvider.get(), this.contextProvider.get(), this.assistUtilsProvider.get(), this.commandQueueProvider.get(), this.phoneStateMonitorProvider.get(), this.overviewProxyServiceProvider.get(), DoubleCheck.lazy(this.sysUiStateProvider), this.defaultUiControllerProvider.get(), this.assistLoggerProvider.get(), this.uiHandlerProvider.get());
    }

    public static AssistManager_Factory create(Provider<DeviceProvisionedController> provider, Provider<Context> provider2, Provider<AssistUtils> provider3, Provider<CommandQueue> provider4, Provider<PhoneStateMonitor> provider5, Provider<OverviewProxyService> provider6, Provider<SysUiState> provider7, Provider<DefaultUiController> provider8, Provider<AssistLogger> provider9, Provider<Handler> provider10) {
        return new AssistManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10);
    }

    public static AssistManager newInstance(DeviceProvisionedController deviceProvisionedController, Context context, AssistUtils assistUtils, CommandQueue commandQueue, PhoneStateMonitor phoneStateMonitor, OverviewProxyService overviewProxyService, Lazy<SysUiState> lazy, DefaultUiController defaultUiController, AssistLogger assistLogger, Handler handler) {
        return new AssistManager(deviceProvisionedController, context, assistUtils, commandQueue, phoneStateMonitor, overviewProxyService, lazy, defaultUiController, assistLogger, handler);
    }
}

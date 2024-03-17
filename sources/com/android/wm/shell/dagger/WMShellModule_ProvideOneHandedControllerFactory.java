package com.android.wm.shell.dagger;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class WMShellModule_ProvideOneHandedControllerFactory implements Factory<OneHandedController> {
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayLayout> displayLayoutProvider;
    public final Provider<InteractionJankMonitor> jankMonitorProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<WindowManager> windowManagerProvider;

    public WMShellModule_ProvideOneHandedControllerFactory(Provider<Context> provider, Provider<WindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayLayout> provider4, Provider<TaskStackListenerImpl> provider5, Provider<UiEventLogger> provider6, Provider<InteractionJankMonitor> provider7, Provider<ShellExecutor> provider8, Provider<Handler> provider9) {
        this.contextProvider = provider;
        this.windowManagerProvider = provider2;
        this.displayControllerProvider = provider3;
        this.displayLayoutProvider = provider4;
        this.taskStackListenerProvider = provider5;
        this.uiEventLoggerProvider = provider6;
        this.jankMonitorProvider = provider7;
        this.mainExecutorProvider = provider8;
        this.mainHandlerProvider = provider9;
    }

    public OneHandedController get() {
        return provideOneHandedController(this.contextProvider.get(), this.windowManagerProvider.get(), this.displayControllerProvider.get(), this.displayLayoutProvider.get(), this.taskStackListenerProvider.get(), this.uiEventLoggerProvider.get(), this.jankMonitorProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get());
    }

    public static WMShellModule_ProvideOneHandedControllerFactory create(Provider<Context> provider, Provider<WindowManager> provider2, Provider<DisplayController> provider3, Provider<DisplayLayout> provider4, Provider<TaskStackListenerImpl> provider5, Provider<UiEventLogger> provider6, Provider<InteractionJankMonitor> provider7, Provider<ShellExecutor> provider8, Provider<Handler> provider9) {
        return new WMShellModule_ProvideOneHandedControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9);
    }

    public static OneHandedController provideOneHandedController(Context context, WindowManager windowManager, DisplayController displayController, DisplayLayout displayLayout, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, InteractionJankMonitor interactionJankMonitor, ShellExecutor shellExecutor, Handler handler) {
        return (OneHandedController) Preconditions.checkNotNullFromProvides(WMShellModule.provideOneHandedController(context, windowManager, displayController, displayLayout, taskStackListenerImpl, uiEventLogger, interactionJankMonitor, shellExecutor, handler));
    }
}

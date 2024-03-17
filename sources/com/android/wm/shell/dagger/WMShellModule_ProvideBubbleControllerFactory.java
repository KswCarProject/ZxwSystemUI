package com.android.wm.shell.dagger;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.os.Handler;
import android.os.UserManager;
import android.view.WindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.onehanded.OneHandedController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellModule_ProvideBubbleControllerFactory implements Factory<BubbleController> {
    public final Provider<ShellExecutor> bgExecutorProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DragAndDropController> dragAndDropControllerProvider;
    public final Provider<FloatingContentCoordinator> floatingContentCoordinatorProvider;
    public final Provider<LauncherApps> launcherAppsProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Handler> mainHandlerProvider;
    public final Provider<Optional<OneHandedController>> oneHandedOptionalProvider;
    public final Provider<ShellTaskOrganizer> organizerProvider;
    public final Provider<IStatusBarService> statusBarServiceProvider;
    public final Provider<SyncTransactionQueue> syncQueueProvider;
    public final Provider<TaskStackListenerImpl> taskStackListenerProvider;
    public final Provider<TaskViewTransitions> taskViewTransitionsProvider;
    public final Provider<UiEventLogger> uiEventLoggerProvider;
    public final Provider<UserManager> userManagerProvider;
    public final Provider<WindowManager> windowManagerProvider;
    public final Provider<WindowManagerShellWrapper> windowManagerShellWrapperProvider;

    public WMShellModule_ProvideBubbleControllerFactory(Provider<Context> provider, Provider<FloatingContentCoordinator> provider2, Provider<IStatusBarService> provider3, Provider<WindowManager> provider4, Provider<WindowManagerShellWrapper> provider5, Provider<UserManager> provider6, Provider<LauncherApps> provider7, Provider<TaskStackListenerImpl> provider8, Provider<UiEventLogger> provider9, Provider<ShellTaskOrganizer> provider10, Provider<DisplayController> provider11, Provider<Optional<OneHandedController>> provider12, Provider<DragAndDropController> provider13, Provider<ShellExecutor> provider14, Provider<Handler> provider15, Provider<ShellExecutor> provider16, Provider<TaskViewTransitions> provider17, Provider<SyncTransactionQueue> provider18) {
        this.contextProvider = provider;
        this.floatingContentCoordinatorProvider = provider2;
        this.statusBarServiceProvider = provider3;
        this.windowManagerProvider = provider4;
        this.windowManagerShellWrapperProvider = provider5;
        this.userManagerProvider = provider6;
        this.launcherAppsProvider = provider7;
        this.taskStackListenerProvider = provider8;
        this.uiEventLoggerProvider = provider9;
        this.organizerProvider = provider10;
        this.displayControllerProvider = provider11;
        this.oneHandedOptionalProvider = provider12;
        this.dragAndDropControllerProvider = provider13;
        this.mainExecutorProvider = provider14;
        this.mainHandlerProvider = provider15;
        this.bgExecutorProvider = provider16;
        this.taskViewTransitionsProvider = provider17;
        this.syncQueueProvider = provider18;
    }

    public BubbleController get() {
        return provideBubbleController(this.contextProvider.get(), this.floatingContentCoordinatorProvider.get(), this.statusBarServiceProvider.get(), this.windowManagerProvider.get(), this.windowManagerShellWrapperProvider.get(), this.userManagerProvider.get(), this.launcherAppsProvider.get(), this.taskStackListenerProvider.get(), this.uiEventLoggerProvider.get(), this.organizerProvider.get(), this.displayControllerProvider.get(), this.oneHandedOptionalProvider.get(), this.dragAndDropControllerProvider.get(), this.mainExecutorProvider.get(), this.mainHandlerProvider.get(), this.bgExecutorProvider.get(), this.taskViewTransitionsProvider.get(), this.syncQueueProvider.get());
    }

    public static WMShellModule_ProvideBubbleControllerFactory create(Provider<Context> provider, Provider<FloatingContentCoordinator> provider2, Provider<IStatusBarService> provider3, Provider<WindowManager> provider4, Provider<WindowManagerShellWrapper> provider5, Provider<UserManager> provider6, Provider<LauncherApps> provider7, Provider<TaskStackListenerImpl> provider8, Provider<UiEventLogger> provider9, Provider<ShellTaskOrganizer> provider10, Provider<DisplayController> provider11, Provider<Optional<OneHandedController>> provider12, Provider<DragAndDropController> provider13, Provider<ShellExecutor> provider14, Provider<Handler> provider15, Provider<ShellExecutor> provider16, Provider<TaskViewTransitions> provider17, Provider<SyncTransactionQueue> provider18) {
        return new WMShellModule_ProvideBubbleControllerFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static BubbleController provideBubbleController(Context context, FloatingContentCoordinator floatingContentCoordinator, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, UserManager userManager, LauncherApps launcherApps, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, Optional<OneHandedController> optional, DragAndDropController dragAndDropController, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2, TaskViewTransitions taskViewTransitions, SyncTransactionQueue syncTransactionQueue) {
        return (BubbleController) Preconditions.checkNotNullFromProvides(WMShellModule.provideBubbleController(context, floatingContentCoordinator, iStatusBarService, windowManager, windowManagerShellWrapper, userManager, launcherApps, taskStackListenerImpl, uiEventLogger, shellTaskOrganizer, displayController, optional, dragAndDropController, shellExecutor, handler, shellExecutor2, taskViewTransitions, syncTransactionQueue));
    }
}

package com.android.wm.shell.dagger;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.view.IWindowManager;
import com.android.internal.logging.UiEventLogger;
import com.android.launcher3.icons.IconProvider;
import com.android.wm.shell.RootDisplayAreaOrganizer;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellCommandHandlerImpl;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.TaskViewFactoryController;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.compatui.CompatUIController;
import com.android.wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.wm.shell.displayareahelper.DisplayAreaHelperController;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import com.android.wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.recents.RecentTasks;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.startingsurface.phone.PhoneStartingWindowTypeAlgorithm;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import com.android.wm.shell.transition.ShellTransitions;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import com.android.wm.shell.unfold.UnfoldTransitionHandler;
import dagger.Lazy;
import java.util.Optional;

public abstract class WMShellBaseModule {
    public static DisplayController provideDisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        return new DisplayController(context, iWindowManager, shellExecutor);
    }

    public static DisplayInsetsController provideDisplayInsetsController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor) {
        return new DisplayInsetsController(iWindowManager, displayController, shellExecutor);
    }

    public static DisplayImeController provideDisplayImeController(Optional<DisplayImeController> optional, IWindowManager iWindowManager, DisplayController displayController, DisplayInsetsController displayInsetsController, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        if (optional.isPresent()) {
            return optional.get();
        }
        return new DisplayImeController(iWindowManager, displayController, displayInsetsController, shellExecutor, transactionPool);
    }

    public static DisplayLayout provideDisplayLayout() {
        return new DisplayLayout();
    }

    public static DragAndDropController provideDragAndDropController(Context context, DisplayController displayController, UiEventLogger uiEventLogger, IconProvider iconProvider, ShellExecutor shellExecutor) {
        return new DragAndDropController(context, displayController, uiEventLogger, iconProvider, shellExecutor);
    }

    public static Optional<DragAndDrop> provideDragAndDrop(DragAndDropController dragAndDropController) {
        return Optional.of(dragAndDropController.asDragAndDrop());
    }

    public static ShellTaskOrganizer provideShellTaskOrganizer(ShellExecutor shellExecutor, Context context, CompatUIController compatUIController, Optional<RecentTasksController> optional) {
        return new ShellTaskOrganizer(shellExecutor, context, compatUIController, optional);
    }

    public static KidsModeTaskOrganizer provideKidsModeTaskOrganizer(ShellExecutor shellExecutor, Handler handler, Context context, SyncTransactionQueue syncTransactionQueue, DisplayController displayController, DisplayInsetsController displayInsetsController, Optional<RecentTasksController> optional) {
        return new KidsModeTaskOrganizer(shellExecutor, handler, context, syncTransactionQueue, displayController, displayInsetsController, optional);
    }

    public static Optional<CompatUI> provideCompatUI(CompatUIController compatUIController) {
        return Optional.of(compatUIController.asCompatUI());
    }

    public static CompatUIController provideCompatUIController(Context context, DisplayController displayController, DisplayInsetsController displayInsetsController, DisplayImeController displayImeController, SyncTransactionQueue syncTransactionQueue, ShellExecutor shellExecutor, Lazy<Transitions> lazy) {
        return new CompatUIController(context, displayController, displayInsetsController, displayImeController, syncTransactionQueue, shellExecutor, lazy);
    }

    public static SyncTransactionQueue provideSyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        return new SyncTransactionQueue(transactionPool, shellExecutor);
    }

    public static SystemWindows provideSystemWindows(DisplayController displayController, IWindowManager iWindowManager) {
        return new SystemWindows(displayController, iWindowManager);
    }

    public static IconProvider provideIconProvider(Context context) {
        return new IconProvider(context);
    }

    public static TaskStackListenerImpl providerTaskStackListenerImpl(Handler handler) {
        return new TaskStackListenerImpl(handler);
    }

    public static TransactionPool provideTransactionPool() {
        return new TransactionPool();
    }

    public static WindowManagerShellWrapper provideWindowManagerShellWrapper(ShellExecutor shellExecutor) {
        return new WindowManagerShellWrapper(shellExecutor);
    }

    public static Optional<BackAnimation> provideBackAnimation(Optional<BackAnimationController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda7());
    }

    public static Optional<Bubbles> provideBubbles(Optional<BubbleController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda4());
    }

    public static FullscreenTaskListener provideFullscreenTaskListener(Optional<FullscreenTaskListener> optional, SyncTransactionQueue syncTransactionQueue, Optional<FullscreenUnfoldController> optional2, Optional<RecentTasksController> optional3) {
        if (optional.isPresent()) {
            return optional.get();
        }
        return new FullscreenTaskListener(syncTransactionQueue, optional2, optional3);
    }

    public static Optional<FullscreenUnfoldController> provideFullscreenUnfoldController(Optional<FullscreenUnfoldController> optional, Optional<ShellUnfoldProgressProvider> optional2) {
        if (!optional2.isPresent() || optional2.get() == ShellUnfoldProgressProvider.NO_PROVIDER) {
            return Optional.empty();
        }
        return optional;
    }

    public static Optional<UnfoldTransitionHandler> provideUnfoldTransitionHandler(Optional<ShellUnfoldProgressProvider> optional, TransactionPool transactionPool, Transitions transitions, ShellExecutor shellExecutor) {
        if (optional.isPresent()) {
            return Optional.of(new UnfoldTransitionHandler(optional.get(), transactionPool, shellExecutor, transitions));
        }
        return Optional.empty();
    }

    public static Optional<FreeformTaskListener> provideFreeformTaskListener(Optional<FreeformTaskListener> optional, Context context) {
        if (FreeformTaskListener.isFreeformEnabled(context)) {
            return optional;
        }
        return Optional.empty();
    }

    public static Optional<HideDisplayCutout> provideHideDisplayCutout(Optional<HideDisplayCutoutController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda1());
    }

    public static Optional<HideDisplayCutoutController> provideHideDisplayCutoutController(Context context, DisplayController displayController, ShellExecutor shellExecutor) {
        return Optional.ofNullable(HideDisplayCutoutController.create(context, displayController, shellExecutor));
    }

    public static Optional<OneHanded> provideOneHanded(Optional<OneHandedController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda8());
    }

    public static Optional<OneHandedController> providesOneHandedController(Optional<OneHandedController> optional) {
        if (SystemProperties.getBoolean("ro.support_one_handed_mode", false)) {
            return optional;
        }
        return Optional.empty();
    }

    public static Optional<TaskSurfaceHelper> provideTaskSurfaceHelper(Optional<TaskSurfaceHelperController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda6());
    }

    public static Optional<TaskSurfaceHelperController> provideTaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return Optional.ofNullable(new TaskSurfaceHelperController(shellTaskOrganizer, shellExecutor));
    }

    public static FloatingContentCoordinator provideFloatingContentCoordinator() {
        return new FloatingContentCoordinator();
    }

    public static PipMediaController providePipMediaController(Context context, Handler handler) {
        return new PipMediaController(context, handler);
    }

    public static PipSurfaceTransactionHelper providePipSurfaceTransactionHelper() {
        return new PipSurfaceTransactionHelper();
    }

    public static PipUiEventLogger providePipUiEventLogger(UiEventLogger uiEventLogger, PackageManager packageManager) {
        return new PipUiEventLogger(uiEventLogger, packageManager);
    }

    public static Optional<RecentTasks> provideRecentTasks(Optional<RecentTasksController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda0());
    }

    public static Optional<RecentTasksController> provideRecentTasksController(Context context, TaskStackListenerImpl taskStackListenerImpl, ShellExecutor shellExecutor) {
        return Optional.ofNullable(RecentTasksController.create(context, taskStackListenerImpl, shellExecutor));
    }

    public static ShellTransitions provideRemoteTransitions(Transitions transitions) {
        return transitions.asRemoteTransitions();
    }

    public static Transitions provideTransitions(ShellTaskOrganizer shellTaskOrganizer, TransactionPool transactionPool, DisplayController displayController, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        return new Transitions(shellTaskOrganizer, transactionPool, displayController, context, shellExecutor, handler, shellExecutor2);
    }

    public static TaskViewTransitions provideTaskViewTransitions(Transitions transitions) {
        return new TaskViewTransitions(transitions);
    }

    public static RootTaskDisplayAreaOrganizer provideRootTaskDisplayAreaOrganizer(ShellExecutor shellExecutor, Context context) {
        return new RootTaskDisplayAreaOrganizer(shellExecutor, context);
    }

    public static RootDisplayAreaOrganizer provideRootDisplayAreaOrganizer(ShellExecutor shellExecutor) {
        return new RootDisplayAreaOrganizer(shellExecutor);
    }

    public static Optional<DisplayAreaHelper> provideDisplayAreaHelper(ShellExecutor shellExecutor, RootDisplayAreaOrganizer rootDisplayAreaOrganizer) {
        return Optional.of(new DisplayAreaHelperController(shellExecutor, rootDisplayAreaOrganizer));
    }

    public static Optional<SplitScreen> provideSplitScreen(Optional<SplitScreenController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda2());
    }

    public static Optional<SplitScreenController> providesSplitScreenController(Optional<SplitScreenController> optional, Context context) {
        if (ActivityTaskManager.supportsSplitScreenMultiWindow(context)) {
            return optional;
        }
        return Optional.empty();
    }

    public static Optional<LegacySplitScreen> provideLegacySplitScreen(Optional<LegacySplitScreenController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda5());
    }

    public static Optional<AppPairs> provideAppPairs(Optional<AppPairsController> optional) {
        return optional.map(new WMShellBaseModule$$ExternalSyntheticLambda3());
    }

    public static Optional<StartingSurface> provideStartingSurface(StartingWindowController startingWindowController) {
        return Optional.of(startingWindowController.asStartingSurface());
    }

    public static StartingWindowController provideStartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, IconProvider iconProvider, TransactionPool transactionPool) {
        return new StartingWindowController(context, shellExecutor, startingWindowTypeAlgorithm, iconProvider, transactionPool);
    }

    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm(Optional<StartingWindowTypeAlgorithm> optional) {
        if (optional.isPresent()) {
            return optional.get();
        }
        return new PhoneStartingWindowTypeAlgorithm();
    }

    public static Optional<TaskViewFactory> provideTaskViewFactory(TaskViewFactoryController taskViewFactoryController) {
        return Optional.of(taskViewFactoryController.asTaskViewFactory());
    }

    public static TaskViewFactoryController provideTaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor, SyncTransactionQueue syncTransactionQueue, TaskViewTransitions taskViewTransitions) {
        return new TaskViewFactoryController(shellTaskOrganizer, shellExecutor, syncTransactionQueue, taskViewTransitions);
    }

    public static ShellInit provideShellInit(ShellInitImpl shellInitImpl) {
        return shellInitImpl.asShellInit();
    }

    public static ShellInitImpl provideShellInitImpl(DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<BubbleController> optional, Optional<SplitScreenController> optional2, Optional<AppPairsController> optional3, Optional<PipTouchHandler> optional4, FullscreenTaskListener fullscreenTaskListener, Optional<FullscreenUnfoldController> optional5, Optional<UnfoldTransitionHandler> optional6, Optional<FreeformTaskListener> optional7, Optional<RecentTasksController> optional8, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        return new ShellInitImpl(displayController, displayImeController, displayInsetsController, dragAndDropController, shellTaskOrganizer, kidsModeTaskOrganizer, optional, optional2, optional3, optional4, fullscreenTaskListener, optional5, optional6, optional7, optional8, transitions, startingWindowController, shellExecutor);
    }

    public static Optional<ShellCommandHandler> provideShellCommandHandler(ShellCommandHandlerImpl shellCommandHandlerImpl) {
        return Optional.of(shellCommandHandlerImpl.asShellCommandHandler());
    }

    public static ShellCommandHandlerImpl provideShellCommandHandlerImpl(ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<LegacySplitScreenController> optional, Optional<SplitScreenController> optional2, Optional<Pip> optional3, Optional<OneHandedController> optional4, Optional<HideDisplayCutoutController> optional5, Optional<AppPairsController> optional6, Optional<RecentTasksController> optional7, ShellExecutor shellExecutor) {
        return new ShellCommandHandlerImpl(shellTaskOrganizer, kidsModeTaskOrganizer, optional, optional2, optional3, optional4, optional5, optional6, optional7, shellExecutor);
    }

    public static Optional<BackAnimationController> provideBackAnimationController(Context context, ShellExecutor shellExecutor, Handler handler) {
        if (BackAnimationController.IS_ENABLED) {
            return Optional.of(new BackAnimationController(shellExecutor, handler, context));
        }
        return Optional.empty();
    }
}

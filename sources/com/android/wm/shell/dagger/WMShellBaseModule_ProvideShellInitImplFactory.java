package com.android.wm.shell.dagger;

import com.android.wm.shell.ShellInitImpl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.apppairs.AppPairsController;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayInsetsController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.freeform.FreeformTaskListener;
import com.android.wm.shell.fullscreen.FullscreenTaskListener;
import com.android.wm.shell.fullscreen.FullscreenUnfoldController;
import com.android.wm.shell.kidsmode.KidsModeTaskOrganizer;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.unfold.UnfoldTransitionHandler;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

public final class WMShellBaseModule_ProvideShellInitImplFactory implements Factory<ShellInitImpl> {
    public final Provider<Optional<AppPairsController>> appPairsOptionalProvider;
    public final Provider<Optional<FullscreenUnfoldController>> appUnfoldTransitionControllerProvider;
    public final Provider<Optional<BubbleController>> bubblesOptionalProvider;
    public final Provider<DisplayController> displayControllerProvider;
    public final Provider<DisplayImeController> displayImeControllerProvider;
    public final Provider<DisplayInsetsController> displayInsetsControllerProvider;
    public final Provider<DragAndDropController> dragAndDropControllerProvider;
    public final Provider<Optional<FreeformTaskListener>> freeformTaskListenerProvider;
    public final Provider<FullscreenTaskListener> fullscreenTaskListenerProvider;
    public final Provider<KidsModeTaskOrganizer> kidsModeTaskOrganizerProvider;
    public final Provider<ShellExecutor> mainExecutorProvider;
    public final Provider<Optional<PipTouchHandler>> pipTouchHandlerOptionalProvider;
    public final Provider<Optional<RecentTasksController>> recentTasksOptionalProvider;
    public final Provider<ShellTaskOrganizer> shellTaskOrganizerProvider;
    public final Provider<Optional<SplitScreenController>> splitScreenOptionalProvider;
    public final Provider<StartingWindowController> startingWindowProvider;
    public final Provider<Transitions> transitionsProvider;
    public final Provider<Optional<UnfoldTransitionHandler>> unfoldTransitionHandlerProvider;

    public WMShellBaseModule_ProvideShellInitImplFactory(Provider<DisplayController> provider, Provider<DisplayImeController> provider2, Provider<DisplayInsetsController> provider3, Provider<DragAndDropController> provider4, Provider<ShellTaskOrganizer> provider5, Provider<KidsModeTaskOrganizer> provider6, Provider<Optional<BubbleController>> provider7, Provider<Optional<SplitScreenController>> provider8, Provider<Optional<AppPairsController>> provider9, Provider<Optional<PipTouchHandler>> provider10, Provider<FullscreenTaskListener> provider11, Provider<Optional<FullscreenUnfoldController>> provider12, Provider<Optional<UnfoldTransitionHandler>> provider13, Provider<Optional<FreeformTaskListener>> provider14, Provider<Optional<RecentTasksController>> provider15, Provider<Transitions> provider16, Provider<StartingWindowController> provider17, Provider<ShellExecutor> provider18) {
        this.displayControllerProvider = provider;
        this.displayImeControllerProvider = provider2;
        this.displayInsetsControllerProvider = provider3;
        this.dragAndDropControllerProvider = provider4;
        this.shellTaskOrganizerProvider = provider5;
        this.kidsModeTaskOrganizerProvider = provider6;
        this.bubblesOptionalProvider = provider7;
        this.splitScreenOptionalProvider = provider8;
        this.appPairsOptionalProvider = provider9;
        this.pipTouchHandlerOptionalProvider = provider10;
        this.fullscreenTaskListenerProvider = provider11;
        this.appUnfoldTransitionControllerProvider = provider12;
        this.unfoldTransitionHandlerProvider = provider13;
        this.freeformTaskListenerProvider = provider14;
        this.recentTasksOptionalProvider = provider15;
        this.transitionsProvider = provider16;
        this.startingWindowProvider = provider17;
        this.mainExecutorProvider = provider18;
    }

    public ShellInitImpl get() {
        return provideShellInitImpl(this.displayControllerProvider.get(), this.displayImeControllerProvider.get(), this.displayInsetsControllerProvider.get(), this.dragAndDropControllerProvider.get(), this.shellTaskOrganizerProvider.get(), this.kidsModeTaskOrganizerProvider.get(), this.bubblesOptionalProvider.get(), this.splitScreenOptionalProvider.get(), this.appPairsOptionalProvider.get(), this.pipTouchHandlerOptionalProvider.get(), this.fullscreenTaskListenerProvider.get(), this.appUnfoldTransitionControllerProvider.get(), this.unfoldTransitionHandlerProvider.get(), this.freeformTaskListenerProvider.get(), this.recentTasksOptionalProvider.get(), this.transitionsProvider.get(), this.startingWindowProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideShellInitImplFactory create(Provider<DisplayController> provider, Provider<DisplayImeController> provider2, Provider<DisplayInsetsController> provider3, Provider<DragAndDropController> provider4, Provider<ShellTaskOrganizer> provider5, Provider<KidsModeTaskOrganizer> provider6, Provider<Optional<BubbleController>> provider7, Provider<Optional<SplitScreenController>> provider8, Provider<Optional<AppPairsController>> provider9, Provider<Optional<PipTouchHandler>> provider10, Provider<FullscreenTaskListener> provider11, Provider<Optional<FullscreenUnfoldController>> provider12, Provider<Optional<UnfoldTransitionHandler>> provider13, Provider<Optional<FreeformTaskListener>> provider14, Provider<Optional<RecentTasksController>> provider15, Provider<Transitions> provider16, Provider<StartingWindowController> provider17, Provider<ShellExecutor> provider18) {
        return new WMShellBaseModule_ProvideShellInitImplFactory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11, provider12, provider13, provider14, provider15, provider16, provider17, provider18);
    }

    public static ShellInitImpl provideShellInitImpl(DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<BubbleController> optional, Optional<SplitScreenController> optional2, Optional<AppPairsController> optional3, Optional<PipTouchHandler> optional4, FullscreenTaskListener fullscreenTaskListener, Optional<FullscreenUnfoldController> optional5, Optional<UnfoldTransitionHandler> optional6, Optional<FreeformTaskListener> optional7, Optional<RecentTasksController> optional8, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        return (ShellInitImpl) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideShellInitImpl(displayController, displayImeController, displayInsetsController, dragAndDropController, shellTaskOrganizer, kidsModeTaskOrganizer, optional, optional2, optional3, optional4, fullscreenTaskListener, optional5, optional6, optional7, optional8, transitions, startingWindowController, shellExecutor));
    }
}

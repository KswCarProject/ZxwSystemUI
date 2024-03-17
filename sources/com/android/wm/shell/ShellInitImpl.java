package com.android.wm.shell;

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
import java.util.Optional;

public class ShellInitImpl {
    public final Optional<AppPairsController> mAppPairsOptional;
    public final Optional<BubbleController> mBubblesOptional;
    public final DisplayController mDisplayController;
    public final DisplayImeController mDisplayImeController;
    public final DisplayInsetsController mDisplayInsetsController;
    public final DragAndDropController mDragAndDropController;
    public final Optional<FreeformTaskListener> mFreeformTaskListenerOptional;
    public final FullscreenTaskListener mFullscreenTaskListener;
    public final Optional<FullscreenUnfoldController> mFullscreenUnfoldController;
    public final InitImpl mImpl = new InitImpl();
    public final KidsModeTaskOrganizer mKidsModeTaskOrganizer;
    public final ShellExecutor mMainExecutor;
    public final Optional<PipTouchHandler> mPipTouchHandlerOptional;
    public final Optional<RecentTasksController> mRecentTasks;
    public final ShellTaskOrganizer mShellTaskOrganizer;
    public final Optional<SplitScreenController> mSplitScreenOptional;
    public final StartingWindowController mStartingWindow;
    public final Transitions mTransitions;
    public final Optional<UnfoldTransitionHandler> mUnfoldTransitionHandler;

    public ShellInitImpl(DisplayController displayController, DisplayImeController displayImeController, DisplayInsetsController displayInsetsController, DragAndDropController dragAndDropController, ShellTaskOrganizer shellTaskOrganizer, KidsModeTaskOrganizer kidsModeTaskOrganizer, Optional<BubbleController> optional, Optional<SplitScreenController> optional2, Optional<AppPairsController> optional3, Optional<PipTouchHandler> optional4, FullscreenTaskListener fullscreenTaskListener, Optional<FullscreenUnfoldController> optional5, Optional<UnfoldTransitionHandler> optional6, Optional<FreeformTaskListener> optional7, Optional<RecentTasksController> optional8, Transitions transitions, StartingWindowController startingWindowController, ShellExecutor shellExecutor) {
        this.mDisplayController = displayController;
        this.mDisplayImeController = displayImeController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mDragAndDropController = dragAndDropController;
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mKidsModeTaskOrganizer = kidsModeTaskOrganizer;
        this.mBubblesOptional = optional;
        this.mSplitScreenOptional = optional2;
        this.mAppPairsOptional = optional3;
        this.mFullscreenTaskListener = fullscreenTaskListener;
        this.mPipTouchHandlerOptional = optional4;
        this.mFullscreenUnfoldController = optional5;
        this.mUnfoldTransitionHandler = optional6;
        this.mFreeformTaskListenerOptional = optional7;
        this.mRecentTasks = optional8;
        this.mTransitions = transitions;
        this.mMainExecutor = shellExecutor;
        this.mStartingWindow = startingWindowController;
    }

    public ShellInit asShellInit() {
        return this.mImpl;
    }

    public final void init() {
        this.mDisplayController.initialize();
        this.mDisplayInsetsController.initialize();
        this.mDisplayImeController.startMonitorDisplays();
        this.mShellTaskOrganizer.addListenerForType(this.mFullscreenTaskListener, -2);
        this.mShellTaskOrganizer.initStartingWindow(this.mStartingWindow);
        this.mShellTaskOrganizer.registerOrganizer();
        this.mAppPairsOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda0());
        this.mSplitScreenOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda1());
        this.mBubblesOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda2());
        this.mDragAndDropController.initialize(this.mSplitScreenOptional);
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mTransitions.register(this.mShellTaskOrganizer);
            this.mUnfoldTransitionHandler.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda3());
        }
        this.mPipTouchHandlerOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda4());
        this.mFreeformTaskListenerOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda5(this));
        this.mFullscreenUnfoldController.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda6());
        this.mRecentTasks.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda7());
        this.mKidsModeTaskOrganizer.initialize(this.mStartingWindow);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(FreeformTaskListener freeformTaskListener) {
        this.mShellTaskOrganizer.addListenerForType(freeformTaskListener, -5);
    }

    public class InitImpl implements ShellInit {
        public InitImpl() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$init$0() {
            ShellInitImpl.this.init();
        }

        public void init() {
            try {
                ShellInitImpl.this.mMainExecutor.executeBlocking(new ShellInitImpl$InitImpl$$ExternalSyntheticLambda0(this));
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to initialize the Shell in 2s", e);
            }
        }
    }
}

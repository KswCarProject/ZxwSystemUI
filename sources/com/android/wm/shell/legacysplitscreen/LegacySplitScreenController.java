package com.android.wm.shell.legacysplitscreen;

import android.animation.AnimationHandler;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.provider.Settings;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.ViewGroup;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LegacySplitScreenController implements DisplayController.OnDisplaysChangedListener {
    public volatile boolean mAdjustedForIme = false;
    public final ArrayList<WeakReference<BiConsumer<Rect, Rect>>> mBoundsChangedListeners = new ArrayList<>();
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final DividerState mDividerState = new DividerState();
    public final CopyOnWriteArrayList<WeakReference<Consumer<Boolean>>> mDockedStackExistsListeners = new CopyOnWriteArrayList<>();
    public final ForcedResizableInfoActivityController mForcedResizableController;
    public boolean mHomeStackResizable = false;
    public final DisplayImeController mImeController;
    public final DividerImeController mImePositionProcessor;
    public final SplitScreenImpl mImpl = new SplitScreenImpl();
    public boolean mIsKeyguardShowing;
    public final ShellExecutor mMainExecutor;
    public volatile boolean mMinimized = false;
    public LegacySplitDisplayLayout mRotateSplitLayout;
    public final DisplayChangeController.OnDisplayChangingListener mRotationController;
    public final AnimationHandler mSfVsyncAnimationHandler;
    public LegacySplitDisplayLayout mSplitLayout;
    public final LegacySplitScreenTaskListener mSplits;
    public final SystemWindows mSystemWindows;
    public final TaskOrganizer mTaskOrganizer;
    public final TransactionPool mTransactionPool;
    public DividerView mView;
    public boolean mVisible = false;
    public DividerWindowManager mWindowManager;
    public final WindowManagerProxy mWindowManagerProxy;

    public LegacySplitScreenController(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, ShellExecutor shellExecutor, AnimationHandler animationHandler) {
        this.mContext = context;
        this.mDisplayController = displayController;
        this.mSystemWindows = systemWindows;
        this.mImeController = displayImeController;
        this.mMainExecutor = shellExecutor;
        this.mSfVsyncAnimationHandler = animationHandler;
        this.mForcedResizableController = new ForcedResizableInfoActivityController(context, this, shellExecutor);
        this.mTransactionPool = transactionPool;
        this.mWindowManagerProxy = new WindowManagerProxy(syncTransactionQueue, shellTaskOrganizer);
        this.mTaskOrganizer = shellTaskOrganizer;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = new LegacySplitScreenTaskListener(this, shellTaskOrganizer, transitions, syncTransactionQueue);
        this.mSplits = legacySplitScreenTaskListener;
        this.mImePositionProcessor = new DividerImeController(legacySplitScreenTaskListener, transactionPool, shellExecutor, shellTaskOrganizer);
        this.mRotationController = new LegacySplitScreenController$$ExternalSyntheticLambda0(this);
        this.mWindowManager = new DividerWindowManager(systemWindows);
        if (context.getResources().getBoolean(17891816)) {
            displayController.addDisplayWindowListener(this);
            taskStackListenerImpl.addListener(new TaskStackListenerCallback() {
                public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                    if (z3 && runningTaskInfo.getWindowingMode() == 3 && LegacySplitScreenController.this.mSplits.isSplitScreenSupported() && LegacySplitScreenController.this.isMinimized()) {
                        LegacySplitScreenController.this.onUndockingTask();
                    }
                }

                public void onActivityForcedResizable(String str, int i, int i2) {
                    LegacySplitScreenController.this.mForcedResizableController.activityForcedResizable(str, i, i2);
                }

                public void onActivityDismissingDockedStack() {
                    LegacySplitScreenController.this.mForcedResizableController.activityDismissingSplitScreen();
                }

                public void onActivityLaunchOnSecondaryDisplayFailed() {
                    LegacySplitScreenController.this.mForcedResizableController.activityLaunchOnSecondaryDisplayFailed();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        int i4;
        if (this.mSplits.isSplitScreenSupported() && this.mWindowManagerProxy != null) {
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            LegacySplitDisplayLayout legacySplitDisplayLayout = new LegacySplitDisplayLayout(this.mContext, new DisplayLayout(this.mDisplayController.getDisplayLayout(i)), this.mSplits);
            legacySplitDisplayLayout.rotateTo(i3);
            this.mRotateSplitLayout = legacySplitDisplayLayout;
            if (this.mMinimized) {
                i4 = this.mView.mSnapTargetBeforeMinimized.position;
            } else {
                i4 = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget().position;
            }
            legacySplitDisplayLayout.resizeSplits(legacySplitDisplayLayout.getSnapAlgorithm().calculateNonDismissingSnapTarget(i4).position, windowContainerTransaction2);
            if (isSplitActive() && this.mHomeStackResizable) {
                this.mWindowManagerProxy.applyHomeTasksMinimized(legacySplitDisplayLayout, this.mSplits.mSecondary.token, windowContainerTransaction2);
            }
            if (this.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction2)) {
                Slog.w("SplitScreenCtrl", "Screen rotated while other operations were pending, this may result in some graphical artifacts.");
            } else {
                windowContainerTransaction.merge(windowContainerTransaction2, true);
            }
        }
    }

    public LegacySplitScreen asLegacySplitScreen() {
        return this.mImpl;
    }

    public void onSplitScreenSupported() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mSplitLayout.resizeSplits(this.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position, windowContainerTransaction);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
    }

    public void onDisplayAdded(int i) {
        if (i == 0) {
            this.mSplitLayout = new LegacySplitDisplayLayout(this.mDisplayController.getDisplayContext(i), this.mDisplayController.getDisplayLayout(i), this.mSplits);
            this.mImeController.addPositionProcessor(this.mImePositionProcessor);
            this.mDisplayController.addDisplayChangingController(this.mRotationController);
            if (!ActivityTaskManager.supportsSplitScreenMultiWindow(this.mContext)) {
                removeDivider();
                return;
            }
            try {
                this.mSplits.init();
            } catch (Exception e) {
                Slog.e("SplitScreenCtrl", "Failed to register docked stack listener", e);
                removeDivider();
            }
        }
    }

    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        if (i == 0 && this.mSplits.isSplitScreenSupported()) {
            LegacySplitDisplayLayout legacySplitDisplayLayout = new LegacySplitDisplayLayout(this.mDisplayController.getDisplayContext(i), this.mDisplayController.getDisplayLayout(i), this.mSplits);
            this.mSplitLayout = legacySplitDisplayLayout;
            if (this.mRotateSplitLayout == null) {
                int i2 = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget().position;
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                this.mSplitLayout.resizeSplits(i2, windowContainerTransaction);
                this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            } else if (legacySplitDisplayLayout.mDisplayLayout.rotation() == this.mRotateSplitLayout.mDisplayLayout.rotation()) {
                this.mSplitLayout.mPrimary = new Rect(this.mRotateSplitLayout.mPrimary);
                this.mSplitLayout.mSecondary = new Rect(this.mRotateSplitLayout.mSecondary);
                this.mRotateSplitLayout = null;
            }
            if (isSplitActive()) {
                update(configuration);
            }
        }
    }

    public boolean isMinimized() {
        return this.mMinimized;
    }

    public boolean isHomeStackResizable() {
        return this.mHomeStackResizable;
    }

    public DividerView getDividerView() {
        return this.mView;
    }

    public boolean isDividerVisible() {
        DividerView dividerView = this.mView;
        return dividerView != null && dividerView.getVisibility() == 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r1 = r1.mSecondary;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSplitActive() {
        /*
            r1 = this;
            com.android.wm.shell.legacysplitscreen.LegacySplitScreenTaskListener r1 = r1.mSplits
            android.app.ActivityManager$RunningTaskInfo r0 = r1.mPrimary
            if (r0 == 0) goto L_0x0014
            android.app.ActivityManager$RunningTaskInfo r1 = r1.mSecondary
            if (r1 == 0) goto L_0x0014
            int r0 = r0.topActivityType
            if (r0 != 0) goto L_0x0012
            int r1 = r1.topActivityType
            if (r1 == 0) goto L_0x0014
        L_0x0012:
            r1 = 1
            goto L_0x0015
        L_0x0014:
            r1 = 0
        L_0x0015:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController.isSplitActive():boolean");
    }

    public void addDivider(Configuration configuration) {
        int i;
        Context displayContext = this.mDisplayController.getDisplayContext(this.mContext.getDisplayId());
        DividerView dividerView = (DividerView) LayoutInflater.from(displayContext).inflate(R.layout.docked_stack_divider, (ViewGroup) null);
        this.mView = dividerView;
        dividerView.setAnimationHandler(this.mSfVsyncAnimationHandler);
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(this.mContext.getDisplayId());
        this.mView.injectDependencies(this, this.mWindowManager, this.mDividerState, this.mForcedResizableController, this.mSplits, this.mSplitLayout, this.mImePositionProcessor, this.mWindowManagerProxy);
        boolean z = false;
        this.mView.setVisibility(this.mVisible ? 0 : 4);
        this.mView.setMinimizedDockStack(this.mMinimized, this.mHomeStackResizable, (SurfaceControl.Transaction) null);
        int dimensionPixelSize = displayContext.getResources().getDimensionPixelSize(17105204);
        if (configuration.orientation == 2) {
            z = true;
        }
        if (z) {
            i = dimensionPixelSize;
        } else {
            i = displayLayout.width();
        }
        if (z) {
            dimensionPixelSize = displayLayout.height();
        }
        this.mWindowManager.add(this.mView, i, dimensionPixelSize, this.mContext.getDisplayId());
    }

    public void removeDivider() {
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            dividerView.onDividerRemoved();
        }
        this.mWindowManager.remove();
    }

    public void update(Configuration configuration) {
        boolean z = this.mView != null && this.mIsKeyguardShowing;
        removeDivider();
        addDivider(configuration);
        if (this.mMinimized) {
            this.mView.setMinimizedDockStack(true, this.mHomeStackResizable, (SurfaceControl.Transaction) null);
            updateTouchable();
        }
        this.mView.setHidden(z);
    }

    public void onTaskVanished() {
        removeDivider();
    }

    public void updateVisibility(boolean z) {
        if (this.mVisible != z) {
            this.mVisible = z;
            this.mView.setVisibility(z ? 0 : 4);
            if (z) {
                this.mView.enterSplitMode(this.mHomeStackResizable);
                this.mWindowManagerProxy.runInSync(new LegacySplitScreenController$$ExternalSyntheticLambda2(this));
            } else {
                this.mView.exitSplitMode();
                this.mWindowManagerProxy.runInSync(new LegacySplitScreenController$$ExternalSyntheticLambda3(this));
            }
            synchronized (this.mDockedStackExistsListeners) {
                this.mDockedStackExistsListeners.removeIf(new LegacySplitScreenController$$ExternalSyntheticLambda4(z));
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateVisibility$1(SurfaceControl.Transaction transaction) {
        this.mView.setMinimizedDockStack(this.mMinimized, this.mHomeStackResizable, transaction);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateVisibility$2(SurfaceControl.Transaction transaction) {
        this.mView.setMinimizedDockStack(false, this.mHomeStackResizable, transaction);
    }

    public static /* synthetic */ boolean lambda$updateVisibility$3(boolean z, WeakReference weakReference) {
        Consumer consumer = (Consumer) weakReference.get();
        if (consumer != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        return consumer == null;
    }

    public void setHomeMinimized(boolean z) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        int i = 0;
        boolean z2 = this.mMinimized != z;
        if (z2) {
            this.mMinimized = z;
        }
        windowContainerTransaction.setFocusable(this.mSplits.mPrimary.token, true ^ this.mMinimized);
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            if (dividerView.getDisplay() != null) {
                i = this.mView.getDisplay().getDisplayId();
            }
            if (this.mMinimized) {
                this.mImePositionProcessor.pause(i);
            }
            if (z2) {
                this.mView.setMinimizedDockStack(z, getAnimDuration(), this.mHomeStackResizable);
            }
            if (!this.mMinimized) {
                this.mImePositionProcessor.resume(i);
            }
        }
        updateTouchable();
        if (!this.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction)) {
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    public void setAdjustedForIme(boolean z) {
        if (this.mAdjustedForIme != z) {
            this.mAdjustedForIme = z;
            updateTouchable();
        }
    }

    public void updateTouchable() {
        this.mWindowManager.setTouchable(!this.mAdjustedForIme);
    }

    public void onUndockingTask() {
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            dividerView.onUndockingTask();
        }
    }

    public void onUndockingTask(boolean z) {
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            dividerView.onUndockingTask(z);
        }
    }

    public void switchSplitTask() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (WindowManagerProxy.buildSwitchSplit(windowContainerTransaction, this.mSplits, this.mSplitLayout)) {
            this.mWindowManagerProxy.applySyncTransaction(windowContainerTransaction);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("  mVisible=");
        printWriter.println(this.mVisible);
        printWriter.print("  mMinimized=");
        printWriter.println(this.mMinimized);
        printWriter.print("  mAdjustedForIme=");
        printWriter.println(this.mAdjustedForIme);
    }

    public long getAnimDuration() {
        return (long) (Settings.Global.getFloat(this.mContext.getContentResolver(), "transition_animation_scale", this.mContext.getResources().getFloat(17105063)) * 336.0f);
    }

    public void registerInSplitScreenListener(Consumer<Boolean> consumer) {
        consumer.accept(Boolean.valueOf(isDividerVisible()));
        synchronized (this.mDockedStackExistsListeners) {
            this.mDockedStackExistsListeners.add(new WeakReference(consumer));
        }
    }

    public void notifyBoundsChanged(Rect rect, Rect rect2) {
        synchronized (this.mBoundsChangedListeners) {
            this.mBoundsChangedListeners.removeIf(new LegacySplitScreenController$$ExternalSyntheticLambda1(rect, rect2));
        }
    }

    public static /* synthetic */ boolean lambda$notifyBoundsChanged$5(Rect rect, Rect rect2, WeakReference weakReference) {
        BiConsumer biConsumer = (BiConsumer) weakReference.get();
        if (biConsumer != null) {
            biConsumer.accept(rect, rect2);
        }
        return biConsumer == null;
    }

    public void startEnterSplit() {
        update(this.mDisplayController.getDisplayContext(this.mContext.getDisplayId()).getResources().getConfiguration());
        WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mRotateSplitLayout;
        if (legacySplitDisplayLayout == null) {
            legacySplitDisplayLayout = this.mSplitLayout;
        }
        this.mHomeStackResizable = windowManagerProxy.applyEnterSplit(legacySplitScreenTaskListener, legacySplitDisplayLayout);
    }

    public void prepareEnterSplitTransition(WindowContainerTransaction windowContainerTransaction) {
        WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mRotateSplitLayout;
        if (legacySplitDisplayLayout == null) {
            legacySplitDisplayLayout = this.mSplitLayout;
        }
        this.mHomeStackResizable = windowManagerProxy.buildEnterSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout);
    }

    public void finishEnterSplitTransition(boolean z) {
        update(this.mDisplayController.getDisplayContext(this.mContext.getDisplayId()).getResources().getConfiguration());
        if (z) {
            ensureMinimizedSplit();
        } else {
            ensureNormalSplit();
        }
    }

    public void startDismissSplit(boolean z) {
        startDismissSplit(z, false);
    }

    public void startDismissSplit(boolean z, boolean z2) {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mSplits.getSplitTransitions().dismissSplit(this.mSplits, this.mSplitLayout, !z, z2);
            return;
        }
        this.mWindowManagerProxy.applyDismissSplit(this.mSplits, this.mSplitLayout, !z);
        onDismissSplit();
    }

    public void onDismissSplit() {
        updateVisibility(false);
        this.mMinimized = false;
        this.mDividerState.mRatioPositionBeforeMinimized = 0.0f;
        removeDivider();
        this.mImePositionProcessor.reset();
    }

    public void ensureMinimizedSplit() {
        setHomeMinimized(true);
        if (this.mView != null && !isDividerVisible()) {
            updateVisibility(true);
        }
    }

    public void ensureNormalSplit() {
        setHomeMinimized(false);
        if (this.mView != null && !isDividerVisible()) {
            updateVisibility(true);
        }
    }

    public LegacySplitDisplayLayout getSplitLayout() {
        return this.mSplitLayout;
    }

    public WindowManagerProxy getWmProxy() {
        return this.mWindowManagerProxy;
    }

    public WindowContainerToken getSecondaryRoot() {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        if (legacySplitScreenTaskListener == null || (runningTaskInfo = legacySplitScreenTaskListener.mSecondary) == null) {
            return null;
        }
        return runningTaskInfo.token;
    }

    public class SplitScreenImpl implements LegacySplitScreen {
        public SplitScreenImpl() {
        }
    }
}

package com.android.wm.shell.unfold;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import com.android.wm.shell.unfold.ShellUnfoldProgressProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class UnfoldTransitionHandler implements Transitions.TransitionHandler, ShellUnfoldProgressProvider.UnfoldListener {
    public final List<TransitionInfo.Change> mAnimatedFullscreenTasks = new ArrayList();
    public final Executor mExecutor;
    public Transitions.TransitionFinishCallback mFinishCallback;
    public final TransactionPool mTransactionPool;
    public IBinder mTransition;
    public final Transitions mTransitions;
    public final ShellUnfoldProgressProvider mUnfoldProgressProvider;

    public UnfoldTransitionHandler(ShellUnfoldProgressProvider shellUnfoldProgressProvider, TransactionPool transactionPool, Executor executor, Transitions transitions) {
        this.mUnfoldProgressProvider = shellUnfoldProgressProvider;
        this.mTransactionPool = transactionPool;
        this.mExecutor = executor;
        this.mTransitions = transitions;
    }

    public void init() {
        this.mTransitions.addHandler(this);
        this.mUnfoldProgressProvider.addListener(this.mExecutor, this);
    }

    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (iBinder != this.mTransition) {
            return false;
        }
        transaction.apply();
        this.mAnimatedFullscreenTasks.clear();
        transitionInfo.getChanges().forEach(new UnfoldTransitionHandler$$ExternalSyntheticLambda0(this));
        this.mFinishCallback = transitionFinishCallback;
        this.mTransition = null;
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$0(TransitionInfo.Change change) {
        boolean z = true;
        if (change.getTaskInfo() == null || change.getTaskInfo().getWindowingMode() != 1 || change.getTaskInfo().getActivityType() == 2 || change.getMode() != 6) {
            z = false;
        }
        if (z) {
            this.mAnimatedFullscreenTasks.add(change);
        }
    }

    public void onStateChangeProgress(float f) {
        this.mAnimatedFullscreenTasks.forEach(new UnfoldTransitionHandler$$ExternalSyntheticLambda1(this, f));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onStateChangeProgress$1(float f, TransitionInfo.Change change) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        float f2 = (f * 0.2f) + 0.8f;
        acquire.setScale(change.getLeash(), f2, f2);
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    public void onStateChangeFinished() {
        Transitions.TransitionFinishCallback transitionFinishCallback = this.mFinishCallback;
        if (transitionFinishCallback != null) {
            transitionFinishCallback.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
            this.mFinishCallback = null;
            this.mAnimatedFullscreenTasks.clear();
        }
    }

    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        if (transitionRequestInfo.getType() != 6 || transitionRequestInfo.getDisplayChange() == null || !transitionRequestInfo.getDisplayChange().isPhysicalDisplayChanged()) {
            return null;
        }
        this.mTransition = iBinder;
        return new WindowContainerTransaction();
    }
}

package com.android.wm.shell.pip;

import android.app.TaskInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTransition$$ExternalSyntheticLambda0 implements Transitions.TransitionFinishCallback {
    public final /* synthetic */ PipTransition f$0;
    public final /* synthetic */ TaskInfo f$1;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$2;

    public /* synthetic */ PipTransition$$ExternalSyntheticLambda0(PipTransition pipTransition, TaskInfo taskInfo, Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.f$0 = pipTransition;
        this.f$1 = taskInfo;
        this.f$2 = transitionFinishCallback;
    }

    public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        this.f$0.lambda$startExitAnimation$0(this.f$1, this.f$2, windowContainerTransaction, windowContainerTransactionCallback);
    }
}

package com.android.wm.shell.splitscreen;

import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenTransitions$$ExternalSyntheticLambda0 implements Transitions.TransitionFinishCallback {
    public final /* synthetic */ SplitScreenTransitions f$0;

    public /* synthetic */ SplitScreenTransitions$$ExternalSyntheticLambda0(SplitScreenTransitions splitScreenTransitions) {
        this.f$0 = splitScreenTransitions;
    }

    public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        this.f$0.onFinish(windowContainerTransaction, windowContainerTransactionCallback);
    }
}

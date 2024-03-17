package com.android.wm.shell.transition;

import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OneShotRemoteHandler$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ Transitions.TransitionFinishCallback f$0;

    public /* synthetic */ OneShotRemoteHandler$$ExternalSyntheticLambda1(Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.f$0 = transitionFinishCallback;
    }

    public final void run() {
        this.f$0.onTransitionFinished((WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
    }
}

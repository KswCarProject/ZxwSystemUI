package com.android.wm.shell.transition;

import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.transition.OneShotRemoteHandler;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OneShotRemoteHandler$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SurfaceControl.Transaction f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$2;
    public final /* synthetic */ WindowContainerTransaction f$3;

    public /* synthetic */ OneShotRemoteHandler$1$$ExternalSyntheticLambda0(SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = transaction;
        this.f$1 = transaction2;
        this.f$2 = transitionFinishCallback;
        this.f$3 = windowContainerTransaction;
    }

    public final void run() {
        OneShotRemoteHandler.AnonymousClass1.lambda$onTransitionFinished$0(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}

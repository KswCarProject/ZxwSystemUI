package com.android.wm.shell.transition;

import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.transition.RemoteTransitionHandler;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RemoteTransitionHandler$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RemoteTransitionHandler.AnonymousClass1 f$0;
    public final /* synthetic */ SurfaceControl.Transaction f$1;
    public final /* synthetic */ SurfaceControl.Transaction f$2;
    public final /* synthetic */ IBinder f$3;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$4;
    public final /* synthetic */ WindowContainerTransaction f$5;

    public /* synthetic */ RemoteTransitionHandler$1$$ExternalSyntheticLambda0(RemoteTransitionHandler.AnonymousClass1 r1, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = r1;
        this.f$1 = transaction;
        this.f$2 = transaction2;
        this.f$3 = iBinder;
        this.f$4 = transitionFinishCallback;
        this.f$5 = windowContainerTransaction;
    }

    public final void run() {
        this.f$0.lambda$onTransitionFinished$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}

package com.android.wm.shell.transition;

import android.os.IBinder;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.transition.RemoteTransitionHandler;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RemoteTransitionHandler$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RemoteTransitionHandler.AnonymousClass2 f$0;
    public final /* synthetic */ IBinder f$1;
    public final /* synthetic */ Transitions.TransitionFinishCallback f$2;
    public final /* synthetic */ WindowContainerTransaction f$3;

    public /* synthetic */ RemoteTransitionHandler$2$$ExternalSyntheticLambda0(RemoteTransitionHandler.AnonymousClass2 r1, IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = r1;
        this.f$1 = iBinder;
        this.f$2 = transitionFinishCallback;
        this.f$3 = windowContainerTransaction;
    }

    public final void run() {
        this.f$0.lambda$onTransitionFinished$0(this.f$1, this.f$2, this.f$3);
    }
}

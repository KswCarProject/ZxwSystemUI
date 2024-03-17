package com.android.wm.shell.transition;

import com.android.wm.shell.transition.RemoteTransitionHandler;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RemoteTransitionHandler$RemoteDeathHandler$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RemoteTransitionHandler.RemoteDeathHandler f$0;

    public /* synthetic */ RemoteTransitionHandler$RemoteDeathHandler$$ExternalSyntheticLambda0(RemoteTransitionHandler.RemoteDeathHandler remoteDeathHandler) {
        this.f$0 = remoteDeathHandler;
    }

    public final void run() {
        this.f$0.lambda$binderDied$0();
    }
}

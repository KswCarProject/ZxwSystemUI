package com.android.wm.shell.transition;

import android.window.RemoteTransition;
import android.window.TransitionFilter;
import com.android.wm.shell.transition.Transitions;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ Transitions.ShellTransitionImpl f$0;
    public final /* synthetic */ TransitionFilter f$1;
    public final /* synthetic */ RemoteTransition f$2;

    public /* synthetic */ Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(Transitions.ShellTransitionImpl shellTransitionImpl, TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
        this.f$0 = shellTransitionImpl;
        this.f$1 = transitionFilter;
        this.f$2 = remoteTransition;
    }

    public final void run() {
        this.f$0.lambda$registerRemote$0(this.f$1, this.f$2);
    }
}

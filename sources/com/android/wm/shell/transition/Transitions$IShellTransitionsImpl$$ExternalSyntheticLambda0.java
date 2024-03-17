package com.android.wm.shell.transition;

import android.window.RemoteTransition;
import android.window.TransitionFilter;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ TransitionFilter f$0;
    public final /* synthetic */ RemoteTransition f$1;

    public /* synthetic */ Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
        this.f$0 = transitionFilter;
        this.f$1 = remoteTransition;
    }

    public final void accept(Object obj) {
        ((Transitions) obj).mRemoteTransitionHandler.addFiltered(this.f$0, this.f$1);
    }
}

package com.android.wm.shell.transition;

import android.window.RemoteTransition;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ RemoteTransition f$0;

    public /* synthetic */ Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1(RemoteTransition remoteTransition) {
        this.f$0 = remoteTransition;
    }

    public final void accept(Object obj) {
        ((Transitions) obj).mRemoteTransitionHandler.removeFiltered(this.f$0);
    }
}

package com.android.wm.shell.pip;

import com.android.wm.shell.pip.PinnedStackListenerForwarder;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ PinnedStackListenerForwarder.PinnedTaskListenerImpl f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2(PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl, boolean z) {
        this.f$0 = pinnedTaskListenerImpl;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.lambda$onMovementBoundsChanged$0(this.f$1);
    }
}

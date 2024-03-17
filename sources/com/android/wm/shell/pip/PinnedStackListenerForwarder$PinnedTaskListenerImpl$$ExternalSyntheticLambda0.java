package com.android.wm.shell.pip;

import com.android.wm.shell.pip.PinnedStackListenerForwarder;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ PinnedStackListenerForwarder.PinnedTaskListenerImpl f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0(PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl, boolean z, int i) {
        this.f$0 = pinnedTaskListenerImpl;
        this.f$1 = z;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$onImeVisibilityChanged$1(this.f$1, this.f$2);
    }
}

package com.android.wm.shell.pip;

import android.content.ComponentName;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ PinnedStackListenerForwarder.PinnedTaskListenerImpl f$0;
    public final /* synthetic */ ComponentName f$1;

    public /* synthetic */ PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda1(PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl, ComponentName componentName) {
        this.f$0 = pinnedTaskListenerImpl;
        this.f$1 = componentName;
    }

    public final void run() {
        this.f$0.lambda$onActivityHidden$2(this.f$1);
    }
}

package com.android.wm.shell;

import android.content.ComponentName;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda15 implements Runnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ ComponentName f$3;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda15(TaskView taskView, boolean z, int i, ComponentName componentName) {
        this.f$0 = taskView;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = componentName;
    }

    public final void run() {
        this.f$0.lambda$prepareOpenAnimation$15(this.f$1, this.f$2, this.f$3);
    }
}

package com.android.wm.shell;

import android.content.ComponentName;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ ComponentName f$2;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda4(TaskView taskView, int i, ComponentName componentName) {
        this.f$0 = taskView;
        this.f$1 = i;
        this.f$2 = componentName;
    }

    public final void run() {
        this.f$0.lambda$onTaskAppeared$8(this.f$1, this.f$2);
    }
}

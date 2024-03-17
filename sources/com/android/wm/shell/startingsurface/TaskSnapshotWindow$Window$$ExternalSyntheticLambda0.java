package com.android.wm.shell.startingsurface;

import android.util.MergedConfiguration;
import com.android.wm.shell.startingsurface.TaskSnapshotWindow;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TaskSnapshotWindow$Window$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ MergedConfiguration f$0;
    public final /* synthetic */ TaskSnapshotWindow f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ TaskSnapshotWindow$Window$$ExternalSyntheticLambda0(MergedConfiguration mergedConfiguration, TaskSnapshotWindow taskSnapshotWindow, boolean z) {
        this.f$0 = mergedConfiguration;
        this.f$1 = taskSnapshotWindow;
        this.f$2 = z;
    }

    public final void run() {
        TaskSnapshotWindow.Window.lambda$resized$0(this.f$0, this.f$1, this.f$2);
    }
}

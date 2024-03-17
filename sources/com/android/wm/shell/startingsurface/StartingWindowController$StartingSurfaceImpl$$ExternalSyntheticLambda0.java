package com.android.wm.shell.startingsurface;

import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.startingsurface.StartingWindowController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StartingWindowController$StartingSurfaceImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StartingWindowController.StartingSurfaceImpl f$0;
    public final /* synthetic */ StartingSurface.SysuiProxy f$1;

    public /* synthetic */ StartingWindowController$StartingSurfaceImpl$$ExternalSyntheticLambda0(StartingWindowController.StartingSurfaceImpl startingSurfaceImpl, StartingSurface.SysuiProxy sysuiProxy) {
        this.f$0 = startingSurfaceImpl;
        this.f$1 = sysuiProxy;
    }

    public final void run() {
        this.f$0.lambda$setSysuiProxy$0(this.f$1);
    }
}

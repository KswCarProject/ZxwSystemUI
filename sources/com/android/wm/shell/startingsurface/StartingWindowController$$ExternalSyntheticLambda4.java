package com.android.wm.shell.startingsurface;

import android.window.StartingWindowRemovalInfo;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StartingWindowController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ StartingWindowController f$0;
    public final /* synthetic */ StartingWindowRemovalInfo f$1;

    public /* synthetic */ StartingWindowController$$ExternalSyntheticLambda4(StartingWindowController startingWindowController, StartingWindowRemovalInfo startingWindowRemovalInfo) {
        this.f$0 = startingWindowController;
        this.f$1 = startingWindowRemovalInfo;
    }

    public final void run() {
        this.f$0.lambda$removeStartingWindow$5(this.f$1);
    }
}

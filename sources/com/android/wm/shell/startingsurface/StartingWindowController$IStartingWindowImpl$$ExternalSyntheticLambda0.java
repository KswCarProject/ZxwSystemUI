package com.android.wm.shell.startingsurface;

import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ StartingWindowController.IStartingWindowImpl f$0;
    public final /* synthetic */ IStartingWindowListener f$1;

    public /* synthetic */ StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0(StartingWindowController.IStartingWindowImpl iStartingWindowImpl, IStartingWindowListener iStartingWindowListener) {
        this.f$0 = iStartingWindowImpl;
        this.f$1 = iStartingWindowListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$setStartingWindowListener$4(this.f$1, (StartingWindowController) obj);
    }
}

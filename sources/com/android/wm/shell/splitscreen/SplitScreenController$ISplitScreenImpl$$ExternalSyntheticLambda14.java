package com.android.wm.shell.splitscreen;

import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda14 implements Consumer {
    public final /* synthetic */ SplitScreenController.ISplitScreenImpl f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda14(SplitScreenController.ISplitScreenImpl iSplitScreenImpl) {
        this.f$0 = iSplitScreenImpl;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$unregisterSplitScreenListener$3((SplitScreenController) obj);
    }
}

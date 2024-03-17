package com.android.wm.shell.splitscreen;

import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ SplitScreenController.ISplitScreenImpl f$0;
    public final /* synthetic */ ISplitScreenListener f$1;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(SplitScreenController.ISplitScreenImpl iSplitScreenImpl, ISplitScreenListener iSplitScreenListener) {
        this.f$0 = iSplitScreenImpl;
        this.f$1 = iSplitScreenListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$registerSplitScreenListener$2(this.f$1, (SplitScreenController) obj);
    }
}

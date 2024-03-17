package com.android.wm.shell.splitscreen;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda13 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda13(boolean z) {
        this.f$0 = z;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).exitSplitScreenOnHide(this.f$0);
    }
}

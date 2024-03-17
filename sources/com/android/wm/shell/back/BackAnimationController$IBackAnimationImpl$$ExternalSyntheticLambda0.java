package com.android.wm.shell.back;

import android.window.IOnBackInvokedCallback;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ IOnBackInvokedCallback f$0;

    public /* synthetic */ BackAnimationController$IBackAnimationImpl$$ExternalSyntheticLambda0(IOnBackInvokedCallback iOnBackInvokedCallback) {
        this.f$0 = iOnBackInvokedCallback;
    }

    public final void accept(Object obj) {
        ((BackAnimationController) obj).setBackToLauncherCallback(this.f$0);
    }
}

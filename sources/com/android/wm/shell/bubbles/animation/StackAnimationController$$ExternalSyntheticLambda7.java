package com.android.wm.shell.bubbles.animation;

import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StackAnimationController$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ StackAnimationController f$0;
    public final /* synthetic */ Runnable f$1;
    public final /* synthetic */ View f$2;
    public final /* synthetic */ Runnable f$3;

    public /* synthetic */ StackAnimationController$$ExternalSyntheticLambda7(StackAnimationController stackAnimationController, Runnable runnable, View view, Runnable runnable2) {
        this.f$0 = stackAnimationController;
        this.f$1 = runnable;
        this.f$2 = view;
        this.f$3 = runnable2;
    }

    public final void run() {
        this.f$0.lambda$animateToFrontThenUpdateIcons$4(this.f$1, this.f$2, this.f$3);
    }
}

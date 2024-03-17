package com.android.systemui.animation;

/* compiled from: DialogLaunchAnimator.kt */
public /* synthetic */ class AnimatedDialog$start$3 implements Runnable {
    public final /* synthetic */ AnimatedDialog $tmp0;

    public AnimatedDialog$start$3(AnimatedDialog animatedDialog) {
        this.$tmp0 = animatedDialog;
    }

    public final void run() {
        this.$tmp0.onDialogDismissed();
    }
}

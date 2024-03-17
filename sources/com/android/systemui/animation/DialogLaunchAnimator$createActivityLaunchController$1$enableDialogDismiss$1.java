package com.android.systemui.animation;

/* compiled from: DialogLaunchAnimator.kt */
public /* synthetic */ class DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1 implements Runnable {
    public final /* synthetic */ AnimatedDialog $tmp0;

    public DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1(AnimatedDialog animatedDialog) {
        this.$tmp0 = animatedDialog;
    }

    public final void run() {
        this.$tmp0.onDialogDismissed();
    }
}

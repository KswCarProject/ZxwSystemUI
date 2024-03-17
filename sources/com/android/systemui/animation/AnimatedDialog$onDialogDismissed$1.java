package com.android.systemui.animation;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$onDialogDismissed$1 implements Runnable {
    public final /* synthetic */ AnimatedDialog this$0;

    public AnimatedDialog$onDialogDismissed$1(AnimatedDialog animatedDialog) {
        this.this$0 = animatedDialog;
    }

    public final void run() {
        this.this$0.onDialogDismissed();
    }
}

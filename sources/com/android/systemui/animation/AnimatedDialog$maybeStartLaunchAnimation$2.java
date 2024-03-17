package com.android.systemui.animation;

import android.view.ViewGroup;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$maybeStartLaunchAnimation$2 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$maybeStartLaunchAnimation$2(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    public final void invoke() {
        this.this$0.getTouchSurface().setTag(R$id.tag_launch_animation_running, (Object) null);
        this.this$0.getTouchSurface().setVisibility(4);
        this.this$0.isLaunching = false;
        if (this.this$0.dismissRequested) {
            this.this$0.getDialog().dismiss();
        }
        if (this.this$0.backgroundLayoutListener != null) {
            ViewGroup dialogContentWithBackground = this.this$0.getDialogContentWithBackground();
            Intrinsics.checkNotNull(dialogContentWithBackground);
            dialogContentWithBackground.addOnLayoutChangeListener(this.this$0.backgroundLayoutListener);
        }
    }
}

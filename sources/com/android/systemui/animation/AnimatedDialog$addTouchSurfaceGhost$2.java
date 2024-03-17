package com.android.systemui.animation;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$addTouchSurfaceGhost$2 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$addTouchSurfaceGhost$2(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    public final void invoke() {
        this.this$0.isTouchSurfaceGhostDrawn = true;
        this.this$0.maybeStartLaunchAnimation();
    }
}

package com.android.systemui.animation;

import android.view.GhostView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$maybeStartLaunchAnimation$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$maybeStartLaunchAnimation$1(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    public final void invoke() {
        GhostView.removeGhost(this.this$0.getTouchSurface());
    }
}

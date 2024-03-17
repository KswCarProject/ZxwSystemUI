package com.android.systemui.animation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$onDialogDismissed$2 extends Lambda implements Function1<Boolean, Unit> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$onDialogDismissed$2(AnimatedDialog animatedDialog) {
        super(1);
        this.this$0 = animatedDialog;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Boolean) obj).booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(boolean z) {
        if (z) {
            this.this$0.getDialog().hide();
        }
        this.this$0.getDialog().setDismissOverride((Runnable) null);
        this.this$0.getDialog().dismiss();
    }
}

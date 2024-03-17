package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog$decorView$2 extends Lambda implements Function0<ViewGroup> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$decorView$2(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    @NotNull
    public final ViewGroup invoke() {
        Window window = this.this$0.getDialog().getWindow();
        Intrinsics.checkNotNull(window);
        View decorView = window.getDecorView();
        if (decorView != null) {
            return (ViewGroup) decorView;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }
}

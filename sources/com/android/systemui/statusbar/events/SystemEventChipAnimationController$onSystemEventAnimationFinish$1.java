package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.widget.FrameLayout;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController$onSystemEventAnimationFinish$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ SystemEventChipAnimationController this$0;

    public SystemEventChipAnimationController$onSystemEventAnimationFinish$1(SystemEventChipAnimationController systemEventChipAnimationController) {
        this.this$0 = systemEventChipAnimationController;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        FrameLayout access$getAnimationWindowView$p = this.this$0.animationWindowView;
        if (access$getAnimationWindowView$p == null) {
            access$getAnimationWindowView$p = null;
        }
        BackgroundAnimatableView access$getCurrentAnimatedView$p = this.this$0.currentAnimatedView;
        Intrinsics.checkNotNull(access$getCurrentAnimatedView$p);
        access$getAnimationWindowView$p.removeView(access$getCurrentAnimatedView$p.getView());
    }
}

package com.android.keyguard;

import com.android.systemui.R$id;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: KeyguardUnfoldTransition.kt */
public final class KeyguardUnfoldTransition$translateAnimator$2 extends Lambda implements Function0<UnfoldConstantTranslateAnimator> {
    public final /* synthetic */ NaturalRotationUnfoldProgressProvider $unfoldProgressProvider;
    public final /* synthetic */ KeyguardUnfoldTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public KeyguardUnfoldTransition$translateAnimator$2(KeyguardUnfoldTransition keyguardUnfoldTransition, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        super(0);
        this.this$0 = keyguardUnfoldTransition;
        this.$unfoldProgressProvider = naturalRotationUnfoldProgressProvider;
    }

    @NotNull
    public final UnfoldConstantTranslateAnimator invoke() {
        int i = R$id.keyguard_status_area;
        UnfoldConstantTranslateAnimator.Direction direction = UnfoldConstantTranslateAnimator.Direction.LEFT;
        int i2 = R$id.notification_stack_scroller;
        UnfoldConstantTranslateAnimator.Direction direction2 = UnfoldConstantTranslateAnimator.Direction.RIGHT;
        return new UnfoldConstantTranslateAnimator(SetsKt__SetsKt.setOf(new UnfoldConstantTranslateAnimator.ViewIdToTranslate(i, direction, this.this$0.filterNever), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.controls_button, direction, this.this$0.filterNever), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.lockscreen_clock_view_large, direction, this.this$0.filterSplitShadeOnly), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.lockscreen_clock_view, direction, this.this$0.filterNever), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(i2, direction2, this.this$0.filterSplitShadeOnly), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(R$id.wallet_button, direction2, this.this$0.filterNever)), this.$unfoldProgressProvider);
    }
}

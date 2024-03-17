package com.android.systemui.animation;

import android.animation.ValueAnimator;
import android.graphics.drawable.GradientDrawable;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.animation.LaunchAnimator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$FloatRef;
import kotlin.jvm.internal.Ref$IntRef;
import kotlin.math.MathKt__MathJVMKt;

/* compiled from: LaunchAnimator.kt */
public final class LaunchAnimator$startAnimation$2 implements ValueAnimator.AnimatorUpdateListener {
    public final /* synthetic */ Ref$BooleanRef $cancelled;
    public final /* synthetic */ LaunchAnimator.Controller $controller;
    public final /* synthetic */ boolean $drawHole;
    public final /* synthetic */ Ref$IntRef $endBottom;
    public final /* synthetic */ float $endBottomCornerRadius;
    public final /* synthetic */ Ref$FloatRef $endCenterX;
    public final /* synthetic */ Ref$IntRef $endLeft;
    public final /* synthetic */ Ref$IntRef $endRight;
    public final /* synthetic */ LaunchAnimator.State $endState;
    public final /* synthetic */ Ref$IntRef $endTop;
    public final /* synthetic */ float $endTopCornerRadius;
    public final /* synthetic */ Ref$IntRef $endWidth;
    public final /* synthetic */ ViewGroup $launchContainer;
    public final /* synthetic */ ViewGroupOverlay $launchContainerOverlay;
    public final /* synthetic */ boolean $moveBackgroundLayerWhenAppIsVisible;
    public final /* synthetic */ Ref$BooleanRef $movedBackgroundLayer;
    public final /* synthetic */ View $openingWindowSyncView;
    public final /* synthetic */ ViewOverlay $openingWindowSyncViewOverlay;
    public final /* synthetic */ int $startBottom;
    public final /* synthetic */ float $startBottomCornerRadius;
    public final /* synthetic */ float $startCenterX;
    public final /* synthetic */ int $startTop;
    public final /* synthetic */ float $startTopCornerRadius;
    public final /* synthetic */ int $startWidth;
    public final /* synthetic */ LaunchAnimator.State $state;
    public final /* synthetic */ GradientDrawable $windowBackgroundLayer;
    public final /* synthetic */ LaunchAnimator this$0;

    public LaunchAnimator$startAnimation$2(Ref$BooleanRef ref$BooleanRef, LaunchAnimator launchAnimator, float f, Ref$FloatRef ref$FloatRef, int i, Ref$IntRef ref$IntRef, LaunchAnimator.State state, int i2, Ref$IntRef ref$IntRef2, int i3, Ref$IntRef ref$IntRef3, float f2, float f3, float f4, float f5, boolean z, Ref$BooleanRef ref$BooleanRef2, ViewGroupOverlay viewGroupOverlay, GradientDrawable gradientDrawable, ViewOverlay viewOverlay, ViewGroup viewGroup, View view, LaunchAnimator.Controller controller, boolean z2, LaunchAnimator.State state2, Ref$IntRef ref$IntRef4, Ref$IntRef ref$IntRef5) {
        this.$cancelled = ref$BooleanRef;
        this.this$0 = launchAnimator;
        this.$startCenterX = f;
        this.$endCenterX = ref$FloatRef;
        this.$startWidth = i;
        this.$endWidth = ref$IntRef;
        this.$state = state;
        this.$startTop = i2;
        this.$endTop = ref$IntRef2;
        this.$startBottom = i3;
        this.$endBottom = ref$IntRef3;
        this.$startTopCornerRadius = f2;
        this.$endTopCornerRadius = f3;
        this.$startBottomCornerRadius = f4;
        this.$endBottomCornerRadius = f5;
        this.$moveBackgroundLayerWhenAppIsVisible = z;
        this.$movedBackgroundLayer = ref$BooleanRef2;
        this.$launchContainerOverlay = viewGroupOverlay;
        this.$windowBackgroundLayer = gradientDrawable;
        this.$openingWindowSyncViewOverlay = viewOverlay;
        this.$launchContainer = viewGroup;
        this.$openingWindowSyncView = view;
        this.$controller = controller;
        this.$drawHole = z2;
        this.$endState = state2;
        this.$endLeft = ref$IntRef4;
        this.$endRight = ref$IntRef5;
    }

    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
        View view;
        if (!this.$cancelled.element) {
            LaunchAnimator.startAnimation$maybeUpdateEndState(this.$endTop, this.$endState, this.$endBottom, this.$endLeft, this.$endRight, this.$endCenterX, this.$endWidth);
            float animatedFraction = valueAnimator.getAnimatedFraction();
            float interpolation = this.this$0.interpolators.getPositionInterpolator().getInterpolation(animatedFraction);
            float lerp = MathUtils.lerp(this.$startCenterX, this.$endCenterX.element, this.this$0.interpolators.getPositionXInterpolator().getInterpolation(animatedFraction));
            float lerp2 = MathUtils.lerp(this.$startWidth, this.$endWidth.element, interpolation) / 2.0f;
            this.$state.setTop(MathKt__MathJVMKt.roundToInt(MathUtils.lerp(this.$startTop, this.$endTop.element, interpolation)));
            this.$state.setBottom(MathKt__MathJVMKt.roundToInt(MathUtils.lerp(this.$startBottom, this.$endBottom.element, interpolation)));
            this.$state.setLeft(MathKt__MathJVMKt.roundToInt(lerp - lerp2));
            this.$state.setRight(MathKt__MathJVMKt.roundToInt(lerp + lerp2));
            this.$state.setTopCornerRadius(MathUtils.lerp(this.$startTopCornerRadius, this.$endTopCornerRadius, interpolation));
            this.$state.setBottomCornerRadius(MathUtils.lerp(this.$startBottomCornerRadius, this.$endBottomCornerRadius, interpolation));
            this.$state.setVisible(LaunchAnimator.Companion.getProgress(this.this$0.timings, animatedFraction, this.this$0.timings.getContentBeforeFadeOutDelay(), this.this$0.timings.getContentBeforeFadeOutDuration()) < 1.0f);
            if (this.$moveBackgroundLayerWhenAppIsVisible && !this.$state.getVisible()) {
                Ref$BooleanRef ref$BooleanRef = this.$movedBackgroundLayer;
                if (!ref$BooleanRef.element) {
                    ref$BooleanRef.element = true;
                    this.$launchContainerOverlay.remove(this.$windowBackgroundLayer);
                    ViewOverlay viewOverlay = this.$openingWindowSyncViewOverlay;
                    Intrinsics.checkNotNull(viewOverlay);
                    viewOverlay.add(this.$windowBackgroundLayer);
                    ViewRootSync.INSTANCE.synchronizeNextDraw(this.$launchContainer, this.$openingWindowSyncView, AnonymousClass1.INSTANCE);
                }
            }
            if (this.$movedBackgroundLayer.element) {
                view = this.$openingWindowSyncView;
                Intrinsics.checkNotNull(view);
            } else {
                view = this.$controller.getLaunchContainer();
            }
            this.this$0.applyStateToWindowBackgroundLayer(this.$windowBackgroundLayer, this.$state, animatedFraction, view, this.$drawHole);
            this.$controller.onLaunchAnimationProgress(this.$state, interpolation, animatedFraction);
        }
    }
}

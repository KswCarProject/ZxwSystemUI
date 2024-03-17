package com.android.systemui.animation;

import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.GradientDrawable;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import android.view.animation.Interpolator;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$FloatRef;
import kotlin.jvm.internal.Ref$IntRef;
import kotlin.math.MathKt__MathJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LaunchAnimator.kt */
public final class LaunchAnimator {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final PorterDuffXfermode SRC_MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    @NotNull
    public final float[] cornerRadii = new float[8];
    @NotNull
    public final Interpolators interpolators;
    @NotNull
    public final int[] launchContainerLocation = new int[2];
    @NotNull
    public final Timings timings;

    /* compiled from: LaunchAnimator.kt */
    public interface Animation {
        void cancel();
    }

    /* compiled from: LaunchAnimator.kt */
    public interface Controller {
        @NotNull
        State createAnimatorState();

        @NotNull
        ViewGroup getLaunchContainer();

        @Nullable
        View getOpeningWindowSyncView() {
            return null;
        }

        void onLaunchAnimationEnd(boolean z) {
        }

        void onLaunchAnimationProgress(@NotNull State state, float f, float f2) {
        }

        void onLaunchAnimationStart(boolean z) {
        }

        void setLaunchContainer(@NotNull ViewGroup viewGroup);
    }

    public static final float getProgress(@NotNull Timings timings2, float f, long j, long j2) {
        return Companion.getProgress(timings2, f, j, j2);
    }

    public LaunchAnimator(@NotNull Timings timings2, @NotNull Interpolators interpolators2) {
        this.timings = timings2;
        this.interpolators = interpolators2;
    }

    /* compiled from: LaunchAnimator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final float getProgress(@NotNull Timings timings, float f, long j, long j2) {
            return MathUtils.constrain(((f * ((float) timings.getTotalDuration())) - ((float) j)) / ((float) j2), 0.0f, 1.0f);
        }
    }

    /* compiled from: LaunchAnimator.kt */
    public static class State {
        public int bottom;
        public float bottomCornerRadius;
        public int left;
        public int right;
        public final int startTop;
        public int top;
        public float topCornerRadius;
        public boolean visible;

        public State(int i, int i2, int i3, int i4, float f, float f2) {
            this.top = i;
            this.bottom = i2;
            this.left = i3;
            this.right = i4;
            this.topCornerRadius = f;
            this.bottomCornerRadius = f2;
            this.startTop = i;
            this.visible = true;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ State(int i, int i2, int i3, int i4, float f, float f2, int i5, DefaultConstructorMarker defaultConstructorMarker) {
            this((i5 & 1) != 0 ? 0 : i, (i5 & 2) != 0 ? 0 : i2, (i5 & 4) != 0 ? 0 : i3, (i5 & 8) != 0 ? 0 : i4, (i5 & 16) != 0 ? 0.0f : f, (i5 & 32) != 0 ? 0.0f : f2);
        }

        public final int getTop() {
            return this.top;
        }

        public final void setTop(int i) {
            this.top = i;
        }

        public final int getBottom() {
            return this.bottom;
        }

        public final void setBottom(int i) {
            this.bottom = i;
        }

        public final int getLeft() {
            return this.left;
        }

        public final void setLeft(int i) {
            this.left = i;
        }

        public final int getRight() {
            return this.right;
        }

        public final void setRight(int i) {
            this.right = i;
        }

        public final float getTopCornerRadius() {
            return this.topCornerRadius;
        }

        public final void setTopCornerRadius(float f) {
            this.topCornerRadius = f;
        }

        public final float getBottomCornerRadius() {
            return this.bottomCornerRadius;
        }

        public final void setBottomCornerRadius(float f) {
            this.bottomCornerRadius = f;
        }

        public final int getWidth() {
            return this.right - this.left;
        }

        public final int getHeight() {
            return this.bottom - this.top;
        }

        public final float getCenterX() {
            return ((float) this.left) + (((float) getWidth()) / 2.0f);
        }

        public final float getCenterY() {
            return ((float) this.top) + (((float) getHeight()) / 2.0f);
        }

        public final boolean getVisible() {
            return this.visible;
        }

        public final void setVisible(boolean z) {
            this.visible = z;
        }
    }

    /* compiled from: LaunchAnimator.kt */
    public static final class Timings {
        public final long contentAfterFadeInDelay;
        public final long contentAfterFadeInDuration;
        public final long contentBeforeFadeOutDelay;
        public final long contentBeforeFadeOutDuration;
        public final long totalDuration;

        public static /* synthetic */ Timings copy$default(Timings timings, long j, long j2, long j3, long j4, long j5, int i, Object obj) {
            Timings timings2 = timings;
            return timings.copy((i & 1) != 0 ? timings2.totalDuration : j, (i & 2) != 0 ? timings2.contentBeforeFadeOutDelay : j2, (i & 4) != 0 ? timings2.contentBeforeFadeOutDuration : j3, (i & 8) != 0 ? timings2.contentAfterFadeInDelay : j4, (i & 16) != 0 ? timings2.contentAfterFadeInDuration : j5);
        }

        @NotNull
        public final Timings copy(long j, long j2, long j3, long j4, long j5) {
            return new Timings(j, j2, j3, j4, j5);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Timings)) {
                return false;
            }
            Timings timings = (Timings) obj;
            return this.totalDuration == timings.totalDuration && this.contentBeforeFadeOutDelay == timings.contentBeforeFadeOutDelay && this.contentBeforeFadeOutDuration == timings.contentBeforeFadeOutDuration && this.contentAfterFadeInDelay == timings.contentAfterFadeInDelay && this.contentAfterFadeInDuration == timings.contentAfterFadeInDuration;
        }

        public int hashCode() {
            return (((((((Long.hashCode(this.totalDuration) * 31) + Long.hashCode(this.contentBeforeFadeOutDelay)) * 31) + Long.hashCode(this.contentBeforeFadeOutDuration)) * 31) + Long.hashCode(this.contentAfterFadeInDelay)) * 31) + Long.hashCode(this.contentAfterFadeInDuration);
        }

        @NotNull
        public String toString() {
            return "Timings(totalDuration=" + this.totalDuration + ", contentBeforeFadeOutDelay=" + this.contentBeforeFadeOutDelay + ", contentBeforeFadeOutDuration=" + this.contentBeforeFadeOutDuration + ", contentAfterFadeInDelay=" + this.contentAfterFadeInDelay + ", contentAfterFadeInDuration=" + this.contentAfterFadeInDuration + ')';
        }

        public Timings(long j, long j2, long j3, long j4, long j5) {
            this.totalDuration = j;
            this.contentBeforeFadeOutDelay = j2;
            this.contentBeforeFadeOutDuration = j3;
            this.contentAfterFadeInDelay = j4;
            this.contentAfterFadeInDuration = j5;
        }

        public final long getTotalDuration() {
            return this.totalDuration;
        }

        public final long getContentBeforeFadeOutDelay() {
            return this.contentBeforeFadeOutDelay;
        }

        public final long getContentBeforeFadeOutDuration() {
            return this.contentBeforeFadeOutDuration;
        }

        public final long getContentAfterFadeInDelay() {
            return this.contentAfterFadeInDelay;
        }

        public final long getContentAfterFadeInDuration() {
            return this.contentAfterFadeInDuration;
        }
    }

    /* compiled from: LaunchAnimator.kt */
    public static final class Interpolators {
        @NotNull
        public final Interpolator contentAfterFadeInInterpolator;
        @NotNull
        public final Interpolator contentBeforeFadeOutInterpolator;
        @NotNull
        public final Interpolator positionInterpolator;
        @NotNull
        public final Interpolator positionXInterpolator;

        public static /* synthetic */ Interpolators copy$default(Interpolators interpolators, Interpolator interpolator, Interpolator interpolator2, Interpolator interpolator3, Interpolator interpolator4, int i, Object obj) {
            if ((i & 1) != 0) {
                interpolator = interpolators.positionInterpolator;
            }
            if ((i & 2) != 0) {
                interpolator2 = interpolators.positionXInterpolator;
            }
            if ((i & 4) != 0) {
                interpolator3 = interpolators.contentBeforeFadeOutInterpolator;
            }
            if ((i & 8) != 0) {
                interpolator4 = interpolators.contentAfterFadeInInterpolator;
            }
            return interpolators.copy(interpolator, interpolator2, interpolator3, interpolator4);
        }

        @NotNull
        public final Interpolators copy(@NotNull Interpolator interpolator, @NotNull Interpolator interpolator2, @NotNull Interpolator interpolator3, @NotNull Interpolator interpolator4) {
            return new Interpolators(interpolator, interpolator2, interpolator3, interpolator4);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Interpolators)) {
                return false;
            }
            Interpolators interpolators = (Interpolators) obj;
            return Intrinsics.areEqual((Object) this.positionInterpolator, (Object) interpolators.positionInterpolator) && Intrinsics.areEqual((Object) this.positionXInterpolator, (Object) interpolators.positionXInterpolator) && Intrinsics.areEqual((Object) this.contentBeforeFadeOutInterpolator, (Object) interpolators.contentBeforeFadeOutInterpolator) && Intrinsics.areEqual((Object) this.contentAfterFadeInInterpolator, (Object) interpolators.contentAfterFadeInInterpolator);
        }

        public int hashCode() {
            return (((((this.positionInterpolator.hashCode() * 31) + this.positionXInterpolator.hashCode()) * 31) + this.contentBeforeFadeOutInterpolator.hashCode()) * 31) + this.contentAfterFadeInInterpolator.hashCode();
        }

        @NotNull
        public String toString() {
            return "Interpolators(positionInterpolator=" + this.positionInterpolator + ", positionXInterpolator=" + this.positionXInterpolator + ", contentBeforeFadeOutInterpolator=" + this.contentBeforeFadeOutInterpolator + ", contentAfterFadeInInterpolator=" + this.contentAfterFadeInInterpolator + ')';
        }

        public Interpolators(@NotNull Interpolator interpolator, @NotNull Interpolator interpolator2, @NotNull Interpolator interpolator3, @NotNull Interpolator interpolator4) {
            this.positionInterpolator = interpolator;
            this.positionXInterpolator = interpolator2;
            this.contentBeforeFadeOutInterpolator = interpolator3;
            this.contentAfterFadeInInterpolator = interpolator4;
        }

        @NotNull
        public final Interpolator getPositionInterpolator() {
            return this.positionInterpolator;
        }

        @NotNull
        public final Interpolator getPositionXInterpolator() {
            return this.positionXInterpolator;
        }

        @NotNull
        public final Interpolator getContentBeforeFadeOutInterpolator() {
            return this.contentBeforeFadeOutInterpolator;
        }

        @NotNull
        public final Interpolator getContentAfterFadeInInterpolator() {
            return this.contentAfterFadeInInterpolator;
        }
    }

    public static /* synthetic */ Animation startAnimation$default(LaunchAnimator launchAnimator, Controller controller, State state, int i, boolean z, int i2, Object obj) {
        if ((i2 & 8) != 0) {
            z = false;
        }
        return launchAnimator.startAnimation(controller, state, i, z);
    }

    @NotNull
    public final Animation startAnimation(@NotNull Controller controller, @NotNull State state, int i, boolean z) {
        ViewOverlay viewOverlay;
        State createAnimatorState = controller.createAnimatorState();
        int top = createAnimatorState.getTop();
        int bottom = createAnimatorState.getBottom();
        int left = createAnimatorState.getLeft();
        int right = createAnimatorState.getRight();
        float f = ((float) (left + right)) / 2.0f;
        int i2 = right - left;
        float topCornerRadius = createAnimatorState.getTopCornerRadius();
        float bottomCornerRadius = createAnimatorState.getBottomCornerRadius();
        Ref$IntRef ref$IntRef = new Ref$IntRef();
        ref$IntRef.element = state.getTop();
        Ref$IntRef ref$IntRef2 = new Ref$IntRef();
        ref$IntRef2.element = state.getBottom();
        Ref$IntRef ref$IntRef3 = new Ref$IntRef();
        ref$IntRef3.element = state.getLeft();
        Ref$IntRef ref$IntRef4 = new Ref$IntRef();
        ref$IntRef4.element = state.getRight();
        Ref$FloatRef ref$FloatRef = new Ref$FloatRef();
        ref$FloatRef.element = ((float) (ref$IntRef3.element + ref$IntRef4.element)) / 2.0f;
        Ref$IntRef ref$IntRef5 = new Ref$IntRef();
        ref$IntRef5.element = ref$IntRef4.element - ref$IntRef3.element;
        float topCornerRadius2 = state.getTopCornerRadius();
        float bottomCornerRadius2 = state.getBottomCornerRadius();
        ViewGroup launchContainer = controller.getLaunchContainer();
        boolean isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib = isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib(launchContainer, state);
        Ref$IntRef ref$IntRef6 = ref$IntRef3;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(i);
        gradientDrawable.setAlpha(0);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        Ref$IntRef ref$IntRef7 = ref$IntRef5;
        Ref$IntRef ref$IntRef8 = ref$IntRef4;
        float f2 = bottomCornerRadius;
        ofFloat.setDuration(this.timings.getTotalDuration());
        ofFloat.setInterpolator(Interpolators.LINEAR);
        View openingWindowSyncView = controller.getOpeningWindowSyncView();
        if (openingWindowSyncView == null) {
            viewOverlay = null;
        } else {
            viewOverlay = openingWindowSyncView.getOverlay();
        }
        ViewOverlay viewOverlay2 = viewOverlay;
        boolean z2 = openingWindowSyncView != null && !Intrinsics.areEqual((Object) openingWindowSyncView.getViewRootImpl(), (Object) controller.getLaunchContainer().getViewRootImpl());
        ViewGroupOverlay overlay = launchContainer.getOverlay();
        ValueAnimator valueAnimator = ofFloat;
        Ref$BooleanRef ref$BooleanRef = r14;
        Ref$BooleanRef ref$BooleanRef2 = new Ref$BooleanRef();
        Ref$BooleanRef ref$BooleanRef3 = r4;
        Ref$BooleanRef ref$BooleanRef4 = new Ref$BooleanRef();
        valueAnimator.addListener(new LaunchAnimator$startAnimation$1(controller, isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib, overlay, gradientDrawable, z2, viewOverlay2));
        LaunchAnimator$startAnimation$2 launchAnimator$startAnimation$2 = r0;
        Ref$BooleanRef ref$BooleanRef5 = ref$BooleanRef2;
        LaunchAnimator$startAnimation$2 launchAnimator$startAnimation$22 = new LaunchAnimator$startAnimation$2(ref$BooleanRef, this, f, ref$FloatRef, i2, ref$IntRef7, createAnimatorState, top, ref$IntRef, bottom, ref$IntRef2, topCornerRadius, topCornerRadius2, f2, bottomCornerRadius2, z2, ref$BooleanRef3, overlay, gradientDrawable, viewOverlay2, launchContainer, openingWindowSyncView, controller, z, state, ref$IntRef6, ref$IntRef8);
        ValueAnimator valueAnimator2 = valueAnimator;
        valueAnimator2.addUpdateListener(launchAnimator$startAnimation$2);
        valueAnimator2.start();
        return new LaunchAnimator$startAnimation$3(ref$BooleanRef5, valueAnimator2);
    }

    public static final void startAnimation$maybeUpdateEndState(Ref$IntRef ref$IntRef, State state, Ref$IntRef ref$IntRef2, Ref$IntRef ref$IntRef3, Ref$IntRef ref$IntRef4, Ref$FloatRef ref$FloatRef, Ref$IntRef ref$IntRef5) {
        if (ref$IntRef.element != state.getTop() || ref$IntRef2.element != state.getBottom() || ref$IntRef3.element != state.getLeft() || ref$IntRef4.element != state.getRight()) {
            ref$IntRef.element = state.getTop();
            ref$IntRef2.element = state.getBottom();
            ref$IntRef3.element = state.getLeft();
            int right = state.getRight();
            ref$IntRef4.element = right;
            int i = ref$IntRef3.element;
            ref$FloatRef.element = ((float) (i + right)) / 2.0f;
            ref$IntRef5.element = right - i;
        }
    }

    public final boolean isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib(@NotNull View view, @NotNull State state) {
        view.getLocationOnScreen(this.launchContainerLocation);
        if (state.getTop() > this.launchContainerLocation[1] || state.getBottom() < this.launchContainerLocation[1] + view.getHeight() || state.getLeft() > this.launchContainerLocation[0] || state.getRight() < this.launchContainerLocation[0] + view.getWidth()) {
            return false;
        }
        return true;
    }

    public final void applyStateToWindowBackgroundLayer(GradientDrawable gradientDrawable, State state, float f, View view, boolean z) {
        GradientDrawable gradientDrawable2 = gradientDrawable;
        view.getLocationOnScreen(this.launchContainerLocation);
        gradientDrawable.setBounds(state.getLeft() - this.launchContainerLocation[0], state.getTop() - this.launchContainerLocation[1], state.getRight() - this.launchContainerLocation[0], state.getBottom() - this.launchContainerLocation[1]);
        this.cornerRadii[0] = state.getTopCornerRadius();
        this.cornerRadii[1] = state.getTopCornerRadius();
        this.cornerRadii[2] = state.getTopCornerRadius();
        this.cornerRadii[3] = state.getTopCornerRadius();
        this.cornerRadii[4] = state.getBottomCornerRadius();
        this.cornerRadii[5] = state.getBottomCornerRadius();
        this.cornerRadii[6] = state.getBottomCornerRadius();
        this.cornerRadii[7] = state.getBottomCornerRadius();
        gradientDrawable.setCornerRadii(this.cornerRadii);
        Companion companion = Companion;
        Timings timings2 = this.timings;
        float progress = companion.getProgress(timings2, f, timings2.getContentBeforeFadeOutDelay(), this.timings.getContentBeforeFadeOutDuration());
        if (progress < 1.0f) {
            gradientDrawable.setAlpha(MathKt__MathJVMKt.roundToInt(this.interpolators.getContentBeforeFadeOutInterpolator().getInterpolation(progress) * ((float) 255)));
            return;
        }
        Timings timings3 = this.timings;
        gradientDrawable.setAlpha(MathKt__MathJVMKt.roundToInt((((float) 1) - this.interpolators.getContentAfterFadeInInterpolator().getInterpolation(companion.getProgress(timings3, f, timings3.getContentAfterFadeInDelay(), this.timings.getContentAfterFadeInDuration()))) * ((float) 255)));
        if (z) {
            gradientDrawable.setXfermode(SRC_MODE);
        }
    }
}

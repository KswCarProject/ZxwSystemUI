package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.animation.AnimationUtil;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SystemEventChipAnimationController.kt */
public final class SystemEventChipAnimationController implements SystemStatusAnimationCallback {
    @NotNull
    public Rect animRect;
    public int animationDirection = 1;
    public FrameLayout animationWindowView;
    public int chipLeft;
    public int chipMinWidth;
    public int chipRight;
    public int chipWidth;
    @NotNull
    public final StatusBarContentInsetsProvider contentInsetsProvider;
    @NotNull
    public final Context context;
    @Nullable
    public BackgroundAnimatableView currentAnimatedView;
    public int dotSize;
    public boolean initialized;
    @NotNull
    public final StatusBarWindowController statusBarWindowController;
    public ContextThemeWrapper themedContext;

    public SystemEventChipAnimationController(@NotNull Context context2, @NotNull StatusBarWindowController statusBarWindowController2, @NotNull StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        this.context = context2;
        this.statusBarWindowController = statusBarWindowController2;
        this.contentInsetsProvider = statusBarContentInsetsProvider;
        this.chipMinWidth = context2.getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_min_animation_width);
        this.dotSize = context2.getResources().getDimensionPixelSize(R$dimen.ongoing_appops_dot_diameter);
        this.animRect = new Rect();
    }

    public final void prepareChipAnimation(@NotNull Function1<? super Context, ? extends BackgroundAnimatableView> function1) {
        Integer num;
        if (!this.initialized) {
            init();
        }
        FrameLayout frameLayout = this.animationWindowView;
        FrameLayout frameLayout2 = null;
        if (frameLayout == null) {
            frameLayout = null;
        }
        this.animationDirection = frameLayout.isLayoutRtl() ? 2 : 1;
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.contentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        ContextThemeWrapper contextThemeWrapper = this.themedContext;
        if (contextThemeWrapper == null) {
            contextThemeWrapper = null;
        }
        BackgroundAnimatableView backgroundAnimatableView = (BackgroundAnimatableView) function1.invoke(contextThemeWrapper);
        FrameLayout frameLayout3 = this.animationWindowView;
        if (frameLayout3 == null) {
            frameLayout3 = null;
        }
        View view = backgroundAnimatableView.getView();
        FrameLayout frameLayout4 = this.animationWindowView;
        if (frameLayout4 == null) {
            frameLayout4 = null;
        }
        if (frameLayout4.isLayoutRtl()) {
            num = (Integer) statusBarContentInsetsForCurrentRotation.first;
        } else {
            num = (Integer) statusBarContentInsetsForCurrentRotation.second;
        }
        frameLayout3.addView(view, layoutParamsDefault(num.intValue()));
        backgroundAnimatableView.getView().setAlpha(0.0f);
        View view2 = backgroundAnimatableView.getView();
        FrameLayout frameLayout5 = this.animationWindowView;
        if (frameLayout5 == null) {
            frameLayout5 = null;
        }
        ViewParent parent = frameLayout5.getParent();
        if (parent != null) {
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) parent).getWidth(), Integer.MIN_VALUE);
            FrameLayout frameLayout6 = this.animationWindowView;
            if (frameLayout6 != null) {
                frameLayout2 = frameLayout6;
            }
            view2.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(frameLayout2.getHeight(), Integer.MIN_VALUE));
            this.chipWidth = backgroundAnimatableView.getChipWidth();
            this.currentAnimatedView = backgroundAnimatableView;
            Rect statusBarContentAreaForCurrentRotation = this.contentInsetsProvider.getStatusBarContentAreaForCurrentRotation();
            if (this.animationDirection == 1) {
                int i = statusBarContentAreaForCurrentRotation.right;
                this.chipRight = i;
                this.chipLeft = i - this.chipWidth;
                return;
            }
            int i2 = statusBarContentAreaForCurrentRotation.left;
            this.chipLeft = i2;
            this.chipRight = i2 + this.chipWidth;
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.View");
    }

    @NotNull
    public Animator onSystemEventAnimationBegin() {
        initializeAnimRect();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        AnimationUtil.Companion companion = AnimationUtil.Companion;
        ofFloat.setStartDelay(companion.getFrames(7));
        ofFloat.setDuration(companion.getFrames(5));
        ofFloat.setInterpolator((TimeInterpolator) null);
        ofFloat.addUpdateListener(new SystemEventChipAnimationController$onSystemEventAnimationBegin$alphaIn$1$1(this, ofFloat));
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.chipMinWidth, this.chipWidth});
        ofInt.setStartDelay(companion.getFrames(7));
        ofInt.setDuration(companion.getFrames(23));
        ofInt.setInterpolator(SystemStatusAnimationSchedulerKt.STATUS_BAR_X_MOVE_IN);
        ofInt.addUpdateListener(new SystemEventChipAnimationController$onSystemEventAnimationBegin$moveIn$1$1(this, ofInt));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofInt});
        return animatorSet;
    }

    @NotNull
    public Animator onSystemEventAnimationFinish(boolean z) {
        Animator animator;
        initializeAnimRect();
        if (z) {
            animator = createMoveOutAnimationForDot();
        } else {
            animator = createMoveOutAnimationDefault();
        }
        animator.addListener(new SystemEventChipAnimationController$onSystemEventAnimationFinish$1(this));
        return animator;
    }

    public final Animator createMoveOutAnimationForDot() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.chipWidth, this.chipMinWidth});
        AnimationUtil.Companion companion = AnimationUtil.Companion;
        ofInt.setDuration(companion.getFrames(9));
        ofInt.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_1());
        ofInt.addUpdateListener(new SystemEventChipAnimationController$createMoveOutAnimationForDot$width1$1$1(this));
        ValueAnimator ofInt2 = ValueAnimator.ofInt(new int[]{this.chipMinWidth, this.dotSize});
        ofInt2.setStartDelay(companion.getFrames(9));
        ofInt2.setDuration(companion.getFrames(20));
        ofInt2.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_WIDTH_TO_DOT_KEYFRAME_2());
        ofInt2.addUpdateListener(new SystemEventChipAnimationController$createMoveOutAnimationForDot$width2$1$1(this));
        int i = this.dotSize * 2;
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView);
        View view = backgroundAnimatableView.getView();
        int top = view.getTop() + (view.getMeasuredHeight() / 2);
        BackgroundAnimatableView backgroundAnimatableView2 = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView2);
        ValueAnimator ofInt3 = ValueAnimator.ofInt(new int[]{backgroundAnimatableView2.getView().getMeasuredHeight(), i});
        ofInt3.setStartDelay(companion.getFrames(8));
        ofInt3.setDuration(companion.getFrames(6));
        ofInt3.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_1());
        ofInt3.addUpdateListener(new SystemEventChipAnimationController$createMoveOutAnimationForDot$height1$1$1(this, top));
        ValueAnimator ofInt4 = ValueAnimator.ofInt(new int[]{i, this.dotSize});
        ofInt4.setStartDelay(companion.getFrames(14));
        ofInt4.setDuration(companion.getFrames(15));
        ofInt4.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_HEIGHT_TO_DOT_KEYFRAME_2());
        ofInt4.addUpdateListener(new SystemEventChipAnimationController$createMoveOutAnimationForDot$height2$1$1(this, top));
        ValueAnimator ofInt5 = ValueAnimator.ofInt(new int[]{0, this.dotSize});
        ofInt5.setStartDelay(companion.getFrames(3));
        ofInt5.setDuration(companion.getFrames(11));
        ofInt5.setInterpolator(SystemStatusAnimationSchedulerKt.getSTATUS_CHIP_MOVE_TO_DOT());
        ofInt5.addUpdateListener(new SystemEventChipAnimationController$createMoveOutAnimationForDot$moveOut$1$1(this, ofInt5));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofInt, ofInt2, ofInt3, ofInt4, ofInt5});
        return animatorSet;
    }

    public final Animator createMoveOutAnimationDefault() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.chipWidth, this.chipMinWidth});
        ofInt.setDuration(AnimationUtil.Companion.getFrames(23));
        ofInt.addUpdateListener(new SystemEventChipAnimationController$createMoveOutAnimationDefault$moveOut$1$1(this));
        return ofInt;
    }

    public final void init() {
        this.initialized = true;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this.context, R$style.Theme_SystemUI_QuickSettings);
        this.themedContext = contextThemeWrapper;
        FrameLayout frameLayout = null;
        View inflate = LayoutInflater.from(contextThemeWrapper).inflate(R$layout.system_event_animation_window, (ViewGroup) null);
        if (inflate != null) {
            this.animationWindowView = (FrameLayout) inflate;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
            layoutParams.gravity = 8388629;
            StatusBarWindowController statusBarWindowController2 = this.statusBarWindowController;
            FrameLayout frameLayout2 = this.animationWindowView;
            if (frameLayout2 == null) {
                frameLayout2 = null;
            }
            statusBarWindowController2.addViewToWindow(frameLayout2, layoutParams);
            FrameLayout frameLayout3 = this.animationWindowView;
            if (frameLayout3 == null) {
                frameLayout3 = null;
            }
            frameLayout3.setClipToPadding(false);
            FrameLayout frameLayout4 = this.animationWindowView;
            if (frameLayout4 != null) {
                frameLayout = frameLayout4;
            }
            frameLayout.setClipChildren(false);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout");
    }

    public final FrameLayout.LayoutParams layoutParamsDefault(int i) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 8388629;
        layoutParams.setMarginEnd(i);
        return layoutParams;
    }

    public final void initializeAnimRect() {
        Rect rect = this.animRect;
        int i = this.chipLeft;
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView);
        int top = backgroundAnimatableView.getView().getTop();
        int i2 = this.chipRight;
        BackgroundAnimatableView backgroundAnimatableView2 = this.currentAnimatedView;
        Intrinsics.checkNotNull(backgroundAnimatableView2);
        rect.set(i, top, i2, backgroundAnimatableView2.getView().getBottom());
    }

    public final void updateAnimatedViewBoundsWidth(int i) {
        if (this.animationDirection == 1) {
            Rect rect = this.animRect;
            int i2 = this.chipRight;
            rect.set(i2 - i, rect.top, i2, rect.bottom);
        } else {
            Rect rect2 = this.animRect;
            int i3 = this.chipLeft;
            rect2.set(i3, rect2.top, i + i3, rect2.bottom);
        }
        updateCurrentAnimatedView();
    }

    public final void updateAnimatedViewBoundsHeight(int i, int i2) {
        Rect rect = this.animRect;
        float f = ((float) i) / ((float) 2);
        rect.set(rect.left, i2 - MathKt__MathJVMKt.roundToInt(f), this.animRect.right, i2 + MathKt__MathJVMKt.roundToInt(f));
        updateCurrentAnimatedView();
    }

    public final void updateAnimatedBoundsX(int i) {
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        View view = backgroundAnimatableView == null ? null : backgroundAnimatableView.getView();
        if (view != null) {
            view.setTranslationX((float) i);
        }
    }

    public final void updateCurrentAnimatedView() {
        BackgroundAnimatableView backgroundAnimatableView = this.currentAnimatedView;
        if (backgroundAnimatableView != null) {
            Rect rect = this.animRect;
            backgroundAnimatableView.setBoundsForAnimation(rect.left, rect.top, rect.right, rect.bottom);
        }
    }
}

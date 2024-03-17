package com.android.systemui.animation;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.service.dreams.IDreamManager;
import android.util.Log;
import android.view.GhostView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.LaunchAnimator;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DialogLaunchAnimator.kt */
public final class AnimatedDialog {
    @Nullable
    public final AnimatedBoundsLayoutListener backgroundLayoutListener;
    @NotNull
    public final Lazy decorView$delegate = LazyKt__LazyJVMKt.lazy(new AnimatedDialog$decorView$2(this));
    @Nullable
    public View.OnLayoutChangeListener decorViewLayoutListener;
    @NotNull
    public final Dialog dialog;
    @Nullable
    public ViewGroup dialogContentWithBackground;
    public boolean dismissRequested;
    @NotNull
    public final IDreamManager dreamManager;
    public boolean exitAnimationDisabled;
    public final boolean forceDisableSynchronization;
    public boolean isDismissing;
    public boolean isLaunching = true;
    public boolean isOriginalDialogViewLaidOut;
    public boolean isTouchSurfaceGhostDrawn;
    @NotNull
    public final LaunchAnimator launchAnimator;
    @NotNull
    public final Function1<AnimatedDialog, Unit> onDialogDismissed;
    public int originalDialogBackgroundColor = -16777216;
    @Nullable
    public final AnimatedDialog parentAnimatedDialog;
    @NotNull
    public View touchSurface;

    public AnimatedDialog(@NotNull LaunchAnimator launchAnimator2, @NotNull IDreamManager iDreamManager, @NotNull View view, @NotNull Function1<? super AnimatedDialog, Unit> function1, @NotNull Dialog dialog2, boolean z, @Nullable AnimatedDialog animatedDialog, boolean z2) {
        this.launchAnimator = launchAnimator2;
        this.dreamManager = iDreamManager;
        this.touchSurface = view;
        this.onDialogDismissed = function1;
        this.dialog = dialog2;
        this.parentAnimatedDialog = animatedDialog;
        this.forceDisableSynchronization = z2;
        this.backgroundLayoutListener = z ? new AnimatedBoundsLayoutListener() : null;
    }

    @NotNull
    public final View getTouchSurface() {
        return this.touchSurface;
    }

    public final void setTouchSurface(@NotNull View view) {
        this.touchSurface = view;
    }

    @NotNull
    public final Dialog getDialog() {
        return this.dialog;
    }

    public final ViewGroup getDecorView() {
        return (ViewGroup) this.decorView$delegate.getValue();
    }

    @Nullable
    public final ViewGroup getDialogContentWithBackground() {
        return this.dialogContentWithBackground;
    }

    public final void setExitAnimationDisabled(boolean z) {
        this.exitAnimationDisabled = z;
    }

    public final void start() {
        FrameLayout frameLayout;
        ColorStateList color;
        this.dialog.create();
        Window window = this.dialog.getWindow();
        Intrinsics.checkNotNull(window);
        if (window.getAttributes().width == -1 && window.getAttributes().height == -1) {
            frameLayout = null;
            int childCount = getDecorView().getChildCount();
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                }
                int i2 = i + 1;
                ViewGroup findFirstViewGroupWithBackground = findFirstViewGroupWithBackground(getDecorView().getChildAt(i));
                if (findFirstViewGroupWithBackground != null) {
                    frameLayout = findFirstViewGroupWithBackground;
                    break;
                }
                ViewGroup viewGroup = findFirstViewGroupWithBackground;
                i = i2;
                frameLayout = viewGroup;
            }
            if (frameLayout == null) {
                throw new IllegalStateException("Unable to find ViewGroup with background");
            }
        } else {
            FrameLayout frameLayout2 = new FrameLayout(this.dialog.getContext());
            getDecorView().addView(frameLayout2, 0, new FrameLayout.LayoutParams(-1, -1));
            FrameLayout frameLayout3 = new FrameLayout(this.dialog.getContext());
            frameLayout3.setBackground(getDecorView().getBackground());
            window.setBackgroundDrawableResource(17170445);
            frameLayout2.setOnClickListener(new AnimatedDialog$start$dialogContentWithBackground$1(this));
            frameLayout3.setClickable(true);
            frameLayout2.setImportantForAccessibility(2);
            frameLayout3.setImportantForAccessibility(2);
            frameLayout2.addView(frameLayout3, new FrameLayout.LayoutParams(window.getAttributes().width, window.getAttributes().height, window.getAttributes().gravity));
            int childCount2 = getDecorView().getChildCount();
            int i3 = 1;
            while (i3 < childCount2) {
                i3++;
                View childAt = getDecorView().getChildAt(1);
                getDecorView().removeViewAt(1);
                frameLayout3.addView(childAt);
            }
            window.setLayout(-1, -1);
            this.decorViewLayoutListener = new AnimatedDialog$start$dialogContentWithBackground$2(window, frameLayout3);
            getDecorView().addOnLayoutChangeListener(this.decorViewLayoutListener);
            frameLayout = frameLayout3;
        }
        this.dialogContentWithBackground = frameLayout;
        frameLayout.setTag(R$id.tag_dialog_background, Boolean.TRUE);
        GradientDrawable findGradientDrawable = GhostedViewLaunchAnimatorController.Companion.findGradientDrawable(frameLayout.getBackground());
        int i4 = -16777216;
        if (!(findGradientDrawable == null || (color = findGradientDrawable.getColor()) == null)) {
            i4 = color.getDefaultColor();
        }
        this.originalDialogBackgroundColor = i4;
        frameLayout.setTransitionVisibility(4);
        window.getAttributes().windowAnimations = R$style.Animation_LaunchAnimation;
        window.getAttributes().layoutInDisplayCutoutMode = 3;
        window.setAttributes(window.getAttributes());
        window.setDecorFitsSystemWindows(false);
        ViewParent parent = frameLayout.getParent();
        if (parent != null) {
            ((ViewGroup) parent).setOnApplyWindowInsetsListener(AnimatedDialog$start$1.INSTANCE);
            frameLayout.addOnLayoutChangeListener(new AnimatedDialog$start$2(frameLayout, this));
            window.clearFlags(2);
            this.dialog.setDismissOverride(new AnimatedDialog$start$3(this));
            this.dialog.show();
            addTouchSurfaceGhost();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public final void addTouchSurfaceGhost() {
        if (getDecorView().getViewRootImpl() == null) {
            getDecorView().post(new AnimatedDialog$addTouchSurfaceGhost$1(this));
            return;
        }
        synchronizeNextDraw(new AnimatedDialog$addTouchSurfaceGhost$2(this));
        GhostView.addGhost(this.touchSurface, getDecorView());
        View view = this.touchSurface;
        LaunchableView launchableView = view instanceof LaunchableView ? (LaunchableView) view : null;
        if (launchableView != null) {
            launchableView.setShouldBlockVisibilityChanges(true);
        }
    }

    public final void synchronizeNextDraw(Function0<Unit> function0) {
        if (this.forceDisableSynchronization) {
            function0.invoke();
        } else {
            ViewRootSync.INSTANCE.synchronizeNextDraw(this.touchSurface, getDecorView(), function0);
        }
    }

    public final ViewGroup findFirstViewGroupWithBackground(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        if (viewGroup.getBackground() != null) {
            return viewGroup;
        }
        int i = 0;
        int childCount = viewGroup.getChildCount();
        while (i < childCount) {
            int i2 = i + 1;
            ViewGroup findFirstViewGroupWithBackground = findFirstViewGroupWithBackground(viewGroup.getChildAt(i));
            if (findFirstViewGroupWithBackground != null) {
                return findFirstViewGroupWithBackground;
            }
            i = i2;
        }
        return null;
    }

    public final void maybeStartLaunchAnimation() {
        if (this.isTouchSurfaceGhostDrawn && this.isOriginalDialogViewLaidOut) {
            this.dialog.getWindow().addFlags(2);
            startAnimation(true, new AnimatedDialog$maybeStartLaunchAnimation$1(this), new AnimatedDialog$maybeStartLaunchAnimation$2(this));
        }
    }

    public final void onDialogDismissed() {
        if (!Intrinsics.areEqual((Object) Looper.myLooper(), (Object) Looper.getMainLooper())) {
            this.dialog.getContext().getMainExecutor().execute(new AnimatedDialog$onDialogDismissed$1(this));
        } else if (this.isLaunching) {
            this.dismissRequested = true;
        } else if (!this.isDismissing) {
            this.isDismissing = true;
            hideDialogIntoView(new AnimatedDialog$onDialogDismissed$2(this));
        }
    }

    public final void hideDialogIntoView(Function1<? super Boolean, Unit> function1) {
        if (this.decorViewLayoutListener != null) {
            getDecorView().removeOnLayoutChangeListener(this.decorViewLayoutListener);
        }
        if (!shouldAnimateDialogIntoView()) {
            Log.i("DialogLaunchAnimator", "Skipping animation of dialog into the touch surface");
            View view = this.touchSurface;
            LaunchableView launchableView = view instanceof LaunchableView ? (LaunchableView) view : null;
            if (launchableView != null) {
                launchableView.setShouldBlockVisibilityChanges(false);
            }
            if (this.touchSurface.getVisibility() == 4) {
                this.touchSurface.setVisibility(0);
            }
            function1.invoke(Boolean.FALSE);
            this.onDialogDismissed.invoke(this);
            return;
        }
        startAnimation(false, new AnimatedDialog$hideDialogIntoView$1(this), new AnimatedDialog$hideDialogIntoView$2(this, function1));
    }

    public final void startAnimation(boolean z, Function0<Unit> function0, Function0<Unit> function02) {
        View view;
        View view2;
        if (z) {
            view = this.touchSurface;
        } else {
            view = this.dialogContentWithBackground;
            Intrinsics.checkNotNull(view);
        }
        View view3 = view;
        if (z) {
            view2 = this.dialogContentWithBackground;
            Intrinsics.checkNotNull(view2);
        } else {
            view2 = this.touchSurface;
        }
        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(view3, (Integer) null, (InteractionJankMonitor) null, 6, (DefaultConstructorMarker) null);
        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController2 = new GhostedViewLaunchAnimatorController(view2, (Integer) null, (InteractionJankMonitor) null, 6, (DefaultConstructorMarker) null);
        ghostedViewLaunchAnimatorController.setLaunchContainer(getDecorView());
        ghostedViewLaunchAnimatorController2.setLaunchContainer(getDecorView());
        LaunchAnimator.State createAnimatorState = ghostedViewLaunchAnimatorController2.createAnimatorState();
        LaunchAnimator.startAnimation$default(this.launchAnimator, new AnimatedDialog$startAnimation$controller$1(ghostedViewLaunchAnimatorController, ghostedViewLaunchAnimatorController2, function0, function02, createAnimatorState), createAnimatorState, this.originalDialogBackgroundColor, false, 8, (Object) null);
    }

    public final boolean shouldAnimateDialogIntoView() {
        if (this.exitAnimationDisabled || !this.dialog.isShowing() || this.dreamManager.isDreaming() || this.touchSurface.getVisibility() != 4 || !this.touchSurface.isAttachedToWindow()) {
            return false;
        }
        ViewParent parent = this.touchSurface.getParent();
        View view = parent instanceof View ? (View) parent : null;
        if (view == null) {
            return true;
        }
        return view.isShown();
    }

    /* compiled from: DialogLaunchAnimator.kt */
    public static final class AnimatedBoundsLayoutListener implements View.OnLayoutChangeListener {
        @NotNull
        public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
        @Nullable
        public ValueAnimator currentAnimator;
        @Nullable
        public Rect lastBounds;

        /* compiled from: DialogLaunchAnimator.kt */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }

        public void onLayoutChange(@NotNull View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            View view2 = view;
            int i9 = i5;
            int i10 = i6;
            int i11 = i7;
            int i12 = i8;
            int i13 = i2;
            int i14 = i3;
            if (i == i9 && i13 == i10) {
                int i15 = i4;
                if (i14 == i11 && i15 == i12) {
                    Rect rect = this.lastBounds;
                    if (rect != null) {
                        view2.setLeft(rect.left);
                        view2.setTop(rect.top);
                        view2.setRight(rect.right);
                        view2.setBottom(rect.bottom);
                        return;
                    }
                    return;
                }
            } else {
                int i16 = i4;
            }
            if (this.lastBounds == null) {
                this.lastBounds = new Rect(i9, i10, i11, i12);
            }
            Rect rect2 = this.lastBounds;
            Intrinsics.checkNotNull(rect2);
            int i17 = rect2.left;
            int i18 = rect2.top;
            int i19 = rect2.right;
            int i20 = rect2.bottom;
            ValueAnimator valueAnimator = this.currentAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.currentAnimator = null;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            ofFloat.setDuration(500);
            ofFloat.setInterpolator(Interpolators.STANDARD);
            ofFloat.addListener(new AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$1(this));
            ofFloat.addUpdateListener(new AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$2(rect2, i17, i, i18, i2, i19, i3, i20, i4, view));
            this.currentAnimator = ofFloat;
            ofFloat.start();
        }
    }

    @NotNull
    public final View prepareForStackDismiss() {
        AnimatedDialog animatedDialog = this.parentAnimatedDialog;
        if (animatedDialog == null) {
            return this.touchSurface;
        }
        animatedDialog.exitAnimationDisabled = true;
        animatedDialog.dialog.hide();
        View prepareForStackDismiss = this.parentAnimatedDialog.prepareForStackDismiss();
        this.parentAnimatedDialog.dialog.dismiss();
        prepareForStackDismiss.setVisibility(4);
        return prepareForStackDismiss;
    }
}

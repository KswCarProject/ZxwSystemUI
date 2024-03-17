package com.android.systemui.statusbar.phone;

import android.view.View;
import android.view.WindowManager;
import com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: StatusBarMoveFromCenterAnimationController.kt */
public final class StatusBarMoveFromCenterAnimationController {
    @NotNull
    public final UnfoldMoveFromCenterAnimator moveFromCenterAnimator;
    @NotNull
    public final ScopedUnfoldTransitionProgressProvider progressProvider;
    @NotNull
    public final TransitionListener transitionListener = new TransitionListener();

    public StatusBarMoveFromCenterAnimationController(@NotNull ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, @NotNull WindowManager windowManager) {
        this.progressProvider = scopedUnfoldTransitionProgressProvider;
        this.moveFromCenterAnimator = new UnfoldMoveFromCenterAnimator(windowManager, (UnfoldMoveFromCenterAnimator.TranslationApplier) null, new PhoneStatusBarViewController.StatusBarViewsCenterProvider(), new StatusBarIconsAlphaProvider(), 2, (DefaultConstructorMarker) null);
    }

    public final void onViewsReady(@NotNull View[] viewArr) {
        this.moveFromCenterAnimator.updateDisplayProperties();
        int length = viewArr.length;
        int i = 0;
        while (i < length) {
            View view = viewArr[i];
            i++;
            this.moveFromCenterAnimator.registerViewForAnimation(view);
        }
        this.progressProvider.addCallback((UnfoldTransitionProgressProvider.TransitionProgressListener) this.transitionListener);
    }

    public final void onViewDetached() {
        this.progressProvider.removeCallback((UnfoldTransitionProgressProvider.TransitionProgressListener) this.transitionListener);
        this.moveFromCenterAnimator.clearRegisteredViews();
    }

    public final void onStatusBarWidthChanged() {
        this.moveFromCenterAnimator.updateDisplayProperties();
        this.moveFromCenterAnimator.updateViewPositions();
    }

    /* compiled from: StatusBarMoveFromCenterAnimationController.kt */
    public final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        public void onTransitionStarted() {
            UnfoldTransitionProgressProvider.TransitionProgressListener.DefaultImpls.onTransitionStarted(this);
        }

        public void onTransitionProgress(float f) {
            StatusBarMoveFromCenterAnimationController.this.moveFromCenterAnimator.onTransitionProgress(f);
        }

        public void onTransitionFinished() {
            StatusBarMoveFromCenterAnimationController.this.moveFromCenterAnimator.onTransitionProgress(1.0f);
        }
    }

    /* compiled from: StatusBarMoveFromCenterAnimationController.kt */
    public static final class StatusBarIconsAlphaProvider implements UnfoldMoveFromCenterAnimator.AlphaProvider {
        public float getAlpha(float f) {
            return Math.max(0.0f, (f - 0.75f) / 0.25f);
        }
    }
}

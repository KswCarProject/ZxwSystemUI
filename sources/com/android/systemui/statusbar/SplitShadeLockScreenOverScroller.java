package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.MathUtils;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SplitShadeLockScreenOverScroller.kt */
public final class SplitShadeLockScreenOverScroller implements LockScreenShadeOverScroller {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final PathInterpolator RELEASE_OVER_SCROLL_INTERPOLATOR = new PathInterpolator(0.17f, 0.0f, 0.0f, 1.0f);
    @NotNull
    public final Context context;
    public float expansionDragDownAmount;
    public int maxOverScrollAmount;
    @NotNull
    public final NotificationStackScrollLayoutController nsslController;
    public int previousOverscrollAmount;
    @NotNull
    public final QS qS;
    @Nullable
    public Animator releaseOverScrollAnimator;
    public long releaseOverScrollDuration;
    @NotNull
    public final ScrimController scrimController;
    @NotNull
    public final SysuiStatusBarStateController statusBarStateController;
    public int transitionToFullShadeDistance;

    /* compiled from: SplitShadeLockScreenOverScroller.kt */
    public interface Factory {
        @NotNull
        SplitShadeLockScreenOverScroller create(@NotNull QS qs, @NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController);
    }

    public SplitShadeLockScreenOverScroller(@NotNull ConfigurationController configurationController, @NotNull Context context2, @NotNull ScrimController scrimController2, @NotNull SysuiStatusBarStateController sysuiStatusBarStateController, @NotNull QS qs, @NotNull NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        this.context = context2;
        this.scrimController = scrimController2;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.qS = qs;
        this.nsslController = notificationStackScrollLayoutController;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            public final /* synthetic */ SplitShadeLockScreenOverScroller this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(@Nullable Configuration configuration) {
                this.this$0.updateResources();
            }
        });
    }

    public final void updateResources() {
        Resources resources = this.context.getResources();
        this.transitionToFullShadeDistance = resources.getDimensionPixelSize(R$dimen.lockscreen_shade_full_transition_distance);
        this.maxOverScrollAmount = resources.getDimensionPixelSize(R$dimen.lockscreen_shade_max_over_scroll_amount);
        this.releaseOverScrollDuration = (long) resources.getInteger(R$integer.lockscreen_shade_over_scroll_release_duration);
    }

    public void setExpansionDragDownAmount(float f) {
        if (!(this.expansionDragDownAmount == f)) {
            this.expansionDragDownAmount = f;
            if (shouldOverscroll()) {
                overScroll(f);
            } else if (shouldReleaseOverscroll()) {
                releaseOverScroll();
            }
        }
    }

    public final boolean shouldOverscroll() {
        return this.statusBarStateController.getState() == 1;
    }

    public final boolean shouldReleaseOverscroll() {
        return !shouldOverscroll() && this.previousOverscrollAmount != 0;
    }

    public final void overScroll(float f) {
        int calculateOverscrollAmount = calculateOverscrollAmount(f);
        applyOverscroll(calculateOverscrollAmount);
        this.previousOverscrollAmount = calculateOverscrollAmount;
    }

    public final void applyOverscroll(int i) {
        this.qS.setOverScrollAmount(i);
        this.scrimController.setNotificationsOverScrollAmount(i);
        this.nsslController.setOverScrollAmount(i);
    }

    public final int calculateOverscrollAmount(float f) {
        float height = (float) this.nsslController.getHeight();
        return (int) (Interpolators.getOvershootInterpolation(MathUtils.saturate(f / height), 0.6f, ((float) this.transitionToFullShadeDistance) / height) * ((float) this.maxOverScrollAmount));
    }

    public final void releaseOverScroll() {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.previousOverscrollAmount, 0});
        ofInt.addUpdateListener(new SplitShadeLockScreenOverScroller$releaseOverScroll$1(this));
        ofInt.setInterpolator(RELEASE_OVER_SCROLL_INTERPOLATOR);
        ofInt.setDuration(this.releaseOverScrollDuration);
        ofInt.start();
        this.releaseOverScrollAnimator = ofInt;
        this.previousOverscrollAmount = 0;
    }

    @VisibleForTesting
    public final void finishAnimations$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        Animator animator = this.releaseOverScrollAnimator;
        if (animator != null) {
            animator.end();
        }
        this.releaseOverScrollAnimator = null;
    }

    /* compiled from: SplitShadeLockScreenOverScroller.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}

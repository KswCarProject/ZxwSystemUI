package com.android.systemui.dreams;

import android.content.res.Resources;
import android.os.Handler;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.dreams.complication.ComplicationHostViewController;
import com.android.systemui.statusbar.BlurUtils;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.util.ViewController;
import java.util.Arrays;

public class DreamOverlayContainerViewController extends ViewController<DreamOverlayContainerView> {
    public final BlurUtils mBlurUtils;
    public boolean mBouncerAnimating;
    public final KeyguardBouncer.BouncerExpansionCallback mBouncerExpansionCallback = new KeyguardBouncer.BouncerExpansionCallback() {
        public void onStartingToShow() {
            DreamOverlayContainerViewController.this.mBouncerAnimating = true;
        }

        public void onStartingToHide() {
            DreamOverlayContainerViewController.this.mBouncerAnimating = true;
        }

        public void onFullyHidden() {
            DreamOverlayContainerViewController.this.mBouncerAnimating = false;
        }

        public void onFullyShown() {
            DreamOverlayContainerViewController.this.mBouncerAnimating = false;
        }

        public void onExpansionChanged(float f) {
            if (DreamOverlayContainerViewController.this.mBouncerAnimating) {
                DreamOverlayContainerViewController.this.updateTransitionState(f);
            }
        }

        public void onVisibilityChanged(boolean z) {
            if (!z) {
                DreamOverlayContainerViewController.this.updateTransitionState(1.0f);
            }
        }
    };
    public final long mBurnInProtectionUpdateInterval;
    public final ComplicationHostViewController mComplicationHostViewController;
    public final ViewGroup mDreamOverlayContentView;
    public final int mDreamOverlayMaxTranslationY;
    public final Handler mHandler;
    public long mJitterStartTimeMillis;
    public final int mMaxBurnInOffset;
    public final long mMillisUntilFullJitter;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final DreamOverlayStatusBarViewController mStatusBarViewController;

    public DreamOverlayContainerViewController(DreamOverlayContainerView dreamOverlayContainerView, ComplicationHostViewController complicationHostViewController, ViewGroup viewGroup, DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, BlurUtils blurUtils, Handler handler, Resources resources, int i, long j, long j2) {
        super(dreamOverlayContainerView);
        this.mDreamOverlayContentView = viewGroup;
        this.mStatusBarViewController = dreamOverlayStatusBarViewController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mBlurUtils = blurUtils;
        this.mComplicationHostViewController = complicationHostViewController;
        this.mDreamOverlayMaxTranslationY = resources.getDimensionPixelSize(R$dimen.dream_overlay_y_offset);
        viewGroup.addView(complicationHostViewController.getView(), new ViewGroup.LayoutParams(-1, -1));
        this.mHandler = handler;
        this.mMaxBurnInOffset = i;
        this.mBurnInProtectionUpdateInterval = j;
        this.mMillisUntilFullJitter = j2;
    }

    public void onInit() {
        this.mStatusBarViewController.init();
        this.mComplicationHostViewController.init();
    }

    public void onViewAttached() {
        this.mJitterStartTimeMillis = System.currentTimeMillis();
        this.mHandler.postDelayed(new DreamOverlayContainerViewController$$ExternalSyntheticLambda1(this), this.mBurnInProtectionUpdateInterval);
        KeyguardBouncer bouncer = this.mStatusBarKeyguardViewManager.getBouncer();
        if (bouncer != null) {
            bouncer.addBouncerExpansionCallback(this.mBouncerExpansionCallback);
        }
    }

    public void onViewDetached() {
        this.mHandler.removeCallbacks(new DreamOverlayContainerViewController$$ExternalSyntheticLambda1(this));
        KeyguardBouncer bouncer = this.mStatusBarKeyguardViewManager.getBouncer();
        if (bouncer != null) {
            bouncer.removeBouncerExpansionCallback(this.mBouncerExpansionCallback);
        }
    }

    public View getContainerView() {
        return this.mView;
    }

    public final void updateBurnInOffsets() {
        int i = this.mMaxBurnInOffset;
        long currentTimeMillis = System.currentTimeMillis() - this.mJitterStartTimeMillis;
        long j = this.mMillisUntilFullJitter;
        if (currentTimeMillis < j) {
            i = Math.round(MathUtils.lerp(0.0f, (float) i, ((float) currentTimeMillis) / ((float) j)));
        }
        int i2 = i * 2;
        int burnInOffset = BurnInHelperKt.getBurnInOffset(i2, true) - i;
        int burnInOffset2 = BurnInHelperKt.getBurnInOffset(i2, false) - i;
        ((DreamOverlayContainerView) this.mView).setTranslationX((float) burnInOffset);
        ((DreamOverlayContainerView) this.mView).setTranslationY((float) burnInOffset2);
        this.mHandler.postDelayed(new DreamOverlayContainerViewController$$ExternalSyntheticLambda1(this), this.mBurnInProtectionUpdateInterval);
    }

    public final void updateTransitionState(float f) {
        for (Integer intValue : Arrays.asList(new Integer[]{1, 2})) {
            int intValue2 = intValue.intValue();
            this.mComplicationHostViewController.getViewsAtPosition(intValue2).forEach(new DreamOverlayContainerViewController$$ExternalSyntheticLambda0(getAlpha(intValue2, f), getTranslationY(intValue2, f)));
        }
        this.mBlurUtils.applyBlur(((DreamOverlayContainerView) this.mView).getViewRootImpl(), (int) this.mBlurUtils.blurRadiusOfRatio(1.0f - BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f)), false);
    }

    public static /* synthetic */ void lambda$updateTransitionState$0(float f, float f2, View view) {
        view.setAlpha(f);
        view.setTranslationY(f2);
    }

    public static float getAlpha(int i, float f) {
        float f2;
        Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        if (i == 1) {
            f2 = BouncerPanelExpansionCalculator.getDreamAlphaScaledExpansion(f);
        } else {
            f2 = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f + 0.03f);
        }
        return interpolator.getInterpolation(f2);
    }

    public final float getTranslationY(int i, float f) {
        float f2;
        Interpolator interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
        if (i == 1) {
            f2 = BouncerPanelExpansionCalculator.getDreamYPositionScaledExpansion(f);
        } else {
            f2 = BouncerPanelExpansionCalculator.aboutToShowBouncerProgress(f + 0.03f);
        }
        return MathUtils.lerp(-this.mDreamOverlayMaxTranslationY, 0, interpolator.getInterpolation(f2));
    }
}

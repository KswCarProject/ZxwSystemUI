package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.util.BurnInHelperKt;
import java.io.PrintWriter;

public class UdfpsKeyguardView extends UdfpsAnimationView {
    public int mAlpha;
    public int mAnimationType = 0;
    public LottieAnimationView mAodFp;
    public AnimatorSet mBackgroundInAnimator = new AnimatorSet();
    public ImageView mBgProtection;
    public float mBurnInOffsetX;
    public float mBurnInOffsetY;
    public float mBurnInProgress;
    public UdfpsDrawable mFingerprintDrawable;
    public boolean mFullyInflated;
    public float mInterpolatedDarkAmount;
    public final AsyncLayoutInflater.OnInflateFinishedListener mLayoutInflaterFinishListener = new AsyncLayoutInflater.OnInflateFinishedListener() {
        public void onInflateFinished(View view, int i, ViewGroup viewGroup) {
            UdfpsKeyguardView.this.mFullyInflated = true;
            UdfpsKeyguardView.this.mAodFp = (LottieAnimationView) view.findViewById(R$id.udfps_aod_fp);
            UdfpsKeyguardView.this.mLockScreenFp = (LottieAnimationView) view.findViewById(R$id.udfps_lockscreen_fp);
            UdfpsKeyguardView.this.mBgProtection = (ImageView) view.findViewById(R$id.udfps_keyguard_fp_bg);
            UdfpsKeyguardView.this.updatePadding();
            UdfpsKeyguardView.this.updateColor();
            UdfpsKeyguardView.this.updateAlpha();
            viewGroup.addView(view);
            UdfpsKeyguardView.this.mLockScreenFp.addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER, new UdfpsKeyguardView$2$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ ColorFilter lambda$onInflateFinished$0(LottieFrameInfo lottieFrameInfo) {
            return new PorterDuffColorFilter(UdfpsKeyguardView.this.mTextColorPrimary, PorterDuff.Mode.SRC_ATOP);
        }
    };
    public LottieAnimationView mLockScreenFp;
    public final int mMaxBurnInOffsetX;
    public final int mMaxBurnInOffsetY;
    public float mScaleFactor = 1.0f;
    public int mTextColorPrimary;
    public boolean mUdfpsRequested;

    public void onIlluminationStarting() {
    }

    public void onIlluminationStopped() {
    }

    public UdfpsKeyguardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mFingerprintDrawable = new UdfpsFpDrawable(context);
        this.mMaxBurnInOffsetX = context.getResources().getDimensionPixelSize(R$dimen.udfps_burn_in_offset_x);
        this.mMaxBurnInOffsetY = context.getResources().getDimensionPixelSize(R$dimen.udfps_burn_in_offset_y);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        new AsyncLayoutInflater(this.mContext).inflate(R$layout.udfps_keyguard_view_internal, this, this.mLayoutInflaterFinishListener);
    }

    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    public boolean dozeTimeTick() {
        updateBurnInOffsets();
        return true;
    }

    public final void updateBurnInOffsets() {
        float f;
        boolean z;
        if (this.mFullyInflated) {
            if (this.mAnimationType == 2) {
                f = 1.0f;
            } else {
                f = this.mInterpolatedDarkAmount;
            }
            boolean z2 = true;
            this.mBurnInOffsetX = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetX * 2, true) - this.mMaxBurnInOffsetX), f);
            this.mBurnInOffsetY = MathUtils.lerp(0.0f, (float) (BurnInHelperKt.getBurnInOffset(this.mMaxBurnInOffsetY * 2, false) - this.mMaxBurnInOffsetY), f);
            this.mBurnInProgress = MathUtils.lerp(0.0f, BurnInHelperKt.getBurnInProgressOffset(), f);
            if (this.mAnimationType == 1 && !this.mPauseAuth) {
                this.mLockScreenFp.setTranslationX(this.mBurnInOffsetX);
                this.mLockScreenFp.setTranslationY(this.mBurnInOffsetY);
                this.mBgProtection.setAlpha(1.0f - this.mInterpolatedDarkAmount);
                this.mLockScreenFp.setAlpha(1.0f - this.mInterpolatedDarkAmount);
            } else if (f == 0.0f) {
                this.mLockScreenFp.setTranslationX(0.0f);
                this.mLockScreenFp.setTranslationY(0.0f);
                this.mBgProtection.setAlpha(((float) this.mAlpha) / 255.0f);
                this.mLockScreenFp.setAlpha(((float) this.mAlpha) / 255.0f);
            } else {
                this.mBgProtection.setAlpha(0.0f);
                this.mLockScreenFp.setAlpha(0.0f);
            }
            this.mLockScreenFp.setProgress(1.0f - this.mInterpolatedDarkAmount);
            this.mAodFp.setTranslationX(this.mBurnInOffsetX);
            this.mAodFp.setTranslationY(this.mBurnInOffsetY);
            this.mAodFp.setProgress(this.mBurnInProgress);
            this.mAodFp.setAlpha(this.mInterpolatedDarkAmount);
            int i = this.mAnimationType;
            if (i == 1) {
                float f2 = this.mInterpolatedDarkAmount;
                if (f2 == 0.0f || f2 == 1.0f) {
                    z = true;
                    if (!(i == 2 && this.mInterpolatedDarkAmount == 1.0f)) {
                        z2 = false;
                    }
                    if (!z || z2) {
                        this.mAnimationType = 0;
                    }
                    return;
                }
            }
            z = false;
            z2 = false;
            if (!z) {
            }
            this.mAnimationType = 0;
        }
    }

    public void requestUdfps(boolean z, int i) {
        this.mUdfpsRequested = z;
    }

    public void updateColor() {
        if (this.mFullyInflated) {
            this.mTextColorPrimary = Utils.getColorAttrDefaultColor(this.mContext, 16842806);
            this.mBgProtection.setImageDrawable(getContext().getDrawable(R$drawable.fingerprint_bg));
            this.mLockScreenFp.invalidate();
        }
    }

    public void setScaleFactor(float f) {
        this.mScaleFactor = f;
    }

    public void updatePadding() {
        if (this.mLockScreenFp != null && this.mAodFp != null) {
            int dimensionPixelSize = (int) (((float) getResources().getDimensionPixelSize(R$dimen.lock_icon_padding)) * this.mScaleFactor);
            this.mLockScreenFp.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
            this.mAodFp.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        }
    }

    public void setUnpausedAlpha(int i) {
        this.mAlpha = i;
        updateAlpha();
    }

    public int getUnpausedAlpha() {
        return this.mAlpha;
    }

    public int updateAlpha() {
        int updateAlpha = super.updateAlpha();
        updateBurnInOffsets();
        return updateAlpha;
    }

    public int calculateAlpha() {
        if (this.mPauseAuth) {
            return 0;
        }
        return this.mAlpha;
    }

    public void onDozeAmountChanged(float f, float f2, int i) {
        this.mAnimationType = i;
        this.mInterpolatedDarkAmount = f2;
        updateAlpha();
    }

    public void animateInUdfpsBouncer(final Runnable runnable) {
        if (!this.mBackgroundInAnimator.isRunning() && this.mFullyInflated) {
            AnimatorSet animatorSet = new AnimatorSet();
            this.mBackgroundInAnimator = animatorSet;
            animatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mBgProtection, View.ALPHA, new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(this.mBgProtection, View.SCALE_X, new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(this.mBgProtection, View.SCALE_Y, new float[]{0.0f, 1.0f})});
            this.mBackgroundInAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mBackgroundInAnimator.setDuration(500);
            this.mBackgroundInAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            });
            this.mBackgroundInAnimator.start();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("UdfpsKeyguardView (" + this + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("    mPauseAuth=");
        sb.append(this.mPauseAuth);
        printWriter.println(sb.toString());
        printWriter.println("    mUnpausedAlpha=" + getUnpausedAlpha());
        printWriter.println("    mUdfpsRequested=" + this.mUdfpsRequested);
        printWriter.println("    mInterpolatedDarkAmount=" + this.mInterpolatedDarkAmount);
        printWriter.println("    mAnimationType=" + this.mAnimationType);
    }
}

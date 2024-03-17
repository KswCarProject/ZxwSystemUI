package com.android.systemui.statusbar.phone;

import android.content.res.Resources;
import android.util.MathUtils;
import com.android.keyguard.BouncerPanelExpansionCalculator;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.util.BurnInHelperKt;
import com.android.systemui.statusbar.notification.NotificationUtils;

public class KeyguardClockPositionAlgorithm {
    public int mBurnInPreventionOffsetX;
    public int mBurnInPreventionOffsetYClock;
    public boolean mBypassEnabled;
    public float mClockBottom;
    public int mContainerTopPadding;
    public int mCutoutTopInset = 0;
    public float mDarkAmount;
    public boolean mIsClockTopAligned;
    public boolean mIsSplitShade;
    public int mKeyguardStatusHeight;
    public int mMinTopMargin;
    public float mOverStretchAmount;
    public float mPanelExpansion;
    public float mQsExpansion;
    public int mSplitShadeTargetTopMargin;
    public int mSplitShadeTopNotificationsMargin;
    public int mStatusViewBottomMargin;
    public float mUdfpsTop;
    public int mUnlockedStackScrollerPadding;
    public int mUserSwitchHeight;
    public int mUserSwitchPreferredY;

    public static class Result {
        public float clockAlpha;
        public float clockScale;
        public int clockX;
        public int clockY;
        public int clockYFullyDozing;
        public int stackScrollerPadding;
        public int stackScrollerPaddingExpanded;
        public int userSwitchY;
    }

    public void loadDimens(Resources resources) {
        this.mStatusViewBottomMargin = resources.getDimensionPixelSize(R$dimen.keyguard_status_view_bottom_margin);
        this.mSplitShadeTopNotificationsMargin = resources.getDimensionPixelSize(R$dimen.large_screen_shade_header_height);
        this.mSplitShadeTargetTopMargin = resources.getDimensionPixelSize(R$dimen.keyguard_split_shade_top_margin);
        this.mContainerTopPadding = resources.getDimensionPixelSize(R$dimen.keyguard_clock_top_margin);
        this.mBurnInPreventionOffsetX = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_x);
        this.mBurnInPreventionOffsetYClock = resources.getDimensionPixelSize(R$dimen.burn_in_prevention_offset_y_clock);
    }

    public void setup(int i, float f, int i2, int i3, int i4, float f2, float f3, boolean z, int i5, float f4, int i6, boolean z2, float f5, float f6, boolean z3) {
        this.mMinTopMargin = Math.max(this.mContainerTopPadding, i3) + i;
        this.mPanelExpansion = BouncerPanelExpansionCalculator.getKeyguardClockScaledExpansion(f);
        this.mKeyguardStatusHeight = this.mStatusViewBottomMargin + i2;
        this.mUserSwitchHeight = i3;
        this.mUserSwitchPreferredY = i4;
        this.mDarkAmount = f2;
        this.mOverStretchAmount = f3;
        this.mBypassEnabled = z;
        this.mUnlockedStackScrollerPadding = i5;
        this.mQsExpansion = f4;
        this.mCutoutTopInset = i6;
        this.mIsSplitShade = z2;
        this.mUdfpsTop = f5;
        this.mClockBottom = f6;
        this.mIsClockTopAligned = z3;
    }

    public void run(Result result) {
        int clockY = getClockY(this.mPanelExpansion, this.mDarkAmount);
        result.clockY = clockY;
        result.userSwitchY = getUserSwitcherY(this.mPanelExpansion);
        result.clockYFullyDozing = getClockY(1.0f, 1.0f);
        result.clockAlpha = getClockAlpha(clockY);
        result.stackScrollerPadding = getStackScrollerPadding(clockY);
        result.stackScrollerPaddingExpanded = getStackScrollerPaddingExpanded();
        result.clockX = (int) NotificationUtils.interpolate(0.0f, burnInPreventionOffsetX(), this.mDarkAmount);
        result.clockScale = NotificationUtils.interpolate(BurnInHelperKt.getBurnInScale(), 1.0f, 1.0f - this.mDarkAmount);
    }

    public final int getStackScrollerPaddingExpanded() {
        int clockY;
        int i;
        if (this.mBypassEnabled) {
            return this.mUnlockedStackScrollerPadding;
        }
        if (this.mIsSplitShade) {
            clockY = getClockY(1.0f, this.mDarkAmount);
            i = this.mUserSwitchHeight;
        } else {
            clockY = getClockY(1.0f, this.mDarkAmount);
            i = this.mKeyguardStatusHeight;
        }
        return clockY + i;
    }

    public final int getStackScrollerPadding(int i) {
        int i2;
        if (this.mBypassEnabled) {
            return (int) (((float) this.mUnlockedStackScrollerPadding) + this.mOverStretchAmount);
        }
        if (this.mIsSplitShade) {
            i -= this.mSplitShadeTopNotificationsMargin;
            i2 = this.mUserSwitchHeight;
        } else {
            i2 = this.mKeyguardStatusHeight;
        }
        return i + i2;
    }

    public float getLockscreenMinStackScrollerPadding() {
        if (this.mBypassEnabled) {
            return (float) this.mUnlockedStackScrollerPadding;
        }
        if (this.mIsSplitShade) {
            return (float) (this.mSplitShadeTargetTopMargin + this.mUserSwitchHeight);
        }
        return (float) (this.mMinTopMargin + this.mKeyguardStatusHeight);
    }

    public final int getExpandedPreferredClockY() {
        if (this.mIsSplitShade) {
            return this.mSplitShadeTargetTopMargin;
        }
        return this.mMinTopMargin;
    }

    public int getLockscreenStatusViewHeight() {
        return this.mKeyguardStatusHeight;
    }

    public final int getClockY(float f, float f2) {
        float lerp = MathUtils.lerp(((float) (-this.mKeyguardStatusHeight)) / 3.0f, (float) getExpandedPreferredClockY(), Interpolators.FAST_OUT_LINEAR_IN.getInterpolation(f));
        int i = this.mBurnInPreventionOffsetYClock;
        int i2 = this.mCutoutTopInset;
        float f3 = lerp - ((float) i) < ((float) i2) ? ((float) i2) - (lerp - ((float) i)) : 0.0f;
        float f4 = this.mUdfpsTop;
        if ((f4 > -1.0f) && !this.mIsClockTopAligned) {
            float f5 = this.mClockBottom;
            if (f4 < f5) {
                int i3 = ((int) (lerp - ((float) i2))) / 2;
                if (i >= i3) {
                    i = i3;
                }
                f3 = (float) (-i);
            } else {
                float f6 = lerp - ((float) i2);
                float f7 = f4 - f5;
                int i4 = ((int) (f7 + f6)) / 2;
                if (i >= i4) {
                    i = i4;
                }
                f3 = (f7 - f6) / 2.0f;
            }
        }
        return (int) (MathUtils.lerp(lerp, burnInPreventionOffsetY(i) + lerp + f3, f2) + this.mOverStretchAmount);
    }

    public final int getUserSwitcherY(float f) {
        return (int) (MathUtils.lerp((float) ((-this.mKeyguardStatusHeight) - this.mUserSwitchHeight), (float) this.mUserSwitchPreferredY, Interpolators.FAST_OUT_LINEAR_IN.getInterpolation(f)) + this.mOverStretchAmount);
    }

    public final float getClockAlpha(int i) {
        return MathUtils.lerp(Interpolators.ACCELERATE.getInterpolation(Math.max(0.0f, ((float) i) / Math.max(1.0f, (float) getClockY(1.0f, this.mDarkAmount))) * (1.0f - MathUtils.saturate(this.mQsExpansion / 0.3f))), 1.0f, this.mDarkAmount);
    }

    public final float burnInPreventionOffsetY(int i) {
        return (float) (BurnInHelperKt.getBurnInOffset(i * 2, false) - i);
    }

    public final float burnInPreventionOffsetX() {
        return (float) BurnInHelperKt.getBurnInOffset(this.mBurnInPreventionOffsetX, true);
    }
}

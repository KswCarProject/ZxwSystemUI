package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.settingslib.Utils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.FakeShadowView;
import com.android.systemui.statusbar.notification.NotificationUtils;

public abstract class ActivatableNotificationView extends ExpandableOutlineView {
    public static final Interpolator ACTIVATE_INVERSE_ALPHA_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.5f, 1.0f);
    public static final Interpolator ACTIVATE_INVERSE_INTERPOLATOR = new PathInterpolator(0.6f, 0.0f, 0.5f, 1.0f);
    public AccessibilityManager mAccessibilityManager;
    public boolean mActivated;
    public float mAnimationTranslationY;
    public float mAppearAnimationFraction = -1.0f;
    public float mAppearAnimationTranslation;
    public ValueAnimator mAppearAnimator;
    public ValueAnimator mBackgroundColorAnimator;
    public NotificationBackgroundView mBackgroundNormal;
    public int mBgTint = 0;
    public Interpolator mCurrentAppearInterpolator;
    public int mCurrentBackgroundTint;
    public boolean mDismissed;
    public boolean mDrawingAppearAnimation;
    public FakeShadowView mFakeShadow;
    public boolean mIsBelowSpeedBump;
    public boolean mIsHeadsUpAnimation;
    public long mLastActionUpTime;
    public float mNormalBackgroundVisibilityAmount;
    public int mNormalColor;
    public int mNormalRippleColor;
    public OnActivatedListener mOnActivatedListener;
    public float mOverrideAmount;
    public int mOverrideTint;
    public boolean mRefocusOnDismiss;
    public boolean mShadowHidden;
    public final Interpolator mSlowOutFastInInterpolator = new PathInterpolator(0.8f, 0.0f, 0.6f, 1.0f);
    public final Interpolator mSlowOutLinearInInterpolator = new PathInterpolator(0.8f, 0.0f, 1.0f, 1.0f);
    public int mStartTint;
    public Point mTargetPoint;
    public int mTargetTint;
    public int mTintedRippleColor;
    public Gefingerpoken mTouchHandler;

    public interface OnActivatedListener {
        void onActivated(ActivatableNotificationView activatableNotificationView);

        void onActivationReset(ActivatableNotificationView activatableNotificationView);
    }

    public abstract View getContentView();

    public boolean handleSlideBack() {
        return false;
    }

    public boolean hideBackground() {
        return false;
    }

    public void onAppearAnimationFinished(boolean z) {
    }

    public void onBelowSpeedBumpChanged() {
    }

    public void onTap() {
    }

    public void resetAllContentAlphas() {
    }

    public ActivatableNotificationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setClipChildren(false);
        setClipToPadding(false);
        updateColors();
    }

    private void updateColors() {
        this.mNormalColor = Utils.getColorAttrDefaultColor(this.mContext, 17956909);
        this.mTintedRippleColor = this.mContext.getColor(R$color.notification_ripple_tinted_color);
        this.mNormalRippleColor = this.mContext.getColor(R$color.notification_ripple_untinted_color);
    }

    public void updateBackgroundColors() {
        updateColors();
        initBackground();
        updateBackgroundTint();
    }

    public void setBackgroundWidth(int i) {
        NotificationBackgroundView notificationBackgroundView = this.mBackgroundNormal;
        if (notificationBackgroundView != null) {
            notificationBackgroundView.setActualWidth(i);
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mBackgroundNormal = (NotificationBackgroundView) findViewById(R$id.backgroundNormal);
        FakeShadowView fakeShadowView = (FakeShadowView) findViewById(R$id.fake_shadow);
        this.mFakeShadow = fakeShadowView;
        this.mShadowHidden = fakeShadowView.getVisibility() != 0;
        initBackground();
        updateBackgroundTint();
        updateOutlineAlpha();
    }

    public void initBackground() {
        this.mBackgroundNormal.setCustomBackground(R$drawable.notification_material_bg);
    }

    public void updateBackground() {
        this.mBackgroundNormal.setVisibility(hideBackground() ? 4 : 0);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mTouchHandler;
        if (gefingerpoken == null || !gefingerpoken.onInterceptTouchEvent(motionEvent)) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    public void setLastActionUpTime(long j) {
        this.mLastActionUpTime = j;
    }

    public long getAndResetLastActionUpTime() {
        long j = this.mLastActionUpTime;
        this.mLastActionUpTime = 0;
        return j;
    }

    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.mBackgroundNormal.setState(getDrawableState());
    }

    public void setRippleAllowed(boolean z) {
        this.mBackgroundNormal.setPressedAllowed(z);
    }

    public void makeActive() {
        this.mActivated = true;
        OnActivatedListener onActivatedListener = this.mOnActivatedListener;
        if (onActivatedListener != null) {
            onActivatedListener.onActivated(this);
        }
    }

    public void makeInactive(boolean z) {
        if (this.mActivated) {
            this.mActivated = false;
        }
        OnActivatedListener onActivatedListener = this.mOnActivatedListener;
        if (onActivatedListener != null) {
            onActivatedListener.onActivationReset(this);
        }
    }

    public final void updateOutlineAlpha() {
        setOutlineAlpha((0.3f * this.mNormalBackgroundVisibilityAmount) + 0.7f);
    }

    public void setBelowSpeedBump(boolean z) {
        super.setBelowSpeedBump(z);
        if (z != this.mIsBelowSpeedBump) {
            this.mIsBelowSpeedBump = z;
            updateBackgroundTint();
            onBelowSpeedBumpChanged();
        }
    }

    public void setTintColor(int i) {
        setTintColor(i, false);
    }

    public void setTintColor(int i, boolean z) {
        if (i != this.mBgTint) {
            this.mBgTint = i;
            updateBackgroundTint(z);
        }
    }

    public void setOverrideTintColor(int i, float f) {
        this.mOverrideTint = i;
        this.mOverrideAmount = f;
        setBackgroundTintColor(calculateBgColor());
    }

    public void updateBackgroundTint() {
        updateBackgroundTint(false);
    }

    public final void updateBackgroundTint(boolean z) {
        ValueAnimator valueAnimator = this.mBackgroundColorAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mBackgroundNormal.setRippleColor(getRippleColor());
        int calculateBgColor = calculateBgColor();
        if (!z) {
            setBackgroundTintColor(calculateBgColor);
            return;
        }
        int i = this.mCurrentBackgroundTint;
        if (calculateBgColor != i) {
            this.mStartTint = i;
            this.mTargetTint = calculateBgColor;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mBackgroundColorAnimator = ofFloat;
            ofFloat.addUpdateListener(new ActivatableNotificationView$$ExternalSyntheticLambda0(this));
            this.mBackgroundColorAnimator.setDuration(360);
            this.mBackgroundColorAnimator.setInterpolator(Interpolators.LINEAR);
            this.mBackgroundColorAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    ActivatableNotificationView.this.mBackgroundColorAnimator = null;
                }
            });
            this.mBackgroundColorAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateBackgroundTint$0(ValueAnimator valueAnimator) {
        setBackgroundTintColor(NotificationUtils.interpolateColors(this.mStartTint, this.mTargetTint, valueAnimator.getAnimatedFraction()));
    }

    public void setBackgroundTintColor(int i) {
        if (i != this.mCurrentBackgroundTint) {
            this.mCurrentBackgroundTint = i;
            if (i == this.mNormalColor) {
                i = 0;
            }
            this.mBackgroundNormal.setTint(i);
        }
    }

    public void updateBackgroundClipping() {
        this.mBackgroundNormal.setBottomAmountClips(!isChildInGroup());
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setPivotX((float) (getWidth() / 2));
    }

    public void setActualHeight(int i, boolean z) {
        super.setActualHeight(i, z);
        setPivotY((float) (i / 2));
        this.mBackgroundNormal.setActualHeight(i);
    }

    public void setClipTopAmount(int i) {
        super.setClipTopAmount(i);
        this.mBackgroundNormal.setClipTopAmount(i);
    }

    public void setClipBottomAmount(int i) {
        super.setClipBottomAmount(i);
        this.mBackgroundNormal.setClipBottomAmount(i);
    }

    public long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(false, f, j2, j, runnable, animatorListenerAdapter);
            return 0;
        } else if (runnable == null) {
            return 0;
        } else {
            runnable.run();
            return 0;
        }
    }

    public void performAddAnimation(long j, long j2, boolean z, Runnable runnable) {
        enableAppearDrawing(true);
        this.mIsHeadsUpAnimation = z;
        if (this.mDrawingAppearAnimation) {
            startAppearAnimation(true, z ? 0.0f : -1.0f, j, j2, (Runnable) null, (AnimatorListenerAdapter) null);
        }
    }

    public final void startAppearAnimation(final boolean z, float f, long j, long j2, final Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        this.mAnimationTranslationY = f * ((float) getActualHeight());
        cancelAppearAnimation();
        float f2 = 1.0f;
        if (this.mAppearAnimationFraction == -1.0f) {
            if (z) {
                this.mAppearAnimationFraction = 0.0f;
                this.mAppearAnimationTranslation = this.mAnimationTranslationY;
            } else {
                this.mAppearAnimationFraction = 1.0f;
                this.mAppearAnimationTranslation = 0.0f;
            }
        }
        if (z) {
            this.mCurrentAppearInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        } else {
            this.mCurrentAppearInterpolator = this.mSlowOutFastInInterpolator;
            f2 = 0.0f;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mAppearAnimationFraction, f2});
        this.mAppearAnimator = ofFloat;
        ofFloat.setInterpolator(Interpolators.LINEAR);
        this.mAppearAnimator.setDuration((long) (((float) j2) * Math.abs(this.mAppearAnimationFraction - f2)));
        this.mAppearAnimator.addUpdateListener(new ActivatableNotificationView$$ExternalSyntheticLambda1(this));
        if (animatorListenerAdapter != null) {
            this.mAppearAnimator.addListener(animatorListenerAdapter);
        }
        if (j > 0) {
            updateAppearAnimationAlpha();
            updateAppearRect();
            this.mAppearAnimator.setStartDelay(j);
        }
        this.mAppearAnimator.addListener(new AnimatorListenerAdapter() {
            public boolean mWasCancelled;

            public void onAnimationEnd(Animator animator) {
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
                if (!this.mWasCancelled) {
                    ActivatableNotificationView.this.enableAppearDrawing(false);
                    ActivatableNotificationView.this.onAppearAnimationFinished(z);
                    InteractionJankMonitor.getInstance().end(ActivatableNotificationView.this.getCujType(z));
                    return;
                }
                InteractionJankMonitor.getInstance().cancel(ActivatableNotificationView.this.getCujType(z));
            }

            public void onAnimationStart(Animator animator) {
                this.mWasCancelled = false;
                InteractionJankMonitor.getInstance().begin(InteractionJankMonitor.Configuration.Builder.withView(ActivatableNotificationView.this.getCujType(z), ActivatableNotificationView.this));
            }

            public void onAnimationCancel(Animator animator) {
                this.mWasCancelled = true;
            }
        });
        this.mAppearAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAppearAnimation$1(ValueAnimator valueAnimator) {
        this.mAppearAnimationFraction = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        updateAppearAnimationAlpha();
        updateAppearRect();
        invalidate();
    }

    public final int getCujType(boolean z) {
        return this.mIsHeadsUpAnimation ? z ? 12 : 13 : z ? 14 : 15;
    }

    public final void cancelAppearAnimation() {
        ValueAnimator valueAnimator = this.mAppearAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mAppearAnimator = null;
        }
    }

    public void cancelAppearDrawing() {
        cancelAppearAnimation();
        enableAppearDrawing(false);
    }

    public final void updateAppearRect() {
        float interpolation = this.mCurrentAppearInterpolator.getInterpolation(this.mAppearAnimationFraction);
        this.mAppearAnimationTranslation = (1.0f - interpolation) * this.mAnimationTranslationY;
        int actualHeight = getActualHeight();
        float f = (float) actualHeight;
        float f2 = interpolation * f;
        if (this.mTargetPoint != null) {
            int width = getWidth();
            float f3 = 1.0f - this.mAppearAnimationFraction;
            Point point = this.mTargetPoint;
            int i = point.x;
            float f4 = this.mAnimationTranslationY;
            int i2 = point.y;
            setOutlineRect(((float) i) * f3, f4 + ((f4 - ((float) i2)) * f3), ((float) width) - (((float) (width - i)) * f3), f - (((float) (actualHeight - i2)) * f3));
            return;
        }
        setOutlineRect(0.0f, this.mAppearAnimationTranslation, (float) getWidth(), f2 + this.mAppearAnimationTranslation);
    }

    public final float getInterpolatedAppearAnimationFraction() {
        float f = this.mAppearAnimationFraction;
        if (f >= 0.0f) {
            return this.mCurrentAppearInterpolator.getInterpolation(f);
        }
        return 1.0f;
    }

    public final void updateAppearAnimationAlpha() {
        setContentAlpha(Interpolators.ALPHA_IN.getInterpolation((MathUtils.constrain(this.mAppearAnimationFraction, 0.4f, 1.0f) - 0.4f) / 0.6f));
    }

    public final void setContentAlpha(float f) {
        View contentView = getContentView();
        if (contentView.hasOverlappingRendering()) {
            contentView.setLayerType((f == 0.0f || f == 1.0f) ? 0 : 2, (Paint) null);
        }
        contentView.setAlpha(f);
        if (f == 1.0f) {
            resetAllContentAlphas();
        }
    }

    public void applyRoundness() {
        super.applyRoundness();
        applyBackgroundRoundness(getCurrentBackgroundRadiusTop(), getCurrentBackgroundRadiusBottom());
    }

    public float getCurrentBackgroundRadiusTop() {
        return MathUtils.lerp(0.0f, super.getCurrentBackgroundRadiusTop(), getInterpolatedAppearAnimationFraction());
    }

    public float getCurrentBackgroundRadiusBottom() {
        return MathUtils.lerp(0.0f, super.getCurrentBackgroundRadiusBottom(), getInterpolatedAppearAnimationFraction());
    }

    public final void applyBackgroundRoundness(float f, float f2) {
        this.mBackgroundNormal.setRadius(f, f2);
    }

    public void setBackgroundTop(int i) {
        this.mBackgroundNormal.setBackgroundTop(i);
    }

    public int calculateBgColor() {
        return calculateBgColor(true, true);
    }

    public boolean childNeedsClipping(View view) {
        if (!(view instanceof NotificationBackgroundView) || !isClippingNeeded()) {
            return super.childNeedsClipping(view);
        }
        return true;
    }

    public final int calculateBgColor(boolean z, boolean z2) {
        int i;
        if (z2 && this.mOverrideTint != 0) {
            return NotificationUtils.interpolateColors(calculateBgColor(z, false), this.mOverrideTint, this.mOverrideAmount);
        }
        if (!z || (i = this.mBgTint) == 0) {
            return this.mNormalColor;
        }
        return i;
    }

    public final int getRippleColor() {
        if (this.mBgTint != 0) {
            return this.mTintedRippleColor;
        }
        return this.mNormalRippleColor;
    }

    public final void enableAppearDrawing(boolean z) {
        if (z != this.mDrawingAppearAnimation) {
            this.mDrawingAppearAnimation = z;
            if (!z) {
                setContentAlpha(1.0f);
                this.mAppearAnimationFraction = -1.0f;
                setOutlineRect((RectF) null);
            }
            invalidate();
        }
    }

    public boolean isDrawingAppearAnimation() {
        return this.mDrawingAppearAnimation;
    }

    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawingAppearAnimation) {
            canvas.save();
            canvas.translate(0.0f, this.mAppearAnimationTranslation);
        }
        super.dispatchDraw(canvas);
        if (this.mDrawingAppearAnimation) {
            canvas.restore();
        }
    }

    public void setOnActivatedListener(OnActivatedListener onActivatedListener) {
        this.mOnActivatedListener = onActivatedListener;
    }

    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
        boolean z = this.mShadowHidden;
        boolean z2 = f == 0.0f;
        this.mShadowHidden = z2;
        if (!z2 || !z) {
            this.mFakeShadow.setFakeShadowTranslationZ(f * (getTranslationZ() + 0.1f), f2, i, i2);
        }
    }

    public int getBackgroundColorWithoutTint() {
        return calculateBgColor(false, false);
    }

    public int getCurrentBackgroundTint() {
        return this.mCurrentBackgroundTint;
    }

    public int getHeadsUpHeightWithoutHeader() {
        return getHeight();
    }

    public void dismiss(boolean z) {
        this.mDismissed = true;
        this.mRefocusOnDismiss = z;
    }

    public void unDismiss() {
        this.mDismissed = false;
    }

    public boolean isDismissed() {
        return this.mDismissed;
    }

    public boolean shouldRefocusOnDismiss() {
        return this.mRefocusOnDismiss || isAccessibilityFocused();
    }

    /* access modifiers changed from: package-private */
    public void setTouchHandler(Gefingerpoken gefingerpoken) {
        this.mTouchHandler = gefingerpoken;
    }

    public void setAccessibilityManager(AccessibilityManager accessibilityManager) {
        this.mAccessibilityManager = accessibilityManager;
    }
}

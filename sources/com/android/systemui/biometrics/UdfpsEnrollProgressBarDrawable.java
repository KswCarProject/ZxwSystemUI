package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;

public class UdfpsEnrollProgressBarDrawable extends Drawable {
    public static final Interpolator DEACCEL = new DecelerateInterpolator();
    public static final VibrationAttributes FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES = VibrationAttributes.createForUsage(66);
    public static final VibrationAttributes HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(50);
    public static final VibrationEffect SUCCESS_VIBRATION_EFFECT = VibrationEffect.get(0);
    public static final VibrationEffect VIBRATE_EFFECT_ERROR = VibrationEffect.createWaveform(new long[]{0, 5, 55, 60}, -1);
    public boolean mAfterFirstTouch;
    public ValueAnimator mBackgroundColorAnimator;
    public final ValueAnimator.AnimatorUpdateListener mBackgroundColorUpdateListener;
    public final Paint mBackgroundPaint;
    public ValueAnimator mCheckmarkAnimator;
    public final Drawable mCheckmarkDrawable;
    public final Interpolator mCheckmarkInterpolator;
    public float mCheckmarkScale = 0.0f;
    public final ValueAnimator.AnimatorUpdateListener mCheckmarkUpdateListener;
    public boolean mComplete = false;
    public final Context mContext;
    public ValueAnimator mFillColorAnimator;
    public final ValueAnimator.AnimatorUpdateListener mFillColorUpdateListener;
    public final Paint mFillPaint;
    public final int mHelpColor;
    public final boolean mIsAccessibilityEnabled;
    public final int mOnFirstBucketFailedColor;
    public float mProgress = 0.0f;
    public ValueAnimator mProgressAnimator;
    public final int mProgressColor;
    public final ValueAnimator.AnimatorUpdateListener mProgressUpdateListener;
    public int mRemainingSteps = 0;
    public boolean mShowingHelp = false;
    public final float mStrokeWidthPx;
    public int mTotalSteps = 0;
    public final Vibrator mVibrator;

    public int getOpacity() {
        return 0;
    }

    public void setAlpha(int i) {
    }

    public void setColorFilter(ColorFilter colorFilter) {
    }

    public UdfpsEnrollProgressBarDrawable(Context context) {
        this.mContext = context;
        float dpToPixels = Utils.dpToPixels(context, 12.0f);
        this.mStrokeWidthPx = dpToPixels;
        int color = context.getColor(R$color.udfps_enroll_progress);
        this.mProgressColor = color;
        boolean isTouchExplorationEnabled = ((AccessibilityManager) context.getSystemService(AccessibilityManager.class)).isTouchExplorationEnabled();
        this.mIsAccessibilityEnabled = isTouchExplorationEnabled;
        if (!isTouchExplorationEnabled) {
            this.mHelpColor = context.getColor(R$color.udfps_enroll_progress_help);
            this.mOnFirstBucketFailedColor = context.getColor(R$color.udfps_moving_target_fill_error);
        } else {
            int color2 = context.getColor(R$color.udfps_enroll_progress_help_with_talkback);
            this.mHelpColor = color2;
            this.mOnFirstBucketFailedColor = color2;
        }
        Drawable drawable = context.getDrawable(R$drawable.udfps_enroll_checkmark);
        this.mCheckmarkDrawable = drawable;
        drawable.mutate();
        this.mCheckmarkInterpolator = new OvershootInterpolator();
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setStrokeWidth(dpToPixels);
        paint.setColor(context.getColor(R$color.udfps_moving_target_fill));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        Paint paint2 = new Paint();
        this.mFillPaint = paint2;
        paint2.setStrokeWidth(dpToPixels);
        paint2.setColor(color);
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        this.mVibrator = (Vibrator) context.getSystemService(Vibrator.class);
        this.mProgressUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda0(this);
        this.mFillColorUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda1(this);
        this.mCheckmarkUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda2(this);
        this.mBackgroundColorUpdateListener = new UdfpsEnrollProgressBarDrawable$$ExternalSyntheticLambda3(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        this.mProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(ValueAnimator valueAnimator) {
        this.mFillPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(ValueAnimator valueAnimator) {
        this.mCheckmarkScale = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(ValueAnimator valueAnimator) {
        this.mBackgroundPaint.setColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
        invalidateSelf();
    }

    public void onEnrollmentProgress(int i, int i2) {
        this.mAfterFirstTouch = true;
        updateState(i, i2, false);
    }

    public void onEnrollmentHelp(int i, int i2) {
        updateState(i, i2, true);
    }

    public void onLastStepAcquired() {
        updateState(0, this.mTotalSteps, false);
    }

    public final void updateState(int i, int i2, boolean z) {
        updateProgress(i, i2, z);
        updateFillColor(z);
    }

    public final void updateProgress(int i, int i2, boolean z) {
        if (this.mRemainingSteps != i || this.mTotalSteps != i2) {
            if (this.mShowingHelp) {
                Vibrator vibrator = this.mVibrator;
                if (vibrator != null && this.mIsAccessibilityEnabled) {
                    int myUid = Process.myUid();
                    String opPackageName = this.mContext.getOpPackageName();
                    VibrationEffect vibrationEffect = VIBRATE_EFFECT_ERROR;
                    vibrator.vibrate(myUid, opPackageName, vibrationEffect, getClass().getSimpleName() + "::onEnrollmentHelp", FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES);
                }
            } else {
                Vibrator vibrator2 = this.mVibrator;
                if (vibrator2 != null) {
                    if (i == -1 && this.mIsAccessibilityEnabled) {
                        int myUid2 = Process.myUid();
                        String opPackageName2 = this.mContext.getOpPackageName();
                        VibrationEffect vibrationEffect2 = VIBRATE_EFFECT_ERROR;
                        vibrator2.vibrate(myUid2, opPackageName2, vibrationEffect2, getClass().getSimpleName() + "::onFirstTouchError", FINGERPRINT_ENROLLING_SONFICATION_ATTRIBUTES);
                    } else if (i != -1 && !this.mIsAccessibilityEnabled) {
                        int myUid3 = Process.myUid();
                        String opPackageName3 = this.mContext.getOpPackageName();
                        VibrationEffect vibrationEffect3 = SUCCESS_VIBRATION_EFFECT;
                        vibrator2.vibrate(myUid3, opPackageName3, vibrationEffect3, getClass().getSimpleName() + "::OnEnrollmentProgress", HARDWARE_FEEDBACK_VIBRATION_ATTRIBUTES);
                    }
                }
            }
            this.mRemainingSteps = i;
            this.mTotalSteps = i2;
            int max = Math.max(0, i2 - i);
            boolean z2 = this.mAfterFirstTouch;
            if (z2) {
                max++;
            }
            float min = Math.min(1.0f, ((float) max) / ((float) (z2 ? this.mTotalSteps + 1 : this.mTotalSteps)));
            ValueAnimator valueAnimator = this.mProgressAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mProgressAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mProgress, min});
            this.mProgressAnimator = ofFloat;
            ofFloat.setDuration(400);
            this.mProgressAnimator.addUpdateListener(this.mProgressUpdateListener);
            this.mProgressAnimator.start();
            if (i == 0) {
                startCompletionAnimation();
            } else if (i > 0) {
                rollBackCompletionAnimation();
            }
        }
    }

    public final void animateBackgroundColor() {
        ValueAnimator valueAnimator = this.mBackgroundColorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mBackgroundColorAnimator.end();
        }
        ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mBackgroundPaint.getColor(), this.mOnFirstBucketFailedColor});
        this.mBackgroundColorAnimator = ofArgb;
        ofArgb.setDuration(350);
        this.mBackgroundColorAnimator.setRepeatCount(1);
        this.mBackgroundColorAnimator.setRepeatMode(2);
        this.mBackgroundColorAnimator.setInterpolator(DEACCEL);
        this.mBackgroundColorAnimator.addUpdateListener(this.mBackgroundColorUpdateListener);
        this.mBackgroundColorAnimator.start();
    }

    public final void updateFillColor(boolean z) {
        if (this.mAfterFirstTouch || !z) {
            ValueAnimator valueAnimator = this.mFillColorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mFillColorAnimator.end();
            }
            ValueAnimator ofArgb = ValueAnimator.ofArgb(new int[]{this.mFillPaint.getColor(), z ? this.mHelpColor : this.mProgressColor});
            this.mFillColorAnimator = ofArgb;
            ofArgb.setDuration(350);
            this.mFillColorAnimator.setRepeatCount(1);
            this.mFillColorAnimator.setRepeatMode(2);
            this.mFillColorAnimator.setInterpolator(DEACCEL);
            this.mFillColorAnimator.addUpdateListener(this.mFillColorUpdateListener);
            this.mFillColorAnimator.start();
            return;
        }
        animateBackgroundColor();
    }

    public final void startCompletionAnimation() {
        if (!this.mComplete) {
            this.mComplete = true;
            ValueAnimator valueAnimator = this.mCheckmarkAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mCheckmarkAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mCheckmarkScale, 1.0f});
            this.mCheckmarkAnimator = ofFloat;
            ofFloat.setStartDelay(200);
            this.mCheckmarkAnimator.setDuration(300);
            this.mCheckmarkAnimator.setInterpolator(this.mCheckmarkInterpolator);
            this.mCheckmarkAnimator.addUpdateListener(this.mCheckmarkUpdateListener);
            this.mCheckmarkAnimator.start();
        }
    }

    public final void rollBackCompletionAnimation() {
        if (this.mComplete) {
            this.mComplete = false;
            ValueAnimator valueAnimator = this.mCheckmarkAnimator;
            long round = (long) Math.round((valueAnimator != null ? valueAnimator.getAnimatedFraction() : 0.0f) * 200.0f);
            ValueAnimator valueAnimator2 = this.mCheckmarkAnimator;
            if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                this.mCheckmarkAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mCheckmarkScale, 0.0f});
            this.mCheckmarkAnimator = ofFloat;
            ofFloat.setDuration(round);
            this.mCheckmarkAnimator.addUpdateListener(this.mCheckmarkUpdateListener);
            this.mCheckmarkAnimator.start();
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90.0f, (float) getBounds().centerX(), (float) getBounds().centerY());
        float f = this.mStrokeWidthPx / 2.0f;
        if (this.mProgress < 1.0f) {
            canvas.drawArc(f, f, ((float) getBounds().right) - f, ((float) getBounds().bottom) - f, 0.0f, 360.0f, false, this.mBackgroundPaint);
        }
        if (this.mProgress > 0.0f) {
            canvas.drawArc(f, f, ((float) getBounds().right) - f, ((float) getBounds().bottom) - f, 0.0f, this.mProgress * 360.0f, false, this.mFillPaint);
        }
        canvas.restore();
        if (this.mCheckmarkScale > 0.0f) {
            float sqrt = ((float) Math.sqrt(2.0d)) / 2.0f;
            float width = ((((float) getBounds().width()) - this.mStrokeWidthPx) / 2.0f) * sqrt;
            float height = ((((float) getBounds().height()) - this.mStrokeWidthPx) / 2.0f) * sqrt;
            float centerX = ((float) getBounds().centerX()) + width;
            float centerY = ((float) getBounds().centerY()) + height;
            float intrinsicWidth = (((float) this.mCheckmarkDrawable.getIntrinsicWidth()) / 2.0f) * this.mCheckmarkScale;
            float intrinsicHeight = (((float) this.mCheckmarkDrawable.getIntrinsicHeight()) / 2.0f) * this.mCheckmarkScale;
            this.mCheckmarkDrawable.setBounds(Math.round(centerX - intrinsicWidth), Math.round(centerY - intrinsicHeight), Math.round(centerX + intrinsicWidth), Math.round(centerY + intrinsicHeight));
            this.mCheckmarkDrawable.draw(canvas);
        }
    }
}

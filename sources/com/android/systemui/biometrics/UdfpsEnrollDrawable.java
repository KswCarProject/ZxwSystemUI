package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;

public class UdfpsEnrollDrawable extends UdfpsDrawable {
    public final Paint mBlueFill;
    public float mCurrentScale = 1.0f;
    public float mCurrentX;
    public float mCurrentY;
    public UdfpsEnrollHelper mEnrollHelper;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final Drawable mMovingTargetFpIcon;
    public final Paint mSensorOutlinePaint;
    public RectF mSensorRect;
    public boolean mShouldShowEdgeHint = false;
    public boolean mShouldShowTipHint = false;
    public final Animator.AnimatorListener mTargetAnimListener;
    public AnimatorSet mTargetAnimatorSet;

    public UdfpsEnrollDrawable(Context context) {
        super(context);
        Paint paint = new Paint(0);
        this.mSensorOutlinePaint = paint;
        paint.setAntiAlias(true);
        int i = R$color.udfps_moving_target_fill;
        paint.setColor(context.getColor(i));
        paint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint(0);
        this.mBlueFill = paint2;
        paint2.setAntiAlias(true);
        paint2.setColor(context.getColor(i));
        paint2.setStyle(Paint.Style.FILL);
        Drawable drawable = context.getResources().getDrawable(R$drawable.ic_kg_fingerprint, (Resources.Theme) null);
        this.mMovingTargetFpIcon = drawable;
        int i2 = R$color.udfps_enroll_icon;
        drawable.setTint(context.getColor(i2));
        drawable.mutate();
        getFingerprintDrawable().setTint(context.getColor(i2));
        this.mTargetAnimListener = new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                UdfpsEnrollDrawable.this.updateTipHintVisibility();
            }
        };
    }

    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mEnrollHelper = udfpsEnrollHelper;
    }

    public void onSensorRectUpdated(RectF rectF) {
        super.onSensorRectUpdated(rectF);
        this.mSensorRect = rectF;
    }

    public void updateFingerprintIconBounds(Rect rect) {
        super.updateFingerprintIconBounds(rect);
        this.mMovingTargetFpIcon.setBounds(rect);
        invalidateSelf();
    }

    public void onEnrollmentProgress(int i, int i2) {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        if (udfpsEnrollHelper != null) {
            if (!udfpsEnrollHelper.isCenterEnrollmentStage()) {
                AnimatorSet animatorSet = this.mTargetAnimatorSet;
                if (animatorSet != null && animatorSet.isRunning()) {
                    this.mTargetAnimatorSet.end();
                }
                PointF nextGuidedEnrollmentPoint = this.mEnrollHelper.getNextGuidedEnrollmentPoint();
                float f = this.mCurrentX;
                float f2 = nextGuidedEnrollmentPoint.x;
                if (f == f2 && this.mCurrentY == nextGuidedEnrollmentPoint.y) {
                    updateTipHintVisibility();
                } else {
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f, f2});
                    ofFloat.addUpdateListener(new UdfpsEnrollDrawable$$ExternalSyntheticLambda0(this));
                    ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{this.mCurrentY, nextGuidedEnrollmentPoint.y});
                    ofFloat2.addUpdateListener(new UdfpsEnrollDrawable$$ExternalSyntheticLambda1(this));
                    long j = (nextGuidedEnrollmentPoint.x > 0.0f ? 1 : (nextGuidedEnrollmentPoint.x == 0.0f ? 0 : -1)) == 0 && (nextGuidedEnrollmentPoint.y > 0.0f ? 1 : (nextGuidedEnrollmentPoint.y == 0.0f ? 0 : -1)) == 0 ? 600 : 800;
                    ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{0.0f, 3.1415927f});
                    ofFloat3.setDuration(j);
                    ofFloat3.addUpdateListener(new UdfpsEnrollDrawable$$ExternalSyntheticLambda2(this));
                    AnimatorSet animatorSet2 = new AnimatorSet();
                    this.mTargetAnimatorSet = animatorSet2;
                    animatorSet2.setInterpolator(new AccelerateDecelerateInterpolator());
                    this.mTargetAnimatorSet.setDuration(j);
                    this.mTargetAnimatorSet.addListener(this.mTargetAnimListener);
                    this.mTargetAnimatorSet.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3});
                    this.mTargetAnimatorSet.start();
                }
            } else {
                updateTipHintVisibility();
            }
            updateEdgeHintVisibility();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$0(ValueAnimator valueAnimator) {
        this.mCurrentX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$1(ValueAnimator valueAnimator) {
        this.mCurrentY = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        invalidateSelf();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$2(ValueAnimator valueAnimator) {
        this.mCurrentScale = (((float) Math.sin((double) ((Float) valueAnimator.getAnimatedValue()).floatValue())) * 0.25f) + 1.0f;
        invalidateSelf();
    }

    public final void updateTipHintVisibility() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        boolean z = udfpsEnrollHelper != null && udfpsEnrollHelper.isTipEnrollmentStage();
        if (this.mShouldShowTipHint != z) {
            this.mShouldShowTipHint = z;
        }
    }

    public final void updateEdgeHintVisibility() {
        UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
        boolean z = udfpsEnrollHelper != null && udfpsEnrollHelper.isEdgeEnrollmentStage();
        if (this.mShouldShowEdgeHint != z) {
            this.mShouldShowEdgeHint = z;
        }
    }

    public void draw(Canvas canvas) {
        if (!isIlluminationShowing()) {
            UdfpsEnrollHelper udfpsEnrollHelper = this.mEnrollHelper;
            if (udfpsEnrollHelper == null || udfpsEnrollHelper.isCenterEnrollmentStage()) {
                RectF rectF = this.mSensorRect;
                if (rectF != null) {
                    canvas.drawOval(rectF, this.mSensorOutlinePaint);
                }
                getFingerprintDrawable().draw(canvas);
                getFingerprintDrawable().setAlpha(getAlpha());
                this.mSensorOutlinePaint.setAlpha(getAlpha());
                return;
            }
            canvas.save();
            canvas.translate(this.mCurrentX, this.mCurrentY);
            RectF rectF2 = this.mSensorRect;
            if (rectF2 != null) {
                float f = this.mCurrentScale;
                canvas.scale(f, f, rectF2.centerX(), this.mSensorRect.centerY());
                canvas.drawOval(this.mSensorRect, this.mBlueFill);
            }
            this.mMovingTargetFpIcon.draw(canvas);
            canvas.restore();
        }
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mSensorOutlinePaint.setAlpha(i);
        this.mBlueFill.setAlpha(i);
        this.mMovingTargetFpIcon.setAlpha(i);
        invalidateSelf();
    }
}

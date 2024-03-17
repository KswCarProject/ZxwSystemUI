package com.android.systemui.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;

public class AssistDisclosure {
    public final Context mContext;
    public final Handler mHandler;
    public Runnable mShowRunnable = new Runnable() {
        public void run() {
            AssistDisclosure.this.show();
        }
    };
    public AssistDisclosureView mView;
    public boolean mViewAdded;
    public final WindowManager mWm;

    public AssistDisclosure(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
        this.mWm = (WindowManager) context.getSystemService(WindowManager.class);
    }

    public void postShow() {
        this.mHandler.removeCallbacks(this.mShowRunnable);
        this.mHandler.post(this.mShowRunnable);
    }

    public final void show() {
        if (this.mView == null) {
            this.mView = new AssistDisclosureView(this.mContext);
        }
        if (!this.mViewAdded) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2015, 525576, -3);
            layoutParams.setTitle("AssistDisclosure");
            layoutParams.setFitInsetsTypes(0);
            this.mWm.addView(this.mView, layoutParams);
            this.mViewAdded = true;
        }
    }

    public final void hide() {
        if (this.mViewAdded) {
            this.mWm.removeView(this.mView);
            this.mViewAdded = false;
        }
    }

    public class AssistDisclosureView extends View implements ValueAnimator.AnimatorUpdateListener {
        public int mAlpha = 0;
        public final ValueAnimator mAlphaInAnimator;
        public final ValueAnimator mAlphaOutAnimator;
        public final AnimatorSet mAnimator;
        public final Paint mPaint;
        public final Paint mShadowPaint;
        public float mShadowThickness;
        public float mThickness;

        public AssistDisclosureView(Context context) {
            super(context);
            Paint paint = new Paint();
            this.mPaint = paint;
            Paint paint2 = new Paint();
            this.mShadowPaint = paint2;
            ValueAnimator duration = ValueAnimator.ofInt(new int[]{0, 222}).setDuration(400);
            this.mAlphaInAnimator = duration;
            duration.addUpdateListener(this);
            Interpolator interpolator = Interpolators.CUSTOM_40_40;
            duration.setInterpolator(interpolator);
            ValueAnimator duration2 = ValueAnimator.ofInt(new int[]{222, 0}).setDuration(300);
            this.mAlphaOutAnimator = duration2;
            duration2.addUpdateListener(this);
            duration2.setInterpolator(interpolator);
            AnimatorSet animatorSet = new AnimatorSet();
            this.mAnimator = animatorSet;
            animatorSet.play(duration).before(duration2);
            animatorSet.addListener(new AnimatorListenerAdapter(AssistDisclosure.this) {
                public boolean mCancelled;

                public void onAnimationStart(Animator animator) {
                    this.mCancelled = false;
                }

                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        AssistDisclosure.this.hide();
                    }
                }
            });
            PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
            paint.setColor(-1);
            paint.setXfermode(porterDuffXfermode);
            paint2.setColor(-12303292);
            paint2.setXfermode(porterDuffXfermode);
            this.mThickness = getResources().getDimension(R$dimen.assist_disclosure_thickness);
            this.mShadowThickness = getResources().getDimension(R$dimen.assist_disclosure_shadow_thickness);
        }

        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            startAnimation();
            sendAccessibilityEvent(16777216);
        }

        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            this.mAnimator.cancel();
            this.mAlpha = 0;
        }

        public final void startAnimation() {
            this.mAnimator.cancel();
            this.mAnimator.start();
        }

        public void onDraw(Canvas canvas) {
            this.mPaint.setAlpha(this.mAlpha);
            this.mShadowPaint.setAlpha(this.mAlpha / 4);
            drawGeometry(canvas, this.mShadowPaint, this.mShadowThickness);
            drawGeometry(canvas, this.mPaint, 0.0f);
        }

        public final void drawGeometry(Canvas canvas, Paint paint, float f) {
            int width = getWidth();
            int height = getHeight();
            float f2 = this.mThickness;
            float f3 = (float) height;
            float f4 = f3 - f2;
            float f5 = (float) width;
            Canvas canvas2 = canvas;
            Paint paint2 = paint;
            float f6 = f;
            drawBeam(canvas2, 0.0f, f4, f5, f3, paint2, f6);
            float f7 = f4;
            drawBeam(canvas2, 0.0f, 0.0f, f2, f7, paint2, f6);
            float f8 = f5 - f2;
            drawBeam(canvas2, f8, 0.0f, f5, f7, paint2, f6);
            drawBeam(canvas2, f2, 0.0f, f8, f2, paint2, f6);
        }

        public final void drawBeam(Canvas canvas, float f, float f2, float f3, float f4, Paint paint, float f5) {
            canvas.drawRect(f - f5, f2 - f5, f3 + f5, f4 + f5, paint);
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            ValueAnimator valueAnimator2 = this.mAlphaOutAnimator;
            if (valueAnimator == valueAnimator2) {
                this.mAlpha = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
            } else {
                ValueAnimator valueAnimator3 = this.mAlphaInAnimator;
                if (valueAnimator == valueAnimator3) {
                    this.mAlpha = ((Integer) valueAnimator3.getAnimatedValue()).intValue();
                }
            }
            invalidate();
        }
    }
}

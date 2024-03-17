package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;

public class DividerHandleView extends View {
    public static final Property<DividerHandleView, Integer> HEIGHT_PROPERTY;
    public static final Property<DividerHandleView, Integer> WIDTH_PROPERTY;
    public AnimatorSet mAnimator;
    public int mCurrentHeight;
    public int mCurrentWidth;
    public final int mHeight;
    public final Paint mPaint;
    public boolean mTouching;
    public final int mTouchingHeight;
    public final int mTouchingWidth;
    public final int mWidth;

    public boolean hasOverlappingRendering() {
        return false;
    }

    static {
        Class<Integer> cls = Integer.class;
        WIDTH_PROPERTY = new Property<DividerHandleView, Integer>(cls, "width") {
            public Integer get(DividerHandleView dividerHandleView) {
                return Integer.valueOf(dividerHandleView.mCurrentWidth);
            }

            public void set(DividerHandleView dividerHandleView, Integer num) {
                dividerHandleView.mCurrentWidth = num.intValue();
                dividerHandleView.invalidate();
            }
        };
        HEIGHT_PROPERTY = new Property<DividerHandleView, Integer>(cls, "height") {
            public Integer get(DividerHandleView dividerHandleView) {
                return Integer.valueOf(dividerHandleView.mCurrentHeight);
            }

            public void set(DividerHandleView dividerHandleView, Integer num) {
                dividerHandleView.mCurrentHeight = num.intValue();
                dividerHandleView.invalidate();
            }
        };
    }

    public DividerHandleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(getResources().getColor(R.color.docked_divider_handle, (Resources.Theme) null));
        paint.setAntiAlias(true);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.split_divider_handle_width);
        this.mWidth = dimensionPixelSize;
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.split_divider_handle_height);
        this.mHeight = dimensionPixelSize2;
        this.mCurrentWidth = dimensionPixelSize;
        this.mCurrentHeight = dimensionPixelSize2;
        this.mTouchingWidth = dimensionPixelSize > dimensionPixelSize2 ? dimensionPixelSize / 2 : dimensionPixelSize;
        this.mTouchingHeight = dimensionPixelSize2 > dimensionPixelSize ? dimensionPixelSize2 / 2 : dimensionPixelSize2;
    }

    public void setTouching(boolean z, boolean z2) {
        if (z != this.mTouching) {
            AnimatorSet animatorSet = this.mAnimator;
            if (animatorSet != null) {
                animatorSet.cancel();
                this.mAnimator = null;
            }
            if (!z2) {
                if (z) {
                    this.mCurrentWidth = this.mTouchingWidth;
                    this.mCurrentHeight = this.mTouchingHeight;
                } else {
                    this.mCurrentWidth = this.mWidth;
                    this.mCurrentHeight = this.mHeight;
                }
                invalidate();
            } else {
                animateToTarget(z ? this.mTouchingWidth : this.mWidth, z ? this.mTouchingHeight : this.mHeight, z);
            }
            this.mTouching = z;
        }
    }

    public final void animateToTarget(int i, int i2, boolean z) {
        Interpolator interpolator;
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, WIDTH_PROPERTY, new int[]{this.mCurrentWidth, i});
        ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this, HEIGHT_PROPERTY, new int[]{this.mCurrentHeight, i2});
        AnimatorSet animatorSet = new AnimatorSet();
        this.mAnimator = animatorSet;
        animatorSet.playTogether(new Animator[]{ofInt, ofInt2});
        this.mAnimator.setDuration(z ? 150 : 200);
        AnimatorSet animatorSet2 = this.mAnimator;
        if (z) {
            interpolator = Interpolators.TOUCH_RESPONSE;
        } else {
            interpolator = Interpolators.FAST_OUT_SLOW_IN;
        }
        animatorSet2.setInterpolator(interpolator);
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                DividerHandleView.this.mAnimator = null;
            }
        });
        this.mAnimator.start();
    }

    public void onDraw(Canvas canvas) {
        int width = (getWidth() / 2) - (this.mCurrentWidth / 2);
        int i = this.mCurrentHeight;
        int height = (getHeight() / 2) - (i / 2);
        float min = (float) (Math.min(this.mCurrentWidth, i) / 2);
        canvas.drawRoundRect((float) width, (float) height, (float) (width + this.mCurrentWidth), (float) (height + this.mCurrentHeight), min, min, this.mPaint);
    }
}

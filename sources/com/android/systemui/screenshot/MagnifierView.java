package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.R$styleable;
import com.android.systemui.screenshot.CropView;

public class MagnifierView extends View implements CropView.CropInteractionListener {
    public final int mBorderColor;
    public final float mBorderPx;
    public Path mCheckerboard;
    public float mCheckerboardBoxSize;
    public Paint mCheckerboardPaint;
    public CropView.CropBoundary mCropBoundary;
    public Drawable mDrawable;
    public final Paint mHandlePaint;
    public Path mInnerCircle;
    public float mLastCenter;
    public float mLastCropPosition;
    public Path mOuterCircle;
    public final Paint mShadePaint;
    public ViewPropertyAnimator mTranslationAnimator;
    public final Animator.AnimatorListener mTranslationAnimatorListener;

    public MagnifierView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MagnifierView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCheckerboardBoxSize = 40.0f;
        this.mLastCenter = 0.5f;
        this.mTranslationAnimatorListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                MagnifierView.this.mTranslationAnimator = null;
            }

            public void onAnimationEnd(Animator animator) {
                MagnifierView.this.mTranslationAnimator = null;
            }
        };
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.MagnifierView, 0, 0);
        Paint paint = new Paint();
        this.mShadePaint = paint;
        paint.setColor(ColorUtils.setAlphaComponent(obtainStyledAttributes.getColor(R$styleable.MagnifierView_scrimColor, 0), obtainStyledAttributes.getInteger(R$styleable.MagnifierView_scrimAlpha, 255)));
        Paint paint2 = new Paint();
        this.mHandlePaint = paint2;
        paint2.setColor(obtainStyledAttributes.getColor(R$styleable.MagnifierView_handleColor, -16777216));
        paint2.setStrokeWidth((float) obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagnifierView_handleThickness, 20));
        this.mBorderPx = (float) obtainStyledAttributes.getDimensionPixelSize(R$styleable.MagnifierView_borderThickness, 0);
        this.mBorderColor = obtainStyledAttributes.getColor(R$styleable.MagnifierView_borderColor, -1);
        obtainStyledAttributes.recycle();
        Paint paint3 = new Paint();
        this.mCheckerboardPaint = paint3;
        paint3.setColor(-7829368);
    }

    public void setDrawable(Drawable drawable, int i, int i2) {
        this.mDrawable = drawable;
        drawable.setBounds(0, 0, i, i2);
        invalidate();
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Path path = new Path();
        this.mOuterCircle = path;
        float width = (float) (getWidth() / 2);
        path.addCircle(width, width, width, Path.Direction.CW);
        Path path2 = new Path();
        this.mInnerCircle = path2;
        path2.addCircle(width, width, width - this.mBorderPx, Path.Direction.CW);
        this.mCheckerboard = generateCheckerboard();
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipPath(this.mOuterCircle);
        canvas.drawColor(this.mBorderColor);
        canvas.clipPath(this.mInnerCircle);
        canvas.drawPath(this.mCheckerboard, this.mCheckerboardPaint);
        if (this.mDrawable != null) {
            canvas.save();
            canvas.translate((((float) (-this.mDrawable.getBounds().width())) * this.mLastCenter) + ((float) (getWidth() / 2)), (((float) (-this.mDrawable.getBounds().height())) * this.mLastCropPosition) + ((float) (getHeight() / 2)));
            this.mDrawable.draw(canvas);
            canvas.restore();
        }
        Rect rect = new Rect(0, 0, getWidth(), getHeight() / 2);
        if (this.mCropBoundary == CropView.CropBoundary.BOTTOM) {
            rect.offset(0, getHeight() / 2);
        }
        canvas.drawRect(rect, this.mShadePaint);
        canvas.drawLine(0.0f, (float) (getHeight() / 2), (float) getWidth(), (float) (getHeight() / 2), this.mHandlePaint);
    }

    public void onCropDragStarted(CropView.CropBoundary cropBoundary, float f, int i, float f2, float f3) {
        float f4;
        this.mCropBoundary = cropBoundary;
        this.mLastCenter = f2;
        if (f3 > ((float) (getParentWidth() / 2))) {
            f4 = 0.0f;
        } else {
            f4 = (float) (getParentWidth() - getWidth());
        }
        this.mLastCropPosition = f;
        setTranslationY((float) (i - (getHeight() / 2)));
        setPivotX((float) (getWidth() / 2));
        setPivotY((float) (getHeight() / 2));
        setScaleX(0.2f);
        setScaleY(0.2f);
        setAlpha(0.0f);
        setTranslationX((float) ((getParentWidth() - getWidth()) / 2));
        setVisibility(0);
        ViewPropertyAnimator scaleY = animate().alpha(1.0f).translationX(f4).scaleX(1.0f).scaleY(1.0f);
        this.mTranslationAnimator = scaleY;
        scaleY.setListener(this.mTranslationAnimatorListener);
        this.mTranslationAnimator.start();
    }

    public void onCropDragMoved(CropView.CropBoundary cropBoundary, float f, int i, float f2, float f3) {
        float f4;
        boolean z = true;
        boolean z2 = f3 > ((float) (getParentWidth() / 2));
        if (z2) {
            f4 = 0.0f;
        } else {
            f4 = (float) (getParentWidth() - getWidth());
        }
        boolean z3 = Math.abs(f3 - ((float) (getParentWidth() / 2))) < ((float) getParentWidth()) / 10.0f;
        if (getTranslationX() >= ((float) ((getParentWidth() - getWidth()) / 2))) {
            z = false;
        }
        if (!z3 && z != z2 && this.mTranslationAnimator == null) {
            ViewPropertyAnimator translationX = animate().translationX(f4);
            this.mTranslationAnimator = translationX;
            translationX.setListener(this.mTranslationAnimatorListener);
            this.mTranslationAnimator.start();
        }
        this.mLastCropPosition = f;
        setTranslationY((float) (i - (getHeight() / 2)));
        invalidate();
    }

    public void onCropDragComplete() {
        animate().alpha(0.0f).translationX((float) ((getParentWidth() - getWidth()) / 2)).scaleX(0.2f).scaleY(0.2f).withEndAction(new MagnifierView$$ExternalSyntheticLambda0(this)).start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCropDragComplete$0() {
        setVisibility(4);
    }

    public final Path generateCheckerboard() {
        Path path = new Path();
        int ceil = (int) Math.ceil((double) (((float) getWidth()) / this.mCheckerboardBoxSize));
        int ceil2 = (int) Math.ceil((double) (((float) getHeight()) / this.mCheckerboardBoxSize));
        for (int i = 0; i < ceil2; i++) {
            for (int i2 = i % 2 == 0 ? 0 : 1; i2 < ceil; i2 += 2) {
                float f = this.mCheckerboardBoxSize;
                path.addRect(((float) i2) * f, ((float) i) * f, ((float) (i2 + 1)) * f, ((float) (i + 1)) * f, Path.Direction.CW);
            }
        }
        return path;
    }

    public final int getParentWidth() {
        return ((View) getParent()).getWidth();
    }
}
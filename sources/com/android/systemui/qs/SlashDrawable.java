package com.android.systemui.qs;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.FloatProperty;

public class SlashDrawable extends Drawable {
    public boolean mAnimationEnabled = true;
    public float mCurrentSlashLength;
    public Drawable mDrawable;
    public final Paint mPaint = new Paint(1);
    public final Path mPath = new Path();
    public float mRotation;
    public final FloatProperty mSlashLengthProp = new FloatProperty<SlashDrawable>("slashLength") {
        public void setValue(SlashDrawable slashDrawable, float f) {
            slashDrawable.mCurrentSlashLength = f;
        }

        public Float get(SlashDrawable slashDrawable) {
            return Float.valueOf(slashDrawable.mCurrentSlashLength);
        }
    };
    public final RectF mSlashRect = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
    public boolean mSlashed;
    public ColorStateList mTintList;
    public PorterDuff.Mode mTintMode;

    public int getOpacity() {
        return 255;
    }

    public final float scale(float f, int i) {
        return f * ((float) i);
    }

    public SlashDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public int getIntrinsicHeight() {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return 0;
    }

    public int getIntrinsicWidth() {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return 0;
    }

    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mDrawable.setBounds(rect);
    }

    public void setDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        drawable.setCallback(getCallback());
        this.mDrawable.setBounds(getBounds());
        PorterDuff.Mode mode = this.mTintMode;
        if (mode != null) {
            this.mDrawable.setTintMode(mode);
        }
        ColorStateList colorStateList = this.mTintList;
        if (colorStateList != null) {
            this.mDrawable.setTintList(colorStateList);
        }
        invalidateSelf();
    }

    public void setRotation(float f) {
        if (this.mRotation != f) {
            this.mRotation = f;
            invalidateSelf();
        }
    }

    public void setAnimationEnabled(boolean z) {
        this.mAnimationEnabled = z;
    }

    public void setSlashed(boolean z) {
        if (this.mSlashed != z) {
            this.mSlashed = z;
            float f = 1.1666666f;
            float f2 = z ? 1.1666666f : 0.0f;
            if (z) {
                f = 0.0f;
            }
            if (this.mAnimationEnabled) {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, this.mSlashLengthProp, new float[]{f, f2});
                ofFloat.addUpdateListener(new SlashDrawable$$ExternalSyntheticLambda0(this));
                ofFloat.setDuration(350);
                ofFloat.start();
                return;
            }
            this.mCurrentSlashLength = f2;
            invalidateSelf();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setSlashed$0(ValueAnimator valueAnimator) {
        invalidateSelf();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        Matrix matrix = new Matrix();
        int width = getBounds().width();
        int height = getBounds().height();
        float scale = scale(1.0f, width);
        float scale2 = scale(1.0f, height);
        updateRect(scale(0.40544835f, width), scale(-0.088781714f, height), scale(0.4820516f, width), scale(this.mCurrentSlashLength - 50.543762f, height));
        this.mPath.reset();
        this.mPath.addRoundRect(this.mSlashRect, scale, scale2, Path.Direction.CW);
        float f = (float) (width / 2);
        float f2 = (float) (height / 2);
        matrix.setRotate(this.mRotation - 0.099609375f, f, f2);
        this.mPath.transform(matrix);
        canvas.drawPath(this.mPath, this.mPaint);
        matrix.setRotate((-this.mRotation) - -45.0f, f, f2);
        this.mPath.transform(matrix);
        matrix.setTranslate(this.mSlashRect.width(), 0.0f);
        this.mPath.transform(matrix);
        this.mPath.addRoundRect(this.mSlashRect, ((float) width) * 1.0f, ((float) height) * 1.0f, Path.Direction.CW);
        matrix.setRotate(this.mRotation - 0.099609375f, f, f2);
        this.mPath.transform(matrix);
        canvas.clipOutPath(this.mPath);
        this.mDrawable.draw(canvas);
        canvas.restore();
    }

    public final void updateRect(float f, float f2, float f3, float f4) {
        RectF rectF = this.mSlashRect;
        rectF.left = f;
        rectF.top = f2;
        rectF.right = f3;
        rectF.bottom = f4;
    }

    public void setTint(int i) {
        super.setTint(i);
        this.mDrawable.setTint(i);
        this.mPaint.setColor(i);
    }

    public void setTintList(ColorStateList colorStateList) {
        this.mTintList = colorStateList;
        super.setTintList(colorStateList);
        setDrawableTintList(colorStateList);
        this.mPaint.setColor(colorStateList.getDefaultColor());
        invalidateSelf();
    }

    public void setDrawableTintList(ColorStateList colorStateList) {
        this.mDrawable.setTintList(colorStateList);
    }

    public void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        super.setTintMode(mode);
        this.mDrawable.setTintMode(mode);
    }

    public void setAlpha(int i) {
        this.mDrawable.setAlpha(i);
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
        this.mPaint.setColorFilter(colorFilter);
    }
}

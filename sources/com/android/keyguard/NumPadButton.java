package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NumPadButton extends AlphaOptimizedImageButton {
    public NumPadAnimator mAnimator;
    public int mOrientation;

    public NumPadButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Drawable background = getBackground();
        if (background instanceof GradientDrawable) {
            this.mAnimator = new NumPadAnimator(context, background.mutate(), attributeSet.getStyleAttribute(), getDrawable());
        } else {
            this.mAnimator = null;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.mOrientation = configuration.orientation;
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        if (this.mAnimator == null || this.mOrientation == 2) {
            measuredWidth = (int) (((float) measuredWidth) * 0.66f);
        }
        setMeasuredDimension(getMeasuredWidth(), measuredWidth);
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.onLayout(i4 - i2);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        NumPadAnimator numPadAnimator;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            NumPadAnimator numPadAnimator2 = this.mAnimator;
            if (numPadAnimator2 != null) {
                numPadAnimator2.expand();
            }
        } else if ((actionMasked == 1 || actionMasked == 3) && (numPadAnimator = this.mAnimator) != null) {
            numPadAnimator.contract();
        }
        return super.onTouchEvent(motionEvent);
    }

    public void reloadColors() {
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.reloadColors(getContext());
        }
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16842809});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        ((VectorDrawable) getDrawable()).setTintList(ColorStateList.valueOf(color));
    }
}

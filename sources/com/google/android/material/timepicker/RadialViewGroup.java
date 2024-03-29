package com.google.android.material.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.view.ViewCompat;
import com.google.android.material.R$id;
import com.google.android.material.R$layout;
import com.google.android.material.R$styleable;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RelativeCornerSize;

class RadialViewGroup extends ConstraintLayout {
    public MaterialShapeDrawable background;
    public int radius;
    public final Runnable updateLayoutParametersRunnable;

    public RadialViewGroup(Context context) {
        this(context, (AttributeSet) null);
    }

    public RadialViewGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RadialViewGroup(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        LayoutInflater.from(context).inflate(R$layout.material_radial_view_group, this);
        ViewCompat.setBackground(this, createBackground());
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RadialViewGroup, i, 0);
        this.radius = obtainStyledAttributes.getDimensionPixelSize(R$styleable.RadialViewGroup_materialCircleRadius, 0);
        this.updateLayoutParametersRunnable = new Runnable() {
            public void run() {
                RadialViewGroup.this.updateLayoutParams();
            }
        };
        obtainStyledAttributes.recycle();
    }

    public final Drawable createBackground() {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        this.background = materialShapeDrawable;
        materialShapeDrawable.setCornerSize((CornerSize) new RelativeCornerSize(0.5f));
        this.background.setFillColor(ColorStateList.valueOf(-1));
        return this.background;
    }

    public void setBackgroundColor(int i) {
        this.background.setFillColor(ColorStateList.valueOf(i));
    }

    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
        if (view.getId() == -1) {
            view.setId(ViewCompat.generateViewId());
        }
        updateLayoutParamsAsync();
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        updateLayoutParamsAsync();
    }

    public final void updateLayoutParamsAsync() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.updateLayoutParametersRunnable);
            handler.post(this.updateLayoutParametersRunnable);
        }
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        updateLayoutParams();
    }

    public void updateLayoutParams() {
        int childCount = getChildCount();
        int i = 1;
        for (int i2 = 0; i2 < childCount; i2++) {
            if (shouldSkipView(getChildAt(i2))) {
                i++;
            }
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout) this);
        float f = 0.0f;
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = getChildAt(i3);
            int id = childAt.getId();
            int i4 = R$id.circle_center;
            if (id != i4 && !shouldSkipView(childAt)) {
                constraintSet.constrainCircle(childAt.getId(), i4, this.radius, f);
                f += 360.0f / ((float) (childCount - i));
            }
        }
        constraintSet.applyTo(this);
    }

    public void setRadius(int i) {
        this.radius = i;
        updateLayoutParams();
    }

    public int getRadius() {
        return this.radius;
    }

    public static boolean shouldSkipView(View view) {
        return "skip".equals(view.getTag());
    }
}

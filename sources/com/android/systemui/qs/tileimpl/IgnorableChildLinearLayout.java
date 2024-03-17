package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: IgnorableChildLinearLayout.kt */
public final class IgnorableChildLinearLayout extends LinearLayout {
    public boolean forceUnspecifiedMeasure;
    public boolean ignoreLastView;

    public IgnorableChildLinearLayout(@NotNull Context context) {
        this(context, (AttributeSet) null, 0, 0, 14, (DefaultConstructorMarker) null);
    }

    public IgnorableChildLinearLayout(@NotNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, (DefaultConstructorMarker) null);
    }

    public IgnorableChildLinearLayout(@NotNull Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ IgnorableChildLinearLayout(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public IgnorableChildLinearLayout(@NotNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public final void setIgnoreLastView(boolean z) {
        this.ignoreLastView = z;
    }

    public final void setForceUnspecifiedMeasure(boolean z) {
        this.forceUnspecifiedMeasure = z;
    }

    public void onMeasure(int i, int i2) {
        if (this.forceUnspecifiedMeasure && getOrientation() == 0) {
            i = View.MeasureSpec.makeMeasureSpec(i, 0);
        }
        if (this.forceUnspecifiedMeasure && getOrientation() == 1) {
            i2 = View.MeasureSpec.makeMeasureSpec(i2, 0);
        }
        super.onMeasure(i, i2);
        if (this.ignoreLastView && getChildCount() > 0) {
            View childAt = getChildAt(getChildCount() - 1);
            if (childAt.getVisibility() != 8) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    if (getOrientation() == 1) {
                        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight() - ((childAt.getMeasuredHeight() + marginLayoutParams.bottomMargin) + marginLayoutParams.topMargin));
                        return;
                    }
                    setMeasuredDimension(getMeasuredWidth() - ((childAt.getMeasuredWidth() + marginLayoutParams.leftMargin) + marginLayoutParams.rightMargin), getMeasuredHeight());
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
        }
    }
}

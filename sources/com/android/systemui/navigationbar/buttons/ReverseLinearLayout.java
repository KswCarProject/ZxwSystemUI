package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;

public class ReverseLinearLayout extends LinearLayout {
    public boolean mIsAlternativeOrder;
    public boolean mIsLayoutReverse;

    public interface Reversable {
        void reverse(boolean z);
    }

    public ReverseLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        updateOrder();
    }

    public void addView(View view) {
        reverseParams(view.getLayoutParams(), view, this.mIsLayoutReverse);
        if (this.mIsLayoutReverse) {
            super.addView(view, 0);
        } else {
            super.addView(view);
        }
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        reverseParams(layoutParams, view, this.mIsLayoutReverse);
        if (this.mIsLayoutReverse) {
            super.addView(view, 0, layoutParams);
        } else {
            super.addView(view, layoutParams);
        }
    }

    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateOrder();
    }

    public void setAlternativeOrder(boolean z) {
        this.mIsAlternativeOrder = z;
        updateOrder();
    }

    public final void updateOrder() {
        boolean z = (getLayoutDirection() == 1) ^ this.mIsAlternativeOrder;
        if (this.mIsLayoutReverse != z) {
            int childCount = getChildCount();
            ArrayList arrayList = new ArrayList(childCount);
            for (int i = 0; i < childCount; i++) {
                arrayList.add(getChildAt(i));
            }
            removeAllViews();
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                super.addView((View) arrayList.get(i2));
            }
            this.mIsLayoutReverse = z;
        }
    }

    public static void reverseParams(ViewGroup.LayoutParams layoutParams, View view, boolean z) {
        if (view instanceof Reversable) {
            ((Reversable) view).reverse(z);
        }
        if (view.getPaddingLeft() == view.getPaddingRight() && view.getPaddingTop() == view.getPaddingBottom()) {
            view.setPadding(view.getPaddingTop(), view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingLeft());
        }
        if (layoutParams != null) {
            int i = layoutParams.width;
            layoutParams.width = layoutParams.height;
            layoutParams.height = i;
        }
    }

    public static class ReverseRelativeLayout extends RelativeLayout implements Reversable {
        public int mDefaultGravity = 0;

        public ReverseRelativeLayout(Context context) {
            super(context);
        }

        public void reverse(boolean z) {
            updateGravity(z);
            ReverseLinearLayout.reverseGroup(this, z);
        }

        public void setDefaultGravity(int i) {
            this.mDefaultGravity = i;
        }

        public void updateGravity(boolean z) {
            int i = this.mDefaultGravity;
            if (i == 48 || i == 80) {
                if (z) {
                    i = i == 48 ? 80 : 48;
                }
                if (getGravity() != i) {
                    setGravity(i);
                }
            }
        }
    }

    public static void reverseGroup(ViewGroup viewGroup, boolean z) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            reverseParams(childAt.getLayoutParams(), childAt, z);
            if (childAt instanceof ViewGroup) {
                reverseGroup((ViewGroup) childAt, z);
            }
        }
    }
}

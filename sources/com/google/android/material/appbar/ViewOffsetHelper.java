package com.google.android.material.appbar;

import android.view.View;
import androidx.core.view.ViewCompat;

public class ViewOffsetHelper {
    public boolean horizontalOffsetEnabled = true;
    public int layoutLeft;
    public int layoutTop;
    public int offsetLeft;
    public int offsetTop;
    public boolean verticalOffsetEnabled = true;
    public final View view;

    public ViewOffsetHelper(View view2) {
        this.view = view2;
    }

    public void onViewLayout() {
        this.layoutTop = this.view.getTop();
        this.layoutLeft = this.view.getLeft();
    }

    public void applyOffsets() {
        View view2 = this.view;
        ViewCompat.offsetTopAndBottom(view2, this.offsetTop - (view2.getTop() - this.layoutTop));
        View view3 = this.view;
        ViewCompat.offsetLeftAndRight(view3, this.offsetLeft - (view3.getLeft() - this.layoutLeft));
    }

    public boolean setTopAndBottomOffset(int i) {
        if (!this.verticalOffsetEnabled || this.offsetTop == i) {
            return false;
        }
        this.offsetTop = i;
        applyOffsets();
        return true;
    }

    public boolean setLeftAndRightOffset(int i) {
        if (!this.horizontalOffsetEnabled || this.offsetLeft == i) {
            return false;
        }
        this.offsetLeft = i;
        applyOffsets();
        return true;
    }

    public int getTopAndBottomOffset() {
        return this.offsetTop;
    }

    public int getLayoutTop() {
        return this.layoutTop;
    }
}

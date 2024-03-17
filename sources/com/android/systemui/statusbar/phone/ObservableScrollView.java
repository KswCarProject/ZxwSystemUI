package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
    public boolean mBlockFlinging;
    public boolean mHandlingTouchEvent;
    public int mLastOverscrollAmount;
    public float mLastX;
    public float mLastY;
    public boolean mTouchCancelled;
    public boolean mTouchEnabled = true;

    public ObservableScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final int getMaxScrollY() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop));
        }
        return 0;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mHandlingTouchEvent = true;
        this.mLastX = motionEvent.getX();
        this.mLastY = motionEvent.getY();
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        this.mHandlingTouchEvent = false;
        return onTouchEvent;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        this.mHandlingTouchEvent = true;
        this.mLastX = motionEvent.getX();
        this.mLastY = motionEvent.getY();
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        this.mHandlingTouchEvent = false;
        return onInterceptTouchEvent;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            if (!this.mTouchEnabled) {
                this.mTouchCancelled = true;
                return false;
            }
            this.mTouchCancelled = false;
        } else if (this.mTouchCancelled) {
            return false;
        } else {
            if (!this.mTouchEnabled) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setAction(3);
                super.dispatchTouchEvent(obtain);
                obtain.recycle();
                this.mTouchCancelled = true;
                return false;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }

    public boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        this.mLastOverscrollAmount = Math.max(0, (i4 + i2) - getMaxScrollY());
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public void fling(int i) {
        if (!this.mBlockFlinging) {
            super.fling(i);
        }
    }

    public void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        super.onOverScrolled(i, i2, z, z2);
    }
}

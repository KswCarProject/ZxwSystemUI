package com.android.systemui.statusbar.window;

import android.content.Context;
import android.graphics.Insets;
import android.util.AttributeSet;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.widget.FrameLayout;

public class StatusBarWindowView extends FrameLayout {
    public int mLeftInset = 0;
    public int mRightInset = 0;
    public int mTopInset = 0;
    public float mTouchDownY = 0.0f;

    public StatusBarWindowView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
        this.mLeftInset = insetsIgnoringVisibility.left;
        this.mRightInset = insetsIgnoringVisibility.right;
        this.mTopInset = 0;
        DisplayCutout displayCutout = getRootWindowInsets().getDisplayCutout();
        if (displayCutout != null) {
            this.mTopInset = displayCutout.getWaterfallInsets().top;
        }
        applyMargins();
        return windowInsets;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && motionEvent.getRawY() > ((float) getHeight())) {
            this.mTouchDownY = motionEvent.getRawY();
            motionEvent.setLocation(motionEvent.getRawX(), (float) this.mTopInset);
        } else if (motionEvent.getAction() == 2 && this.mTouchDownY != 0.0f) {
            motionEvent.setLocation(motionEvent.getRawX(), (((float) this.mTopInset) + motionEvent.getRawY()) - this.mTouchDownY);
        } else if (motionEvent.getAction() == 1) {
            this.mTouchDownY = 0.0f;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0013, code lost:
        r3 = (android.widget.FrameLayout.LayoutParams) r2.getLayoutParams();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void applyMargins() {
        /*
            r7 = this;
            int r0 = r7.getChildCount()
            r1 = 0
        L_0x0005:
            if (r1 >= r0) goto L_0x003b
            android.view.View r2 = r7.getChildAt(r1)
            android.view.ViewGroup$LayoutParams r3 = r2.getLayoutParams()
            boolean r3 = r3 instanceof android.widget.FrameLayout.LayoutParams
            if (r3 == 0) goto L_0x0038
            android.view.ViewGroup$LayoutParams r3 = r2.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r3 = (android.widget.FrameLayout.LayoutParams) r3
            int r4 = r3.rightMargin
            int r5 = r7.mRightInset
            if (r4 != r5) goto L_0x002b
            int r4 = r3.leftMargin
            int r6 = r7.mLeftInset
            if (r4 != r6) goto L_0x002b
            int r4 = r3.topMargin
            int r6 = r7.mTopInset
            if (r4 == r6) goto L_0x0038
        L_0x002b:
            r3.rightMargin = r5
            int r4 = r7.mLeftInset
            r3.leftMargin = r4
            int r4 = r7.mTopInset
            r3.topMargin = r4
            r2.requestLayout()
        L_0x0038:
            int r1 = r1 + 1
            goto L_0x0005
        L_0x003b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.window.StatusBarWindowView.applyMargins():void");
    }
}

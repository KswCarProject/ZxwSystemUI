package com.android.systemui.navigationbar.gestural;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.android.systemui.R$dimen;

public class QuickswitchOrientedNavHandle extends NavigationHandle {
    public int mDeltaRotation;
    public final RectF mTmpBoundsRectF = new RectF();
    public final int mWidth;

    public QuickswitchOrientedNavHandle(Context context) {
        super(context);
        this.mWidth = context.getResources().getDimensionPixelSize(R$dimen.navigation_home_handle_width);
    }

    public void setDeltaRotation(int i) {
        this.mDeltaRotation = i;
    }

    public void onDraw(Canvas canvas) {
        RectF computeHomeHandleBounds = computeHomeHandleBounds();
        float f = this.mRadius;
        canvas.drawRoundRect(computeHomeHandleBounds, f, f, this.mPaint);
    }

    public RectF computeHomeHandleBounds() {
        float f;
        float f2;
        float f3;
        float f4;
        float f5;
        float f6 = this.mRadius * 2.0f;
        int i = getLocationOnScreen()[1];
        int i2 = this.mDeltaRotation;
        if (i2 == 1) {
            float f7 = this.mBottom;
            f = f7 + f6;
            int i3 = this.mWidth;
            float height = ((((float) getHeight()) / 2.0f) - (((float) i3) / 2.0f)) - (((float) i) / 2.0f);
            f5 = ((float) i3) + height;
            f3 = f7;
            f2 = height;
        } else if (i2 != 3) {
            float f8 = this.mRadius * 2.0f;
            f3 = (((float) getWidth()) / 2.0f) - (((float) this.mWidth) / 2.0f);
            f2 = (((float) getHeight()) - this.mBottom) - f8;
            f = (((float) getWidth()) / 2.0f) + (((float) this.mWidth) / 2.0f);
            f4 = f8 + f2;
            this.mTmpBoundsRectF.set(f3, f2, f, f4);
            return this.mTmpBoundsRectF;
        } else {
            f = ((float) getWidth()) - this.mBottom;
            int i4 = this.mWidth;
            f2 = ((((float) getHeight()) / 2.0f) - (((float) i4) / 2.0f)) - (((float) i) / 2.0f);
            f5 = ((float) i4) + f2;
            f3 = f - f6;
        }
        f4 = f5;
        this.mTmpBoundsRectF.set(f3, f2, f, f4);
        return this.mTmpBoundsRectF;
    }
}

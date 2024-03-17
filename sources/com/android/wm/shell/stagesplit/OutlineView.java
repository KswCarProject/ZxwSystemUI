package com.android.wm.shell.stagesplit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.RoundedCorner;
import android.view.View;

public class OutlineView extends View {
    public final Paint mPaint;
    public final Path mPath = new Path();
    public final float[] mRadii = new float[8];

    public boolean hasOverlappingRendering() {
        return false;
    }

    public OutlineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(getResources().getDimension(17104907));
        paint.setColor(getResources().getColor(17170490, (Resources.Theme) null));
    }

    public void onAttachedToWindow() {
        float[] fArr = this.mRadii;
        float cornerRadius = (float) getCornerRadius(0);
        fArr[1] = cornerRadius;
        fArr[0] = cornerRadius;
        float[] fArr2 = this.mRadii;
        float cornerRadius2 = (float) getCornerRadius(1);
        fArr2[3] = cornerRadius2;
        fArr2[2] = cornerRadius2;
        float[] fArr3 = this.mRadii;
        float cornerRadius3 = (float) getCornerRadius(2);
        fArr3[5] = cornerRadius3;
        fArr3[4] = cornerRadius3;
        float[] fArr4 = this.mRadii;
        float cornerRadius4 = (float) getCornerRadius(3);
        fArr4[7] = cornerRadius4;
        fArr4[6] = cornerRadius4;
    }

    public final int getCornerRadius(int i) {
        RoundedCorner roundedCorner = getDisplay().getRoundedCorner(i);
        if (roundedCorner == null) {
            return 0;
        }
        return roundedCorner.getRadius();
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (z) {
            this.mPath.reset();
            this.mPath.addRoundRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.mRadii, Path.Direction.CW);
        }
    }

    public void onDraw(Canvas canvas) {
        canvas.drawPath(this.mPath, this.mPaint);
    }
}

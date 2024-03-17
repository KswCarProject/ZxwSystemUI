package com.android.launcher3.icons;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import androidx.core.graphics.ColorUtils;

public class PlaceHolderIconDrawable extends FastBitmapDrawable {
    public final Path mProgressPath = GraphicsUtils.getShapePath(100);

    public PlaceHolderIconDrawable(BitmapInfo bitmapInfo, Context context) {
        super(bitmapInfo);
        this.mPaint.setColor(ColorUtils.compositeColors(GraphicsUtils.getAttrColor(context, R$attr.loadingIconColor), bitmapInfo.color));
    }

    public void drawInternal(Canvas canvas, Rect rect) {
        int save = canvas.save();
        canvas.translate((float) rect.left, (float) rect.top);
        canvas.scale(((float) rect.width()) / 100.0f, ((float) rect.height()) / 100.0f);
        canvas.drawPath(this.mProgressPath, this.mPaint);
        canvas.restoreToCount(save);
    }
}

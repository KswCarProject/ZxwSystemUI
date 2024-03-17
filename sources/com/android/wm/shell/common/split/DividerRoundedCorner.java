package com.android.wm.shell.common.split;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.RoundedCorner;
import android.view.View;
import com.android.wm.shell.R;

public class DividerRoundedCorner extends View {
    public InvertedRoundedCornerDrawInfo mBottomLeftCorner;
    public InvertedRoundedCornerDrawInfo mBottomRightCorner;
    public final Paint mDividerBarBackground;
    public final int mDividerWidth = getResources().getDimensionPixelSize(R.dimen.split_divider_bar_width);
    public final Point mStartPos = new Point();
    public InvertedRoundedCornerDrawInfo mTopLeftCorner;
    public InvertedRoundedCornerDrawInfo mTopRightCorner;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public DividerRoundedCorner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mDividerBarBackground = paint;
        paint.setColor(getResources().getColor(R.color.split_divider_background, (Resources.Theme) null));
        paint.setFlags(1);
        paint.setStyle(Paint.Style.FILL);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTopLeftCorner = new InvertedRoundedCornerDrawInfo(0);
        this.mTopRightCorner = new InvertedRoundedCornerDrawInfo(1);
        this.mBottomLeftCorner = new InvertedRoundedCornerDrawInfo(3);
        this.mBottomRightCorner = new InvertedRoundedCornerDrawInfo(2);
    }

    public void onDraw(Canvas canvas) {
        canvas.save();
        this.mTopLeftCorner.calculateStartPos(this.mStartPos);
        Point point = this.mStartPos;
        canvas.translate((float) point.x, (float) point.y);
        canvas.drawPath(this.mTopLeftCorner.mPath, this.mDividerBarBackground);
        Point point2 = this.mStartPos;
        canvas.translate((float) (-point2.x), (float) (-point2.y));
        this.mTopRightCorner.calculateStartPos(this.mStartPos);
        Point point3 = this.mStartPos;
        canvas.translate((float) point3.x, (float) point3.y);
        canvas.drawPath(this.mTopRightCorner.mPath, this.mDividerBarBackground);
        Point point4 = this.mStartPos;
        canvas.translate((float) (-point4.x), (float) (-point4.y));
        this.mBottomLeftCorner.calculateStartPos(this.mStartPos);
        Point point5 = this.mStartPos;
        canvas.translate((float) point5.x, (float) point5.y);
        canvas.drawPath(this.mBottomLeftCorner.mPath, this.mDividerBarBackground);
        Point point6 = this.mStartPos;
        canvas.translate((float) (-point6.x), (float) (-point6.y));
        this.mBottomRightCorner.calculateStartPos(this.mStartPos);
        Point point7 = this.mStartPos;
        canvas.translate((float) point7.x, (float) point7.y);
        canvas.drawPath(this.mBottomRightCorner.mPath, this.mDividerBarBackground);
        canvas.restore();
    }

    public final boolean isLandscape() {
        return getResources().getConfiguration().orientation == 2;
    }

    public class InvertedRoundedCornerDrawInfo {
        public final int mCornerPosition;
        public final Path mPath;
        public final int mRadius;

        public InvertedRoundedCornerDrawInfo(int i) {
            int i2;
            Path path = new Path();
            this.mPath = path;
            this.mCornerPosition = i;
            RoundedCorner roundedCorner = DividerRoundedCorner.this.getDisplay().getRoundedCorner(i);
            if (roundedCorner == null) {
                i2 = 0;
            } else {
                i2 = roundedCorner.getRadius();
            }
            this.mRadius = i2;
            Path path2 = new Path();
            float f = 0.0f;
            path2.addRect(0.0f, 0.0f, (float) i2, (float) i2, Path.Direction.CW);
            Path path3 = new Path();
            path3.addCircle(isLeftCorner() ? (float) i2 : 0.0f, isTopCorner() ? (float) i2 : f, (float) i2, Path.Direction.CW);
            path.op(path2, path3, Path.Op.DIFFERENCE);
        }

        public final void calculateStartPos(Point point) {
            int i;
            int i2;
            int i3 = 0;
            if (DividerRoundedCorner.this.isLandscape()) {
                if (isLeftCorner()) {
                    i2 = (DividerRoundedCorner.this.getWidth() / 2) + (DividerRoundedCorner.this.mDividerWidth / 2);
                } else {
                    i2 = ((DividerRoundedCorner.this.getWidth() / 2) - (DividerRoundedCorner.this.mDividerWidth / 2)) - this.mRadius;
                }
                point.x = i2;
                if (!isTopCorner()) {
                    i3 = DividerRoundedCorner.this.getHeight() - this.mRadius;
                }
                point.y = i3;
                return;
            }
            if (!isLeftCorner()) {
                i3 = DividerRoundedCorner.this.getWidth() - this.mRadius;
            }
            point.x = i3;
            if (isTopCorner()) {
                i = (DividerRoundedCorner.this.getHeight() / 2) + (DividerRoundedCorner.this.mDividerWidth / 2);
            } else {
                i = ((DividerRoundedCorner.this.getHeight() / 2) - (DividerRoundedCorner.this.mDividerWidth / 2)) - this.mRadius;
            }
            point.y = i;
        }

        public final boolean isLeftCorner() {
            int i = this.mCornerPosition;
            return i == 0 || i == 3;
        }

        public final boolean isTopCorner() {
            int i = this.mCornerPosition;
            return i == 0 || i == 1;
        }
    }
}

package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.function.Consumer;

public class ScreenshotSelectorView extends View {
    public Consumer<Rect> mOnScreenshotSelected;
    public final Paint mPaintBackground;
    public final Paint mPaintSelection;
    public Rect mSelectionRect;
    public Point mStartPoint;

    public ScreenshotSelectorView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ScreenshotSelectorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint(-16777216);
        this.mPaintBackground = paint;
        paint.setAlpha(160);
        Paint paint2 = new Paint(0);
        this.mPaintSelection = paint2;
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setOnTouchListener(new ScreenshotSelectorView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            startSelection((int) motionEvent.getX(), (int) motionEvent.getY());
            return true;
        } else if (action == 1) {
            setVisibility(8);
            Rect selectionRect = getSelectionRect();
            if (!(this.mOnScreenshotSelected == null || selectionRect == null || selectionRect.width() == 0 || selectionRect.height() == 0)) {
                this.mOnScreenshotSelected.accept(selectionRect);
            }
            stopSelection();
            return true;
        } else if (action != 2) {
            return false;
        } else {
            updateSelection((int) motionEvent.getX(), (int) motionEvent.getY());
            return true;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect((float) this.mLeft, (float) this.mTop, (float) this.mRight, (float) this.mBottom, this.mPaintBackground);
        Rect rect = this.mSelectionRect;
        if (rect != null) {
            canvas.drawRect(rect, this.mPaintSelection);
        }
    }

    public void setOnScreenshotSelected(Consumer<Rect> consumer) {
        this.mOnScreenshotSelected = consumer;
    }

    public void stop() {
        if (getSelectionRect() != null) {
            stopSelection();
        }
    }

    public final void startSelection(int i, int i2) {
        this.mStartPoint = new Point(i, i2);
        this.mSelectionRect = new Rect(i, i2, i, i2);
    }

    public final void updateSelection(int i, int i2) {
        Rect rect = this.mSelectionRect;
        if (rect != null) {
            rect.left = Math.min(this.mStartPoint.x, i);
            this.mSelectionRect.right = Math.max(this.mStartPoint.x, i);
            this.mSelectionRect.top = Math.min(this.mStartPoint.y, i2);
            this.mSelectionRect.bottom = Math.max(this.mStartPoint.y, i2);
            invalidate();
        }
    }

    public final Rect getSelectionRect() {
        return this.mSelectionRect;
    }

    public final void stopSelection() {
        this.mStartPoint = null;
        this.mSelectionRect = null;
    }
}

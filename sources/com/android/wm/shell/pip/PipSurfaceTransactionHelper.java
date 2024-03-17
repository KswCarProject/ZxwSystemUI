package com.android.wm.shell.pip;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceControl;
import com.android.wm.shell.R;
import com.android.wm.shell.transition.Transitions;

public class PipSurfaceTransactionHelper {
    public int mCornerRadius;
    public int mShadowRadius;
    public final Rect mTmpDestinationRect = new Rect();
    public final RectF mTmpDestinationRectF = new RectF();
    public final float[] mTmpFloat9 = new float[9];
    public final RectF mTmpSourceRectF = new RectF();
    public final Matrix mTmpTransform = new Matrix();

    public interface SurfaceControlTransactionFactory {
        SurfaceControl.Transaction getTransaction();
    }

    public void onDensityOrFontScaleChanged(Context context) {
        this.mCornerRadius = context.getResources().getDimensionPixelSize(R.dimen.pip_corner_radius);
        this.mShadowRadius = context.getResources().getDimensionPixelSize(R.dimen.pip_shadow_radius);
    }

    public PipSurfaceTransactionHelper alpha(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f) {
        transaction.setAlpha(surfaceControl, f);
        return this;
    }

    public PipSurfaceTransactionHelper crop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height()).setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        return this;
    }

    public PipSurfaceTransactionHelper scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        return scale(transaction, surfaceControl, rect, rect2, 0.0f);
    }

    public PipSurfaceTransactionHelper scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, float f) {
        this.mTmpSourceRectF.set(rect);
        this.mTmpSourceRectF.offsetTo(0.0f, 0.0f);
        this.mTmpDestinationRectF.set(rect2);
        this.mTmpTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
        this.mTmpTransform.postRotate(f, this.mTmpDestinationRectF.centerX(), this.mTmpDestinationRectF.centerY());
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9);
        return this;
    }

    public PipSurfaceTransactionHelper scaleAndCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, Rect rect4, boolean z) {
        float f;
        int i;
        float f2;
        float f3;
        int i2;
        this.mTmpDestinationRect.set(rect2);
        this.mTmpDestinationRect.offsetTo(0, 0);
        this.mTmpDestinationRect.inset(rect4);
        if (!z || rect == null || rect.width() >= rect2.width()) {
            if (rect2.width() <= rect2.height()) {
                f2 = (float) rect3.width();
                i = rect2.width();
            } else {
                f2 = (float) rect3.height();
                i = rect2.height();
            }
            f = f2 / ((float) i);
        } else {
            if (rect2.width() <= rect2.height()) {
                f3 = (float) rect3.width();
                i2 = rect.width();
            } else {
                f3 = (float) rect3.height();
                i2 = rect.height();
            }
            f = f3 / ((float) i2);
        }
        float f4 = ((float) rect3.top) - (((float) rect4.top) * f);
        this.mTmpTransform.setScale(f, f);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setCrop(surfaceControl, this.mTmpDestinationRect).setPosition(surfaceControl, ((float) rect3.left) - (((float) rect4.left) * f), f4);
        return this;
    }

    public PipSurfaceTransactionHelper rotateAndScaleWithCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, float f, float f2, float f3, boolean z, boolean z2) {
        float f4;
        float f5;
        int i;
        this.mTmpDestinationRect.set(rect);
        this.mTmpDestinationRect.inset(rect3);
        int width = this.mTmpDestinationRect.width();
        int height = this.mTmpDestinationRect.height();
        int width2 = rect2.width();
        int height2 = rect2.height();
        float f6 = width <= height ? ((float) width2) / ((float) width) : ((float) height2) / ((float) height);
        Rect rect4 = this.mTmpDestinationRect;
        boolean z3 = Transitions.SHELL_TRANSITIONS_ROTATION;
        int i2 = z3 ? height2 : width2;
        if (!z3) {
            width2 = height2;
        }
        rect4.set(0, 0, i2, width2);
        rect4.scale(1.0f / f6);
        rect4.offset(rect3.left, rect3.top);
        if (z) {
            f5 = f2 - (((float) rect3.left) * f6);
            i = rect3.top;
        } else if (z2) {
            f5 = f2 - (((float) rect3.top) * f6);
            f4 = f3 + (((float) rect3.left) * f6);
            this.mTmpTransform.setScale(f6, f6);
            this.mTmpTransform.postRotate(f);
            this.mTmpTransform.postTranslate(f5, f4);
            transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setCrop(surfaceControl, rect4);
            return this;
        } else {
            f5 = f2 + (((float) rect3.top) * f6);
            i = rect3.left;
        }
        f4 = f3 - (((float) i) * f6);
        this.mTmpTransform.setScale(f6, f6);
        this.mTmpTransform.postRotate(f);
        this.mTmpTransform.postTranslate(f5, f4);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setCrop(surfaceControl, rect4);
        return this;
    }

    public PipSurfaceTransactionHelper resetScale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setMatrix(surfaceControl, Matrix.IDENTITY_MATRIX, this.mTmpFloat9).setPosition(surfaceControl, (float) rect.left, (float) rect.top);
        return this;
    }

    public PipSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, boolean z) {
        transaction.setCornerRadius(surfaceControl, z ? (float) this.mCornerRadius : 0.0f);
        return this;
    }

    public PipSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        transaction.setCornerRadius(surfaceControl, ((float) this.mCornerRadius) * ((float) (Math.hypot((double) rect.width(), (double) rect.height()) / Math.hypot((double) rect2.width(), (double) rect2.height()))));
        return this;
    }

    public PipSurfaceTransactionHelper shadow(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, boolean z) {
        transaction.setShadowRadius(surfaceControl, z ? (float) this.mShadowRadius : 0.0f);
        return this;
    }
}

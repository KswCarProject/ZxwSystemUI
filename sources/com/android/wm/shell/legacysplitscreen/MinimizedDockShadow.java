package com.android.wm.shell.legacysplitscreen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import com.android.wm.shell.R;

public class MinimizedDockShadow extends View {
    public int mDockSide = -1;
    public final Paint mShadowPaint = new Paint();

    public boolean hasOverlappingRendering() {
        return false;
    }

    public MinimizedDockShadow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setDockSide(int i) {
        if (i != this.mDockSide) {
            this.mDockSide = i;
            updatePaint(getLeft(), getTop(), getRight(), getBottom());
            invalidate();
        }
    }

    public final void updatePaint(int i, int i2, int i3, int i4) {
        int color = this.mContext.getResources().getColor(R.color.minimize_dock_shadow_start, (Resources.Theme) null);
        int color2 = this.mContext.getResources().getColor(R.color.minimize_dock_shadow_end, (Resources.Theme) null);
        int argb = Color.argb((Color.alpha(color) + Color.alpha(color2)) / 2, 0, 0, 0);
        int argb2 = Color.argb((int) ((((float) Color.alpha(color)) * 0.25f) + (((float) Color.alpha(color2)) * 0.75f)), 0, 0, 0);
        int i5 = this.mDockSide;
        if (i5 == 2) {
            this.mShadowPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) (i4 - i2), new int[]{color, argb, argb2, color2}, new float[]{0.0f, 0.35f, 0.6f, 1.0f}, Shader.TileMode.CLAMP));
        } else if (i5 == 1) {
            this.mShadowPaint.setShader(new LinearGradient(0.0f, 0.0f, (float) (i3 - i), 0.0f, new int[]{color, argb, argb2, color2}, new float[]{0.0f, 0.35f, 0.6f, 1.0f}, Shader.TileMode.CLAMP));
        } else if (i5 == 3) {
            this.mShadowPaint.setShader(new LinearGradient((float) (i3 - i), 0.0f, 0.0f, 0.0f, new int[]{color, argb, argb2, color2}, new float[]{0.0f, 0.35f, 0.6f, 1.0f}, Shader.TileMode.CLAMP));
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            updatePaint(i, i2, i3, i4);
            invalidate();
        }
    }

    public void onDraw(Canvas canvas) {
        canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.mShadowPaint);
    }
}

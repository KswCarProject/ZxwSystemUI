package com.android.systemui.navigationbar.gestural;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.navigationbar.buttons.ButtonInterface;

public class NavigationHandle extends View implements ButtonInterface {
    public final float mBottom;
    public final int mDarkColor;
    public final int mLightColor;
    public final Paint mPaint;
    public final float mRadius;
    public boolean mRequiresInvalidate;

    public void abortCurrentGesture() {
    }

    public void setDelayTouchFeedback(boolean z) {
    }

    public void setImageDrawable(Drawable drawable) {
    }

    public void setVertical(boolean z) {
    }

    public NavigationHandle(Context context) {
        this(context, (AttributeSet) null);
    }

    public NavigationHandle(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mPaint = paint;
        Resources resources = context.getResources();
        this.mRadius = resources.getDimension(R$dimen.navigation_handle_radius);
        this.mBottom = resources.getDimension(R$dimen.navigation_handle_bottom);
        int themeAttr = Utils.getThemeAttr(context, R$attr.darkIconTheme);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.lightIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, themeAttr);
        int i = R$attr.homeHandleColor;
        this.mLightColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        this.mDarkColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, i);
        paint.setAntiAlias(true);
        setFocusable(false);
    }

    public void setAlpha(float f) {
        super.setAlpha(f);
        if (f > 0.0f && this.mRequiresInvalidate) {
            this.mRequiresInvalidate = false;
            invalidate();
        }
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        float f = this.mRadius * 2.0f;
        int width = getWidth();
        float f2 = (((float) height) - this.mBottom) - f;
        float f3 = this.mRadius;
        canvas.drawRoundRect(0.0f, f2, (float) width, f2 + f, f3, f3, this.mPaint);
    }

    public void setDarkIntensity(float f) {
        int intValue = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightColor), Integer.valueOf(this.mDarkColor))).intValue();
        if (this.mPaint.getColor() != intValue) {
            this.mPaint.setColor(intValue);
            if (getVisibility() != 0 || getAlpha() <= 0.0f) {
                this.mRequiresInvalidate = true;
            } else {
                invalidate();
            }
        }
    }
}

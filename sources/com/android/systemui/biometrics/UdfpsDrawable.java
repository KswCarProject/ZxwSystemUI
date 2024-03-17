package com.android.systemui.biometrics;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsDrawable.kt */
public abstract class UdfpsDrawable extends Drawable {
    public int _alpha;
    @NotNull
    public final Context context;
    @NotNull
    public final ShapeDrawable fingerprintDrawable;
    public boolean isIlluminationShowing;
    public float strokeWidth;

    public int getOpacity() {
        return 0;
    }

    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    public UdfpsDrawable(@NotNull Context context2, @NotNull Function1<? super Context, ? extends ShapeDrawable> function1) {
        this.context = context2;
        ShapeDrawable shapeDrawable = (ShapeDrawable) function1.invoke(context2);
        this.fingerprintDrawable = shapeDrawable;
        this._alpha = 255;
        this.strokeWidth = shapeDrawable.getPaint().getStrokeWidth();
    }

    public UdfpsDrawable(@NotNull Context context2) {
        this(context2, UdfpsDrawableKt.defaultFactory);
    }

    @NotNull
    public final ShapeDrawable getFingerprintDrawable() {
        return this.fingerprintDrawable;
    }

    public final boolean isIlluminationShowing() {
        return this.isIlluminationShowing;
    }

    public final void setIlluminationShowing(boolean z) {
        if (this.isIlluminationShowing != z) {
            this.isIlluminationShowing = z;
            invalidateSelf();
        }
    }

    public void onSensorRectUpdated(@NotNull RectF rectF) {
        int height = ((int) rectF.height()) / 8;
        updateFingerprintIconBounds(new Rect(((int) rectF.left) + height, ((int) rectF.top) + height, ((int) rectF.right) - height, ((int) rectF.bottom) - height));
    }

    public void updateFingerprintIconBounds(@NotNull Rect rect) {
        this.fingerprintDrawable.setBounds(rect);
        invalidateSelf();
    }

    public int getAlpha() {
        return this._alpha;
    }

    public void setAlpha(int i) {
        this._alpha = i;
        this.fingerprintDrawable.setAlpha(i);
        invalidateSelf();
    }
}

package com.android.systemui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.view.ContextThemeWrapper;
import com.android.settingslib.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DualToneHandler.kt */
public final class DualToneHandler {
    public Color darkColor;
    public Color lightColor;

    public DualToneHandler(@NotNull Context context) {
        setColorsFromContext(context);
    }

    public final void setColorsFromContext(@NotNull Context context) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.darkIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, Utils.getThemeAttr(context, R$attr.lightIconTheme));
        int i = R$attr.singleToneColor;
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        int i2 = R$attr.iconBackgroundColor;
        int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(contextThemeWrapper, i2);
        int i3 = R$attr.fillColor;
        this.darkColor = new Color(colorAttrDefaultColor, colorAttrDefaultColor2, Utils.getColorAttrDefaultColor(contextThemeWrapper, i3));
        this.lightColor = new Color(Utils.getColorAttrDefaultColor(contextThemeWrapper2, i), Utils.getColorAttrDefaultColor(contextThemeWrapper2, i2), Utils.getColorAttrDefaultColor(contextThemeWrapper2, i3));
    }

    public final int getColorForDarkIntensity(float f, int i, int i2) {
        Object evaluate = ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(i), Integer.valueOf(i2));
        if (evaluate != null) {
            return ((Integer) evaluate).intValue();
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final int getSingleColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            color = null;
        }
        int single = color.getSingle();
        Color color3 = this.darkColor;
        if (color3 != null) {
            color2 = color3;
        }
        return getColorForDarkIntensity(f, single, color2.getSingle());
    }

    public final int getBackgroundColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            color = null;
        }
        int background = color.getBackground();
        Color color3 = this.darkColor;
        if (color3 != null) {
            color2 = color3;
        }
        return getColorForDarkIntensity(f, background, color2.getBackground());
    }

    public final int getFillColor(float f) {
        Color color = this.lightColor;
        Color color2 = null;
        if (color == null) {
            color = null;
        }
        int fill = color.getFill();
        Color color3 = this.darkColor;
        if (color3 != null) {
            color2 = color3;
        }
        return getColorForDarkIntensity(f, fill, color2.getFill());
    }

    /* compiled from: DualToneHandler.kt */
    public static final class Color {
        public final int background;
        public final int fill;
        public final int single;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Color)) {
                return false;
            }
            Color color = (Color) obj;
            return this.single == color.single && this.background == color.background && this.fill == color.fill;
        }

        public int hashCode() {
            return (((Integer.hashCode(this.single) * 31) + Integer.hashCode(this.background)) * 31) + Integer.hashCode(this.fill);
        }

        @NotNull
        public String toString() {
            return "Color(single=" + this.single + ", background=" + this.background + ", fill=" + this.fill + ')';
        }

        public Color(int i, int i2, int i3) {
            this.single = i;
            this.background = i2;
            this.fill = i3;
        }

        public final int getBackground() {
            return this.background;
        }

        public final int getFill() {
            return this.fill;
        }

        public final int getSingle() {
            return this.single;
        }
    }
}

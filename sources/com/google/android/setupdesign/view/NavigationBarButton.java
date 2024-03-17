package com.google.android.setupdesign.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

@SuppressLint({"AppCompatCustomView"})
public class NavigationBarButton extends Button {
    public NavigationBarButton(Context context) {
        super(context);
        init();
    }

    public NavigationBarButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        if (!isInEditMode()) {
            Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
            for (int i = 0; i < compoundDrawablesRelative.length; i++) {
                Drawable drawable = compoundDrawablesRelative[i];
                if (drawable != null) {
                    compoundDrawablesRelative[i] = TintedDrawable.wrap(drawable);
                }
            }
            setCompoundDrawablesRelativeWithIntrinsicBounds(compoundDrawablesRelative[0], compoundDrawablesRelative[1], compoundDrawablesRelative[2], compoundDrawablesRelative[3]);
        }
    }

    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            drawable = TintedDrawable.wrap(drawable);
        }
        if (drawable2 != null) {
            drawable2 = TintedDrawable.wrap(drawable2);
        }
        if (drawable3 != null) {
            drawable3 = TintedDrawable.wrap(drawable3);
        }
        if (drawable4 != null) {
            drawable4 = TintedDrawable.wrap(drawable4);
        }
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        tintDrawables();
    }

    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            drawable = TintedDrawable.wrap(drawable);
        }
        if (drawable2 != null) {
            drawable2 = TintedDrawable.wrap(drawable2);
        }
        if (drawable3 != null) {
            drawable3 = TintedDrawable.wrap(drawable3);
        }
        if (drawable4 != null) {
            drawable4 = TintedDrawable.wrap(drawable4);
        }
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        tintDrawables();
    }

    public void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        tintDrawables();
    }

    public final void tintDrawables() {
        ColorStateList textColors = getTextColors();
        if (textColors != null) {
            for (Drawable drawable : getAllCompoundDrawables()) {
                if (drawable instanceof TintedDrawable) {
                    ((TintedDrawable) drawable).setTintListCompat(textColors);
                }
            }
            invalidate();
        }
    }

    public final Drawable[] getAllCompoundDrawables() {
        Drawable[] compoundDrawables = getCompoundDrawables();
        Drawable[] compoundDrawablesRelative = getCompoundDrawablesRelative();
        return new Drawable[]{compoundDrawables[0], compoundDrawables[1], compoundDrawables[2], compoundDrawables[3], compoundDrawablesRelative[0], compoundDrawablesRelative[2]};
    }

    public static class TintedDrawable extends LayerDrawable {
        public ColorStateList tintList = null;

        public boolean isStateful() {
            return true;
        }

        public static TintedDrawable wrap(Drawable drawable) {
            if (drawable instanceof TintedDrawable) {
                return (TintedDrawable) drawable;
            }
            return new TintedDrawable(drawable.mutate());
        }

        public TintedDrawable(Drawable drawable) {
            super(new Drawable[]{drawable});
        }

        public boolean setState(int[] iArr) {
            return super.setState(iArr) || updateState();
        }

        public void setTintListCompat(ColorStateList colorStateList) {
            this.tintList = colorStateList;
            if (updateState()) {
                invalidateSelf();
            }
        }

        public final boolean updateState() {
            ColorStateList colorStateList = this.tintList;
            if (colorStateList == null) {
                return false;
            }
            setColorFilter(colorStateList.getColorForState(getState(), 0), PorterDuff.Mode.SRC_IN);
            return true;
        }
    }
}

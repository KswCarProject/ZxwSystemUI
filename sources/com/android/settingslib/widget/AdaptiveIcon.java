package com.android.settingslib.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

public class AdaptiveIcon extends LayerDrawable {
    public AdaptiveConstantState mAdaptiveConstantState;
    public int mBackgroundColor;

    public AdaptiveIcon(Context context, Drawable drawable) {
        this(context, drawable, R$dimen.dashboard_tile_foreground_image_inset);
    }

    public AdaptiveIcon(Context context, Drawable drawable, int i) {
        super(new Drawable[]{new AdaptiveIconShapeDrawable(context.getResources()), drawable});
        this.mBackgroundColor = -1;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(i);
        setLayerInset(1, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        this.mAdaptiveConstantState = new AdaptiveConstantState(context, drawable);
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        getDrawable(0).setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
        Log.d("AdaptiveHomepageIcon", "Setting background color " + this.mBackgroundColor);
        this.mAdaptiveConstantState.mColor = i;
    }

    public Drawable.ConstantState getConstantState() {
        return this.mAdaptiveConstantState;
    }

    public static class AdaptiveConstantState extends Drawable.ConstantState {
        public int mColor;
        public Context mContext;
        public Drawable mDrawable;

        public int getChangingConfigurations() {
            return 0;
        }

        public AdaptiveConstantState(Context context, Drawable drawable) {
            this.mContext = context;
            this.mDrawable = drawable;
        }

        public Drawable newDrawable() {
            AdaptiveIcon adaptiveIcon = new AdaptiveIcon(this.mContext, this.mDrawable);
            adaptiveIcon.setBackgroundColor(this.mColor);
            return adaptiveIcon;
        }
    }
}

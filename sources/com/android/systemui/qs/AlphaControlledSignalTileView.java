package com.android.systemui.qs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import com.android.systemui.qs.tileimpl.SlashImageView;

public class AlphaControlledSignalTileView extends SignalTileView {
    public AlphaControlledSignalTileView(Context context) {
        super(context);
    }

    public SlashImageView createSlashImageView(Context context) {
        return new AlphaControlledSlashImageView(context);
    }

    public static class AlphaControlledSlashImageView extends SlashImageView {
        public AlphaControlledSlashImageView(Context context) {
            super(context);
        }

        public void setFinalImageTintList(ColorStateList colorStateList) {
            super.setImageTintList(colorStateList);
            SlashDrawable slash = getSlash();
            if (slash != null) {
                ((AlphaControlledSlashDrawable) slash).setFinalTintList(colorStateList);
            }
        }

        public void ensureSlashDrawable() {
            if (getSlash() == null) {
                AlphaControlledSlashDrawable alphaControlledSlashDrawable = new AlphaControlledSlashDrawable(getDrawable());
                setSlash(alphaControlledSlashDrawable);
                alphaControlledSlashDrawable.setAnimationEnabled(getAnimationEnabled());
                setImageViewDrawable(alphaControlledSlashDrawable);
            }
        }
    }

    public static class AlphaControlledSlashDrawable extends SlashDrawable {
        public void setDrawableTintList(ColorStateList colorStateList) {
        }

        public AlphaControlledSlashDrawable(Drawable drawable) {
            super(drawable);
        }

        public void setFinalTintList(ColorStateList colorStateList) {
            super.setDrawableTintList(colorStateList);
        }
    }
}

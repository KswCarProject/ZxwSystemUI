package com.google.android.setupdesign.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.google.android.setupdesign.R$color;

public class IconUniformityAppImageView extends ImageView {
    public static final Float APPS_ICON_RADIUS_MULTIPLIER = Float.valueOf(0.2f);
    public static final Float LEGACY_SIZE_SCALE_FACTOR;
    public static final Float LEGACY_SIZE_SCALE_MARGIN_FACTOR;
    public static final boolean ON_L_PLUS = true;
    public int backdropColorResId = 0;
    public final GradientDrawable backdropDrawable = new GradientDrawable();

    static {
        Float valueOf = Float.valueOf(0.75f);
        LEGACY_SIZE_SCALE_FACTOR = valueOf;
        LEGACY_SIZE_SCALE_MARGIN_FACTOR = Float.valueOf((1.0f - valueOf.floatValue()) / 2.0f);
    }

    public IconUniformityAppImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.backdropColorResId = R$color.sud_uniformity_backdrop_color;
        this.backdropDrawable.setColor(ContextCompat.getColor(getContext(), this.backdropColorResId));
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean z = ON_L_PLUS;
        super.onDraw(canvas);
    }
}

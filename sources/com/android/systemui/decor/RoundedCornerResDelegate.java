package com.android.systemui.decor;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.DisplayUtils;
import android.util.Size;
import android.view.RoundedCorners;
import com.android.systemui.Dumpable;
import com.android.systemui.R$array;
import com.android.systemui.R$drawable;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: RoundedCornerResDelegate.kt */
public final class RoundedCornerResDelegate implements Dumpable {
    @Nullable
    public Drawable bottomRoundedDrawable;
    @NotNull
    public Size bottomRoundedSize = new Size(0, 0);
    public ColorStateList colorTintList = ColorStateList.valueOf(-16777216);
    @Nullable
    public String displayUniqueId;
    public boolean hasBottom;
    public boolean hasTop;
    public float physicalPixelDisplaySizeRatio = 1.0f;
    public int reloadToken;
    @NotNull
    public final Resources res;
    @Nullable
    public Drawable topRoundedDrawable;
    @NotNull
    public Size topRoundedSize = new Size(0, 0);
    @Nullable
    public Integer tuningSizeFactor;

    public RoundedCornerResDelegate(@NotNull Resources resources, @Nullable String str) {
        this.res = resources;
        this.displayUniqueId = str;
        reloadRes();
        reloadMeasures();
    }

    public final float getDensity() {
        return this.res.getDisplayMetrics().density;
    }

    public final boolean getHasTop() {
        return this.hasTop;
    }

    public final boolean getHasBottom() {
        return this.hasBottom;
    }

    @Nullable
    public final Drawable getTopRoundedDrawable() {
        return this.topRoundedDrawable;
    }

    @Nullable
    public final Drawable getBottomRoundedDrawable() {
        return this.bottomRoundedDrawable;
    }

    @NotNull
    public final Size getTopRoundedSize() {
        return this.topRoundedSize;
    }

    @NotNull
    public final Size getBottomRoundedSize() {
        return this.bottomRoundedSize;
    }

    public final ColorStateList getColorTintList() {
        return this.colorTintList;
    }

    public final void setColorTintList(ColorStateList colorStateList) {
        this.colorTintList = colorStateList;
    }

    public final void setTuningSizeFactor(@Nullable Integer num) {
        if (!Intrinsics.areEqual((Object) this.tuningSizeFactor, (Object) num)) {
            this.tuningSizeFactor = num;
            reloadMeasures();
        }
    }

    public final float getPhysicalPixelDisplaySizeRatio() {
        return this.physicalPixelDisplaySizeRatio;
    }

    public final void setPhysicalPixelDisplaySizeRatio(float f) {
        if (!(this.physicalPixelDisplaySizeRatio == f)) {
            this.physicalPixelDisplaySizeRatio = f;
            reloadMeasures();
        }
    }

    public final void reloadAll(int i) {
        if (this.reloadToken != i) {
            this.reloadToken = i;
            reloadRes();
            reloadMeasures();
        }
    }

    public final void updateDisplayUniqueId(@Nullable String str, @Nullable Integer num) {
        if (!Intrinsics.areEqual((Object) this.displayUniqueId, (Object) str)) {
            this.displayUniqueId = str;
            if (num != null) {
                this.reloadToken = num.intValue();
            }
            reloadRes();
            reloadMeasures();
        } else if (num != null) {
            reloadAll(num.intValue());
        }
    }

    public final void reloadRes() {
        int displayUniqueIdConfigIndex = DisplayUtils.getDisplayUniqueIdConfigIndex(this.res, this.displayUniqueId);
        boolean z = true;
        boolean z2 = RoundedCorners.getRoundedCornerRadius(this.res, this.displayUniqueId) > 0;
        this.hasTop = z2 || RoundedCorners.getRoundedCornerTopRadius(this.res, this.displayUniqueId) > 0;
        if (!z2 && RoundedCorners.getRoundedCornerBottomRadius(this.res, this.displayUniqueId) <= 0) {
            z = false;
        }
        this.hasBottom = z;
        this.topRoundedDrawable = getDrawable(displayUniqueIdConfigIndex, R$array.config_roundedCornerTopDrawableArray, R$drawable.rounded_corner_top);
        this.bottomRoundedDrawable = getDrawable(displayUniqueIdConfigIndex, R$array.config_roundedCornerBottomDrawableArray, R$drawable.rounded_corner_bottom);
    }

    public final void reloadMeasures() {
        Drawable drawable = this.topRoundedDrawable;
        if (drawable != null) {
            this.topRoundedSize = new Size(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        Drawable drawable2 = this.bottomRoundedDrawable;
        if (drawable2 != null) {
            this.bottomRoundedSize = new Size(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
        }
        Integer num = this.tuningSizeFactor;
        if (num != null) {
            int intValue = num.intValue();
            if (intValue > 0) {
                int density = (int) (((float) intValue) * getDensity());
                if (getTopRoundedSize().getWidth() > 0) {
                    this.topRoundedSize = new Size(density, density);
                }
                if (getBottomRoundedSize().getWidth() > 0) {
                    this.bottomRoundedSize = new Size(density, density);
                }
            } else {
                return;
            }
        }
        if (!(this.physicalPixelDisplaySizeRatio == 1.0f)) {
            if (this.topRoundedSize.getWidth() != 0) {
                this.topRoundedSize = new Size((int) ((this.physicalPixelDisplaySizeRatio * ((float) this.topRoundedSize.getWidth())) + 0.5f), (int) ((this.physicalPixelDisplaySizeRatio * ((float) this.topRoundedSize.getHeight())) + 0.5f));
            }
            if (this.bottomRoundedSize.getWidth() != 0) {
                this.bottomRoundedSize = new Size((int) ((this.physicalPixelDisplaySizeRatio * ((float) this.bottomRoundedSize.getWidth())) + 0.5f), (int) ((this.physicalPixelDisplaySizeRatio * ((float) this.bottomRoundedSize.getHeight())) + 0.5f));
            }
        }
    }

    public final Drawable getDrawable(int i, int i2, int i3) {
        Drawable drawable;
        TypedArray obtainTypedArray = this.res.obtainTypedArray(i2);
        if (i < 0 || i >= obtainTypedArray.length()) {
            drawable = this.res.getDrawable(i3, (Resources.Theme) null);
        } else {
            drawable = obtainTypedArray.getDrawable(i);
        }
        obtainTypedArray.recycle();
        return drawable;
    }

    public void dump(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        printWriter.println("RoundedCornerResDelegate state:");
        printWriter.println(Intrinsics.stringPlus("  hasTop=", Boolean.valueOf(this.hasTop)));
        printWriter.println(Intrinsics.stringPlus("  hasBottom=", Boolean.valueOf(this.hasBottom)));
        printWriter.println("  topRoundedSize(w,h)=(" + this.topRoundedSize.getWidth() + ',' + this.topRoundedSize.getHeight() + ')');
        printWriter.println("  bottomRoundedSize(w,h)=(" + this.bottomRoundedSize.getWidth() + ',' + this.bottomRoundedSize.getHeight() + ')');
        printWriter.println(Intrinsics.stringPlus("  physicalPixelDisplaySizeRatio=", Float.valueOf(this.physicalPixelDisplaySizeRatio)));
    }
}

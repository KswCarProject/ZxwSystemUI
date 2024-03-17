package com.android.keyguard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.graphics.ColorUtils;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$attr;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import java.io.PrintWriter;

public class LockIconView extends FrameLayout implements Dumpable {
    public boolean mAod;
    public ImageView mBgView;
    public float mDozeAmount = 0.0f;
    public int mIconType;
    public ImageView mLockIcon;
    public PointF mLockIconCenter = new PointF(0.0f, 0.0f);
    public int mLockIconColor;
    public int mLockIconPadding;
    public float mRadius;
    public final RectF mSensorRect = new RectF();
    public boolean mUseBackground = false;

    public static int[] getLockIconState(int i, boolean z) {
        if (i == -1) {
            return new int[0];
        }
        int[] iArr = new int[2];
        if (i == 0) {
            iArr[0] = 16842916;
        } else if (i == 1) {
            iArr[0] = 16842917;
        } else if (i == 2) {
            iArr[0] = 16842918;
        }
        if (z) {
            iArr[1] = 16842915;
        } else {
            iArr[1] = -16842915;
        }
        return iArr;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public final String typeToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 2 ? "invalid" : "unlock" : "fingerprint" : "lock" : "none";
    }

    public LockIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLockIcon = (ImageView) findViewById(R$id.lock_icon);
        this.mBgView = (ImageView) findViewById(R$id.lock_icon_bg);
    }

    public void setDozeAmount(float f) {
        this.mDozeAmount = f;
        updateColorAndBackgroundVisibility();
    }

    public void updateColorAndBackgroundVisibility() {
        if (!this.mUseBackground || this.mLockIcon.getDrawable() == null) {
            this.mLockIconColor = ColorUtils.blendARGB(Utils.getColorAttrDefaultColor(getContext(), R$attr.wallpaperTextColorAccent), -1, this.mDozeAmount);
            this.mBgView.setVisibility(8);
        } else {
            this.mLockIconColor = ColorUtils.blendARGB(Utils.getColorAttrDefaultColor(getContext(), 16842806), -1, this.mDozeAmount);
            this.mBgView.setBackground(getContext().getDrawable(R$drawable.fingerprint_bg));
            this.mBgView.setAlpha(1.0f - this.mDozeAmount);
            this.mBgView.setVisibility(0);
        }
        this.mLockIcon.setImageTintList(ColorStateList.valueOf(this.mLockIconColor));
    }

    public void setImageDrawable(Drawable drawable) {
        this.mLockIcon.setImageDrawable(drawable);
        if (this.mUseBackground) {
            if (drawable == null) {
                this.mBgView.setVisibility(4);
            } else {
                this.mBgView.setVisibility(0);
            }
        }
    }

    public void setUseBackground(boolean z) {
        this.mUseBackground = z;
        updateColorAndBackgroundVisibility();
    }

    public void setCenterLocation(PointF pointF, float f, int i) {
        this.mLockIconCenter = pointF;
        this.mRadius = f;
        this.mLockIconPadding = i;
        this.mLockIcon.setPadding(i, i, i, i);
        RectF rectF = this.mSensorRect;
        PointF pointF2 = this.mLockIconCenter;
        float f2 = pointF2.x;
        float f3 = this.mRadius;
        float f4 = pointF2.y;
        rectF.set(f2 - f3, f4 - f3, f2 + f3, f4 + f3);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        RectF rectF2 = this.mSensorRect;
        float f5 = rectF2.right;
        float f6 = rectF2.left;
        layoutParams.width = (int) (f5 - f6);
        float f7 = rectF2.bottom;
        float f8 = rectF2.top;
        layoutParams.height = (int) (f7 - f8);
        layoutParams.topMargin = (int) f8;
        layoutParams.setMarginStart((int) f6);
        setLayoutParams(layoutParams);
    }

    public float getLocationTop() {
        return this.mLockIconCenter.y - this.mRadius;
    }

    public void clearIcon() {
        updateIcon(-1, false);
    }

    public void updateIcon(int i, boolean z) {
        this.mIconType = i;
        this.mAod = z;
        this.mLockIcon.setImageState(getLockIconState(i, z), true);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("Lock Icon View Parameters:");
        printWriter.println("    Center in px (x, y)= (" + this.mLockIconCenter.x + ", " + this.mLockIconCenter.y + ")");
        StringBuilder sb = new StringBuilder();
        sb.append("    Radius in pixels: ");
        sb.append(this.mRadius);
        printWriter.println(sb.toString());
        printWriter.println("    Drawable padding: " + this.mLockIconPadding);
        printWriter.println("    mIconType=" + typeToString(this.mIconType));
        printWriter.println("    mAod=" + this.mAod);
        printWriter.println("Lock Icon View actual measurements:");
        printWriter.println("    topLeft= (" + getX() + ", " + getY() + ")");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("    width=");
        sb2.append(getWidth());
        sb2.append(" height=");
        sb2.append(getHeight());
        printWriter.println(sb2.toString());
    }
}

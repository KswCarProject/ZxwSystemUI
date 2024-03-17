package com.android.systemui.decor;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.systemui.R$drawable;

/* compiled from: RoundedCornerDecorProviderImpl.kt */
public final class RoundedCornerDecorProviderImplKt {
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        if (r6 != 2) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
        if (r6 != 2) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int toLayoutGravity(int r6, int r7) {
        /*
            r0 = 80
            r1 = 5
            r2 = 48
            r3 = 2
            r4 = 3
            r5 = 1
            if (r7 == 0) goto L_0x0023
            if (r7 == r5) goto L_0x001c
            if (r7 == r4) goto L_0x0015
            if (r6 == 0) goto L_0x002a
            if (r6 == r5) goto L_0x002f
            if (r6 == r3) goto L_0x002e
            goto L_0x002c
        L_0x0015:
            if (r6 == 0) goto L_0x002c
            if (r6 == r5) goto L_0x002a
            if (r6 == r3) goto L_0x002f
            goto L_0x002e
        L_0x001c:
            if (r6 == 0) goto L_0x002f
            if (r6 == r5) goto L_0x002e
            if (r6 == r3) goto L_0x002c
            goto L_0x002a
        L_0x0023:
            if (r6 == 0) goto L_0x002e
            if (r6 == r5) goto L_0x002c
            if (r6 == r3) goto L_0x002a
            goto L_0x002f
        L_0x002a:
            r0 = r1
            goto L_0x002f
        L_0x002c:
            r0 = r2
            goto L_0x002f
        L_0x002e:
            r0 = r4
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.decor.RoundedCornerDecorProviderImplKt.toLayoutGravity(int, int):int");
    }

    public static final void setRoundedCornerImage(ImageView imageView, RoundedCornerResDelegate roundedCornerResDelegate, boolean z) {
        Drawable drawable;
        int i;
        if (z) {
            drawable = roundedCornerResDelegate.getTopRoundedDrawable();
        } else {
            drawable = roundedCornerResDelegate.getBottomRoundedDrawable();
        }
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            return;
        }
        if (z) {
            i = R$drawable.rounded_corner_top;
        } else {
            i = R$drawable.rounded_corner_bottom;
        }
        imageView.setImageResource(i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002d, code lost:
        if (r8 != false) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0040, code lost:
        if (r8 != false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004f, code lost:
        if (r8 != false) goto L_0x0024;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0060, code lost:
        if (r8 != false) goto L_0x0057;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void adjustRotation(android.widget.ImageView r7, java.util.List<java.lang.Integer> r8, int r9) {
        /*
            r0 = 1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r0)
            boolean r1 = r8.contains(r1)
            r2 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            boolean r8 = r8.contains(r2)
            r2 = 1065353216(0x3f800000, float:1.0)
            r3 = 1127481344(0x43340000, float:180.0)
            r4 = -1082130432(0xffffffffbf800000, float:-1.0)
            r5 = 0
            if (r9 == 0) goto L_0x0052
            if (r9 == r0) goto L_0x0043
            r0 = 3
            if (r9 == r0) goto L_0x0030
            if (r1 == 0) goto L_0x0026
            if (r8 == 0) goto L_0x0026
        L_0x0024:
            r4 = r2
            goto L_0x0063
        L_0x0026:
            if (r1 == 0) goto L_0x002b
            if (r8 != 0) goto L_0x002b
            goto L_0x0047
        L_0x002b:
            if (r1 != 0) goto L_0x0056
            if (r8 == 0) goto L_0x0056
            goto L_0x0034
        L_0x0030:
            if (r1 == 0) goto L_0x0039
            if (r8 == 0) goto L_0x0039
        L_0x0034:
            r3 = r5
            r6 = r4
            r4 = r2
            r2 = r6
            goto L_0x0063
        L_0x0039:
            if (r1 == 0) goto L_0x003e
            if (r8 != 0) goto L_0x003e
            goto L_0x0024
        L_0x003e:
            if (r1 != 0) goto L_0x0057
            if (r8 == 0) goto L_0x0057
            goto L_0x0056
        L_0x0043:
            if (r1 == 0) goto L_0x0048
            if (r8 == 0) goto L_0x0048
        L_0x0047:
            goto L_0x0057
        L_0x0048:
            if (r1 == 0) goto L_0x004d
            if (r8 != 0) goto L_0x004d
            goto L_0x0056
        L_0x004d:
            if (r1 != 0) goto L_0x0034
            if (r8 == 0) goto L_0x0034
            goto L_0x0024
        L_0x0052:
            if (r1 == 0) goto L_0x0059
            if (r8 == 0) goto L_0x0059
        L_0x0056:
            r4 = r2
        L_0x0057:
            r3 = r5
            goto L_0x0063
        L_0x0059:
            if (r1 == 0) goto L_0x005e
            if (r8 != 0) goto L_0x005e
            goto L_0x0034
        L_0x005e:
            if (r1 != 0) goto L_0x0024
            if (r8 == 0) goto L_0x0024
            goto L_0x0047
        L_0x0063:
            r7.setRotation(r3)
            r7.setScaleX(r2)
            r7.setScaleY(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.decor.RoundedCornerDecorProviderImplKt.adjustRotation(android.widget.ImageView, java.util.List, int):void");
    }
}

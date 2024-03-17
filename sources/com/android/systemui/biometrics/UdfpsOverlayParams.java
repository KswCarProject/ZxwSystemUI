package com.android.systemui.biometrics;

import android.graphics.Rect;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: UdfpsOverlayParams.kt */
public final class UdfpsOverlayParams {
    public final int naturalDisplayHeight;
    public final int naturalDisplayWidth;
    public final int rotation;
    public final float scaleFactor;
    @NotNull
    public final Rect sensorBounds;

    public UdfpsOverlayParams() {
        this((Rect) null, 0, 0, 0.0f, 0, 31, (DefaultConstructorMarker) null);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof UdfpsOverlayParams)) {
            return false;
        }
        UdfpsOverlayParams udfpsOverlayParams = (UdfpsOverlayParams) obj;
        return Intrinsics.areEqual((Object) this.sensorBounds, (Object) udfpsOverlayParams.sensorBounds) && this.naturalDisplayWidth == udfpsOverlayParams.naturalDisplayWidth && this.naturalDisplayHeight == udfpsOverlayParams.naturalDisplayHeight && Intrinsics.areEqual((Object) Float.valueOf(this.scaleFactor), (Object) Float.valueOf(udfpsOverlayParams.scaleFactor)) && this.rotation == udfpsOverlayParams.rotation;
    }

    public int hashCode() {
        return (((((((this.sensorBounds.hashCode() * 31) + Integer.hashCode(this.naturalDisplayWidth)) * 31) + Integer.hashCode(this.naturalDisplayHeight)) * 31) + Float.hashCode(this.scaleFactor)) * 31) + Integer.hashCode(this.rotation);
    }

    @NotNull
    public String toString() {
        return "UdfpsOverlayParams(sensorBounds=" + this.sensorBounds + ", naturalDisplayWidth=" + this.naturalDisplayWidth + ", naturalDisplayHeight=" + this.naturalDisplayHeight + ", scaleFactor=" + this.scaleFactor + ", rotation=" + this.rotation + ')';
    }

    public UdfpsOverlayParams(@NotNull Rect rect, int i, int i2, float f, int i3) {
        this.sensorBounds = rect;
        this.naturalDisplayWidth = i;
        this.naturalDisplayHeight = i2;
        this.scaleFactor = f;
        this.rotation = i3;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ UdfpsOverlayParams(android.graphics.Rect r4, int r5, int r6, float r7, int r8, int r9, kotlin.jvm.internal.DefaultConstructorMarker r10) {
        /*
            r3 = this;
            r10 = r9 & 1
            if (r10 == 0) goto L_0x0009
            android.graphics.Rect r4 = new android.graphics.Rect
            r4.<init>()
        L_0x0009:
            r10 = r9 & 2
            r0 = 0
            if (r10 == 0) goto L_0x0010
            r10 = r0
            goto L_0x0011
        L_0x0010:
            r10 = r5
        L_0x0011:
            r5 = r9 & 4
            if (r5 == 0) goto L_0x0017
            r1 = r0
            goto L_0x0018
        L_0x0017:
            r1 = r6
        L_0x0018:
            r5 = r9 & 8
            if (r5 == 0) goto L_0x001e
            r7 = 1065353216(0x3f800000, float:1.0)
        L_0x001e:
            r2 = r7
            r5 = r9 & 16
            if (r5 == 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r0 = r8
        L_0x0025:
            r5 = r3
            r6 = r4
            r7 = r10
            r8 = r1
            r9 = r2
            r10 = r0
            r5.<init>(r6, r7, r8, r9, r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.UdfpsOverlayParams.<init>(android.graphics.Rect, int, int, float, int, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    @NotNull
    public final Rect getSensorBounds() {
        return this.sensorBounds;
    }

    public final int getNaturalDisplayWidth() {
        return this.naturalDisplayWidth;
    }

    public final int getNaturalDisplayHeight() {
        return this.naturalDisplayHeight;
    }

    public final float getScaleFactor() {
        return this.scaleFactor;
    }

    public final int getRotation() {
        return this.rotation;
    }

    public final int getLogicalDisplayWidth() {
        int i = this.rotation;
        if (i == 1 || i == 3) {
            return this.naturalDisplayHeight;
        }
        return this.naturalDisplayWidth;
    }

    public final int getLogicalDisplayHeight() {
        int i = this.rotation;
        if (i == 1 || i == 3) {
            return this.naturalDisplayWidth;
        }
        return this.naturalDisplayHeight;
    }
}

package com.android.systemui.util.drawable;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimatedRotateDrawable;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.NotNull;

/* compiled from: DrawableSize.kt */
public final class DrawableSize {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @NotNull
    public static final Drawable downscaleToSize(@NotNull Resources resources, @NotNull Drawable drawable, int i, int i2) {
        return Companion.downscaleToSize(resources, drawable, i, i2);
    }

    /* compiled from: DrawableSize.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x0024 A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0029 A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0031 A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:18:0x0035 A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x003a A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:26:0x004b A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x0050 A[Catch:{ all -> 0x0171 }] */
        /* JADX WARNING: Removed duplicated region for block: B:29:0x0056 A[ADDED_TO_REGION] */
        @org.jetbrains.annotations.NotNull
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final android.graphics.drawable.Drawable downscaleToSize(@org.jetbrains.annotations.NotNull android.content.res.Resources r9, @org.jetbrains.annotations.NotNull android.graphics.drawable.Drawable r10, int r11, int r12) {
            /*
                r8 = this;
                java.lang.String r8 = "DrawableSize#downscaleToSize"
                android.os.Trace.beginSection(r8)
                boolean r8 = r10 instanceof android.graphics.drawable.BitmapDrawable     // Catch:{ all -> 0x0171 }
                r0 = 0
                if (r8 == 0) goto L_0x000e
                r8 = r10
                android.graphics.drawable.BitmapDrawable r8 = (android.graphics.drawable.BitmapDrawable) r8     // Catch:{ all -> 0x0171 }
                goto L_0x000f
            L_0x000e:
                r8 = r0
            L_0x000f:
                if (r8 != 0) goto L_0x0013
            L_0x0011:
                r8 = r0
                goto L_0x0022
            L_0x0013:
                android.graphics.Bitmap r8 = r8.getBitmap()     // Catch:{ all -> 0x0171 }
                if (r8 != 0) goto L_0x001a
                goto L_0x0011
            L_0x001a:
                int r8 = r8.getWidth()     // Catch:{ all -> 0x0171 }
                java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ all -> 0x0171 }
            L_0x0022:
                if (r8 != 0) goto L_0x0029
                int r8 = r10.getIntrinsicWidth()     // Catch:{ all -> 0x0171 }
                goto L_0x002d
            L_0x0029:
                int r8 = r8.intValue()     // Catch:{ all -> 0x0171 }
            L_0x002d:
                boolean r1 = r10 instanceof android.graphics.drawable.BitmapDrawable     // Catch:{ all -> 0x0171 }
                if (r1 == 0) goto L_0x0035
                r1 = r10
                android.graphics.drawable.BitmapDrawable r1 = (android.graphics.drawable.BitmapDrawable) r1     // Catch:{ all -> 0x0171 }
                goto L_0x0036
            L_0x0035:
                r1 = r0
            L_0x0036:
                if (r1 != 0) goto L_0x003a
            L_0x0038:
                r1 = r0
                goto L_0x0049
            L_0x003a:
                android.graphics.Bitmap r1 = r1.getBitmap()     // Catch:{ all -> 0x0171 }
                if (r1 != 0) goto L_0x0041
                goto L_0x0038
            L_0x0041:
                int r1 = r1.getHeight()     // Catch:{ all -> 0x0171 }
                java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ all -> 0x0171 }
            L_0x0049:
                if (r1 != 0) goto L_0x0050
                int r1 = r10.getIntrinsicHeight()     // Catch:{ all -> 0x0171 }
                goto L_0x0054
            L_0x0050:
                int r1 = r1.intValue()     // Catch:{ all -> 0x0171 }
            L_0x0054:
                if (r8 <= 0) goto L_0x016d
                if (r1 > 0) goto L_0x005a
                goto L_0x016d
            L_0x005a:
                java.lang.String r2 = " to "
                r3 = 3
                java.lang.String r4 = "SysUiDrawableSize"
                java.lang.String r5 = " x "
                if (r8 >= r11) goto L_0x0095
                if (r1 >= r12) goto L_0x0095
                boolean r9 = android.util.Log.isLoggable(r4, r3)     // Catch:{ all -> 0x0171 }
                if (r9 == 0) goto L_0x0091
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
                r9.<init>()     // Catch:{ all -> 0x0171 }
                java.lang.String r0 = "Not resizing "
                r9.append(r0)     // Catch:{ all -> 0x0171 }
                r9.append(r8)     // Catch:{ all -> 0x0171 }
                r9.append(r5)     // Catch:{ all -> 0x0171 }
                r9.append(r1)     // Catch:{ all -> 0x0171 }
                r9.append(r2)     // Catch:{ all -> 0x0171 }
                r9.append(r11)     // Catch:{ all -> 0x0171 }
                r9.append(r5)     // Catch:{ all -> 0x0171 }
                r9.append(r12)     // Catch:{ all -> 0x0171 }
                java.lang.String r8 = r9.toString()     // Catch:{ all -> 0x0171 }
                android.util.Log.d(r4, r8)     // Catch:{ all -> 0x0171 }
            L_0x0091:
                android.os.Trace.endSection()
                return r10
            L_0x0095:
                com.android.systemui.util.drawable.DrawableSize$Companion r6 = com.android.systemui.util.drawable.DrawableSize.Companion     // Catch:{ all -> 0x0171 }
                boolean r6 = r6.isSimpleBitmap(r10)     // Catch:{ all -> 0x0171 }
                if (r6 != 0) goto L_0x00a1
                android.os.Trace.endSection()
                return r10
            L_0x00a1:
                float r11 = (float) r11
                float r6 = (float) r8
                float r11 = r11 / r6
                float r12 = (float) r12
                float r7 = (float) r1
                float r12 = r12 / r7
                float r11 = java.lang.Math.min(r12, r11)     // Catch:{ all -> 0x0171 }
                float r6 = r6 * r11
                int r12 = (int) r6     // Catch:{ all -> 0x0171 }
                float r7 = r7 * r11
                int r11 = (int) r7     // Catch:{ all -> 0x0171 }
                if (r12 <= 0) goto L_0x012c
                if (r11 > 0) goto L_0x00b5
                goto L_0x012c
            L_0x00b5:
                boolean r3 = android.util.Log.isLoggable(r4, r3)     // Catch:{ all -> 0x0171 }
                if (r3 == 0) goto L_0x00f1
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
                r3.<init>()     // Catch:{ all -> 0x0171 }
                java.lang.String r6 = "Resizing large drawable ("
                r3.append(r6)     // Catch:{ all -> 0x0171 }
                java.lang.Class r6 = r10.getClass()     // Catch:{ all -> 0x0171 }
                java.lang.String r6 = r6.getSimpleName()     // Catch:{ all -> 0x0171 }
                r3.append(r6)     // Catch:{ all -> 0x0171 }
                java.lang.String r6 = ") from "
                r3.append(r6)     // Catch:{ all -> 0x0171 }
                r3.append(r8)     // Catch:{ all -> 0x0171 }
                r3.append(r5)     // Catch:{ all -> 0x0171 }
                r3.append(r1)     // Catch:{ all -> 0x0171 }
                r3.append(r2)     // Catch:{ all -> 0x0171 }
                r3.append(r12)     // Catch:{ all -> 0x0171 }
                r3.append(r5)     // Catch:{ all -> 0x0171 }
                r3.append(r11)     // Catch:{ all -> 0x0171 }
                java.lang.String r8 = r3.toString()     // Catch:{ all -> 0x0171 }
                android.util.Log.d(r4, r8)     // Catch:{ all -> 0x0171 }
            L_0x00f1:
                boolean r8 = r10 instanceof android.graphics.drawable.BitmapDrawable     // Catch:{ all -> 0x0171 }
                if (r8 == 0) goto L_0x00f9
                r8 = r10
                android.graphics.drawable.BitmapDrawable r8 = (android.graphics.drawable.BitmapDrawable) r8     // Catch:{ all -> 0x0171 }
                goto L_0x00fa
            L_0x00f9:
                r8 = r0
            L_0x00fa:
                if (r8 != 0) goto L_0x00fd
                goto L_0x0108
            L_0x00fd:
                android.graphics.Bitmap r8 = r8.getBitmap()     // Catch:{ all -> 0x0171 }
                if (r8 != 0) goto L_0x0104
                goto L_0x0108
            L_0x0104:
                android.graphics.Bitmap$Config r0 = r8.getConfig()     // Catch:{ all -> 0x0171 }
            L_0x0108:
                if (r0 != 0) goto L_0x010c
                android.graphics.Bitmap$Config r0 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ all -> 0x0171 }
            L_0x010c:
                android.graphics.Bitmap r8 = android.graphics.Bitmap.createBitmap(r12, r11, r0)     // Catch:{ all -> 0x0171 }
                android.graphics.Canvas r0 = new android.graphics.Canvas     // Catch:{ all -> 0x0171 }
                r0.<init>(r8)     // Catch:{ all -> 0x0171 }
                android.graphics.Rect r1 = r10.getBounds()     // Catch:{ all -> 0x0171 }
                r2 = 0
                r10.setBounds(r2, r2, r12, r11)     // Catch:{ all -> 0x0171 }
                r10.draw(r0)     // Catch:{ all -> 0x0171 }
                r10.setBounds(r1)     // Catch:{ all -> 0x0171 }
                android.graphics.drawable.BitmapDrawable r10 = new android.graphics.drawable.BitmapDrawable     // Catch:{ all -> 0x0171 }
                r10.<init>(r9, r8)     // Catch:{ all -> 0x0171 }
                android.os.Trace.endSection()
                return r10
            L_0x012c:
                java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0171 }
                r9.<init>()     // Catch:{ all -> 0x0171 }
                java.lang.String r0 = "Attempted to resize "
                r9.append(r0)     // Catch:{ all -> 0x0171 }
                java.lang.Class r0 = r10.getClass()     // Catch:{ all -> 0x0171 }
                java.lang.String r0 = r0.getSimpleName()     // Catch:{ all -> 0x0171 }
                r9.append(r0)     // Catch:{ all -> 0x0171 }
                java.lang.String r0 = " from "
                r9.append(r0)     // Catch:{ all -> 0x0171 }
                r9.append(r8)     // Catch:{ all -> 0x0171 }
                r9.append(r5)     // Catch:{ all -> 0x0171 }
                r9.append(r1)     // Catch:{ all -> 0x0171 }
                java.lang.String r8 = " to invalid "
                r9.append(r8)     // Catch:{ all -> 0x0171 }
                r9.append(r12)     // Catch:{ all -> 0x0171 }
                r9.append(r5)     // Catch:{ all -> 0x0171 }
                r9.append(r11)     // Catch:{ all -> 0x0171 }
                r8 = 46
                r9.append(r8)     // Catch:{ all -> 0x0171 }
                java.lang.String r8 = r9.toString()     // Catch:{ all -> 0x0171 }
                android.util.Log.w(r4, r8)     // Catch:{ all -> 0x0171 }
                android.os.Trace.endSection()
                return r10
            L_0x016d:
                android.os.Trace.endSection()
                return r10
            L_0x0171:
                r8 = move-exception
                android.os.Trace.endSection()
                throw r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.drawable.DrawableSize.Companion.downscaleToSize(android.content.res.Resources, android.graphics.drawable.Drawable, int, int):android.graphics.drawable.Drawable");
        }

        public final boolean isSimpleBitmap(Drawable drawable) {
            return !drawable.isStateful() && !isAnimated(drawable);
        }

        public final boolean isAnimated(Drawable drawable) {
            if ((drawable instanceof Animatable) || (drawable instanceof Animatable2) || (drawable instanceof AnimatedImageDrawable) || (drawable instanceof AnimatedRotateDrawable) || (drawable instanceof AnimatedStateListDrawable) || (drawable instanceof AnimatedVectorDrawable)) {
                return true;
            }
            return false;
        }
    }
}

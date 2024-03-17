package com.android.systemui.statusbar;

import android.graphics.Bitmap;
import android.graphics.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: MediaArtworkProcessor.kt */
public final class MediaArtworkProcessor {
    @Nullable
    public Bitmap mArtworkCache;
    @NotNull
    public final Point mTmpSize = new Point();

    /* JADX WARNING: Removed duplicated region for block: B:54:0x00da  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00e9  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0101  */
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.Bitmap processArtwork(@org.jetbrains.annotations.NotNull android.content.Context r8, @org.jetbrains.annotations.NotNull android.graphics.Bitmap r9) {
        /*
            r7 = this;
            android.graphics.Bitmap r0 = r7.mArtworkCache
            if (r0 == 0) goto L_0x0005
            return r0
        L_0x0005:
            android.renderscript.RenderScript r0 = android.renderscript.RenderScript.create(r8)
            android.renderscript.Element r1 = android.renderscript.Element.U8_4(r0)
            android.renderscript.ScriptIntrinsicBlur r1 = android.renderscript.ScriptIntrinsicBlur.create(r0, r1)
            r2 = 0
            android.view.Display r8 = r8.getDisplay()     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            if (r8 != 0) goto L_0x0019
            goto L_0x001e
        L_0x0019:
            android.graphics.Point r3 = r7.mTmpSize     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            r8.getSize(r3)     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
        L_0x001e:
            android.graphics.Rect r8 = new android.graphics.Rect     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r3 = r9.getWidth()     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r4 = r9.getHeight()     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            r5 = 0
            r8.<init>(r5, r5, r3, r4)     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            android.graphics.Point r7 = r7.mTmpSize     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r3 = r7.x     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r3 = r3 / 6
            int r7 = r7.y     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r7 = r7 / 6
            int r7 = java.lang.Math.max(r3, r7)     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            android.util.MathUtils.fitRect(r8, r7)     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r7 = r8.width()     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            int r8 = r8.height()     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            r3 = 1
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createScaledBitmap(r9, r7, r8, r3)     // Catch:{ IllegalArgumentException -> 0x00cc, all -> 0x00c8 }
            android.graphics.Bitmap$Config r8 = r7.getConfig()     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            if (r8 == r3) goto L_0x0063
            android.graphics.Bitmap r8 = r7.copy(r3, r5)     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            r7.recycle()     // Catch:{ IllegalArgumentException -> 0x005f, all -> 0x005b }
            r7 = r8
            goto L_0x0063
        L_0x005b:
            r7 = move-exception
            r0 = r2
            goto L_0x00ef
        L_0x005f:
            r7 = move-exception
            r0 = r2
            goto L_0x00cf
        L_0x0063:
            int r8 = r7.getWidth()     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            int r3 = r7.getHeight()     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            android.graphics.Bitmap$Config r4 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            android.graphics.Bitmap r8 = android.graphics.Bitmap.createBitmap(r8, r3, r4)     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            android.renderscript.Allocation$MipmapControl r3 = android.renderscript.Allocation.MipmapControl.MIPMAP_NONE     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            r4 = 2
            android.renderscript.Allocation r3 = android.renderscript.Allocation.createFromBitmap(r0, r7, r3, r4)     // Catch:{ IllegalArgumentException -> 0x00c1, all -> 0x00bb }
            android.renderscript.Allocation r0 = android.renderscript.Allocation.createFromBitmap(r0, r8)     // Catch:{ IllegalArgumentException -> 0x00b8, all -> 0x00b4 }
            r4 = 1103626240(0x41c80000, float:25.0)
            r1.setRadius(r4)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            r1.setInput(r3)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            r1.forEach(r0)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            r0.copyTo(r8)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            androidx.palette.graphics.Palette$Swatch r9 = com.android.systemui.statusbar.notification.MediaNotificationProcessor.findBackgroundSwatch((android.graphics.Bitmap) r9)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            android.graphics.Canvas r4 = new android.graphics.Canvas     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            r4.<init>(r8)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            int r9 = r9.getRgb()     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            r5 = 178(0xb2, float:2.5E-43)
            int r9 = com.android.internal.graphics.ColorUtils.setAlphaComponent(r9, r5)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            r4.drawColor(r9)     // Catch:{ IllegalArgumentException -> 0x00b2, all -> 0x00b0 }
            if (r3 != 0) goto L_0x00a3
            goto L_0x00a6
        L_0x00a3:
            r3.destroy()
        L_0x00a6:
            r0.destroy()
            r1.destroy()
            r7.recycle()
            return r8
        L_0x00b0:
            r8 = move-exception
            goto L_0x00b6
        L_0x00b2:
            r8 = move-exception
            goto L_0x00c4
        L_0x00b4:
            r8 = move-exception
            r0 = r2
        L_0x00b6:
            r2 = r3
            goto L_0x00bd
        L_0x00b8:
            r8 = move-exception
            r0 = r2
            goto L_0x00c4
        L_0x00bb:
            r8 = move-exception
            r0 = r2
        L_0x00bd:
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x00ef
        L_0x00c1:
            r8 = move-exception
            r0 = r2
            r3 = r0
        L_0x00c4:
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x00d0
        L_0x00c8:
            r7 = move-exception
            r8 = r2
            r0 = r8
            goto L_0x00ef
        L_0x00cc:
            r7 = move-exception
            r8 = r2
            r0 = r8
        L_0x00cf:
            r3 = r0
        L_0x00d0:
            java.lang.String r9 = "MediaArtworkProcessor"
            java.lang.String r4 = "error while processing artwork"
            android.util.Log.e(r9, r4, r7)     // Catch:{ all -> 0x00ed }
            if (r3 != 0) goto L_0x00da
            goto L_0x00dd
        L_0x00da:
            r3.destroy()
        L_0x00dd:
            if (r0 != 0) goto L_0x00e0
            goto L_0x00e3
        L_0x00e0:
            r0.destroy()
        L_0x00e3:
            r1.destroy()
            if (r8 != 0) goto L_0x00e9
            goto L_0x00ec
        L_0x00e9:
            r8.recycle()
        L_0x00ec:
            return r2
        L_0x00ed:
            r7 = move-exception
            r2 = r3
        L_0x00ef:
            if (r2 != 0) goto L_0x00f2
            goto L_0x00f5
        L_0x00f2:
            r2.destroy()
        L_0x00f5:
            if (r0 != 0) goto L_0x00f8
            goto L_0x00fb
        L_0x00f8:
            r0.destroy()
        L_0x00fb:
            r1.destroy()
            if (r8 != 0) goto L_0x0101
            goto L_0x0104
        L_0x0101:
            r8.recycle()
        L_0x0104:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.MediaArtworkProcessor.processArtwork(android.content.Context, android.graphics.Bitmap):android.graphics.Bitmap");
    }

    public final void clearCache() {
        Bitmap bitmap = this.mArtworkCache;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mArtworkCache = null;
    }
}

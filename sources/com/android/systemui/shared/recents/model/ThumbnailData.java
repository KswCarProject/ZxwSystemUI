package com.android.systemui.shared.recents.model;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;
import android.util.Log;
import android.window.TaskSnapshot;

public class ThumbnailData {
    public int appearance;
    public Rect insets;
    public boolean isRealSnapshot;
    public boolean isTranslucent;
    public Rect letterboxInsets;
    public int orientation;
    public boolean reducedResolution;
    public int rotation;
    public float scale;
    public long snapshotId;
    public final Bitmap thumbnail;
    public int windowingMode;

    public void recycleBitmap() {
        Bitmap bitmap = this.thumbnail;
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    public static Bitmap makeThumbnail(TaskSnapshot taskSnapshot) {
        HardwareBuffer hardwareBuffer;
        Bitmap bitmap = null;
        try {
            hardwareBuffer = taskSnapshot.getHardwareBuffer();
            if (hardwareBuffer != null) {
                bitmap = Bitmap.wrapHardwareBuffer(hardwareBuffer, taskSnapshot.getColorSpace());
            }
            if (hardwareBuffer != null) {
                hardwareBuffer.close();
            }
        } catch (IllegalArgumentException e) {
            Log.e("ThumbnailData", "Unexpected snapshot without USAGE_GPU_SAMPLED_IMAGE: " + taskSnapshot.getHardwareBuffer(), e);
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        if (bitmap != null) {
            return bitmap;
        }
        Point taskSize = taskSnapshot.getTaskSize();
        Bitmap createBitmap = Bitmap.createBitmap(taskSize.x, taskSize.y, Bitmap.Config.ARGB_8888);
        createBitmap.eraseColor(-16777216);
        return createBitmap;
        throw th;
    }

    public ThumbnailData(TaskSnapshot taskSnapshot) {
        Bitmap makeThumbnail = makeThumbnail(taskSnapshot);
        this.thumbnail = makeThumbnail;
        this.insets = new Rect(taskSnapshot.getContentInsets());
        this.letterboxInsets = new Rect(taskSnapshot.getLetterboxInsets());
        this.orientation = taskSnapshot.getOrientation();
        this.rotation = taskSnapshot.getRotation();
        this.reducedResolution = taskSnapshot.isLowResolution();
        this.scale = ((float) makeThumbnail.getWidth()) / ((float) taskSnapshot.getTaskSize().x);
        this.isRealSnapshot = taskSnapshot.isRealSnapshot();
        this.isTranslucent = taskSnapshot.isTranslucent();
        this.windowingMode = taskSnapshot.getWindowingMode();
        this.appearance = taskSnapshot.getAppearance();
        this.snapshotId = taskSnapshot.getId();
    }
}

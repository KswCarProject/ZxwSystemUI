package com.android.wm.shell.transition;

import android.graphics.ColorSpace;
import android.graphics.GraphicBuffer;
import android.hardware.HardwareBuffer;
import android.view.SurfaceControl;
import android.view.SurfaceSession;

public class WindowThumbnail {
    public SurfaceControl mSurfaceControl;

    public static WindowThumbnail createAndAttach(SurfaceSession surfaceSession, SurfaceControl surfaceControl, HardwareBuffer hardwareBuffer, SurfaceControl.Transaction transaction) {
        WindowThumbnail windowThumbnail = new WindowThumbnail();
        SurfaceControl.Builder parent = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl);
        windowThumbnail.mSurfaceControl = parent.setName("WindowThumanil : " + surfaceControl.toString()).setCallsite("WindowThumanil").setFormat(-3).build();
        transaction.setBuffer(windowThumbnail.mSurfaceControl, GraphicBuffer.createFromHardwareBuffer(hardwareBuffer));
        transaction.setColorSpace(windowThumbnail.mSurfaceControl, ColorSpace.get(ColorSpace.Named.SRGB));
        transaction.setLayer(windowThumbnail.mSurfaceControl, Integer.MAX_VALUE);
        transaction.show(windowThumbnail.mSurfaceControl);
        transaction.apply();
        return windowThumbnail;
    }

    public SurfaceControl getSurface() {
        return this.mSurfaceControl;
    }

    public void destroy(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            transaction.apply();
            this.mSurfaceControl.release();
            this.mSurfaceControl = null;
        }
    }
}

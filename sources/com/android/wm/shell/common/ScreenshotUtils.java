package com.android.wm.shell.common;

import android.graphics.Rect;
import android.view.SurfaceControl;
import java.util.function.Consumer;

public class ScreenshotUtils {
    public static void captureLayer(SurfaceControl surfaceControl, Rect rect, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
        consumer.accept(SurfaceControl.captureLayers(new SurfaceControl.LayerCaptureArgs.Builder(surfaceControl).setSourceCrop(rect).setCaptureSecureLayers(true).setAllowProtected(true).build()));
    }

    public static class BufferConsumer implements Consumer<SurfaceControl.ScreenshotHardwareBuffer> {
        public int mLayer;
        public SurfaceControl mScreenshot = null;
        public SurfaceControl mSurfaceControl;
        public SurfaceControl.Transaction mTransaction;

        public BufferConsumer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, int i) {
            this.mTransaction = transaction;
            this.mSurfaceControl = surfaceControl;
            this.mLayer = i;
        }

        public void accept(SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
            if (screenshotHardwareBuffer != null && screenshotHardwareBuffer.getHardwareBuffer() != null) {
                SurfaceControl build = new SurfaceControl.Builder().setName("ScreenshotUtils screenshot").setFormat(-3).setSecure(screenshotHardwareBuffer.containsSecureLayers()).setCallsite("ScreenshotUtils.takeScreenshot").setBLASTLayer().build();
                this.mScreenshot = build;
                this.mTransaction.setBuffer(build, screenshotHardwareBuffer.getHardwareBuffer());
                this.mTransaction.setColorSpace(this.mScreenshot, screenshotHardwareBuffer.getColorSpace());
                this.mTransaction.reparent(this.mScreenshot, this.mSurfaceControl);
                this.mTransaction.setLayer(this.mScreenshot, this.mLayer);
                this.mTransaction.show(this.mScreenshot);
                this.mTransaction.apply();
            }
        }
    }

    public static SurfaceControl takeScreenshot(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, int i) {
        BufferConsumer bufferConsumer = new BufferConsumer(transaction, surfaceControl, i);
        captureLayer(surfaceControl, rect, bufferConsumer);
        return bufferConsumer.mScreenshot;
    }
}

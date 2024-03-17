package com.android.systemui.screenshot;

import android.graphics.Bitmap;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RenderNode;
import android.media.Image;
import java.util.Objects;

public class ImageTile implements AutoCloseable {
    public static final ColorSpace COLOR_SPACE = ColorSpace.get(ColorSpace.Named.SRGB);
    public final Image mImage;
    public final Rect mLocation;
    public RenderNode mNode;

    public ImageTile(Image image, Rect rect) {
        Objects.requireNonNull(image, "image");
        Image image2 = image;
        this.mImage = image;
        Objects.requireNonNull(rect);
        Rect rect2 = rect;
        this.mLocation = rect;
        Objects.requireNonNull(image.getHardwareBuffer(), "image must be a hardware image");
    }

    public synchronized RenderNode getDisplayList() {
        if (this.mNode == null) {
            this.mNode = new RenderNode("Tile{" + Integer.toHexString(this.mImage.hashCode()) + "}");
        }
        if (this.mNode.hasDisplayList()) {
            return this.mNode;
        }
        int min = Math.min(this.mImage.getWidth(), this.mLocation.width());
        int min2 = Math.min(this.mImage.getHeight(), this.mLocation.height());
        this.mNode.setPosition(0, 0, min, min2);
        RecordingCanvas beginRecording = this.mNode.beginRecording(min, min2);
        beginRecording.save();
        beginRecording.clipRect(0, 0, this.mLocation.width(), this.mLocation.height());
        beginRecording.drawBitmap(Bitmap.wrapHardwareBuffer(this.mImage.getHardwareBuffer(), COLOR_SPACE), 0.0f, 0.0f, (Paint) null);
        beginRecording.restore();
        this.mNode.endRecording();
        return this.mNode;
    }

    public Rect getLocation() {
        return this.mLocation;
    }

    public int getLeft() {
        return this.mLocation.left;
    }

    public int getTop() {
        return this.mLocation.top;
    }

    public synchronized void close() {
        this.mImage.close();
        RenderNode renderNode = this.mNode;
        if (renderNode != null) {
            renderNode.discardDisplayList();
        }
    }

    public String toString() {
        return "{location=" + this.mLocation + ", source=" + this.mImage + ", buffer=" + this.mImage.getHardwareBuffer() + "}";
    }
}

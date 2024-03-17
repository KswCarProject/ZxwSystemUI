package com.android.systemui.screenshot;

import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.android.internal.util.CallbackRegistry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageTileSet {
    public CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> mContentListeners;
    public final Handler mHandler;
    public final Region mRegion = new Region();
    public final List<ImageTile> mTiles = new ArrayList();

    public interface OnContentChangedListener {
        void onContentChanged();
    }

    public ImageTileSet(Handler handler) {
        this.mHandler = handler;
    }

    public void addOnContentChangedListener(OnContentChangedListener onContentChangedListener) {
        if (this.mContentListeners == null) {
            this.mContentListeners = new CallbackRegistry<>(new CallbackRegistry.NotifierCallback<OnContentChangedListener, ImageTileSet, Rect>() {
                public void onNotifyCallback(OnContentChangedListener onContentChangedListener, ImageTileSet imageTileSet, int i, Rect rect) {
                    onContentChangedListener.onContentChanged();
                }
            });
        }
        this.mContentListeners.add(onContentChangedListener);
    }

    /* renamed from: addTile */
    public void lambda$addTile$0(ImageTile imageTile) {
        if (!this.mHandler.getLooper().isCurrentThread()) {
            this.mHandler.post(new ImageTileSet$$ExternalSyntheticLambda0(this, imageTile));
            return;
        }
        this.mTiles.add(imageTile);
        this.mRegion.op(imageTile.getLocation(), this.mRegion, Region.Op.UNION);
        notifyContentChanged();
    }

    public final void notifyContentChanged() {
        CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> callbackRegistry = this.mContentListeners;
        if (callbackRegistry != null) {
            callbackRegistry.notifyCallbacks(this, 0, (Object) null);
        }
    }

    public Drawable getDrawable() {
        return new TiledImageDrawable(this);
    }

    public int size() {
        return this.mTiles.size();
    }

    public Rect getGaps() {
        Region region = new Region();
        region.op(this.mRegion.getBounds(), this.mRegion, Region.Op.DIFFERENCE);
        return region.getBounds();
    }

    public ImageTile get(int i) {
        return this.mTiles.get(i);
    }

    public Bitmap toBitmap() {
        return toBitmap(new Rect(0, 0, getWidth(), getHeight()));
    }

    public Bitmap toBitmap(Rect rect) {
        if (this.mTiles.isEmpty()) {
            return null;
        }
        RenderNode renderNode = new RenderNode("Bitmap Export");
        renderNode.setPosition(0, 0, rect.width(), rect.height());
        RecordingCanvas beginRecording = renderNode.beginRecording();
        Drawable drawable = getDrawable();
        drawable.setBounds(rect);
        drawable.draw(beginRecording);
        renderNode.endRecording();
        return HardwareRenderer.createHardwareBitmap(renderNode, rect.width(), rect.height());
    }

    public int getLeft() {
        return this.mRegion.getBounds().left;
    }

    public int getTop() {
        return this.mRegion.getBounds().top;
    }

    public int getBottom() {
        return this.mRegion.getBounds().bottom;
    }

    public int getWidth() {
        return this.mRegion.getBounds().width();
    }

    public int getHeight() {
        return this.mRegion.getBounds().height();
    }

    public void clear() {
        if (!this.mTiles.isEmpty()) {
            this.mRegion.setEmpty();
            Iterator<ImageTile> it = this.mTiles.iterator();
            while (it.hasNext()) {
                it.next().close();
                it.remove();
            }
            notifyContentChanged();
        }
    }
}

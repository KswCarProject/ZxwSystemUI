package com.android.systemui.glwallpaper;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.util.Log;
import android.util.Size;
import com.android.systemui.R$raw;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ImageWallpaperRenderer {
    public static final String TAG = "ImageWallpaperRenderer";
    public Consumer<Bitmap> mOnBitmapUpdated;
    public final ImageGLProgram mProgram;
    public final Rect mSurfaceSize = new Rect();
    public final WallpaperTexture mTexture;
    public final ImageGLWallpaper mWallpaper;

    public void finish() {
    }

    public ImageWallpaperRenderer(Context context) {
        WallpaperManager wallpaperManager = (WallpaperManager) context.getSystemService(WallpaperManager.class);
        if (wallpaperManager == null) {
            Log.w(TAG, "WallpaperManager not available");
        }
        this.mTexture = new WallpaperTexture(wallpaperManager);
        ImageGLProgram imageGLProgram = new ImageGLProgram(context);
        this.mProgram = imageGLProgram;
        this.mWallpaper = new ImageGLWallpaper(imageGLProgram);
    }

    public void setOnBitmapChanged(Consumer<Bitmap> consumer) {
        this.mOnBitmapUpdated = consumer;
    }

    public void use(Consumer<Bitmap> consumer) {
        this.mTexture.use(consumer);
    }

    public boolean isWcgContent() {
        return this.mTexture.isWcgContent();
    }

    public void onSurfaceCreated() {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        this.mProgram.useGLProgram(R$raw.image_wallpaper_vertex_shader, R$raw.image_wallpaper_fragment_shader);
        this.mTexture.use(new ImageWallpaperRenderer$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSurfaceCreated$0(Bitmap bitmap) {
        if (bitmap == null) {
            Log.w(TAG, "reload texture failed!");
        } else {
            Consumer<Bitmap> consumer = this.mOnBitmapUpdated;
            if (consumer != null) {
                consumer.accept(bitmap);
            }
        }
        this.mWallpaper.setup(bitmap);
    }

    public void onSurfaceChanged(int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
    }

    public void onDrawFrame() {
        GLES20.glClear(16384);
        GLES20.glViewport(0, 0, this.mSurfaceSize.width(), this.mSurfaceSize.height());
        this.mWallpaper.useTexture();
        this.mWallpaper.draw();
    }

    public Size reportSurfaceSize() {
        this.mSurfaceSize.set(this.mTexture.getTextureDimensions());
        return new Size(this.mSurfaceSize.width(), this.mSurfaceSize.height());
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print(str);
        printWriter.print("mSurfaceSize=");
        printWriter.print(this.mSurfaceSize);
        printWriter.print(str);
        printWriter.print("mWcgContent=");
        printWriter.print(isWcgContent());
        this.mWallpaper.dump(str, fileDescriptor, printWriter, strArr);
    }

    public static class WallpaperTexture {
        public Bitmap mBitmap;
        public final Rect mDimensions;
        public final AtomicInteger mRefCount;
        public boolean mTextureUsed;
        public final WallpaperManager mWallpaperManager;
        public boolean mWcgContent;

        public WallpaperTexture(WallpaperManager wallpaperManager) {
            this.mWallpaperManager = wallpaperManager;
            this.mRefCount = new AtomicInteger();
            this.mDimensions = new Rect();
        }

        public void use(Consumer<Bitmap> consumer) {
            Bitmap bitmap;
            this.mRefCount.incrementAndGet();
            synchronized (this.mRefCount) {
                if (this.mBitmap == null) {
                    this.mBitmap = this.mWallpaperManager.getBitmapAsUser(-2, false);
                    this.mWcgContent = this.mWallpaperManager.wallpaperSupportsWcg(1);
                    this.mWallpaperManager.forgetLoadedWallpaper();
                    Bitmap bitmap2 = this.mBitmap;
                    if (bitmap2 != null) {
                        this.mDimensions.set(0, 0, bitmap2.getWidth(), this.mBitmap.getHeight());
                        this.mTextureUsed = true;
                    } else {
                        Log.w(ImageWallpaperRenderer.TAG, "Can't get bitmap");
                    }
                }
            }
            if (consumer != null) {
                consumer.accept(this.mBitmap);
            }
            synchronized (this.mRefCount) {
                if (this.mRefCount.decrementAndGet() == 0 && (bitmap = this.mBitmap) != null) {
                    bitmap.recycle();
                    this.mBitmap = null;
                }
            }
        }

        public final boolean isWcgContent() {
            return this.mWcgContent;
        }

        public final String getHash() {
            Bitmap bitmap = this.mBitmap;
            return bitmap != null ? Integer.toHexString(bitmap.hashCode()) : "null";
        }

        public final Rect getTextureDimensions() {
            if (!this.mTextureUsed) {
                this.mDimensions.set(this.mWallpaperManager.peekBitmapDimensions());
            }
            return this.mDimensions;
        }

        public String toString() {
            return "{" + getHash() + ", " + this.mRefCount.get() + "}";
        }
    }
}

package com.android.systemui;

import android.app.WallpaperColors;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.service.wallpaper.WallpaperService;
import android.util.ArraySet;
import android.util.Log;
import android.util.MathUtils;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.glwallpaper.ImageWallpaperRenderer;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ImageWallpaper extends WallpaperService {
    public static final RectF LOCAL_COLOR_BOUNDS = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
    public static final String TAG = ImageWallpaper.class.getSimpleName();
    public final ArraySet<RectF> mColorAreas = new ArraySet<>();
    public final ArrayList<RectF> mLocalColorsToAdd = new ArrayList<>();
    public Bitmap mMiniBitmap;
    public volatile int mPages = 1;
    public HandlerThread mWorker;

    public void onCreate() {
        super.onCreate();
        HandlerThread handlerThread = new HandlerThread(TAG);
        this.mWorker = handlerThread;
        handlerThread.start();
    }

    public WallpaperService.Engine onCreateEngine() {
        return new GLEngine();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mWorker.quitSafely();
        this.mWorker = null;
        this.mMiniBitmap = null;
    }

    public class GLEngine extends WallpaperService.Engine implements DisplayManager.DisplayListener {
        @VisibleForTesting
        public static final int MIN_SURFACE_HEIGHT = 128;
        @VisibleForTesting
        public static final int MIN_SURFACE_WIDTH = 128;
        public int mDisplayHeight = 1;
        public boolean mDisplaySizeValid = false;
        public int mDisplayWidth = 1;
        public EglHelper mEglHelper;
        public final Runnable mFinishRenderingTask = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda5(this);
        public int mImgHeight = 1;
        public int mImgWidth = 1;
        public ImageWallpaperRenderer mRenderer;

        public void onDisplayAdded(int i) {
        }

        public void onDisplayRemoved(int i) {
        }

        public boolean shouldWaitForEngineShown() {
            return true;
        }

        public boolean shouldZoomOutWallpaper() {
            return true;
        }

        public boolean supportsLocalColorExtraction() {
            return true;
        }

        public GLEngine() {
            super(ImageWallpaper.this);
        }

        @VisibleForTesting
        public GLEngine(Handler handler) {
            super(ImageWallpaper.this, new ImageWallpaper$GLEngine$$ExternalSyntheticLambda8(), handler);
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper.Engine#onCreate");
            this.mEglHelper = getEglHelperInstance();
            this.mRenderer = getRendererInstance();
            setFixedSizeAllowed(true);
            updateSurfaceSize();
            setShowForAllUsers(true);
            this.mRenderer.setOnBitmapChanged(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda4(this));
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).registerDisplayListener(this, ImageWallpaper.this.mWorker.getThreadHandler());
            Trace.endSection();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onCreate$0(Bitmap bitmap) {
            ImageWallpaper.this.mLocalColorsToAdd.addAll(ImageWallpaper.this.mColorAreas);
            if (ImageWallpaper.this.mLocalColorsToAdd.size() > 0) {
                updateMiniBitmapAndNotify(bitmap);
            }
        }

        public void onDisplayChanged(int i) {
            if (i == getDisplayContext().getDisplayId()) {
                this.mDisplaySizeValid = false;
            }
        }

        public EglHelper getEglHelperInstance() {
            return new EglHelper();
        }

        public ImageWallpaperRenderer getRendererInstance() {
            return new ImageWallpaperRenderer(getDisplayContext());
        }

        public void onOffsetsChanged(float f, float f2, float f3, float f4, int i, int i2) {
            int i3 = 1;
            if (f3 > 0.0f && f3 <= 1.0f) {
                i3 = 1 + Math.round(1.0f / f3);
            }
            if (i3 != ImageWallpaper.this.mPages) {
                ImageWallpaper.this.mPages = i3;
                if (ImageWallpaper.this.mMiniBitmap != null && !ImageWallpaper.this.mMiniBitmap.isRecycled()) {
                    ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda3(this));
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onOffsetsChanged$1() {
            computeAndNotifyLocalColors(new ArrayList(ImageWallpaper.this.mColorAreas), ImageWallpaper.this.mMiniBitmap);
        }

        public final void updateMiniBitmapAndNotify(Bitmap bitmap) {
            if (bitmap != null) {
                int min = Math.min(bitmap.getWidth(), bitmap.getHeight());
                float f = min > 128 ? 128.0f / ((float) min) : 1.0f;
                this.mImgHeight = bitmap.getHeight();
                this.mImgWidth = bitmap.getWidth();
                ImageWallpaper.this.mMiniBitmap = Bitmap.createScaledBitmap(bitmap, (int) Math.max(((float) bitmap.getWidth()) * f, 1.0f), (int) Math.max(f * ((float) bitmap.getHeight()), 1.0f), false);
                computeAndNotifyLocalColors(ImageWallpaper.this.mLocalColorsToAdd, ImageWallpaper.this.mMiniBitmap);
                ImageWallpaper.this.mLocalColorsToAdd.clear();
            }
        }

        public final void updateSurfaceSize() {
            Trace.beginSection("ImageWallpaper#updateSurfaceSize");
            SurfaceHolder surfaceHolder = getSurfaceHolder();
            Size reportSurfaceSize = this.mRenderer.reportSurfaceSize();
            surfaceHolder.setFixedSize(Math.max(128, reportSurfaceSize.getWidth()), Math.max(128, reportSurfaceSize.getHeight()));
            Trace.endSection();
        }

        public void onDestroy() {
            ((DisplayManager) getDisplayContext().getSystemService(DisplayManager.class)).unregisterDisplayListener(this);
            ImageWallpaper.this.mMiniBitmap = null;
            ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda9(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDestroy$2() {
            this.mRenderer.finish();
            this.mRenderer = null;
            this.mEglHelper.finish();
            this.mEglHelper = null;
        }

        public void addLocalColorsAreas(List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, list));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$addLocalColorsAreas$3(List list) {
            if (ImageWallpaper.this.mColorAreas.size() + ImageWallpaper.this.mLocalColorsToAdd.size() == 0) {
                setOffsetNotificationsEnabled(true);
            }
            Bitmap r0 = ImageWallpaper.this.mMiniBitmap;
            if (r0 == null) {
                ImageWallpaper.this.mLocalColorsToAdd.addAll(list);
                ImageWallpaperRenderer imageWallpaperRenderer = this.mRenderer;
                if (imageWallpaperRenderer != null) {
                    imageWallpaperRenderer.use(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda10(this));
                    return;
                }
                return;
            }
            computeAndNotifyLocalColors(list, r0);
        }

        public final void computeAndNotifyLocalColors(List<RectF> list, Bitmap bitmap) {
            List<WallpaperColors> localWallpaperColors = getLocalWallpaperColors(list, bitmap);
            ImageWallpaper.this.mColorAreas.addAll(list);
            try {
                notifyLocalColorsChanged(list, localWallpaperColors);
            } catch (RuntimeException e) {
                Log.e(ImageWallpaper.TAG, e.getMessage(), e);
            }
        }

        public void removeLocalColorsAreas(List<RectF> list) {
            ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda6(this, list));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$removeLocalColorsAreas$4(List list) {
            ImageWallpaper.this.mColorAreas.removeAll(list);
            ImageWallpaper.this.mLocalColorsToAdd.removeAll(list);
            if (ImageWallpaper.this.mColorAreas.size() + ImageWallpaper.this.mLocalColorsToAdd.size() == 0) {
                setOffsetNotificationsEnabled(false);
            }
        }

        public final RectF pageToImgRect(RectF rectF) {
            int i;
            int i2;
            if (!this.mDisplaySizeValid) {
                Rect bounds = ((WindowManager) getDisplayContext().getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
                this.mDisplayWidth = bounds.width();
                this.mDisplayHeight = bounds.height();
                this.mDisplaySizeValid = true;
            }
            float r0 = 1.0f / ((float) ImageWallpaper.this.mPages);
            float f = (rectF.left % r0) / r0;
            float f2 = (rectF.right % r0) / r0;
            int floor = (int) Math.floor((double) (rectF.centerX() / r0));
            RectF rectF2 = new RectF();
            if (this.mImgWidth != 0 && (i = this.mImgHeight) != 0 && this.mDisplayWidth > 0 && (i2 = this.mDisplayHeight) > 0) {
                rectF2.bottom = rectF.bottom;
                rectF2.top = rectF.top;
                float min = ((float) this.mDisplayWidth) * Math.min(((float) i) / ((float) i2), 1.0f);
                int i3 = this.mImgWidth;
                float min2 = Math.min(1.0f, i3 > 0 ? min / ((float) i3) : 1.0f);
                float r9 = ((float) floor) * ((1.0f - min2) / ((float) (ImageWallpaper.this.mPages - 1)));
                rectF2.left = MathUtils.constrain((f * min2) + r9, 0.0f, 1.0f);
                float constrain = MathUtils.constrain((f2 * min2) + r9, 0.0f, 1.0f);
                rectF2.right = constrain;
                if (rectF2.left > constrain) {
                    rectF2.left = 0.0f;
                    rectF2.right = 1.0f;
                }
            }
            return rectF2;
        }

        public final List<WallpaperColors> getLocalWallpaperColors(List<RectF> list, Bitmap bitmap) {
            ArrayList arrayList = new ArrayList(list.size());
            for (int i = 0; i < list.size(); i++) {
                RectF pageToImgRect = pageToImgRect(list.get(i));
                if (pageToImgRect == null || !ImageWallpaper.LOCAL_COLOR_BOUNDS.contains(pageToImgRect)) {
                    arrayList.add((Object) null);
                } else {
                    Rect rect = new Rect((int) Math.floor((double) (pageToImgRect.left * ((float) bitmap.getWidth()))), (int) Math.floor((double) (pageToImgRect.top * ((float) bitmap.getHeight()))), (int) Math.ceil((double) (pageToImgRect.right * ((float) bitmap.getWidth()))), (int) Math.ceil((double) (pageToImgRect.bottom * ((float) bitmap.getHeight()))));
                    if (rect.isEmpty()) {
                        arrayList.add((Object) null);
                    } else {
                        arrayList.add(WallpaperColors.fromBitmap(Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())));
                    }
                }
            }
            return arrayList;
        }

        public void onSurfaceCreated(SurfaceHolder surfaceHolder) {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda7(this, surfaceHolder));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceCreated$5(SurfaceHolder surfaceHolder) {
            Trace.beginSection("ImageWallpaper#onSurfaceCreated");
            this.mEglHelper.init(surfaceHolder, needSupportWideColorGamut());
            this.mRenderer.onSurfaceCreated();
            Trace.endSection();
        }

        public void onSurfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda2(this, i2, i3));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSurfaceChanged$6(int i, int i2) {
            this.mRenderer.onSurfaceChanged(i, i2);
        }

        public void onSurfaceRedrawNeeded(SurfaceHolder surfaceHolder) {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().post(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda1(this));
            }
        }

        public final void drawFrame() {
            Trace.beginSection("ImageWallpaper#drawFrame");
            preRender();
            requestRender();
            postRender();
            Trace.endSection();
        }

        public void preRender() {
            Trace.beginSection("ImageWallpaper#preRender");
            preRenderInternal();
            Trace.endSection();
        }

        public final void preRenderInternal() {
            boolean z;
            Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
            cancelFinishRenderingTask();
            if (!this.mEglHelper.hasEglContext()) {
                this.mEglHelper.destroyEglSurface();
                if (!this.mEglHelper.createEglContext()) {
                    Log.w(ImageWallpaper.TAG, "recreate egl context failed!");
                } else {
                    z = true;
                    if (this.mEglHelper.hasEglContext() && !this.mEglHelper.hasEglSurface() && !this.mEglHelper.createEglSurface(getSurfaceHolder(), needSupportWideColorGamut())) {
                        Log.w(ImageWallpaper.TAG, "recreate egl surface failed!");
                    }
                    if (!this.mEglHelper.hasEglContext() && !this.mEglHelper.hasEglSurface() && z) {
                        this.mRenderer.onSurfaceCreated();
                        this.mRenderer.onSurfaceChanged(surfaceFrame.width(), surfaceFrame.height());
                        return;
                    }
                }
            }
            z = false;
            Log.w(ImageWallpaper.TAG, "recreate egl surface failed!");
            if (!this.mEglHelper.hasEglContext() && !this.mEglHelper.hasEglSurface()) {
            }
        }

        public void requestRender() {
            Trace.beginSection("ImageWallpaper#requestRender");
            requestRenderInternal();
            Trace.endSection();
        }

        public final void requestRenderInternal() {
            Rect surfaceFrame = getSurfaceHolder().getSurfaceFrame();
            if (this.mEglHelper.hasEglContext() && this.mEglHelper.hasEglSurface() && surfaceFrame.width() > 0 && surfaceFrame.height() > 0) {
                this.mRenderer.onDrawFrame();
                if (!this.mEglHelper.swapBuffer()) {
                    Log.e(ImageWallpaper.TAG, "drawFrame failed!");
                    return;
                }
                return;
            }
            String r1 = ImageWallpaper.TAG;
            Log.e(r1, "requestRender: not ready, has context=" + this.mEglHelper.hasEglContext() + ", has surface=" + this.mEglHelper.hasEglSurface() + ", frame=" + surfaceFrame);
        }

        public void postRender() {
            scheduleFinishRendering();
            reportEngineShown(false);
        }

        public final void cancelFinishRenderingTask() {
            if (ImageWallpaper.this.mWorker != null) {
                ImageWallpaper.this.mWorker.getThreadHandler().removeCallbacks(this.mFinishRenderingTask);
            }
        }

        public final void scheduleFinishRendering() {
            if (ImageWallpaper.this.mWorker != null) {
                cancelFinishRenderingTask();
                ImageWallpaper.this.mWorker.getThreadHandler().postDelayed(this.mFinishRenderingTask, 1000);
            }
        }

        public final void finishRendering() {
            Trace.beginSection("ImageWallpaper#finishRendering");
            EglHelper eglHelper = this.mEglHelper;
            if (eglHelper != null) {
                eglHelper.destroyEglSurface();
                this.mEglHelper.destroyEglContext();
            }
            Trace.endSection();
        }

        public final boolean needSupportWideColorGamut() {
            return this.mRenderer.isWcgContent();
        }

        public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            super.dump(str, fileDescriptor, printWriter, strArr);
            printWriter.print(str);
            printWriter.print("Engine=");
            printWriter.println(this);
            printWriter.print(str);
            printWriter.print("valid surface=");
            Object obj = "null";
            printWriter.println((getSurfaceHolder() == null || getSurfaceHolder().getSurface() == null) ? obj : Boolean.valueOf(getSurfaceHolder().getSurface().isValid()));
            printWriter.print(str);
            printWriter.print("surface frame=");
            if (getSurfaceHolder() != null) {
                obj = getSurfaceHolder().getSurfaceFrame();
            }
            printWriter.println(obj);
            this.mEglHelper.dump(str, fileDescriptor, printWriter, strArr);
            this.mRenderer.dump(str, fileDescriptor, printWriter, strArr);
        }
    }
}

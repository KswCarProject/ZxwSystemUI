package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class ScrollCaptureController {
    public static final String TAG = LogConfig.logTag(ScrollCaptureController.class);
    public final Executor mBgExecutor;
    public volatile boolean mCancelled;
    public CallbackToFutureAdapter.Completer<LongScreenshot> mCaptureCompleter;
    public final ScrollCaptureClient mClient;
    public final Context mContext;
    public ListenableFuture<Void> mEndFuture;
    public final UiEventLogger mEventLogger;
    public boolean mFinishOnBoundary;
    public final ImageTileSet mImageTileSet;
    public boolean mScrollingUp = true;
    public ScrollCaptureClient.Session mSession;
    public ListenableFuture<ScrollCaptureClient.Session> mSessionFuture;
    public ListenableFuture<ScrollCaptureClient.CaptureResult> mTileFuture;
    public String mWindowOwner;

    @VisibleForTesting
    public float getTargetTopSizeRatio() {
        return 0.4f;
    }

    public static class LongScreenshot {
        public final ImageTileSet mImageTileSet;
        public final ScrollCaptureClient.Session mSession;

        public LongScreenshot(ScrollCaptureClient.Session session, ImageTileSet imageTileSet) {
            this.mSession = session;
            this.mImageTileSet = imageTileSet;
        }

        public Bitmap toBitmap() {
            return this.mImageTileSet.toBitmap();
        }

        public void release() {
            this.mImageTileSet.clear();
            this.mSession.release();
        }

        public int getLeft() {
            return this.mImageTileSet.getLeft();
        }

        public int getTop() {
            return this.mImageTileSet.getTop();
        }

        public int getBottom() {
            return this.mImageTileSet.getBottom();
        }

        public int getWidth() {
            return this.mImageTileSet.getWidth();
        }

        public int getHeight() {
            return this.mImageTileSet.getHeight();
        }

        public int getPageHeight() {
            return this.mSession.getPageHeight();
        }

        public String toString() {
            return "LongScreenshot{w=" + this.mImageTileSet.getWidth() + ", h=" + this.mImageTileSet.getHeight() + "}";
        }

        public Drawable getDrawable() {
            return this.mImageTileSet.getDrawable();
        }
    }

    public ScrollCaptureController(Context context, Executor executor, ScrollCaptureClient scrollCaptureClient, ImageTileSet imageTileSet, UiEventLogger uiEventLogger) {
        this.mContext = context;
        this.mBgExecutor = executor;
        this.mClient = scrollCaptureClient;
        this.mImageTileSet = imageTileSet;
        this.mEventLogger = uiEventLogger;
    }

    public ListenableFuture<LongScreenshot> run(ScrollCaptureResponse scrollCaptureResponse) {
        this.mCancelled = false;
        return CallbackToFutureAdapter.getFuture(new ScrollCaptureController$$ExternalSyntheticLambda0(this, scrollCaptureResponse));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$run$1(ScrollCaptureResponse scrollCaptureResponse, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mCaptureCompleter = completer;
        this.mWindowOwner = scrollCaptureResponse.getPackageName();
        this.mCaptureCompleter.addCancellationListener(new ScrollCaptureController$$ExternalSyntheticLambda1(this), this.mBgExecutor);
        this.mBgExecutor.execute(new ScrollCaptureController$$ExternalSyntheticLambda2(this, scrollCaptureResponse));
        return "<batch scroll capture>";
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$run$0(ScrollCaptureResponse scrollCaptureResponse) {
        ListenableFuture<ScrollCaptureClient.Session> start = this.mClient.start(scrollCaptureResponse, Settings.Secure.getFloat(this.mContext.getContentResolver(), "screenshot.scroll_max_pages", 3.0f));
        this.mSessionFuture = start;
        start.addListener(new ScrollCaptureController$$ExternalSyntheticLambda3(this), this.mContext.getMainExecutor());
    }

    public final void onCancelled() {
        this.mCancelled = true;
        ListenableFuture<ScrollCaptureClient.Session> listenableFuture = this.mSessionFuture;
        if (listenableFuture != null) {
            listenableFuture.cancel(true);
        }
        ListenableFuture<ScrollCaptureClient.CaptureResult> listenableFuture2 = this.mTileFuture;
        if (listenableFuture2 != null) {
            listenableFuture2.cancel(true);
        }
        ScrollCaptureClient.Session session = this.mSession;
        if (session != null) {
            session.end();
        }
        this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
    }

    public final void onStartComplete() {
        try {
            this.mSession = this.mSessionFuture.get();
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_STARTED, 0, this.mWindowOwner);
            requestNextTile(0);
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "session start failed!");
            this.mCaptureCompleter.setException(e);
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
        }
    }

    public final void requestNextTile(int i) {
        if (this.mCancelled) {
            Log.d(TAG, "requestNextTile: CANCELLED");
            return;
        }
        ListenableFuture<ScrollCaptureClient.CaptureResult> requestTile = this.mSession.requestTile(i);
        this.mTileFuture = requestTile;
        requestTile.addListener(new ScrollCaptureController$$ExternalSyntheticLambda4(this), this.mBgExecutor);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestNextTile$2() {
        try {
            onCaptureResult(this.mTileFuture.get());
        } catch (CancellationException unused) {
            Log.e(TAG, "requestTile cancelled");
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, "requestTile failed!", e);
            this.mCaptureCompleter.setException(e);
        }
    }

    public final void onCaptureResult(ScrollCaptureClient.CaptureResult captureResult) {
        int i;
        int top;
        int tileHeight;
        boolean z = captureResult.captured.height() == 0;
        if (z) {
            if (this.mFinishOnBoundary) {
                finishCapture();
                return;
            }
            this.mImageTileSet.clear();
            this.mFinishOnBoundary = true;
            this.mScrollingUp = !this.mScrollingUp;
        } else if (this.mImageTileSet.size() + 1 >= this.mSession.getMaxTiles()) {
            finishCapture();
            return;
        } else if (this.mScrollingUp && !this.mFinishOnBoundary && ((float) (this.mImageTileSet.getHeight() + captureResult.captured.height())) >= ((float) this.mSession.getTargetHeight()) * 0.4f) {
            this.mImageTileSet.clear();
            this.mScrollingUp = false;
        }
        if (!z) {
            this.mImageTileSet.lambda$addTile$0(new ImageTile(captureResult.image, captureResult.captured));
        }
        Rect gaps = this.mImageTileSet.getGaps();
        if (!gaps.isEmpty()) {
            requestNextTile(gaps.top);
        } else if (this.mImageTileSet.getHeight() >= this.mSession.getTargetHeight()) {
            finishCapture();
        } else {
            if (z) {
                if (this.mScrollingUp) {
                    top = captureResult.requested.top;
                    tileHeight = this.mSession.getTileHeight();
                } else {
                    i = captureResult.requested.bottom;
                    requestNextTile(i);
                }
            } else if (this.mScrollingUp) {
                top = this.mImageTileSet.getTop();
                tileHeight = this.mSession.getTileHeight();
            } else {
                i = this.mImageTileSet.getBottom();
                requestNextTile(i);
            }
            i = top - tileHeight;
            requestNextTile(i);
        }
    }

    public final void finishCapture() {
        if (this.mImageTileSet.getHeight() > 0) {
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_COMPLETED, 0, this.mWindowOwner);
        } else {
            this.mEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_FAILURE, 0, this.mWindowOwner);
        }
        ListenableFuture<Void> end = this.mSession.end();
        this.mEndFuture = end;
        end.addListener(new ScrollCaptureController$$ExternalSyntheticLambda5(this), this.mContext.getMainExecutor());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finishCapture$3() {
        this.mCaptureCompleter.set(new LongScreenshot(this.mSession, this.mImageTileSet));
    }
}

package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Rect;
import android.media.Image;
import android.media.ImageReader;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.RemoteException;
import android.util.Log;
import android.view.IScrollCaptureCallbacks;
import android.view.IScrollCaptureConnection;
import android.view.IScrollCaptureResponseListener;
import android.view.IWindowManager;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.annotations.VisibleForTesting;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Objects;
import java.util.concurrent.Executor;

public class ScrollCaptureClient {
    @VisibleForTesting
    public static final int MATCH_ANY_TASK = -1;
    public static final String TAG = LogConfig.logTag(ScrollCaptureClient.class);
    public final Executor mBgExecutor;
    public IBinder mHostWindowToken;
    public final IWindowManager mWindowManagerService;

    public interface Session {
        ListenableFuture<Void> end();

        int getMaxTiles();

        int getPageHeight();

        int getTargetHeight();

        int getTileHeight();

        void release();

        ListenableFuture<CaptureResult> requestTile(int i);
    }

    public static class CaptureResult {
        public final Rect captured;
        public final Image image;
        public final Rect requested;

        public CaptureResult(Image image2, Rect rect, Rect rect2) {
            this.image = image2;
            this.requested = rect;
            this.captured = rect2;
        }

        public String toString() {
            return "CaptureResult{requested=" + this.requested + " (" + this.requested.width() + "x" + this.requested.height() + "), captured=" + this.captured + " (" + this.captured.width() + "x" + this.captured.height() + "), image=" + this.image + '}';
        }
    }

    public ScrollCaptureClient(IWindowManager iWindowManager, Executor executor, Context context) {
        Objects.requireNonNull(context.getDisplay(), "context must be associated with a Display!");
        this.mBgExecutor = executor;
        this.mWindowManagerService = iWindowManager;
    }

    public void setHostWindowToken(IBinder iBinder) {
        this.mHostWindowToken = iBinder;
    }

    public ListenableFuture<ScrollCaptureResponse> request(int i) {
        return request(i, -1);
    }

    public ListenableFuture<ScrollCaptureResponse> request(int i, int i2) {
        return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$$ExternalSyntheticLambda1(this, i, i2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$request$0(int i, int i2, final CallbackToFutureAdapter.Completer completer) throws Exception {
        try {
            this.mWindowManagerService.requestScrollCapture(i, this.mHostWindowToken, i2, new IScrollCaptureResponseListener.Stub() {
                public void onScrollCaptureResponse(ScrollCaptureResponse scrollCaptureResponse) {
                    completer.set(scrollCaptureResponse);
                }
            });
        } catch (RemoteException e) {
            completer.setException(e);
        }
        return "ScrollCaptureClient#request(displayId=" + i + ", taskId=" + i2 + ")";
    }

    public ListenableFuture<Session> start(ScrollCaptureResponse scrollCaptureResponse, float f) {
        return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$$ExternalSyntheticLambda0(this, scrollCaptureResponse.getConnection(), scrollCaptureResponse, f));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$start$1(IScrollCaptureConnection iScrollCaptureConnection, ScrollCaptureResponse scrollCaptureResponse, float f, CallbackToFutureAdapter.Completer completer) throws Exception {
        if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
            completer.setException(new DeadObjectException("No active connection!"));
            return "";
        }
        new SessionWrapper(iScrollCaptureConnection, scrollCaptureResponse.getWindowBounds(), scrollCaptureResponse.getBoundsInWindow(), f, this.mBgExecutor).start(completer);
        return "IScrollCaptureCallbacks#onCaptureStarted";
    }

    public static class SessionWrapper extends IScrollCaptureCallbacks.Stub implements Session, IBinder.DeathRecipient, ImageReader.OnImageAvailableListener {
        public final Executor mBgExecutor;
        public final Rect mBoundsInWindow;
        public ICancellationSignal mCancellationSignal;
        public Rect mCapturedArea;
        public Image mCapturedImage;
        public IScrollCaptureConnection mConnection;
        public CallbackToFutureAdapter.Completer<Void> mEndCompleter;
        public final Object mLock;
        public ImageReader mReader;
        public Rect mRequestRect;
        public CallbackToFutureAdapter.Completer<Session> mStartCompleter;
        public boolean mStarted;
        public final int mTargetHeight;
        public final int mTileHeight;
        public CallbackToFutureAdapter.Completer<CaptureResult> mTileRequestCompleter;
        public final int mTileWidth;
        public final Rect mWindowBounds;

        public int getMaxTiles() {
            return 30;
        }

        public SessionWrapper(IScrollCaptureConnection iScrollCaptureConnection, Rect rect, Rect rect2, float f, Executor executor) throws RemoteException {
            this.mLock = new Object();
            Objects.requireNonNull(iScrollCaptureConnection);
            IScrollCaptureConnection iScrollCaptureConnection2 = iScrollCaptureConnection;
            this.mConnection = iScrollCaptureConnection2;
            iScrollCaptureConnection2.asBinder().linkToDeath(this, 0);
            Objects.requireNonNull(rect);
            Rect rect3 = rect;
            this.mWindowBounds = rect;
            Objects.requireNonNull(rect2);
            Rect rect4 = rect2;
            this.mBoundsInWindow = rect2;
            int min = Math.min(4194304, (rect2.width() * rect2.height()) / 2);
            this.mTileWidth = rect2.width();
            this.mTileHeight = min / rect2.width();
            this.mTargetHeight = (int) (((float) rect2.height()) * f);
            this.mBgExecutor = executor;
        }

        public void binderDied() {
            Log.d(ScrollCaptureClient.TAG, "binderDied! The target process just crashed :-(");
            this.mConnection = null;
            CallbackToFutureAdapter.Completer<Session> completer = this.mStartCompleter;
            if (completer != null) {
                completer.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<CaptureResult> completer2 = this.mTileRequestCompleter;
            if (completer2 != null) {
                completer2.setException(new DeadObjectException("The remote process died"));
            }
            CallbackToFutureAdapter.Completer<Void> completer3 = this.mEndCompleter;
            if (completer3 != null) {
                completer3.setException(new DeadObjectException("The remote process died"));
            }
        }

        public final void start(CallbackToFutureAdapter.Completer<Session> completer) {
            ImageReader newInstance = ImageReader.newInstance(this.mTileWidth, this.mTileHeight, 1, 30, 256);
            this.mReader = newInstance;
            this.mStartCompleter = completer;
            newInstance.setOnImageAvailableListenerWithExecutor(this, this.mBgExecutor);
            try {
                this.mCancellationSignal = this.mConnection.startCapture(this.mReader.getSurface(), this);
                completer.addCancellationListener(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda2(this), new SaveImageInBackgroundTask$$ExternalSyntheticLambda1());
                this.mStarted = true;
            } catch (RemoteException e) {
                this.mReader.close();
                completer.setException(e);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$start$0() {
            try {
                this.mCancellationSignal.cancel();
            } catch (RemoteException unused) {
            }
        }

        public void onCaptureStarted() {
            Log.d(ScrollCaptureClient.TAG, "onCaptureStarted");
            this.mStartCompleter.set(this);
        }

        public ListenableFuture<CaptureResult> requestTile(int i) {
            this.mRequestRect = new Rect(0, i, this.mTileWidth, this.mTileHeight + i);
            return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Object lambda$requestTile$2(CallbackToFutureAdapter.Completer completer) throws Exception {
            IScrollCaptureConnection iScrollCaptureConnection = this.mConnection;
            if (iScrollCaptureConnection == null || !iScrollCaptureConnection.asBinder().isBinderAlive()) {
                completer.setException(new DeadObjectException("Connection is closed!"));
                return "";
            }
            try {
                this.mTileRequestCompleter = completer;
                this.mCancellationSignal = this.mConnection.requestImage(this.mRequestRect);
                completer.addCancellationListener(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda3(this), new SaveImageInBackgroundTask$$ExternalSyntheticLambda1());
                return "IScrollCaptureCallbacks#onImageRequestCompleted";
            } catch (RemoteException e) {
                completer.setException(e);
                return "IScrollCaptureCallbacks#onImageRequestCompleted";
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$requestTile$1() {
            try {
                this.mCancellationSignal.cancel();
            } catch (RemoteException unused) {
            }
        }

        public void onImageRequestCompleted(int i, Rect rect) {
            synchronized (this.mLock) {
                this.mCapturedArea = rect;
                if (this.mCapturedImage != null || rect == null || rect.isEmpty()) {
                    completeCaptureRequest();
                }
            }
        }

        public void onImageAvailable(ImageReader imageReader) {
            synchronized (this.mLock) {
                this.mCapturedImage = this.mReader.acquireLatestImage();
                if (this.mCapturedArea != null) {
                    completeCaptureRequest();
                }
            }
        }

        public final void completeCaptureRequest() {
            CaptureResult captureResult = new CaptureResult(this.mCapturedImage, this.mRequestRect, this.mCapturedArea);
            this.mCapturedImage = null;
            this.mRequestRect = null;
            this.mCapturedArea = null;
            this.mTileRequestCompleter.set(captureResult);
        }

        public ListenableFuture<Void> end() {
            Log.d(ScrollCaptureClient.TAG, "end()");
            return CallbackToFutureAdapter.getFuture(new ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Object lambda$end$3(CallbackToFutureAdapter.Completer completer) throws Exception {
            if (!this.mStarted) {
                try {
                    this.mConnection.asBinder().unlinkToDeath(this, 0);
                    this.mConnection.close();
                } catch (RemoteException unused) {
                }
                this.mConnection = null;
                completer.set(null);
                return "";
            }
            this.mEndCompleter = completer;
            try {
                this.mConnection.endCapture();
                return "IScrollCaptureCallbacks#onCaptureEnded";
            } catch (RemoteException e) {
                completer.setException(e);
                return "IScrollCaptureCallbacks#onCaptureEnded";
            }
        }

        public void release() {
            this.mReader.close();
        }

        public void onCaptureEnded() {
            try {
                this.mConnection.close();
            } catch (RemoteException unused) {
            }
            this.mConnection = null;
            this.mEndCompleter.set(null);
        }

        public int getPageHeight() {
            return this.mBoundsInWindow.height();
        }

        public int getTileHeight() {
            return this.mTileHeight;
        }

        public int getTargetHeight() {
            return this.mTargetHeight;
        }
    }
}

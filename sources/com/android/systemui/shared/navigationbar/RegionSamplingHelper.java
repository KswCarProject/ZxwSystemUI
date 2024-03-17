package com.android.systemui.shared.navigationbar;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Handler;
import android.view.CompositionSamplingListener;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;

@TargetApi(29)
public class RegionSamplingHelper implements View.OnAttachStateChangeListener, View.OnLayoutChangeListener {
    public final Executor mBackgroundExecutor;
    public final SamplingCallback mCallback;
    public final SysuiCompositionSamplingListener mCompositionSamplingListener;
    public float mCurrentMedianLuma;
    public boolean mFirstSamplingAfterStart;
    public final Handler mHandler;
    public boolean mIsDestroyed;
    public float mLastMedianLuma;
    public final Rect mRegisteredSamplingBounds;
    public SurfaceControl mRegisteredStopLayer;
    public Runnable mRemoveDrawRunnable;
    public final View mSampledView;
    public boolean mSamplingEnabled;
    public final CompositionSamplingListener mSamplingListener;
    public boolean mSamplingListenerRegistered;
    public final Rect mSamplingRequestBounds;
    public ViewTreeObserver.OnDrawListener mUpdateOnDraw;
    public boolean mWaitingOnDraw;
    public boolean mWindowHasBlurs;
    public boolean mWindowVisible;
    public SurfaceControl mWrappedStopLayer;

    public interface SamplingCallback {
        Rect getSampledRegion(View view);

        boolean isSamplingEnabled() {
            return true;
        }

        void onRegionDarknessChanged(boolean z);
    }

    public RegionSamplingHelper(View view, SamplingCallback samplingCallback, Executor executor) {
        this(view, samplingCallback, view.getContext().getMainExecutor(), executor);
    }

    public RegionSamplingHelper(View view, SamplingCallback samplingCallback, Executor executor, Executor executor2) {
        this(view, samplingCallback, executor, executor2, new SysuiCompositionSamplingListener());
    }

    public RegionSamplingHelper(View view, SamplingCallback samplingCallback, Executor executor, Executor executor2, SysuiCompositionSamplingListener sysuiCompositionSamplingListener) {
        this.mHandler = new Handler();
        this.mSamplingRequestBounds = new Rect();
        this.mRegisteredSamplingBounds = new Rect();
        this.mSamplingEnabled = false;
        this.mSamplingListenerRegistered = false;
        this.mRegisteredStopLayer = null;
        this.mWrappedStopLayer = null;
        this.mUpdateOnDraw = new ViewTreeObserver.OnDrawListener() {
            public void onDraw() {
                RegionSamplingHelper.this.mHandler.post(RegionSamplingHelper.this.mRemoveDrawRunnable);
                RegionSamplingHelper.this.onDraw();
            }
        };
        this.mRemoveDrawRunnable = new Runnable() {
            public void run() {
                RegionSamplingHelper.this.mSampledView.getViewTreeObserver().removeOnDrawListener(RegionSamplingHelper.this.mUpdateOnDraw);
            }
        };
        this.mBackgroundExecutor = executor2;
        this.mCompositionSamplingListener = sysuiCompositionSamplingListener;
        this.mSamplingListener = new CompositionSamplingListener(executor) {
            public void onSampleCollected(float f) {
                if (RegionSamplingHelper.this.mSamplingEnabled) {
                    RegionSamplingHelper.this.updateMediaLuma(f);
                }
            }
        };
        this.mSampledView = view;
        view.addOnAttachStateChangeListener(this);
        view.addOnLayoutChangeListener(this);
        this.mCallback = samplingCallback;
    }

    public final void onDraw() {
        if (this.mWaitingOnDraw) {
            this.mWaitingOnDraw = false;
            updateSamplingListener();
        }
    }

    public void start(Rect rect) {
        if (this.mCallback.isSamplingEnabled()) {
            if (rect != null) {
                this.mSamplingRequestBounds.set(rect);
            }
            this.mSamplingEnabled = true;
            this.mLastMedianLuma = -1.0f;
            this.mFirstSamplingAfterStart = true;
            updateSamplingListener();
        }
    }

    public void stop() {
        this.mSamplingEnabled = false;
        updateSamplingListener();
    }

    public void stopAndDestroy() {
        stop();
        Executor executor = this.mBackgroundExecutor;
        CompositionSamplingListener compositionSamplingListener = this.mSamplingListener;
        Objects.requireNonNull(compositionSamplingListener);
        executor.execute(new RegionSamplingHelper$$ExternalSyntheticLambda0(compositionSamplingListener));
        this.mIsDestroyed = true;
    }

    public void onViewAttachedToWindow(View view) {
        updateSamplingListener();
    }

    public void onViewDetachedFromWindow(View view) {
        stopAndDestroy();
    }

    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateSamplingRect();
    }

    public final void updateSamplingListener() {
        if (this.mSamplingEnabled && !this.mSamplingRequestBounds.isEmpty() && this.mWindowVisible && !this.mWindowHasBlurs && (this.mSampledView.isAttachedToWindow() || this.mFirstSamplingAfterStart)) {
            ViewRootImpl viewRootImpl = this.mSampledView.getViewRootImpl();
            SurfaceControl surfaceControl = null;
            SurfaceControl surfaceControl2 = viewRootImpl != null ? viewRootImpl.getSurfaceControl() : null;
            if (surfaceControl2 != null && surfaceControl2.isValid()) {
                surfaceControl = surfaceControl2;
            } else if (!this.mWaitingOnDraw) {
                this.mWaitingOnDraw = true;
                if (this.mHandler.hasCallbacks(this.mRemoveDrawRunnable)) {
                    this.mHandler.removeCallbacks(this.mRemoveDrawRunnable);
                } else {
                    this.mSampledView.getViewTreeObserver().addOnDrawListener(this.mUpdateOnDraw);
                }
            }
            if (!this.mSamplingRequestBounds.equals(this.mRegisteredSamplingBounds) || this.mRegisteredStopLayer != surfaceControl) {
                unregisterSamplingListener();
                this.mSamplingListenerRegistered = true;
                SurfaceControl wrap = wrap(surfaceControl);
                this.mBackgroundExecutor.execute(new RegionSamplingHelper$$ExternalSyntheticLambda1(this, wrap));
                this.mRegisteredSamplingBounds.set(this.mSamplingRequestBounds);
                this.mRegisteredStopLayer = surfaceControl;
                this.mWrappedStopLayer = wrap;
            }
            this.mFirstSamplingAfterStart = false;
            return;
        }
        unregisterSamplingListener();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSamplingListener$0(SurfaceControl surfaceControl) {
        if (surfaceControl == null || surfaceControl.isValid()) {
            this.mCompositionSamplingListener.register(this.mSamplingListener, 0, surfaceControl, this.mSamplingRequestBounds);
        }
    }

    public SurfaceControl wrap(SurfaceControl surfaceControl) {
        if (surfaceControl == null) {
            return null;
        }
        return new SurfaceControl(surfaceControl, "regionSampling");
    }

    public final void unregisterSamplingListener() {
        if (this.mSamplingListenerRegistered) {
            this.mSamplingListenerRegistered = false;
            SurfaceControl surfaceControl = this.mWrappedStopLayer;
            this.mRegisteredStopLayer = null;
            this.mWrappedStopLayer = null;
            this.mRegisteredSamplingBounds.setEmpty();
            this.mBackgroundExecutor.execute(new RegionSamplingHelper$$ExternalSyntheticLambda2(this, surfaceControl));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$unregisterSamplingListener$1(SurfaceControl surfaceControl) {
        this.mCompositionSamplingListener.unregister(this.mSamplingListener);
        if (surfaceControl != null && surfaceControl.isValid()) {
            surfaceControl.release();
        }
    }

    public final void updateMediaLuma(float f) {
        this.mCurrentMedianLuma = f;
        if (Math.abs(f - this.mLastMedianLuma) > 0.05f) {
            this.mCallback.onRegionDarknessChanged(f < 0.5f);
            this.mLastMedianLuma = f;
        }
    }

    public void updateSamplingRect() {
        Rect sampledRegion = this.mCallback.getSampledRegion(this.mSampledView);
        if (!this.mSamplingRequestBounds.equals(sampledRegion)) {
            this.mSamplingRequestBounds.set(sampledRegion);
            updateSamplingListener();
        }
    }

    public void setWindowVisible(boolean z) {
        this.mWindowVisible = z;
        updateSamplingListener();
    }

    public void setWindowHasBlurs(boolean z) {
        this.mWindowHasBlurs = z;
        updateSamplingListener();
    }

    public void dump(PrintWriter printWriter) {
        dump("", printWriter);
    }

    public void dump(String str, PrintWriter printWriter) {
        printWriter.println(str + "RegionSamplingHelper:");
        printWriter.println(str + "\tsampleView isAttached: " + this.mSampledView.isAttachedToWindow());
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("\tsampleView isScValid: ");
        sb.append(this.mSampledView.isAttachedToWindow() ? Boolean.valueOf(this.mSampledView.getViewRootImpl().getSurfaceControl().isValid()) : "notAttached");
        printWriter.println(sb.toString());
        printWriter.println(str + "\tmSamplingEnabled: " + this.mSamplingEnabled);
        printWriter.println(str + "\tmSamplingListenerRegistered: " + this.mSamplingListenerRegistered);
        printWriter.println(str + "\tmSamplingRequestBounds: " + this.mSamplingRequestBounds);
        printWriter.println(str + "\tmRegisteredSamplingBounds: " + this.mRegisteredSamplingBounds);
        printWriter.println(str + "\tmLastMedianLuma: " + this.mLastMedianLuma);
        printWriter.println(str + "\tmCurrentMedianLuma: " + this.mCurrentMedianLuma);
        printWriter.println(str + "\tmWindowVisible: " + this.mWindowVisible);
        printWriter.println(str + "\tmWindowHasBlurs: " + this.mWindowHasBlurs);
        printWriter.println(str + "\tmWaitingOnDraw: " + this.mWaitingOnDraw);
        printWriter.println(str + "\tmRegisteredStopLayer: " + this.mRegisteredStopLayer);
        printWriter.println(str + "\tmWrappedStopLayer: " + this.mWrappedStopLayer);
        printWriter.println(str + "\tmIsDestroyed: " + this.mIsDestroyed);
    }

    public static class SysuiCompositionSamplingListener {
        public void register(CompositionSamplingListener compositionSamplingListener, int i, SurfaceControl surfaceControl, Rect rect) {
            CompositionSamplingListener.register(compositionSamplingListener, i, surfaceControl, rect);
        }

        public void unregister(CompositionSamplingListener compositionSamplingListener) {
            CompositionSamplingListener.unregister(compositionSamplingListener);
        }
    }
}

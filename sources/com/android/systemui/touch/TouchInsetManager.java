package com.android.systemui.touch;

import android.graphics.Rect;
import android.graphics.Region;
import android.view.View;
import android.view.ViewRootImpl;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Executor;

public class TouchInsetManager {
    public final View.OnAttachStateChangeListener mAttachListener;
    public final HashMap<TouchInsetSession, Region> mDefinedRegions = new HashMap<>();
    public final Executor mExecutor;
    public final View mRootView;

    public static class TouchInsetSession {
        public final Executor mExecutor;
        public final TouchInsetManager mManager;
        public final View.OnLayoutChangeListener mOnLayoutChangeListener = new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda0(this);
        public final HashSet<View> mTrackedViews;

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            updateTouchRegion();
        }

        public TouchInsetSession(TouchInsetManager touchInsetManager, Executor executor) {
            this.mManager = touchInsetManager;
            this.mTrackedViews = new HashSet<>();
            this.mExecutor = executor;
        }

        public void addViewToTracking(View view) {
            this.mExecutor.execute(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda2(this, view));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$addViewToTracking$1(View view) {
            this.mTrackedViews.add(view);
            view.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
            updateTouchRegion();
        }

        public void removeViewFromTracking(View view) {
            this.mExecutor.execute(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda4(this, view));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$removeViewFromTracking$2(View view) {
            this.mTrackedViews.remove(view);
            view.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
            updateTouchRegion();
        }

        public final void updateTouchRegion() {
            Region obtain = Region.obtain();
            this.mTrackedViews.stream().forEach(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda3(obtain));
            this.mManager.setTouchRegion(this, obtain);
            obtain.recycle();
        }

        public static /* synthetic */ void lambda$updateTouchRegion$3(Region region, View view) {
            Rect rect = new Rect();
            view.getBoundsOnScreen(rect);
            region.op(rect, Region.Op.UNION);
        }

        public void clear() {
            this.mExecutor.execute(new TouchInsetManager$TouchInsetSession$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$clear$4() {
            this.mManager.clearRegion(this);
            this.mTrackedViews.clear();
        }
    }

    public TouchInsetManager(Executor executor, View view) {
        AnonymousClass1 r0 = new View.OnAttachStateChangeListener() {
            public void onViewDetachedFromWindow(View view) {
            }

            public void onViewAttachedToWindow(View view) {
                TouchInsetManager.this.updateTouchInset();
            }
        };
        this.mAttachListener = r0;
        this.mExecutor = executor;
        this.mRootView = view;
        view.addOnAttachStateChangeListener(r0);
    }

    public TouchInsetSession createSession() {
        return new TouchInsetSession(this, this.mExecutor);
    }

    public ListenableFuture<Boolean> checkWithinTouchRegion(int i, int i2) {
        return CallbackToFutureAdapter.getFuture(new TouchInsetManager$$ExternalSyntheticLambda1(this, i, i2));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$checkWithinTouchRegion$1(CallbackToFutureAdapter.Completer completer, int i, int i2) {
        completer.set(Boolean.valueOf(this.mDefinedRegions.values().stream().anyMatch(new TouchInsetManager$$ExternalSyntheticLambda4(i, i2))));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$checkWithinTouchRegion$2(int i, int i2, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new TouchInsetManager$$ExternalSyntheticLambda3(this, completer, i, i2));
        return "DreamOverlayTouchMonitor::checkWithinTouchRegion";
    }

    public final void updateTouchInset() {
        ViewRootImpl viewRootImpl = this.mRootView.getViewRootImpl();
        if (viewRootImpl != null) {
            Region obtain = Region.obtain();
            for (Region op : this.mDefinedRegions.values()) {
                obtain.op(op, Region.Op.UNION);
            }
            viewRootImpl.setTouchableRegion(obtain);
            obtain.recycle();
        }
    }

    public void setTouchRegion(TouchInsetSession touchInsetSession, Region region) {
        this.mExecutor.execute(new TouchInsetManager$$ExternalSyntheticLambda2(this, touchInsetSession, Region.obtain(region)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setTouchRegion$3(TouchInsetSession touchInsetSession, Region region) {
        this.mDefinedRegions.put(touchInsetSession, region);
        updateTouchInset();
    }

    public final void clearRegion(TouchInsetSession touchInsetSession) {
        this.mExecutor.execute(new TouchInsetManager$$ExternalSyntheticLambda0(this, touchInsetSession));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$clearRegion$4(TouchInsetSession touchInsetSession) {
        Region remove = this.mDefinedRegions.remove(touchInsetSession);
        if (remove != null) {
            remove.recycle();
        }
        updateTouchInset();
    }
}

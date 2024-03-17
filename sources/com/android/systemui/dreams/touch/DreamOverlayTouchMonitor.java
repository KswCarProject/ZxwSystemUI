package com.android.systemui.dreams.touch;

import android.graphics.Region;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.MotionEvent;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda2;
import com.android.systemui.dreams.touch.DreamTouchHandler;
import com.android.systemui.dreams.touch.dagger.InputSessionComponent;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DreamOverlayTouchMonitor {
    public final HashSet<TouchSessionImpl> mActiveTouchSessions = new HashSet<>();
    public InputSession mCurrentInputSession;
    public final Executor mExecutor;
    public final Collection<DreamTouchHandler> mHandlers;
    public InputChannelCompat$InputEventListener mInputEventListener = new InputChannelCompat$InputEventListener() {
        public void onInputEvent(InputEvent inputEvent) {
            if (DreamOverlayTouchMonitor.this.mActiveTouchSessions.isEmpty()) {
                HashMap hashMap = new HashMap();
                for (DreamTouchHandler dreamTouchHandler : DreamOverlayTouchMonitor.this.mHandlers) {
                    Region obtain = Region.obtain();
                    dreamTouchHandler.getTouchInitiationRegion(obtain);
                    if (!obtain.isEmpty()) {
                        if (inputEvent instanceof MotionEvent) {
                            MotionEvent motionEvent = (MotionEvent) inputEvent;
                            if (!obtain.contains(Math.round(motionEvent.getX()), Math.round(motionEvent.getY()))) {
                            }
                        }
                    }
                    TouchSessionImpl touchSessionImpl = new TouchSessionImpl(DreamOverlayTouchMonitor.this, (TouchSessionImpl) null);
                    DreamOverlayTouchMonitor.this.mActiveTouchSessions.add(touchSessionImpl);
                    hashMap.put(dreamTouchHandler, touchSessionImpl);
                }
                hashMap.forEach(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda0());
            }
            DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda1()).flatMap(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda2()).forEach(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda3(inputEvent));
        }
    };
    public InputSessionComponent.Factory mInputSessionFactory;
    public final Lifecycle mLifecycle;
    public final LifecycleObserver mLifecycleObserver = new DefaultLifecycleObserver() {
        public void onResume(LifecycleOwner lifecycleOwner) {
            DreamOverlayTouchMonitor.this.startMonitoring();
        }

        public void onPause(LifecycleOwner lifecycleOwner) {
            DreamOverlayTouchMonitor.this.stopMonitoring();
        }
    };
    public GestureDetector.OnGestureListener mOnGestureListener = new GestureDetector.OnGestureListener() {
        public final boolean evaluate(Evaluator evaluator) {
            HashSet hashSet = new HashSet();
            boolean anyMatch = DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda6(evaluator, hashSet)).anyMatch(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda7());
            if (anyMatch) {
                DreamOverlayTouchMonitor.this.isolate(hashSet);
            }
            return anyMatch;
        }

        public static /* synthetic */ Boolean lambda$evaluate$2(Evaluator evaluator, Set set, TouchSessionImpl touchSessionImpl) {
            boolean anyMatch = touchSessionImpl.getGestureListeners().stream().map(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda10(evaluator)).anyMatch(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda11());
            if (anyMatch) {
                set.add(touchSessionImpl);
            }
            return Boolean.valueOf(anyMatch);
        }

        public final void observe(Consumer<GestureDetector.OnGestureListener> consumer) {
            DreamOverlayTouchMonitor.this.mActiveTouchSessions.stream().map(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda8()).flatMap(new DreamOverlayTouchMonitor$2$$ExternalSyntheticLambda2()).forEach(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda9(consumer));
        }

        public boolean onDown(MotionEvent motionEvent) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda3(motionEvent));
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda0(motionEvent, motionEvent2, f, f2));
        }

        public void onLongPress(MotionEvent motionEvent) {
            observe(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda4(motionEvent));
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda1(motionEvent, motionEvent2, f, f2));
        }

        public void onShowPress(MotionEvent motionEvent) {
            observe(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda2(motionEvent));
        }

        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return evaluate(new DreamOverlayTouchMonitor$3$$ExternalSyntheticLambda5(motionEvent));
        }
    };

    public interface Evaluator {
        boolean evaluate(GestureDetector.OnGestureListener onGestureListener);
    }

    public final ListenableFuture<DreamTouchHandler.TouchSession> pop(TouchSessionImpl touchSessionImpl) {
        return CallbackToFutureAdapter.getFuture(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda2(this, touchSessionImpl));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Object lambda$pop$3(TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) throws Exception {
        this.mExecutor.execute(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda3(this, touchSessionImpl, completer));
        return "DreamOverlayTouchMonitor::pop";
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$pop$2(TouchSessionImpl touchSessionImpl, CallbackToFutureAdapter.Completer completer) {
        if (this.mActiveTouchSessions.remove(touchSessionImpl)) {
            touchSessionImpl.onRemoved();
            TouchSessionImpl r2 = touchSessionImpl.getPredecessor();
            if (r2 != null) {
                this.mActiveTouchSessions.add(r2);
            }
            completer.set(r2);
        }
    }

    public final int getSessionCount() {
        return this.mActiveTouchSessions.size();
    }

    public static class TouchSessionImpl implements DreamTouchHandler.TouchSession {
        public final HashSet<DreamTouchHandler.TouchSession.Callback> mCallbacks = new HashSet<>();
        public final HashSet<InputChannelCompat$InputEventListener> mEventListeners = new HashSet<>();
        public final HashSet<GestureDetector.OnGestureListener> mGestureListeners = new HashSet<>();
        public final TouchSessionImpl mPredecessor;
        public final DreamOverlayTouchMonitor mTouchMonitor;

        public TouchSessionImpl(DreamOverlayTouchMonitor dreamOverlayTouchMonitor, TouchSessionImpl touchSessionImpl) {
            this.mPredecessor = touchSessionImpl;
            this.mTouchMonitor = dreamOverlayTouchMonitor;
        }

        public void registerCallback(DreamTouchHandler.TouchSession.Callback callback) {
            this.mCallbacks.add(callback);
        }

        public boolean registerInputListener(InputChannelCompat$InputEventListener inputChannelCompat$InputEventListener) {
            return this.mEventListeners.add(inputChannelCompat$InputEventListener);
        }

        public boolean registerGestureListener(GestureDetector.OnGestureListener onGestureListener) {
            return this.mGestureListeners.add(onGestureListener);
        }

        public ListenableFuture<DreamTouchHandler.TouchSession> pop() {
            return this.mTouchMonitor.pop(this);
        }

        public int getActiveSessionCount() {
            return this.mTouchMonitor.getSessionCount();
        }

        public Collection<InputChannelCompat$InputEventListener> getEventListeners() {
            return this.mEventListeners;
        }

        public Collection<GestureDetector.OnGestureListener> getGestureListeners() {
            return this.mGestureListeners;
        }

        public final TouchSessionImpl getPredecessor() {
            return this.mPredecessor;
        }

        public final void onRemoved() {
            this.mCallbacks.forEach(new DreamOverlayTouchMonitor$TouchSessionImpl$$ExternalSyntheticLambda0());
        }
    }

    public final void startMonitoring() {
        stopMonitoring();
        this.mCurrentInputSession = this.mInputSessionFactory.create("dreamOverlay", this.mInputEventListener, this.mOnGestureListener, true).getInputSession();
    }

    public final void stopMonitoring() {
        InputSession inputSession = this.mCurrentInputSession;
        if (inputSession != null) {
            inputSession.dispose();
            this.mCurrentInputSession = null;
        }
    }

    public DreamOverlayTouchMonitor(Executor executor, Lifecycle lifecycle, InputSessionComponent.Factory factory, Set<DreamTouchHandler> set) {
        this.mHandlers = set;
        this.mInputSessionFactory = factory;
        this.mExecutor = executor;
        this.mLifecycle = lifecycle;
    }

    public void init() {
        this.mLifecycle.addObserver(this.mLifecycleObserver);
    }

    public final void isolate(Set<TouchSessionImpl> set) {
        Collection collection = (Collection) this.mActiveTouchSessions.stream().filter(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda0(set)).collect(Collectors.toCollection(new DreamOverlayStateController$$ExternalSyntheticLambda2()));
        collection.forEach(new DreamOverlayTouchMonitor$$ExternalSyntheticLambda1());
        this.mActiveTouchSessions.removeAll(collection);
    }

    public static /* synthetic */ boolean lambda$isolate$4(Set set, TouchSessionImpl touchSessionImpl) {
        return !set.contains(touchSessionImpl);
    }
}

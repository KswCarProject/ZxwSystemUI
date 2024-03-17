package androidx.dynamicanimation.animation;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Choreographer;
import androidx.collection.SimpleArrayMap;
import java.util.ArrayList;

public class AnimationHandler {
    public static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal<>();
    public final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList<>();
    public final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    public long mCurrentFrameTime = 0;
    public final SimpleArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new SimpleArrayMap<>();
    public float mDurationScale = 1.0f;
    public DurationScaleChangeListener mDurationScaleChangeListener;
    public boolean mListDirty = false;
    public final Runnable mRunnable = new AnimationHandler$$ExternalSyntheticLambda0(this);
    public FrameCallbackScheduler mScheduler;

    public interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    public interface DurationScaleChangeListener {
        boolean register();

        boolean unregister();
    }

    public class AnimationCallbackDispatcher {
        public AnimationCallbackDispatcher() {
        }

        public void dispatchAnimationFrame() {
            AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.mScheduler.postFrameCallback(AnimationHandler.this.mRunnable);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mCallbackDispatcher.dispatchAnimationFrame();
    }

    public static AnimationHandler getInstance() {
        ThreadLocal<AnimationHandler> threadLocal = sAnimatorHandler;
        if (threadLocal.get() == null) {
            threadLocal.set(new AnimationHandler(new FrameCallbackScheduler16()));
        }
        return threadLocal.get();
    }

    public AnimationHandler(FrameCallbackScheduler frameCallbackScheduler) {
        this.mScheduler = frameCallbackScheduler;
    }

    public void addAnimationFrameCallback(AnimationFrameCallback animationFrameCallback, long j) {
        if (this.mAnimationCallbacks.size() == 0) {
            this.mScheduler.postFrameCallback(this.mRunnable);
            this.mDurationScale = ValueAnimator.getDurationScale();
            if (this.mDurationScaleChangeListener == null) {
                this.mDurationScaleChangeListener = new DurationScaleChangeListener33();
            }
            this.mDurationScaleChangeListener.register();
        }
        if (!this.mAnimationCallbacks.contains(animationFrameCallback)) {
            this.mAnimationCallbacks.add(animationFrameCallback);
        }
        if (j > 0) {
            this.mDelayedCallbackStartTime.put(animationFrameCallback, Long.valueOf(SystemClock.uptimeMillis() + j));
        }
    }

    public void removeCallback(AnimationFrameCallback animationFrameCallback) {
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        int indexOf = this.mAnimationCallbacks.indexOf(animationFrameCallback);
        if (indexOf >= 0) {
            this.mAnimationCallbacks.set(indexOf, (Object) null);
            this.mListDirty = true;
        }
    }

    public void doAnimationFrame(long j) {
        long uptimeMillis = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback animationFrameCallback = this.mAnimationCallbacks.get(i);
            if (animationFrameCallback != null && isCallbackDue(animationFrameCallback, uptimeMillis)) {
                animationFrameCallback.doAnimationFrame(j);
            }
        }
        cleanUpList();
    }

    public boolean isCurrentThread() {
        return this.mScheduler.isCurrentThread();
    }

    public final boolean isCallbackDue(AnimationFrameCallback animationFrameCallback, long j) {
        Long l = this.mDelayedCallbackStartTime.get(animationFrameCallback);
        if (l == null) {
            return true;
        }
        if (l.longValue() >= j) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(animationFrameCallback);
        return true;
    }

    public final void cleanUpList() {
        if (this.mListDirty) {
            for (int size = this.mAnimationCallbacks.size() - 1; size >= 0; size--) {
                if (this.mAnimationCallbacks.get(size) == null) {
                    this.mAnimationCallbacks.remove(size);
                }
            }
            if (this.mAnimationCallbacks.size() == 0) {
                this.mDurationScaleChangeListener.unregister();
            }
            this.mListDirty = false;
        }
    }

    public FrameCallbackScheduler getScheduler() {
        return this.mScheduler;
    }

    public static final class FrameCallbackScheduler16 implements FrameCallbackScheduler {
        public final Choreographer mChoreographer = Choreographer.getInstance();
        public final Looper mLooper = Looper.myLooper();

        public void postFrameCallback(Runnable runnable) {
            this.mChoreographer.postFrameCallback(new AnimationHandler$FrameCallbackScheduler16$$ExternalSyntheticLambda0(runnable));
        }

        public boolean isCurrentThread() {
            return Thread.currentThread() == this.mLooper.getThread();
        }
    }

    public static class FrameCallbackScheduler14 implements FrameCallbackScheduler {
        public final Handler mHandler = new Handler(Looper.myLooper());
        public long mLastFrameTime;

        public void postFrameCallback(Runnable runnable) {
            this.mHandler.postDelayed(new AnimationHandler$FrameCallbackScheduler14$$ExternalSyntheticLambda0(this, runnable), Math.max(10 - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$postFrameCallback$0(Runnable runnable) {
            this.mLastFrameTime = SystemClock.uptimeMillis();
            runnable.run();
        }

        public boolean isCurrentThread() {
            return Thread.currentThread() == this.mHandler.getLooper().getThread();
        }
    }

    public float getDurationScale() {
        return this.mDurationScale;
    }

    public class DurationScaleChangeListener33 implements DurationScaleChangeListener {
        public ValueAnimator.DurationScaleChangeListener mListener;

        public DurationScaleChangeListener33() {
        }

        public boolean register() {
            if (this.mListener != null) {
                return true;
            }
            AnimationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0 animationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0 = new AnimationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0(this);
            this.mListener = animationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0;
            return ValueAnimator.registerDurationScaleChangeListener(animationHandler$DurationScaleChangeListener33$$ExternalSyntheticLambda0);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$register$0(float f) {
            AnimationHandler.this.mDurationScale = f;
        }

        public boolean unregister() {
            boolean unregisterDurationScaleChangeListener = ValueAnimator.unregisterDurationScaleChangeListener(this.mListener);
            this.mListener = null;
            return unregisterDurationScaleChangeListener;
        }
    }
}

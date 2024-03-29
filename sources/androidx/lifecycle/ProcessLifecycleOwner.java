package androidx.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ReportFragment;

public class ProcessLifecycleOwner implements LifecycleOwner {
    public static final long TIMEOUT_MS = 700;
    public static final ProcessLifecycleOwner sInstance = new ProcessLifecycleOwner();
    public Runnable mDelayedPauseRunnable = new Runnable() {
        public void run() {
            ProcessLifecycleOwner.this.dispatchPauseIfNeeded();
            ProcessLifecycleOwner.this.dispatchStopIfNeeded();
        }
    };
    public Handler mHandler;
    public ReportFragment.ActivityInitializationListener mInitializationListener = new ReportFragment.ActivityInitializationListener() {
        public void onCreate() {
        }

        public void onStart() {
            ProcessLifecycleOwner.this.activityStarted();
        }

        public void onResume() {
            ProcessLifecycleOwner.this.activityResumed();
        }
    };
    public boolean mPauseSent = true;
    public final LifecycleRegistry mRegistry = new LifecycleRegistry(this);
    public int mResumedCounter = 0;
    public int mStartedCounter = 0;
    public boolean mStopSent = true;

    public static LifecycleOwner get() {
        return sInstance;
    }

    public static void init(Context context) {
        sInstance.attach(context);
    }

    public void activityStarted() {
        int i = this.mStartedCounter + 1;
        this.mStartedCounter = i;
        if (i == 1 && this.mStopSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
            this.mStopSent = false;
        }
    }

    public void activityResumed() {
        int i = this.mResumedCounter + 1;
        this.mResumedCounter = i;
        if (i != 1) {
            return;
        }
        if (this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
            this.mPauseSent = false;
            return;
        }
        this.mHandler.removeCallbacks(this.mDelayedPauseRunnable);
    }

    public void activityPaused() {
        int i = this.mResumedCounter - 1;
        this.mResumedCounter = i;
        if (i == 0) {
            this.mHandler.postDelayed(this.mDelayedPauseRunnable, 700);
        }
    }

    public void activityStopped() {
        this.mStartedCounter--;
        dispatchStopIfNeeded();
    }

    public void dispatchPauseIfNeeded() {
        if (this.mResumedCounter == 0) {
            this.mPauseSent = true;
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        }
    }

    public void dispatchStopIfNeeded() {
        if (this.mStartedCounter == 0 && this.mPauseSent) {
            this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            this.mStopSent = true;
        }
    }

    public void attach(Context context) {
        this.mHandler = new Handler();
        this.mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            public void onActivityPreCreated(Activity activity, Bundle bundle) {
                activity.registerActivityLifecycleCallbacks(new EmptyActivityLifecycleCallbacks() {
                    public void onActivityPostStarted(Activity activity) {
                        ProcessLifecycleOwner.this.activityStarted();
                    }

                    public void onActivityPostResumed(Activity activity) {
                        ProcessLifecycleOwner.this.activityResumed();
                    }
                });
            }

            public void onActivityPaused(Activity activity) {
                ProcessLifecycleOwner.this.activityPaused();
            }

            public void onActivityStopped(Activity activity) {
                ProcessLifecycleOwner.this.activityStopped();
            }
        });
    }

    public Lifecycle getLifecycle() {
        return this.mRegistry;
    }
}

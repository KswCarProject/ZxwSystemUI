package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.MathUtils;
import android.util.TimeUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.PrintWriter;

public class LightBarTransitionsController implements Dumpable, CommandQueue.Callbacks, StatusBarStateController.StateListener {
    public final DarkIntensityApplier mApplier;
    public final CommandQueue mCommandQueue;
    public final Context mContext;
    public float mDarkIntensity;
    public int mDisplayId;
    public float mDozeAmount;
    public final Handler mHandler;
    public final KeyguardStateController mKeyguardStateController;
    public float mNextDarkIntensity;
    public Boolean mOverrideIconTintForNavMode;
    public float mPendingDarkIntensity;
    public final StatusBarStateController mStatusBarStateController;
    public ValueAnimator mTintAnimator;
    public boolean mTintChangePending;
    public boolean mTransitionDeferring;
    public final Runnable mTransitionDeferringDoneRunnable = new Runnable() {
        public void run() {
            LightBarTransitionsController.this.mTransitionDeferring = false;
        }
    };
    public long mTransitionDeferringDuration;
    public long mTransitionDeferringStartTime;
    public boolean mTransitionPending;

    public interface DarkIntensityApplier {
        void applyDarkIntensity(float f);

        int getTintAnimationDuration();
    }

    public interface Factory {
        LightBarTransitionsController create(DarkIntensityApplier darkIntensityApplier);
    }

    public void onStateChanged(int i) {
    }

    public LightBarTransitionsController(Context context, DarkIntensityApplier darkIntensityApplier, CommandQueue commandQueue, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController) {
        this.mApplier = darkIntensityApplier;
        this.mHandler = new Handler();
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = statusBarStateController;
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) this);
        statusBarStateController.addCallback(this);
        this.mDozeAmount = statusBarStateController.getDozeAmount();
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
    }

    public void destroy() {
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.removeCallback(this);
    }

    public void saveState(Bundle bundle) {
        ValueAnimator valueAnimator = this.mTintAnimator;
        bundle.putFloat("dark_intensity", (valueAnimator == null || !valueAnimator.isRunning()) ? this.mDarkIntensity : this.mNextDarkIntensity);
    }

    public void restoreState(Bundle bundle) {
        setIconTintInternal(bundle.getFloat("dark_intensity", 0.0f));
        this.mNextDarkIntensity = this.mDarkIntensity;
    }

    public void appTransitionPending(int i, boolean z) {
        if (this.mDisplayId != i) {
            return;
        }
        if (!this.mKeyguardStateController.isKeyguardGoingAway() || z) {
            this.mTransitionPending = true;
        }
    }

    public void appTransitionCancelled(int i) {
        if (this.mDisplayId == i) {
            if (this.mTransitionPending && this.mTintChangePending) {
                this.mTintChangePending = false;
                animateIconTint(this.mPendingDarkIntensity, 0, (long) this.mApplier.getTintAnimationDuration());
            }
            this.mTransitionPending = false;
        }
    }

    public void appTransitionStarting(int i, long j, long j2, boolean z) {
        if (this.mDisplayId != i) {
            return;
        }
        if (!this.mKeyguardStateController.isKeyguardGoingAway() || z) {
            boolean z2 = this.mTransitionPending;
            if (z2 && this.mTintChangePending) {
                this.mTintChangePending = false;
                animateIconTint(this.mPendingDarkIntensity, Math.max(0, j - SystemClock.uptimeMillis()), j2);
            } else if (z2) {
                this.mTransitionDeferring = true;
                this.mTransitionDeferringStartTime = j;
                this.mTransitionDeferringDuration = j2;
                this.mHandler.removeCallbacks(this.mTransitionDeferringDoneRunnable);
                this.mHandler.postAtTime(this.mTransitionDeferringDoneRunnable, j);
            }
            this.mTransitionPending = false;
        }
    }

    public void setIconsDark(boolean z, boolean z2) {
        float f = 1.0f;
        if (!z2) {
            setIconTintInternal(z ? 1.0f : 0.0f);
            if (!z) {
                f = 0.0f;
            }
            this.mNextDarkIntensity = f;
        } else if (this.mTransitionPending) {
            if (!z) {
                f = 0.0f;
            }
            deferIconTintChange(f);
        } else if (this.mTransitionDeferring) {
            if (!z) {
                f = 0.0f;
            }
            animateIconTint(f, Math.max(0, this.mTransitionDeferringStartTime - SystemClock.uptimeMillis()), this.mTransitionDeferringDuration);
        } else {
            if (!z) {
                f = 0.0f;
            }
            animateIconTint(f, 0, (long) this.mApplier.getTintAnimationDuration());
        }
    }

    public float getCurrentDarkIntensity() {
        return this.mDarkIntensity;
    }

    public final void deferIconTintChange(float f) {
        if (!this.mTintChangePending || f != this.mPendingDarkIntensity) {
            this.mTintChangePending = true;
            this.mPendingDarkIntensity = f;
        }
    }

    public final void animateIconTint(float f, long j, long j2) {
        if (this.mNextDarkIntensity != f) {
            ValueAnimator valueAnimator = this.mTintAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.mNextDarkIntensity = f;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mDarkIntensity, f});
            this.mTintAnimator = ofFloat;
            ofFloat.addUpdateListener(new LightBarTransitionsController$$ExternalSyntheticLambda0(this));
            this.mTintAnimator.setDuration(j2);
            this.mTintAnimator.setStartDelay(j);
            this.mTintAnimator.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            this.mTintAnimator.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateIconTint$0(ValueAnimator valueAnimator) {
        setIconTintInternal(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public final void setIconTintInternal(float f) {
        this.mDarkIntensity = f;
        dispatchDark();
    }

    public final void dispatchDark() {
        this.mApplier.applyDarkIntensity(MathUtils.lerp(this.mDarkIntensity, 0.0f, this.mDozeAmount));
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.print("  mTransitionDeferring=");
        printWriter.print(this.mTransitionDeferring);
        if (this.mTransitionDeferring) {
            printWriter.println();
            printWriter.print("   mTransitionDeferringStartTime=");
            printWriter.println(TimeUtils.formatUptime(this.mTransitionDeferringStartTime));
            printWriter.print("   mTransitionDeferringDuration=");
            TimeUtils.formatDuration(this.mTransitionDeferringDuration, printWriter);
            printWriter.println();
        }
        printWriter.print("  mTransitionPending=");
        printWriter.print(this.mTransitionPending);
        printWriter.print(" mTintChangePending=");
        printWriter.println(this.mTintChangePending);
        printWriter.print("  mPendingDarkIntensity=");
        printWriter.print(this.mPendingDarkIntensity);
        printWriter.print(" mDarkIntensity=");
        printWriter.print(this.mDarkIntensity);
        printWriter.print(" mNextDarkIntensity=");
        printWriter.println(this.mNextDarkIntensity);
    }

    public void onDozeAmountChanged(float f, float f2) {
        this.mDozeAmount = f2;
        dispatchDark();
    }

    public void overrideIconTintForNavMode(boolean z) {
        this.mOverrideIconTintForNavMode = Boolean.valueOf(z);
    }

    public boolean supportsIconTintForNavMode(int i) {
        Boolean bool = this.mOverrideIconTintForNavMode;
        if (bool != null) {
            return bool.booleanValue();
        }
        return !QuickStepContract.isGesturalMode(i);
    }
}

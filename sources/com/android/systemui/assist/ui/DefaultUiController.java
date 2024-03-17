package com.android.systemui.assist.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.metrics.LogMaker;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$layout;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.AssistantSessionEvent;
import dagger.Lazy;
import java.util.Locale;

public class DefaultUiController implements AssistManager.UiController {
    public static final boolean VERBOSE;
    public final AssistLogger mAssistLogger;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public boolean mAttached = false;
    public ValueAnimator mInvocationAnimator = new ValueAnimator();
    public boolean mInvocationInProgress = false;
    public InvocationLightsView mInvocationLightsView;
    public float mLastInvocationProgress = 0.0f;
    public final WindowManager.LayoutParams mLayoutParams;
    public final MetricsLogger mMetricsLogger;
    public final PathInterpolator mProgressInterpolator = new PathInterpolator(0.83f, 0.0f, 0.84f, 1.0f);
    public final FrameLayout mRoot;
    public final WindowManager mWindowManager;

    static {
        String str = Build.TYPE;
        Locale locale = Locale.ROOT;
        VERBOSE = str.toLowerCase(locale).contains("debug") || str.toLowerCase(locale).equals("eng");
    }

    public DefaultUiController(Context context, AssistLogger assistLogger, WindowManager windowManager, MetricsLogger metricsLogger, Lazy<AssistManager> lazy) {
        this.mAssistLogger = assistLogger;
        FrameLayout frameLayout = new FrameLayout(context);
        this.mRoot = frameLayout;
        this.mWindowManager = windowManager;
        this.mMetricsLogger = metricsLogger;
        this.mAssistManagerLazy = lazy;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -2, 0, 0, 2024, 808, -3);
        this.mLayoutParams = layoutParams;
        layoutParams.privateFlags = 64;
        layoutParams.gravity = 80;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Assist");
        InvocationLightsView invocationLightsView = (InvocationLightsView) LayoutInflater.from(context).inflate(R$layout.invocation_lights, frameLayout, false);
        this.mInvocationLightsView = invocationLightsView;
        frameLayout.addView(invocationLightsView);
    }

    public void onInvocationProgress(int i, float f) {
        boolean z = this.mInvocationInProgress;
        if (f == 1.0f) {
            animateInvocationCompletion(i, 0.0f);
        } else if (f == 0.0f) {
            hide();
        } else {
            if (!z) {
                attach();
                this.mInvocationInProgress = true;
            }
            setProgressInternal(i, f);
        }
        this.mLastInvocationProgress = f;
        logInvocationProgressMetrics(i, f, z);
    }

    public void onGestureCompletion(float f) {
        animateInvocationCompletion(1, f);
        logInvocationProgressMetrics(1, 1.0f, this.mInvocationInProgress);
    }

    public void hide() {
        detach();
        if (this.mInvocationAnimator.isRunning()) {
            this.mInvocationAnimator.cancel();
        }
        this.mInvocationLightsView.hide();
        this.mInvocationInProgress = false;
    }

    public void logInvocationProgressMetrics(int i, float f, boolean z) {
        if (f == 1.0f && VERBOSE) {
            Log.v("DefaultUiController", "Invocation complete: type=" + i);
        }
        if (!z && f > 0.0f) {
            if (VERBOSE) {
                Log.v("DefaultUiController", "Invocation started: type=" + i);
            }
            this.mAssistLogger.reportAssistantInvocationEventFromLegacy(i, false, (ComponentName) null, (Integer) null);
            this.mMetricsLogger.write(new LogMaker(1716).setType(4).setSubtype(this.mAssistManagerLazy.get().toLoggingSubType(i)));
        }
        ValueAnimator valueAnimator = this.mInvocationAnimator;
        if ((valueAnimator == null || !valueAnimator.isRunning()) && z && f == 0.0f) {
            if (VERBOSE) {
                Log.v("DefaultUiController", "Invocation cancelled: type=" + i);
            }
            this.mAssistLogger.reportAssistantSessionEvent(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED);
            MetricsLogger.action(new LogMaker(1716).setType(5).setSubtype(1));
        }
    }

    public final void attach() {
        if (!this.mAttached) {
            this.mWindowManager.addView(this.mRoot, this.mLayoutParams);
            this.mAttached = true;
        }
    }

    public final void detach() {
        if (this.mAttached) {
            this.mWindowManager.removeViewImmediate(this.mRoot);
            this.mAttached = false;
        }
    }

    public final void setProgressInternal(int i, float f) {
        this.mInvocationLightsView.onInvocationProgress(this.mProgressInterpolator.getInterpolation(f));
    }

    public final void animateInvocationCompletion(int i, float f) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.mLastInvocationProgress, 1.0f});
        this.mInvocationAnimator = ofFloat;
        ofFloat.setStartDelay(1);
        this.mInvocationAnimator.setDuration(200);
        this.mInvocationAnimator.addUpdateListener(new DefaultUiController$$ExternalSyntheticLambda0(this, i));
        this.mInvocationAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                DefaultUiController.this.mInvocationInProgress = false;
                DefaultUiController.this.mLastInvocationProgress = 0.0f;
                DefaultUiController.this.hide();
            }
        });
        this.mInvocationAnimator.start();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$animateInvocationCompletion$0(int i, ValueAnimator valueAnimator) {
        setProgressInternal(i, ((Float) valueAnimator.getAnimatedValue()).floatValue());
    }
}

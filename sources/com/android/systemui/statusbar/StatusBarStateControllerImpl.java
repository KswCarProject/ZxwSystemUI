package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.format.DateFormat;
import android.util.FloatProperty;
import android.util.Log;
import android.view.Choreographer;
import android.view.InsetsFlags;
import android.view.InsetsVisibilities;
import android.view.View;
import android.view.ViewDebug;
import android.view.animation.Interpolator;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.policy.CallbackController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

public class StatusBarStateControllerImpl implements SysuiStatusBarStateController, CallbackController<StatusBarStateController.StateListener>, Dumpable {
    public static final boolean DEBUG_IMMERSIVE_APPS = SystemProperties.getBoolean("persist.debug.immersive_apps", false);
    public static final FloatProperty<StatusBarStateControllerImpl> SET_DARK_AMOUNT_PROPERTY = new FloatProperty<StatusBarStateControllerImpl>("mDozeAmount") {
        public void setValue(StatusBarStateControllerImpl statusBarStateControllerImpl, float f) {
            statusBarStateControllerImpl.setDozeAmountInternal(f);
        }

        public Float get(StatusBarStateControllerImpl statusBarStateControllerImpl) {
            return Float.valueOf(statusBarStateControllerImpl.mDozeAmount);
        }
    };
    public static final Comparator<SysuiStatusBarStateController.RankedListener> sComparator = Comparator.comparingInt(new StatusBarStateControllerImpl$$ExternalSyntheticLambda1());
    public ValueAnimator mDarkAnimator;
    public float mDozeAmount;
    public float mDozeAmountTarget;
    public Interpolator mDozeInterpolator;
    public HistoricalState[] mHistoricalRecords;
    public int mHistoryIndex;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public boolean mIsDozing;
    public boolean mIsExpanded;
    public boolean mIsFullscreen;
    public boolean mKeyguardRequested;
    public int mLastState;
    public boolean mLeaveOpenOnKeyguardHide;
    public final ArrayList<SysuiStatusBarStateController.RankedListener> mListeners = new ArrayList<>();
    public boolean mPulsing;
    public int mState;
    public final UiEventLogger mUiEventLogger;
    public int mUpcomingState;
    public View mView;

    public StatusBarStateControllerImpl(UiEventLogger uiEventLogger, DumpManager dumpManager, InteractionJankMonitor interactionJankMonitor) {
        this.mHistoryIndex = 0;
        this.mHistoricalRecords = new HistoricalState[32];
        this.mIsFullscreen = false;
        this.mDozeInterpolator = Interpolators.FAST_OUT_SLOW_IN;
        this.mUiEventLogger = uiEventLogger;
        this.mInteractionJankMonitor = interactionJankMonitor;
        for (int i = 0; i < 32; i++) {
            this.mHistoricalRecords[i] = new HistoricalState();
        }
        dumpManager.registerDumpable(this);
    }

    public int getState() {
        return this.mState;
    }

    public boolean setState(int i, boolean z) {
        if (i > 2 || i < 0) {
            throw new IllegalArgumentException("Invalid state " + i);
        } else if (!z && i == this.mState && i == this.mUpcomingState) {
            return false;
        } else {
            if (i != this.mUpcomingState) {
                Log.d("SbStateController", "setState: requested state " + StatusBarState.toString(i) + "!= upcomingState: " + StatusBarState.toString(this.mUpcomingState) + ". This usually means the status bar state transition was interrupted before the upcoming state could be applied.");
            }
            recordHistoricalState(i, this.mState, false);
            if (this.mState == 0 && i == 2) {
                Log.e("SbStateController", "Invalid state transition: SHADE -> SHADE_LOCKED", new Throwable());
            }
            synchronized (this.mListeners) {
                String str = getClass().getSimpleName() + "#setState(" + i + ")";
                DejankUtils.startDetectingBlockingIpcs(str);
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onStatePreChange(this.mState, i);
                }
                this.mLastState = this.mState;
                this.mState = i;
                updateUpcomingState(i);
                this.mUiEventLogger.log(StatusBarStateEvent.fromState(this.mState));
                Trace.instantForTrack(4096, "UI Events", "StatusBarState " + str);
                Iterator it2 = new ArrayList(this.mListeners).iterator();
                while (it2.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it2.next()).mListener.onStateChanged(this.mState);
                }
                Iterator it3 = new ArrayList(this.mListeners).iterator();
                while (it3.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it3.next()).mListener.onStatePostChange();
                }
                DejankUtils.stopDetectingBlockingIpcs(str);
            }
            return true;
        }
    }

    public void setUpcomingState(int i) {
        recordHistoricalState(i, this.mState, true);
        updateUpcomingState(i);
    }

    public final void updateUpcomingState(int i) {
        if (this.mUpcomingState != i) {
            this.mUpcomingState = i;
            Iterator it = new ArrayList(this.mListeners).iterator();
            while (it.hasNext()) {
                ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onUpcomingStateChanged(this.mUpcomingState);
            }
        }
    }

    public int getCurrentOrUpcomingState() {
        return this.mUpcomingState;
    }

    public boolean isDozing() {
        return this.mIsDozing;
    }

    public boolean isPulsing() {
        return this.mPulsing;
    }

    public float getDozeAmount() {
        return this.mDozeAmount;
    }

    public boolean isExpanded() {
        return this.mIsExpanded;
    }

    public boolean setPanelExpanded(boolean z) {
        if (this.mIsExpanded == z) {
            return false;
        }
        this.mIsExpanded = z;
        String str = getClass().getSimpleName() + "#setIsExpanded";
        DejankUtils.startDetectingBlockingIpcs(str);
        Iterator it = new ArrayList(this.mListeners).iterator();
        while (it.hasNext()) {
            ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onExpandedChanged(this.mIsExpanded);
        }
        DejankUtils.stopDetectingBlockingIpcs(str);
        return true;
    }

    public float getInterpolatedDozeAmount() {
        return this.mDozeInterpolator.getInterpolation(this.mDozeAmount);
    }

    public boolean setIsDozing(boolean z) {
        if (this.mIsDozing == z) {
            return false;
        }
        this.mIsDozing = z;
        synchronized (this.mListeners) {
            String str = getClass().getSimpleName() + "#setIsDozing";
            DejankUtils.startDetectingBlockingIpcs(str);
            Iterator it = new ArrayList(this.mListeners).iterator();
            while (it.hasNext()) {
                ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onDozingChanged(z);
            }
            DejankUtils.stopDetectingBlockingIpcs(str);
        }
        return true;
    }

    public void setAndInstrumentDozeAmount(View view, float f, boolean z) {
        ValueAnimator valueAnimator = this.mDarkAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            if (!z || this.mDozeAmountTarget != f) {
                this.mDarkAnimator.cancel();
            } else {
                return;
            }
        }
        View view2 = this.mView;
        if ((view2 == null || !view2.isAttachedToWindow()) && view != null && view.isAttachedToWindow()) {
            this.mView = view;
        }
        this.mDozeAmountTarget = f;
        if (z) {
            startDozeAnimation();
        } else {
            setDozeAmountInternal(f);
        }
    }

    public final void startDozeAnimation() {
        Interpolator interpolator;
        float f = this.mDozeAmount;
        if (f == 0.0f || f == 1.0f) {
            if (this.mIsDozing) {
                interpolator = Interpolators.FAST_OUT_SLOW_IN;
            } else {
                interpolator = Interpolators.TOUCH_RESPONSE_REVERSE;
            }
            this.mDozeInterpolator = interpolator;
        }
        if (f == 1.0f && !this.mIsDozing) {
            setDozeAmountInternal(0.99f);
        }
        this.mDarkAnimator = createDarkAnimator();
    }

    @VisibleForTesting
    public ObjectAnimator createDarkAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, SET_DARK_AMOUNT_PROPERTY, new float[]{this.mDozeAmountTarget});
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.setDuration(500);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                StatusBarStateControllerImpl.this.cancelInteractionJankMonitor();
            }

            public void onAnimationEnd(Animator animator) {
                StatusBarStateControllerImpl.this.endInteractionJankMonitor();
            }

            public void onAnimationStart(Animator animator) {
                StatusBarStateControllerImpl.this.beginInteractionJankMonitor();
            }
        });
        ofFloat.start();
        return ofFloat;
    }

    public final void setDozeAmountInternal(float f) {
        if (Float.compare(f, this.mDozeAmount) != 0) {
            this.mDozeAmount = f;
            float interpolation = this.mDozeInterpolator.getInterpolation(f);
            synchronized (this.mListeners) {
                String str = getClass().getSimpleName() + "#setDozeAmount";
                DejankUtils.startDetectingBlockingIpcs(str);
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onDozeAmountChanged(this.mDozeAmount, interpolation);
                }
                DejankUtils.stopDetectingBlockingIpcs(str);
            }
        }
    }

    public final void beginInteractionJankMonitor() {
        View view;
        boolean z = this.mIsDozing;
        boolean z2 = (z && this.mDozeAmount == 0.0f) || (!z && this.mDozeAmount == 1.0f);
        if (this.mInteractionJankMonitor != null && (view = this.mView) != null && view.isAttachedToWindow()) {
            if (z2) {
                Choreographer.getInstance().postCallback(1, new StatusBarStateControllerImpl$$ExternalSyntheticLambda2(this), (Object) null);
            } else {
                this.mInteractionJankMonitor.begin(InteractionJankMonitor.Configuration.Builder.withView(getCujType(), this.mView).setDeferMonitorForAnimationStart(false));
            }
        }
    }

    public final void endInteractionJankMonitor() {
        InteractionJankMonitor interactionJankMonitor = this.mInteractionJankMonitor;
        if (interactionJankMonitor != null) {
            interactionJankMonitor.end(getCujType());
        }
    }

    public final void cancelInteractionJankMonitor() {
        InteractionJankMonitor interactionJankMonitor = this.mInteractionJankMonitor;
        if (interactionJankMonitor != null) {
            interactionJankMonitor.cancel(getCujType());
        }
    }

    public final int getCujType() {
        return this.mIsDozing ? 24 : 23;
    }

    public boolean goingToFullShade() {
        return this.mState == 0 && this.mLeaveOpenOnKeyguardHide;
    }

    public void setLeaveOpenOnKeyguardHide(boolean z) {
        this.mLeaveOpenOnKeyguardHide = z;
    }

    public boolean leaveOpenOnKeyguardHide() {
        return this.mLeaveOpenOnKeyguardHide;
    }

    public boolean fromShadeLocked() {
        return this.mLastState == 2;
    }

    public void addCallback(StatusBarStateController.StateListener stateListener) {
        synchronized (this.mListeners) {
            addListenerInternalLocked(stateListener, Integer.MAX_VALUE);
        }
    }

    @Deprecated
    public void addCallback(StatusBarStateController.StateListener stateListener, int i) {
        synchronized (this.mListeners) {
            addListenerInternalLocked(stateListener, i);
        }
    }

    @GuardedBy({"mListeners"})
    public final void addListenerInternalLocked(StatusBarStateController.StateListener stateListener, int i) {
        Iterator<SysuiStatusBarStateController.RankedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            if (it.next().mListener.equals(stateListener)) {
                return;
            }
        }
        this.mListeners.add(new SysuiStatusBarStateController.RankedListener(stateListener, i));
        this.mListeners.sort(sComparator);
    }

    public void removeCallback(StatusBarStateController.StateListener stateListener) {
        synchronized (this.mListeners) {
            this.mListeners.removeIf(new StatusBarStateControllerImpl$$ExternalSyntheticLambda0(stateListener));
        }
    }

    public void setKeyguardRequested(boolean z) {
        this.mKeyguardRequested = z;
    }

    public boolean isKeyguardRequested() {
        return this.mKeyguardRequested;
    }

    public void setSystemBarAttributes(int i, int i2, InsetsVisibilities insetsVisibilities, String str) {
        boolean z = false;
        boolean z2 = !insetsVisibilities.getVisibility(0) || !insetsVisibilities.getVisibility(1);
        if (this.mIsFullscreen != z2) {
            this.mIsFullscreen = z2;
            synchronized (this.mListeners) {
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onFullscreenStateChanged(z2);
                }
            }
        }
        if (DEBUG_IMMERSIVE_APPS) {
            if ((i & 4) != 0) {
                z = true;
            }
            String flagsToString = ViewDebug.flagsToString(InsetsFlags.class, "behavior", i2);
            String insetsVisibilities2 = insetsVisibilities.toString();
            if (insetsVisibilities2.isEmpty()) {
                insetsVisibilities2 = "none";
            }
            Log.d("SbStateController", str + " dim=" + z + " behavior=" + flagsToString + " requested visibilities: " + insetsVisibilities2);
        }
    }

    public void setPulsing(boolean z) {
        if (this.mPulsing != z) {
            this.mPulsing = z;
            synchronized (this.mListeners) {
                Iterator it = new ArrayList(this.mListeners).iterator();
                while (it.hasNext()) {
                    ((SysuiStatusBarStateController.RankedListener) it.next()).mListener.onPulsingChanged(z);
                }
            }
        }
    }

    public static String describe(int i) {
        return StatusBarState.toString(i);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("StatusBarStateController: ");
        printWriter.println(" mState=" + this.mState + " (" + describe(this.mState) + ")");
        printWriter.println(" mLastState=" + this.mLastState + " (" + describe(this.mLastState) + ")");
        StringBuilder sb = new StringBuilder();
        sb.append(" mLeaveOpenOnKeyguardHide=");
        sb.append(this.mLeaveOpenOnKeyguardHide);
        printWriter.println(sb.toString());
        printWriter.println(" mKeyguardRequested=" + this.mKeyguardRequested);
        printWriter.println(" mIsDozing=" + this.mIsDozing);
        printWriter.println(" mListeners{" + this.mListeners.size() + "}=");
        Iterator<SysuiStatusBarStateController.RankedListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            printWriter.println("    " + it.next().mListener);
        }
        printWriter.println(" Historical states:");
        int i = 0;
        for (int i2 = 0; i2 < 32; i2++) {
            if (this.mHistoricalRecords[i2].mTimestamp != 0) {
                i++;
            }
        }
        for (int i3 = this.mHistoryIndex + 32; i3 >= ((this.mHistoryIndex + 32) - i) + 1; i3 += -1) {
            printWriter.println("  (" + (((this.mHistoryIndex + 32) - i3) + 1) + ")" + this.mHistoricalRecords[i3 & 31]);
        }
    }

    public final void recordHistoricalState(int i, int i2, boolean z) {
        Trace.traceCounter(4096, "statusBarState", i);
        int i3 = (this.mHistoryIndex + 1) % 32;
        this.mHistoryIndex = i3;
        HistoricalState historicalState = this.mHistoricalRecords[i3];
        historicalState.mNewState = i;
        historicalState.mLastState = i2;
        historicalState.mTimestamp = System.currentTimeMillis();
        historicalState.mUpcoming = z;
    }

    public static class HistoricalState {
        public int mLastState;
        public int mNewState;
        public long mTimestamp;
        public boolean mUpcoming;

        public HistoricalState() {
        }

        public String toString() {
            if (this.mTimestamp != 0) {
                StringBuilder sb = new StringBuilder();
                if (this.mUpcoming) {
                    sb.append("upcoming-");
                }
                sb.append("newState=");
                sb.append(this.mNewState);
                sb.append("(");
                sb.append(StatusBarStateControllerImpl.describe(this.mNewState));
                sb.append(")");
                sb.append(" lastState=");
                sb.append(this.mLastState);
                sb.append("(");
                sb.append(StatusBarStateControllerImpl.describe(this.mLastState));
                sb.append(")");
                sb.append(" timestamp=");
                sb.append(DateFormat.format("MM-dd HH:mm:ss", this.mTimestamp));
                return sb.toString();
            }
            return "Empty " + getClass().getSimpleName();
        }
    }
}

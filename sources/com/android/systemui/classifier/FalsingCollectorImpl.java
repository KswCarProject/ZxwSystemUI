package com.android.systemui.classifier;

import android.hardware.biometrics.BiometricSourceType;
import android.view.MotionEvent;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.StatusBarState;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.systemui.util.time.SystemClock;
import java.util.Collections;
import java.util.Objects;

public class FalsingCollectorImpl implements FalsingCollector {
    public boolean mAvoidGesture;
    public final BatteryController mBatteryController;
    public final BatteryController.BatteryStateChangeCallback mBatteryListener;
    public final DockManager.DockEventListener mDockEventListener;
    public final DockManager mDockManager;
    public final FalsingDataProvider mFalsingDataProvider;
    public final FalsingManager mFalsingManager;
    public final HistoryTracker mHistoryTracker;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitorCallback mKeyguardUpdateCallback;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final DelayableExecutor mMainExecutor;
    public MotionEvent mPendingDownEvent;
    public final ProximitySensor mProximitySensor;
    public boolean mScreenOn;
    public final ThresholdSensor.Listener mSensorEventListener = new FalsingCollectorImpl$$ExternalSyntheticLambda0(this);
    public boolean mSessionStarted;
    public boolean mShowingAod;
    public int mState;
    public final StatusBarStateController mStatusBarStateController;
    public final StatusBarStateController.StateListener mStatusBarStateListener;
    public final SystemClock mSystemClock;

    public static void logDebug(String str, Throwable th) {
    }

    public boolean isReportingEnabled() {
        return false;
    }

    public void onAffordanceSwipingAborted() {
    }

    public void onAffordanceSwipingStarted(boolean z) {
    }

    public void onCameraHintStarted() {
    }

    public void onCameraOn() {
    }

    public void onExpansionFromPulseStopped() {
    }

    public void onLeftAffordanceHintStarted() {
    }

    public void onLeftAffordanceOn() {
    }

    public void onNotificationActive() {
    }

    public void onNotificationDismissed() {
    }

    public void onNotificationStartDismissing() {
    }

    public void onNotificationStartDraggingDown() {
    }

    public void onNotificationStopDismissing() {
    }

    public void onNotificationStopDraggingDown() {
    }

    public void onQsDown() {
    }

    public void onStartExpandingFromPulse() {
    }

    public void onTrackingStarted(boolean z) {
    }

    public void onTrackingStopped() {
    }

    public void onUnlockHintStarted() {
    }

    public void setNotificationExpanded() {
    }

    public boolean shouldEnforceBouncer() {
        return false;
    }

    public FalsingCollectorImpl(FalsingDataProvider falsingDataProvider, FalsingManager falsingManager, KeyguardUpdateMonitor keyguardUpdateMonitor, HistoryTracker historyTracker, ProximitySensor proximitySensor, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController, BatteryController batteryController, DockManager dockManager, DelayableExecutor delayableExecutor, SystemClock systemClock) {
        AnonymousClass1 r0 = new StatusBarStateController.StateListener() {
            public void onStateChanged(int i) {
                FalsingCollectorImpl.logDebug("StatusBarState=" + StatusBarState.toString(i));
                FalsingCollectorImpl.this.mState = i;
                FalsingCollectorImpl.this.updateSessionActive();
            }
        };
        this.mStatusBarStateListener = r0;
        AnonymousClass2 r1 = new KeyguardUpdateMonitorCallback() {
            public void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
                if (i == KeyguardUpdateMonitor.getCurrentUser() && biometricSourceType == BiometricSourceType.FACE) {
                    FalsingCollectorImpl.this.mFalsingDataProvider.setJustUnlockedWithFace(true);
                }
            }
        };
        this.mKeyguardUpdateCallback = r1;
        AnonymousClass3 r2 = new BatteryController.BatteryStateChangeCallback() {
            public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            }

            public void onWirelessChargingChanged(boolean z) {
                if (z || FalsingCollectorImpl.this.mDockManager.isDocked()) {
                    FalsingCollectorImpl.this.mProximitySensor.pause();
                } else {
                    FalsingCollectorImpl.this.mProximitySensor.resume();
                }
            }
        };
        this.mBatteryListener = r2;
        AnonymousClass4 r3 = new DockManager.DockEventListener() {
        };
        this.mDockEventListener = r3;
        this.mFalsingDataProvider = falsingDataProvider;
        this.mFalsingManager = falsingManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mHistoryTracker = historyTracker;
        this.mProximitySensor = proximitySensor;
        this.mStatusBarStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mBatteryController = batteryController;
        this.mDockManager = dockManager;
        this.mMainExecutor = delayableExecutor;
        this.mSystemClock = systemClock;
        proximitySensor.setTag("FalsingManager");
        proximitySensor.setDelay(1);
        statusBarStateController.addCallback(r0);
        this.mState = statusBarStateController.getState();
        keyguardUpdateMonitor.registerCallback(r1);
        batteryController.addCallback(r2);
        dockManager.addListener(r3);
    }

    public void onSuccessfulUnlock() {
        this.mFalsingManager.onSuccessfulUnlock();
        sessionEnd();
    }

    public void setShowingAod(boolean z) {
        this.mShowingAod = z;
        updateSessionActive();
    }

    public void setQsExpanded(boolean z) {
        if (z) {
            unregisterSensors();
        } else if (this.mSessionStarted) {
            registerSensors();
        }
    }

    public void onScreenOnFromTouch() {
        onScreenTurningOn();
    }

    public void onScreenTurningOn() {
        this.mScreenOn = true;
        updateSessionActive();
    }

    public void onScreenOff() {
        this.mScreenOn = false;
        updateSessionActive();
    }

    public void onBouncerShown() {
        unregisterSensors();
    }

    public void onBouncerHidden() {
        if (this.mSessionStarted) {
            registerSensors();
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        if (!this.mKeyguardStateController.isShowing() || (this.mStatusBarStateController.isDozing() && !this.mStatusBarStateController.isPulsing())) {
            avoidGesture();
        } else if (motionEvent.getActionMasked() != 4) {
            if (motionEvent.getActionMasked() == 0) {
                this.mPendingDownEvent = MotionEvent.obtain(motionEvent);
                this.mAvoidGesture = false;
            } else if (!this.mAvoidGesture) {
                MotionEvent motionEvent2 = this.mPendingDownEvent;
                if (motionEvent2 != null) {
                    this.mFalsingDataProvider.onMotionEvent(motionEvent2);
                    this.mPendingDownEvent.recycle();
                    this.mPendingDownEvent = null;
                }
                this.mFalsingDataProvider.onMotionEvent(motionEvent);
            }
        }
    }

    public void onMotionEventComplete() {
        DelayableExecutor delayableExecutor = this.mMainExecutor;
        FalsingDataProvider falsingDataProvider = this.mFalsingDataProvider;
        Objects.requireNonNull(falsingDataProvider);
        delayableExecutor.executeDelayed(new FalsingCollectorImpl$$ExternalSyntheticLambda1(falsingDataProvider), 100);
    }

    public void avoidGesture() {
        this.mAvoidGesture = true;
        MotionEvent motionEvent = this.mPendingDownEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mPendingDownEvent = null;
        }
    }

    public void updateFalseConfidence(FalsingClassifier.Result result) {
        this.mHistoryTracker.addResults(Collections.singleton(result), this.mSystemClock.uptimeMillis());
    }

    public final boolean shouldSessionBeActive() {
        return this.mScreenOn && this.mState == 1 && !this.mShowingAod;
    }

    public final void updateSessionActive() {
        if (shouldSessionBeActive()) {
            sessionStart();
        } else {
            sessionEnd();
        }
    }

    public final void sessionStart() {
        if (!this.mSessionStarted && shouldSessionBeActive()) {
            logDebug("Starting Session");
            this.mSessionStarted = true;
            this.mFalsingDataProvider.setJustUnlockedWithFace(false);
            registerSensors();
            this.mFalsingDataProvider.onSessionStarted();
        }
    }

    public final void sessionEnd() {
        if (this.mSessionStarted) {
            logDebug("Ending Session");
            this.mSessionStarted = false;
            unregisterSensors();
            this.mFalsingDataProvider.onSessionEnd();
        }
    }

    public final void registerSensors() {
        this.mProximitySensor.register(this.mSensorEventListener);
    }

    public final void unregisterSensors() {
        this.mProximitySensor.unregister(this.mSensorEventListener);
    }

    public final void onProximityEvent(ThresholdSensorEvent thresholdSensorEvent) {
        this.mFalsingManager.onProximityEvent(new ProximityEventImpl(thresholdSensorEvent));
    }

    public static void logDebug(String str) {
        logDebug(str, (Throwable) null);
    }

    public static class ProximityEventImpl implements FalsingManager.ProximityEvent {
        public ThresholdSensorEvent mThresholdSensorEvent;

        public ProximityEventImpl(ThresholdSensorEvent thresholdSensorEvent) {
            this.mThresholdSensorEvent = thresholdSensorEvent;
        }

        public boolean getCovered() {
            return this.mThresholdSensorEvent.getBelow();
        }

        public long getTimestampNs() {
            return this.mThresholdSensorEvent.getTimestampNs();
        }
    }
}

package com.android.systemui.doze;

import android.os.Handler;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.util.wakelock.SettableWakeLock;
import com.android.systemui.util.wakelock.WakeLock;
import javax.inject.Provider;

public class DozeScreenState implements DozeMachine.Part {
    public static final boolean DEBUG = DozeService.DEBUG;
    public final Runnable mApplyPendingScreenState = new DozeScreenState$$ExternalSyntheticLambda0(this);
    public final AuthController mAuthController;
    public final AuthController.Callback mAuthControllerCallback;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final DozeScreenBrightness mDozeScreenBrightness;
    public final DozeMachine.Service mDozeService;
    public final Handler mHandler;
    public final DozeParameters mParameters;
    public int mPendingScreenState = 0;
    public UdfpsController mUdfpsController;
    public final Provider<UdfpsController> mUdfpsControllerProvider;
    public SettableWakeLock mWakeLock;

    public DozeScreenState(DozeMachine.Service service, Handler handler, DozeHost dozeHost, DozeParameters dozeParameters, WakeLock wakeLock, AuthController authController, Provider<UdfpsController> provider, DozeLog dozeLog, DozeScreenBrightness dozeScreenBrightness) {
        AnonymousClass1 r0 = new AuthController.Callback() {
            public void onAllAuthenticatorsRegistered() {
                DozeScreenState.this.updateUdfpsController();
            }

            public void onEnrollmentsChanged() {
                DozeScreenState.this.updateUdfpsController();
            }
        };
        this.mAuthControllerCallback = r0;
        this.mDozeService = service;
        this.mHandler = handler;
        this.mParameters = dozeParameters;
        this.mDozeHost = dozeHost;
        this.mWakeLock = new SettableWakeLock(wakeLock, "DozeScreenState");
        this.mAuthController = authController;
        this.mUdfpsControllerProvider = provider;
        this.mDozeLog = dozeLog;
        this.mDozeScreenBrightness = dozeScreenBrightness;
        updateUdfpsController();
        if (this.mUdfpsController == null) {
            authController.addCallback(r0);
        }
    }

    public final void updateUdfpsController() {
        if (this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
            this.mUdfpsController = this.mUdfpsControllerProvider.get();
        } else {
            this.mUdfpsController = null;
        }
    }

    public void destroy() {
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        UdfpsController udfpsController;
        int screenState = state2.screenState(this.mParameters);
        this.mDozeHost.cancelGentleSleep();
        boolean z = false;
        if (state2 == DozeMachine.State.FINISH) {
            this.mPendingScreenState = 0;
            this.mHandler.removeCallbacks(this.mApplyPendingScreenState);
            lambda$transitionTo$0(screenState);
            this.mWakeLock.setAcquired(false);
        } else if (screenState != 0) {
            boolean hasCallbacks = this.mHandler.hasCallbacks(this.mApplyPendingScreenState);
            boolean z2 = state == DozeMachine.State.DOZE_PULSE_DONE && state2.isAlwaysOn();
            DozeMachine.State state3 = DozeMachine.State.DOZE_AOD_PAUSED;
            boolean z3 = (state == state3 || state == DozeMachine.State.DOZE) && state2.isAlwaysOn();
            boolean z4 = (state.isAlwaysOn() && state2 == DozeMachine.State.DOZE) || (state == DozeMachine.State.DOZE_AOD_PAUSING && state2 == state3);
            boolean z5 = state == DozeMachine.State.INITIALIZED;
            if (hasCallbacks || z5 || z2 || z3) {
                this.mPendingScreenState = screenState;
                DozeMachine.State state4 = DozeMachine.State.DOZE_AOD;
                boolean z6 = state2 == state4 && this.mParameters.shouldDelayDisplayDozeTransition() && !z3;
                if (state2 == state4 && (udfpsController = this.mUdfpsController) != null && udfpsController.isFingerDown()) {
                    z = true;
                }
                if (!hasCallbacks) {
                    if (DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Display state changed to ");
                        sb.append(screenState);
                        sb.append(" delayed by ");
                        sb.append(z6 ? 4000 : 1);
                        Log.d("DozeScreenState", sb.toString());
                    }
                    if (z6) {
                        if (z5) {
                            lambda$transitionTo$0(2);
                            this.mPendingScreenState = screenState;
                        }
                        this.mHandler.postDelayed(this.mApplyPendingScreenState, 4000);
                    } else if (z) {
                        this.mDozeLog.traceDisplayStateDelayedByUdfps(this.mPendingScreenState);
                        this.mHandler.postDelayed(this.mApplyPendingScreenState, 1200);
                    } else {
                        this.mHandler.post(this.mApplyPendingScreenState);
                    }
                } else if (DEBUG) {
                    Log.d("DozeScreenState", "Pending display state change to " + screenState);
                }
                if (z6 || z) {
                    this.mWakeLock.setAcquired(true);
                }
            } else if (z4) {
                this.mDozeHost.prepareForGentleSleep(new DozeScreenState$$ExternalSyntheticLambda1(this, screenState));
            } else {
                lambda$transitionTo$0(screenState);
            }
        }
    }

    public final void applyPendingScreenState() {
        UdfpsController udfpsController = this.mUdfpsController;
        if (udfpsController == null || !udfpsController.isFingerDown()) {
            lambda$transitionTo$0(this.mPendingScreenState);
            this.mPendingScreenState = 0;
            return;
        }
        this.mDozeLog.traceDisplayStateDelayedByUdfps(this.mPendingScreenState);
        this.mHandler.postDelayed(this.mApplyPendingScreenState, 1200);
    }

    /* renamed from: applyScreenState */
    public final void lambda$transitionTo$0(int i) {
        if (i != 0) {
            if (DEBUG) {
                Log.d("DozeScreenState", "setDozeScreenState(" + i + ")");
            }
            this.mDozeService.setDozeScreenState(i);
            if (i == 3) {
                this.mDozeScreenBrightness.updateBrightnessAndReady(false);
            }
            this.mPendingScreenState = 0;
            this.mWakeLock.setAcquired(false);
        }
    }
}

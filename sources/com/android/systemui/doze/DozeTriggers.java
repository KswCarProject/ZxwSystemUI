package com.android.systemui.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximityCheck;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class DozeTriggers implements DozeMachine.Part {
    public static final boolean DEBUG = DozeService.DEBUG;
    public static boolean sWakeDisplaySensorState = true;
    public final boolean mAllowPulseTriggers;
    public Runnable mAodInterruptRunnable;
    public final AuthController mAuthController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final TriggerReceiver mBroadcastReceiver = new TriggerReceiver();
    public final AmbientDisplayConfiguration mConfig;
    public final Context mContext;
    public final DevicePostureController.Callback mDevicePostureCallback = new DozeTriggers$$ExternalSyntheticLambda1();
    public final DevicePostureController mDevicePostureController;
    public final DockEventListener mDockEventListener = new DockEventListener();
    public final DockManager mDockManager;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public final DozeSensors mDozeSensors;
    public DozeHost.Callback mHostCallback = new DozeHost.Callback() {
        public void onNotificationAlerted(Runnable runnable) {
            DozeTriggers.this.onNotification(runnable);
        }
    };
    public final KeyguardStateController mKeyguardStateController;
    public DozeMachine mMachine;
    public final DelayableExecutor mMainExecutor;
    public long mNotificationPulseTime;
    public final ProximityCheck mProxCheck;
    public boolean mPulsePending;
    public final AsyncSensorManager mSensorManager;
    public final UiEventLogger mUiEventLogger;
    public final WakeLock mWakeLock;
    public boolean mWantProxSensor;
    public boolean mWantSensors;
    public boolean mWantTouchScreenSensors;

    public static /* synthetic */ void lambda$new$0(int i) {
    }

    @VisibleForTesting
    public enum DozingUpdateUiEvent implements UiEventLogger.UiEventEnum {
        DOZING_UPDATE_NOTIFICATION(433),
        DOZING_UPDATE_SIGMOTION(434),
        DOZING_UPDATE_SENSOR_PICKUP(435),
        DOZING_UPDATE_SENSOR_DOUBLE_TAP(436),
        DOZING_UPDATE_SENSOR_LONG_SQUEEZE(437),
        DOZING_UPDATE_DOCKING(438),
        DOZING_UPDATE_SENSOR_WAKEUP(439),
        DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN(440),
        DOZING_UPDATE_SENSOR_TAP(441),
        DOZING_UPDATE_AUTH_TRIGGERED(657),
        DOZING_UPDATE_QUICK_PICKUP(708),
        DOZING_UPDATE_WAKE_TIMEOUT(794);
        
        private final int mId;

        /* access modifiers changed from: public */
        DozingUpdateUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }

        public static DozingUpdateUiEvent fromReason(int i) {
            switch (i) {
                case 1:
                    return DOZING_UPDATE_NOTIFICATION;
                case 2:
                    return DOZING_UPDATE_SIGMOTION;
                case 3:
                    return DOZING_UPDATE_SENSOR_PICKUP;
                case 4:
                    return DOZING_UPDATE_SENSOR_DOUBLE_TAP;
                case 5:
                    return DOZING_UPDATE_SENSOR_LONG_SQUEEZE;
                case 6:
                    return DOZING_UPDATE_DOCKING;
                case 7:
                    return DOZING_UPDATE_SENSOR_WAKEUP;
                case 8:
                    return DOZING_UPDATE_SENSOR_WAKE_LOCKSCREEN;
                case 9:
                    return DOZING_UPDATE_SENSOR_TAP;
                case 10:
                    return DOZING_UPDATE_AUTH_TRIGGERED;
                case 11:
                    return DOZING_UPDATE_QUICK_PICKUP;
                default:
                    return null;
            }
        }
    }

    public DozeTriggers(Context context, DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeParameters dozeParameters, AsyncSensorManager asyncSensorManager, WakeLock wakeLock, DockManager dockManager, ProximitySensor proximitySensor, ProximityCheck proximityCheck, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, SecureSettings secureSettings, AuthController authController, DelayableExecutor delayableExecutor, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, DevicePostureController devicePostureController) {
        this.mContext = context;
        this.mDozeHost = dozeHost;
        AmbientDisplayConfiguration ambientDisplayConfiguration2 = ambientDisplayConfiguration;
        this.mConfig = ambientDisplayConfiguration2;
        DozeParameters dozeParameters2 = dozeParameters;
        this.mDozeParameters = dozeParameters2;
        AsyncSensorManager asyncSensorManager2 = asyncSensorManager;
        this.mSensorManager = asyncSensorManager2;
        WakeLock wakeLock2 = wakeLock;
        this.mWakeLock = wakeLock2;
        this.mAllowPulseTriggers = true;
        DevicePostureController devicePostureController2 = devicePostureController;
        this.mDevicePostureController = devicePostureController2;
        this.mDozeSensors = new DozeSensors(context, asyncSensorManager2, dozeParameters2, ambientDisplayConfiguration2, wakeLock2, new DozeTriggers$$ExternalSyntheticLambda2(this), new DozeTriggers$$ExternalSyntheticLambda3(this), dozeLog, proximitySensor, secureSettings, authController, devicePostureController2);
        this.mDockManager = dockManager;
        this.mProxCheck = proximityCheck;
        this.mDozeLog = dozeLog;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mAuthController = authController;
        this.mMainExecutor = delayableExecutor;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
    }

    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    public void destroy() {
        this.mDozeSensors.destroy();
        this.mProxCheck.destroy();
    }

    public final void onNotification(Runnable runnable) {
        if (DozeMachine.DEBUG) {
            Log.d("DozeTriggers", "requestNotificationPulse");
        }
        if (!sWakeDisplaySensorState) {
            Log.d("DozeTriggers", "Wake display false. Pulse denied.");
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("wakeDisplaySensor");
            return;
        }
        this.mNotificationPulseTime = SystemClock.elapsedRealtime();
        if (!this.mConfig.pulseOnNotificationEnabled(-2)) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("pulseOnNotificationsDisabled");
        } else if (this.mDozeHost.isAlwaysOnSuppressed()) {
            runIfNotNull(runnable);
            this.mDozeLog.tracePulseDropped("dozeSuppressed");
        } else {
            requestPulse(1, false, runnable);
            this.mDozeLog.traceNotificationPulse();
        }
    }

    public static void runIfNotNull(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void proximityCheckThenCall(Consumer<Boolean> consumer, boolean z, int i) {
        Boolean isProximityCurrentlyNear = this.mDozeSensors.isProximityCurrentlyNear();
        if (z) {
            consumer.accept((Object) null);
        } else if (isProximityCurrentlyNear != null) {
            consumer.accept(isProximityCurrentlyNear);
        } else {
            this.mProxCheck.check(500, new DozeTriggers$$ExternalSyntheticLambda7(this, SystemClock.uptimeMillis(), i, consumer));
            this.mWakeLock.acquire("DozeTriggers");
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$proximityCheckThenCall$1(long j, int i, Consumer consumer, Boolean bool) {
        boolean z;
        long uptimeMillis = SystemClock.uptimeMillis();
        DozeLog dozeLog = this.mDozeLog;
        if (bool == null) {
            z = false;
        } else {
            z = bool.booleanValue();
        }
        dozeLog.traceProximityResult(z, uptimeMillis - j, i);
        consumer.accept(bool);
        this.mWakeLock.release("DozeTriggers");
    }

    @VisibleForTesting
    public void onSensor(int i, float f, float f2, float[] fArr) {
        int i2 = i;
        float[] fArr2 = fArr;
        boolean z = false;
        boolean z2 = i2 == 4;
        boolean z3 = i2 == 9;
        boolean z4 = i2 == 3;
        boolean z5 = i2 == 5;
        boolean z6 = i2 == 7;
        boolean z7 = i2 == 8;
        boolean z8 = i2 == 10;
        boolean z9 = i2 == 11;
        boolean z10 = z9 || ((z6 || z7) && fArr2 != null && fArr2.length > 0 && fArr2[0] != 0.0f);
        DozeMachine.State state = null;
        if (z6) {
            if (!this.mMachine.isExecutingTransition()) {
                state = this.mMachine.getState();
            }
            onWakeScreen(z10, state, i2);
        } else if (z5) {
            requestPulse(i2, true, (Runnable) null);
        } else if (!z7 && !z9) {
            proximityCheckThenCall(new DozeTriggers$$ExternalSyntheticLambda0(this, i, z2, z3, f, f2, z4, z8, fArr), true, i2);
        } else if (z10) {
            requestPulse(i2, true, (Runnable) null);
        }
        if (z4 && !shouldDropPickupEvent()) {
            if (SystemClock.elapsedRealtime() - this.mNotificationPulseTime < ((long) this.mDozeParameters.getPickupVibrationThreshold())) {
                z = true;
            }
            this.mDozeLog.tracePickupWakeUp(z);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSensor$3(int i, boolean z, boolean z2, float f, float f2, boolean z3, boolean z4, float[] fArr, Boolean bool) {
        if (bool != null && bool.booleanValue()) {
            this.mDozeLog.traceSensorEventDropped(i, "prox reporting near");
        } else if (z || z2) {
            if (!(f == -1.0f || f2 == -1.0f)) {
                this.mDozeHost.onSlpiTap(f, f2);
            }
            gentleWakeUp(i);
        } else if (z3) {
            if (shouldDropPickupEvent()) {
                this.mDozeLog.traceSensorEventDropped(i, "keyguard occluded");
            } else {
                gentleWakeUp(i);
            }
        } else if (z4) {
            DozeMachine.State state = this.mMachine.getState();
            if (state == DozeMachine.State.DOZE_AOD || state == DozeMachine.State.DOZE) {
                this.mAodInterruptRunnable = new DozeTriggers$$ExternalSyntheticLambda8(this, f, f2, fArr);
            }
            requestPulse(10, true, (Runnable) null);
        } else {
            this.mDozeHost.extendPulse(i);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onSensor$2(float f, float f2, float[] fArr) {
        this.mAuthController.onAodInterrupt((int) f, (int) f2, fArr[3], fArr[4]);
    }

    public final boolean shouldDropPickupEvent() {
        return this.mKeyguardStateController.isOccluded();
    }

    public final void gentleWakeUp(int i) {
        Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        Objects.requireNonNull(uiEventLogger);
        ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda5(uiEventLogger));
        if (this.mDozeParameters.getDisplayNeedsBlanking()) {
            this.mDozeHost.setAodDimmingScrim(1.0f);
        }
        this.mMachine.wakeUp();
    }

    public final void onProximityFar(boolean z) {
        if (this.mMachine.isExecutingTransition()) {
            Log.w("DozeTriggers", "onProximityFar called during transition. Ignoring sensor response.");
            return;
        }
        boolean z2 = !z;
        DozeMachine.State state = this.mMachine.getState();
        boolean z3 = false;
        boolean z4 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        DozeMachine.State state2 = DozeMachine.State.DOZE_AOD_PAUSING;
        boolean z5 = state == state2;
        DozeMachine.State state3 = DozeMachine.State.DOZE_AOD;
        if (state == state3) {
            z3 = true;
        }
        if (state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox changed, ignore touch = " + z2);
            }
            this.mDozeHost.onIgnoreTouchWhilePulsing(z2);
        }
        if (z && (z4 || z5)) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox FAR, unpausing AOD");
            }
            this.mMachine.requestState(state3);
        } else if (z2 && z3) {
            if (DEBUG) {
                Log.i("DozeTriggers", "Prox NEAR, pausing AOD");
            }
            this.mMachine.requestState(state2);
        }
    }

    public final void onWakeScreen(boolean z, DozeMachine.State state, int i) {
        this.mDozeLog.traceWakeDisplay(z, i);
        sWakeDisplaySensorState = z;
        boolean z2 = false;
        if (z) {
            proximityCheckThenCall(new DozeTriggers$$ExternalSyntheticLambda6(this, state, i), false, i);
            return;
        }
        boolean z3 = state == DozeMachine.State.DOZE_AOD_PAUSED;
        if (state == DozeMachine.State.DOZE_AOD_PAUSING) {
            z2 = true;
        }
        if (!z2 && !z3) {
            this.mMachine.requestState(DozeMachine.State.DOZE);
            this.mUiEventLogger.log(DozingUpdateUiEvent.DOZING_UPDATE_WAKE_TIMEOUT);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onWakeScreen$4(DozeMachine.State state, int i, Boolean bool) {
        if ((bool == null || !bool.booleanValue()) && state == DozeMachine.State.DOZE) {
            this.mMachine.requestState(DozeMachine.State.DOZE_AOD);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda5(uiEventLogger));
        }
    }

    /* renamed from: com.android.systemui.doze.DozeTriggers$2  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0054 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0060 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            /*
                com.android.systemui.doze.DozeMachine$State[] r0 = com.android.systemui.doze.DozeMachine.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$doze$DozeMachine$State = r0
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSING     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSING     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSING_BRIGHT     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_DOCKED     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x006c }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSE_DONE     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0078 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.FINISH     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeTriggers.AnonymousClass2.<clinit>():void");
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        switch (AnonymousClass2.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
                this.mAodInterruptRunnable = null;
                sWakeDisplaySensorState = true;
                this.mBroadcastReceiver.register(this.mBroadcastDispatcher);
                this.mDockManager.addListener(this.mDockEventListener);
                this.mDozeSensors.requestTemporaryDisable();
                this.mDozeHost.addCallback(this.mHostCallback);
                break;
            case 2:
            case 3:
                this.mAodInterruptRunnable = null;
                this.mWantProxSensor = state2 != DozeMachine.State.DOZE;
                this.mWantSensors = true;
                this.mWantTouchScreenSensors = true;
                if (state2 == DozeMachine.State.DOZE_AOD && !sWakeDisplaySensorState) {
                    onWakeScreen(false, state2, 7);
                    break;
                }
            case 4:
            case 5:
                this.mWantProxSensor = true;
                break;
            case 6:
            case 7:
                this.mWantProxSensor = true;
                this.mWantTouchScreenSensors = false;
                break;
            case 8:
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
            case 9:
                this.mDozeSensors.requestTemporaryDisable();
                break;
            case 10:
                this.mBroadcastReceiver.unregister(this.mBroadcastDispatcher);
                this.mDozeHost.removeCallback(this.mHostCallback);
                this.mDockManager.removeListener(this.mDockEventListener);
                this.mDozeSensors.setListening(false, false);
                this.mDozeSensors.setProxListening(false);
                this.mWantSensors = false;
                this.mWantProxSensor = false;
                this.mWantTouchScreenSensors = false;
                break;
        }
        this.mDozeSensors.setListening(this.mWantSensors, this.mWantTouchScreenSensors);
    }

    public void onScreenState(int i) {
        this.mDozeSensors.onScreenState(i);
        boolean z = false;
        boolean z2 = i == 3 || i == 4 || i == 1;
        DozeSensors dozeSensors = this.mDozeSensors;
        if (this.mWantProxSensor && z2) {
            z = true;
        }
        dozeSensors.setProxListening(z);
        this.mDozeSensors.setListening(this.mWantSensors, this.mWantTouchScreenSensors, z2);
        Runnable runnable = this.mAodInterruptRunnable;
        if (runnable != null && i == 2) {
            runnable.run();
            this.mAodInterruptRunnable = null;
        }
    }

    public final void requestPulse(int i, boolean z, Runnable runnable) {
        Assert.isMainThread();
        this.mDozeHost.extendPulse(i);
        DozeMachine.State state = this.mMachine.isExecutingTransition() ? null : this.mMachine.getState();
        if (state == DozeMachine.State.DOZE_PULSING && i == 8) {
            this.mMachine.requestState(DozeMachine.State.DOZE_PULSING_BRIGHT);
        } else if (this.mPulsePending || !this.mAllowPulseTriggers || !canPulse()) {
            if (this.mAllowPulseTriggers) {
                this.mDozeLog.tracePulseDropped(this.mPulsePending, state, this.mDozeHost.isPulsingBlocked());
            }
            runIfNotNull(runnable);
        } else {
            boolean z2 = true;
            this.mPulsePending = true;
            DozeTriggers$$ExternalSyntheticLambda4 dozeTriggers$$ExternalSyntheticLambda4 = new DozeTriggers$$ExternalSyntheticLambda4(this, runnable, i);
            if (this.mDozeParameters.getProxCheckBeforePulse() && !z) {
                z2 = false;
            }
            proximityCheckThenCall(dozeTriggers$$ExternalSyntheticLambda4, z2, i);
            Optional ofNullable = Optional.ofNullable(DozingUpdateUiEvent.fromReason(i));
            UiEventLogger uiEventLogger = this.mUiEventLogger;
            Objects.requireNonNull(uiEventLogger);
            ofNullable.ifPresent(new DozeTriggers$$ExternalSyntheticLambda5(uiEventLogger));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestPulse$5(Runnable runnable, int i, Boolean bool) {
        if (bool == null || !bool.booleanValue()) {
            continuePulseRequest(i);
            return;
        }
        this.mDozeLog.tracePulseDropped("inPocket");
        this.mPulsePending = false;
        runIfNotNull(runnable);
    }

    public final boolean canPulse() {
        return this.mMachine.getState() == DozeMachine.State.DOZE || this.mMachine.getState() == DozeMachine.State.DOZE_AOD || this.mMachine.getState() == DozeMachine.State.DOZE_AOD_DOCKED;
    }

    public final void continuePulseRequest(int i) {
        this.mPulsePending = false;
        if (this.mDozeHost.isPulsingBlocked() || !canPulse()) {
            this.mDozeLog.tracePulseDropped(this.mPulsePending, this.mMachine.getState(), this.mDozeHost.isPulsingBlocked());
        } else {
            this.mMachine.requestPulse(i);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(" mAodInterruptRunnable=" + this.mAodInterruptRunnable);
        printWriter.print(" notificationPulseTime=");
        printWriter.println(Formatter.formatShortElapsedTime(this.mContext, this.mNotificationPulseTime));
        printWriter.println(" pulsePending=" + this.mPulsePending);
        printWriter.println("DozeSensors:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        this.mDozeSensors.dump(indentingPrintWriter);
    }

    public class TriggerReceiver extends BroadcastReceiver {
        public boolean mRegistered;

        public TriggerReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("com.android.systemui.doze.pulse".equals(intent.getAction())) {
                if (DozeMachine.DEBUG) {
                    Log.d("DozeTriggers", "Received pulse intent");
                }
                DozeTriggers.this.requestPulse(0, false, (Runnable) null);
            }
            if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                DozeTriggers.this.mDozeSensors.onUserSwitched();
            }
        }

        public void register(BroadcastDispatcher broadcastDispatcher) {
            if (!this.mRegistered) {
                IntentFilter intentFilter = new IntentFilter("com.android.systemui.doze.pulse");
                intentFilter.addAction("android.intent.action.USER_SWITCHED");
                broadcastDispatcher.registerReceiver(this, intentFilter);
                this.mRegistered = true;
            }
        }

        public void unregister(BroadcastDispatcher broadcastDispatcher) {
            if (this.mRegistered) {
                broadcastDispatcher.unregisterReceiver(this);
                this.mRegistered = false;
            }
        }
    }

    public class DockEventListener implements DockManager.DockEventListener {
        public DockEventListener() {
        }
    }
}

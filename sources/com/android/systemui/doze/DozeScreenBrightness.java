package com.android.systemui.doze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings;
import android.util.IndentingPrintWriter;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;

public class DozeScreenBrightness extends BroadcastReceiver implements DozeMachine.Part, SensorEventListener {
    public static final boolean DEBUG_AOD_BRIGHTNESS = SystemProperties.getBoolean("debug.aod_brightness", false);
    public final Context mContext;
    public int mDebugBrightnessBucket = -1;
    public int mDefaultDozeBrightness;
    public int mDevicePosture;
    public final DevicePostureController.Callback mDevicePostureCallback;
    public final DevicePostureController mDevicePostureController;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public final DozeMachine.Service mDozeService;
    public final Handler mHandler;
    public int mLastSensorValue = -1;
    public final Optional<Sensor>[] mLightSensorOptional;
    public boolean mPaused = false;
    public boolean mRegistered;
    public final int mScreenBrightnessDim;
    public final float mScreenBrightnessMinimumDimAmountFloat;
    public boolean mScreenOff = false;
    public final SensorManager mSensorManager;
    public final int[] mSensorToBrightness;
    public final int[] mSensorToScrimOpacity;
    public DozeMachine.State mState = DozeMachine.State.UNINITIALIZED;
    public final WakefulnessLifecycle mWakefulnessLifecycle;

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public DozeScreenBrightness(Context context, DozeMachine.Service service, AsyncSensorManager asyncSensorManager, Optional<Sensor>[] optionalArr, DozeHost dozeHost, Handler handler, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy, WakefulnessLifecycle wakefulnessLifecycle, DozeParameters dozeParameters, DevicePostureController devicePostureController, DozeLog dozeLog) {
        AnonymousClass1 r0 = new DevicePostureController.Callback() {
            public void onPostureChanged(int i) {
                if (DozeScreenBrightness.this.mDevicePosture != i && DozeScreenBrightness.this.mLightSensorOptional.length >= 2 && i < DozeScreenBrightness.this.mLightSensorOptional.length) {
                    Sensor sensor = (Sensor) DozeScreenBrightness.this.mLightSensorOptional[DozeScreenBrightness.this.mDevicePosture].get();
                    Sensor sensor2 = (Sensor) DozeScreenBrightness.this.mLightSensorOptional[i].get();
                    if (Objects.equals(sensor, sensor2)) {
                        DozeScreenBrightness.this.mDevicePosture = i;
                        return;
                    }
                    if (DozeScreenBrightness.this.mRegistered) {
                        DozeScreenBrightness.this.setLightSensorEnabled(false);
                        DozeScreenBrightness.this.mDevicePosture = i;
                        DozeScreenBrightness.this.setLightSensorEnabled(true);
                    } else {
                        DozeScreenBrightness.this.mDevicePosture = i;
                    }
                    DozeLog r6 = DozeScreenBrightness.this.mDozeLog;
                    int r2 = DozeScreenBrightness.this.mDevicePosture;
                    r6.tracePostureChanged(r2, "DozeScreenBrightness swap {" + sensor + "} => {" + sensor2 + "}, mRegistered=" + DozeScreenBrightness.this.mRegistered);
                }
            }
        };
        this.mDevicePostureCallback = r0;
        this.mContext = context;
        this.mDozeService = service;
        this.mSensorManager = asyncSensorManager;
        this.mLightSensorOptional = optionalArr;
        this.mDevicePostureController = devicePostureController;
        this.mDevicePosture = devicePostureController.getDevicePosture();
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mDozeParameters = dozeParameters;
        this.mDozeHost = dozeHost;
        this.mHandler = handler;
        this.mDozeLog = dozeLog;
        this.mScreenBrightnessMinimumDimAmountFloat = context.getResources().getFloat(17105104);
        this.mDefaultDozeBrightness = alwaysOnDisplayPolicy.defaultDozeBrightness;
        this.mScreenBrightnessDim = alwaysOnDisplayPolicy.dimBrightness;
        this.mSensorToBrightness = alwaysOnDisplayPolicy.screenBrightnessArray;
        this.mSensorToScrimOpacity = alwaysOnDisplayPolicy.dimmingScrimArray;
        devicePostureController.addCallback(r0);
    }

    /* renamed from: com.android.systemui.doze.DozeScreenBrightness$2  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
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
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_REQUEST_PULSE     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_DOCKED     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.DOZE_AOD_PAUSED     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.FINISH     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeScreenBrightness.AnonymousClass2.<clinit>():void");
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        this.mState = state2;
        boolean z = true;
        switch (AnonymousClass2.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()]) {
            case 1:
                resetBrightnessToDefault();
                break;
            case 2:
            case 3:
            case 4:
                setLightSensorEnabled(true);
                break;
            case 5:
                setLightSensorEnabled(false);
                resetBrightnessToDefault();
                break;
            case 6:
                setLightSensorEnabled(false);
                break;
            case 7:
                onDestroy();
                break;
        }
        if (state2 != DozeMachine.State.FINISH) {
            setScreenOff(state2 == DozeMachine.State.DOZE);
            if (state2 != DozeMachine.State.DOZE_AOD_PAUSED) {
                z = false;
            }
            setPaused(z);
        }
    }

    public final void onDestroy() {
        setLightSensorEnabled(false);
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        Trace.beginSection("DozeScreenBrightness.onSensorChanged" + sensorEvent.values[0]);
        try {
            if (this.mRegistered) {
                this.mLastSensorValue = (int) sensorEvent.values[0];
                updateBrightnessAndReady(false);
            }
        } finally {
            Trace.endSection();
        }
    }

    public void updateBrightnessAndReady(boolean z) {
        int i = -1;
        if (z || this.mRegistered || this.mDebugBrightnessBucket != -1) {
            int i2 = this.mDebugBrightnessBucket;
            if (i2 == -1) {
                i2 = this.mLastSensorValue;
            }
            int computeBrightness = computeBrightness(i2);
            boolean z2 = computeBrightness > 0;
            if (z2) {
                this.mDozeService.setDozeScreenBrightness(clampToDimBrightnessForScreenOff(clampToUserSetting(computeBrightness)));
            }
            if (!isLightSensorPresent()) {
                i = 0;
            } else if (z2) {
                i = computeScrimOpacity(i2);
            }
            if (i >= 0) {
                this.mDozeHost.setAodDimmingScrim(((float) i) / 255.0f);
            }
        }
    }

    public final boolean lightSensorSupportsCurrentPosture() {
        Optional<Sensor>[] optionalArr = this.mLightSensorOptional;
        return optionalArr != null && this.mDevicePosture < optionalArr.length;
    }

    public final boolean isLightSensorPresent() {
        if (lightSensorSupportsCurrentPosture()) {
            return this.mLightSensorOptional[this.mDevicePosture].isPresent();
        }
        Optional<Sensor>[] optionalArr = this.mLightSensorOptional;
        return optionalArr != null && optionalArr[0].isPresent();
    }

    public final Sensor getLightSensor() {
        if (!lightSensorSupportsCurrentPosture()) {
            return null;
        }
        return this.mLightSensorOptional[this.mDevicePosture].get();
    }

    public final int computeScrimOpacity(int i) {
        if (i < 0) {
            return -1;
        }
        int[] iArr = this.mSensorToScrimOpacity;
        if (i >= iArr.length) {
            return -1;
        }
        return iArr[i];
    }

    public final int computeBrightness(int i) {
        if (i < 0) {
            return -1;
        }
        int[] iArr = this.mSensorToBrightness;
        if (i >= iArr.length) {
            return -1;
        }
        return iArr[i];
    }

    public final void resetBrightnessToDefault() {
        this.mDozeService.setDozeScreenBrightness(clampToDimBrightnessForScreenOff(clampToUserSetting(this.mDefaultDozeBrightness)));
        this.mDozeHost.setAodDimmingScrim(0.0f);
    }

    public final int clampToUserSetting(int i) {
        return Math.min(i, Settings.System.getIntForUser(this.mContext.getContentResolver(), "screen_brightness", Integer.MAX_VALUE, -2));
    }

    public final int clampToDimBrightnessForScreenOff(int i) {
        return (!((this.mDozeParameters.shouldClampToDimBrightness() || this.mWakefulnessLifecycle.getWakefulness() == 3) && this.mState == DozeMachine.State.INITIALIZED) || this.mWakefulnessLifecycle.getLastSleepReason() != 2) ? i : Math.max(0, Math.min(i - ((int) Math.floor((double) (this.mScreenBrightnessMinimumDimAmountFloat * 255.0f))), this.mScreenBrightnessDim));
    }

    public final void setLightSensorEnabled(boolean z) {
        if (z && !this.mRegistered && isLightSensorPresent()) {
            this.mRegistered = this.mSensorManager.registerListener(this, getLightSensor(), 3, this.mHandler);
            this.mLastSensorValue = -1;
        } else if (!z && this.mRegistered) {
            this.mSensorManager.unregisterListener(this);
            this.mRegistered = false;
            this.mLastSensorValue = -1;
        }
    }

    public final void setPaused(boolean z) {
        if (this.mPaused != z) {
            this.mPaused = z;
            updateBrightnessAndReady(false);
        }
    }

    public final void setScreenOff(boolean z) {
        if (this.mScreenOff != z) {
            this.mScreenOff = z;
            updateBrightnessAndReady(true);
        }
    }

    public void onReceive(Context context, Intent intent) {
        this.mDebugBrightnessBucket = intent.getIntExtra("brightness_bucket", -1);
        updateBrightnessAndReady(false);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("DozeScreenBrightness:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("registered=" + this.mRegistered);
        indentingPrintWriter.println("posture=" + DevicePostureController.devicePostureToString(this.mDevicePosture));
    }
}

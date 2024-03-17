package com.android.systemui.doze;

import android.app.ActivityManager;
import android.content.Context;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.hardware.display.AmbientDisplayConfiguration;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import com.android.systemui.util.sensors.ProximitySensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class DozeSensors {
    public static final boolean DEBUG = DozeService.DEBUG;
    public static final UiEventLogger UI_EVENT_LOGGER = new UiEventLoggerImpl();
    public final AuthController mAuthController;
    public final AuthController.Callback mAuthControllerCallback;
    public final AmbientDisplayConfiguration mConfig;
    public final Context mContext;
    public long mDebounceFrom;
    public int mDevicePosture;
    public final DevicePostureController.Callback mDevicePostureCallback;
    public final DevicePostureController mDevicePostureController;
    public final DozeLog mDozeLog;
    public final Handler mHandler;
    public boolean mListening;
    public boolean mListeningProxSensors;
    public boolean mListeningTouchScreenSensors;
    public final Consumer<Boolean> mProxCallback;
    public final ProximitySensor mProximitySensor;
    public final boolean mScreenOffUdfpsEnabled;
    public final SecureSettings mSecureSettings;
    public boolean mSelectivelyRegisterProxSensors;
    public final Callback mSensorCallback;
    public final AsyncSensorManager mSensorManager;
    public boolean mSettingRegistered;
    public final ContentObserver mSettingsObserver;
    public TriggerSensor[] mTriggerSensors;
    public boolean mUdfpsEnrolled;
    public final WakeLock mWakeLock;

    public interface Callback {
        void onSensorPulse(int i, float f, float f2, float[] fArr);
    }

    public enum DozeSensorsUiEvent implements UiEventLogger.UiEventEnum {
        ACTION_AMBIENT_GESTURE_PICKUP(459);
        
        private final int mId;

        /* access modifiers changed from: public */
        DozeSensorsUiEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public DozeSensors(Context context, AsyncSensorManager asyncSensorManager, DozeParameters dozeParameters, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, Callback callback, Consumer<Boolean> consumer, DozeLog dozeLog, ProximitySensor proximitySensor, SecureSettings secureSettings, AuthController authController, DevicePostureController devicePostureController) {
        AsyncSensorManager asyncSensorManager2 = asyncSensorManager;
        AmbientDisplayConfiguration ambientDisplayConfiguration2 = ambientDisplayConfiguration;
        ProximitySensor proximitySensor2 = proximitySensor;
        AuthController authController2 = authController;
        Handler handler = new Handler();
        this.mHandler = handler;
        this.mSettingsObserver = new ContentObserver(handler) {
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                if (i2 == ActivityManager.getCurrentUser()) {
                    for (TriggerSensor updateListening : DozeSensors.this.mTriggerSensors) {
                        updateListening.updateListening();
                    }
                }
            }
        };
        DozeSensors$$ExternalSyntheticLambda0 dozeSensors$$ExternalSyntheticLambda0 = new DozeSensors$$ExternalSyntheticLambda0(this);
        this.mDevicePostureCallback = dozeSensors$$ExternalSyntheticLambda0;
        AnonymousClass2 r1 = new AuthController.Callback() {
            public void onAllAuthenticatorsRegistered() {
                updateUdfpsEnrolled();
            }

            public void onEnrollmentsChanged() {
                updateUdfpsEnrolled();
            }

            public final void updateUdfpsEnrolled() {
                DozeSensors dozeSensors = DozeSensors.this;
                dozeSensors.mUdfpsEnrolled = dozeSensors.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser());
                for (TriggerSensor triggerSensor : DozeSensors.this.mTriggerSensors) {
                    int i = triggerSensor.mPulseReason;
                    if (11 == i) {
                        triggerSensor.setConfigured(DozeSensors.this.quickPickUpConfigured());
                    } else if (10 == i) {
                        triggerSensor.setConfigured(DozeSensors.this.udfpsLongPressConfigured());
                    }
                }
            }
        };
        this.mAuthControllerCallback = r1;
        this.mContext = context;
        this.mSensorManager = asyncSensorManager2;
        this.mConfig = ambientDisplayConfiguration2;
        this.mWakeLock = wakeLock;
        this.mProxCallback = consumer;
        this.mSecureSettings = secureSettings;
        this.mSensorCallback = callback;
        this.mDozeLog = dozeLog;
        this.mProximitySensor = proximitySensor2;
        proximitySensor2.setTag("DozeSensors");
        boolean selectivelyRegisterSensorsUsingProx = dozeParameters.getSelectivelyRegisterSensorsUsingProx();
        this.mSelectivelyRegisterProxSensors = selectivelyRegisterSensorsUsingProx;
        this.mListeningProxSensors = !selectivelyRegisterSensorsUsingProx;
        this.mScreenOffUdfpsEnabled = ambientDisplayConfiguration2.screenOffUdfpsEnabled(KeyguardUpdateMonitor.getCurrentUser());
        this.mDevicePostureController = devicePostureController;
        this.mDevicePosture = devicePostureController.getDevicePosture();
        this.mAuthController = authController2;
        this.mUdfpsEnrolled = authController2.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser());
        authController2.addCallback(r1);
        TriggerSensor[] triggerSensorArr = new TriggerSensor[9];
        triggerSensorArr[0] = new TriggerSensor(this, asyncSensorManager2.getDefaultSensor(17), (String) null, dozeParameters.getPulseOnSigMotion(), 2, false, false);
        TriggerSensor[] triggerSensorArr2 = triggerSensorArr;
        triggerSensorArr2[1] = new TriggerSensor(this, asyncSensorManager2.getDefaultSensor(25), "doze_pulse_on_pick_up", true, ambientDisplayConfiguration.dozePickupSensorAvailable(), 3, false, false, false, false);
        triggerSensorArr2[2] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.doubleTapSensorType()), "doze_pulse_on_double_tap", true, 4, dozeParameters.doubleTapReportsTouchCoordinates(), true);
        TriggerSensor[] triggerSensorArr3 = triggerSensorArr2;
        DozeSensors$$ExternalSyntheticLambda0 dozeSensors$$ExternalSyntheticLambda02 = dozeSensors$$ExternalSyntheticLambda0;
        triggerSensorArr3[3] = new TriggerSensor(findSensors(ambientDisplayConfiguration.tapSensorTypeMapping()), "doze_tap_gesture", true, true, 9, false, true, false, dozeParameters.singleTapUsesProx(this.mDevicePosture), this.mDevicePosture);
        triggerSensorArr3[4] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.longPressSensorType()), "doze_pulse_on_long_press", false, true, 5, true, true, false, dozeParameters.longPressUsesProx());
        triggerSensorArr3[5] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.udfpsLongPressSensorType()), "doze_pulse_on_auth", true, udfpsLongPressConfigured(), 10, true, true, false, dozeParameters.longPressUsesProx());
        triggerSensorArr3[6] = new PluginSensor(this, new SensorManagerPlugin.Sensor(2), "doze_wake_display_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable() && ambientDisplayConfiguration2.alwaysOnEnabled(-2), 7, false, false);
        triggerSensorArr3[7] = new PluginSensor(this, new SensorManagerPlugin.Sensor(1), "doze_wake_screen_gesture", ambientDisplayConfiguration.wakeScreenGestureAvailable(), 8, false, false, ambientDisplayConfiguration.getWakeLockScreenDebounce());
        triggerSensorArr3[8] = new TriggerSensor(this, findSensor(ambientDisplayConfiguration.quickPickupSensorType()), "doze_quick_pickup_gesture", true, quickPickUpConfigured(), 11, false, false, false, false);
        this.mTriggerSensors = triggerSensorArr3;
        setProxListening(false);
        proximitySensor2.register(new DozeSensors$$ExternalSyntheticLambda1(this));
        devicePostureController.addCallback(dozeSensors$$ExternalSyntheticLambda02);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ThresholdSensorEvent thresholdSensorEvent) {
        if (thresholdSensorEvent != null) {
            this.mProxCallback.accept(Boolean.valueOf(!thresholdSensorEvent.getBelow()));
        }
    }

    public final boolean udfpsLongPressConfigured() {
        return this.mUdfpsEnrolled && (this.mConfig.alwaysOnEnabled(-2) || this.mScreenOffUdfpsEnabled);
    }

    public final boolean quickPickUpConfigured() {
        return this.mUdfpsEnrolled && this.mConfig.quickPickupSensorEnabled(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void destroy() {
        for (TriggerSensor listening : this.mTriggerSensors) {
            listening.setListening(false);
        }
        this.mProximitySensor.destroy();
        this.mDevicePostureController.removeCallback(this.mDevicePostureCallback);
        this.mAuthController.removeCallback(this.mAuthControllerCallback);
    }

    public void requestTemporaryDisable() {
        this.mDebounceFrom = SystemClock.uptimeMillis();
    }

    public final Sensor findSensor(String str) {
        return findSensor(this.mSensorManager, str, (String) null);
    }

    public final Sensor[] findSensors(String[] strArr) {
        Sensor[] sensorArr = new Sensor[5];
        HashMap hashMap = new HashMap();
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            if (!hashMap.containsKey(str)) {
                hashMap.put(str, findSensor(str));
            }
            sensorArr[i] = (Sensor) hashMap.get(str);
        }
        return sensorArr;
    }

    public static Sensor findSensor(SensorManager sensorManager, String str, String str2) {
        boolean z = !TextUtils.isEmpty(str2);
        boolean z2 = !TextUtils.isEmpty(str);
        if (!z && !z2) {
            return null;
        }
        for (Sensor next : sensorManager.getSensorList(-1)) {
            if ((!z || str2.equals(next.getName())) && (!z2 || str.equals(next.getStringType()))) {
                return next;
            }
        }
        return null;
    }

    public void setListening(boolean z, boolean z2) {
        if (this.mListening != z || this.mListeningTouchScreenSensors != z2) {
            this.mListening = z;
            this.mListeningTouchScreenSensors = z2;
            updateListening();
        }
    }

    public void setListening(boolean z, boolean z2, boolean z3) {
        boolean z4 = !this.mSelectivelyRegisterProxSensors || z3;
        if (this.mListening != z || this.mListeningTouchScreenSensors != z2 || this.mListeningProxSensors != z4) {
            this.mListening = z;
            this.mListeningTouchScreenSensors = z2;
            this.mListeningProxSensors = z4;
            updateListening();
        }
    }

    public final void updateListening() {
        boolean z = false;
        for (TriggerSensor triggerSensor : this.mTriggerSensors) {
            boolean z2 = this.mListening && (!triggerSensor.mRequiresTouchscreen || this.mListeningTouchScreenSensors) && (!triggerSensor.mRequiresProx || this.mListeningProxSensors);
            triggerSensor.setListening(z2);
            if (z2) {
                z = true;
            }
        }
        if (!z) {
            this.mSecureSettings.unregisterContentObserver(this.mSettingsObserver);
        } else if (!this.mSettingRegistered) {
            for (TriggerSensor registerSettingsObserver : this.mTriggerSensors) {
                registerSettingsObserver.registerSettingsObserver(this.mSettingsObserver);
            }
        }
        this.mSettingRegistered = z;
    }

    public void onUserSwitched() {
        for (TriggerSensor updateListening : this.mTriggerSensors) {
            updateListening.updateListening();
        }
    }

    public void onScreenState(int i) {
        ProximitySensor proximitySensor = this.mProximitySensor;
        boolean z = true;
        if (!(i == 3 || i == 4 || i == 1)) {
            z = false;
        }
        proximitySensor.setSecondarySafe(z);
    }

    public void setProxListening(boolean z) {
        if (this.mProximitySensor.isRegistered() && z) {
            this.mProximitySensor.alertListeners();
        } else if (z) {
            this.mProximitySensor.resume();
        } else {
            this.mProximitySensor.pause();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("mListening=" + this.mListening);
        printWriter.println("mDevicePosture=" + DevicePostureController.devicePostureToString(this.mDevicePosture));
        printWriter.println("mListeningTouchScreenSensors=" + this.mListeningTouchScreenSensors);
        printWriter.println("mSelectivelyRegisterProxSensors=" + this.mSelectivelyRegisterProxSensors);
        printWriter.println("mListeningProxSensors=" + this.mListeningProxSensors);
        printWriter.println("mScreenOffUdfpsEnabled=" + this.mScreenOffUdfpsEnabled);
        printWriter.println("mUdfpsEnrolled=" + this.mUdfpsEnrolled);
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        indentingPrintWriter.increaseIndent();
        for (TriggerSensor triggerSensor : this.mTriggerSensors) {
            indentingPrintWriter.println("Sensor: " + triggerSensor.toString());
        }
        indentingPrintWriter.println("ProxSensor: " + this.mProximitySensor.toString());
    }

    public Boolean isProximityCurrentlyNear() {
        return this.mProximitySensor.isNear();
    }

    public class TriggerSensor extends TriggerEventListener {
        public boolean mConfigured;
        public boolean mDisabled;
        public boolean mIgnoresSetting;
        public int mPosture;
        public final int mPulseReason;
        public boolean mRegistered;
        public final boolean mReportsTouchCoordinates;
        public boolean mRequested;
        public final boolean mRequiresProx;
        public final boolean mRequiresTouchscreen;
        public final Sensor[] mSensors;
        public final String mSetting;
        public final boolean mSettingDefault;

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3) {
            this(dozeSensors, sensor, str, true, z, i, z2, z3, false, false);
        }

        public TriggerSensor(DozeSensors dozeSensors, Sensor sensor, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, boolean z6) {
            this(new Sensor[]{sensor}, str, z, z2, i, z3, z4, z5, z6, 0);
        }

        public TriggerSensor(Sensor[] sensorArr, String str, boolean z, boolean z2, int i, boolean z3, boolean z4, boolean z5, boolean z6, int i2) {
            this.mSensors = sensorArr;
            this.mSetting = str;
            this.mSettingDefault = z;
            this.mConfigured = z2;
            this.mPulseReason = i;
            this.mReportsTouchCoordinates = z3;
            this.mRequiresTouchscreen = z4;
            this.mIgnoresSetting = z5;
            this.mRequiresProx = z6;
            this.mPosture = i2;
        }

        public boolean setPosture(int i) {
            int i2 = this.mPosture;
            if (i2 != i) {
                Sensor[] sensorArr = this.mSensors;
                if (sensorArr.length >= 2 && i < sensorArr.length) {
                    Sensor sensor = sensorArr[i2];
                    Sensor sensor2 = sensorArr[i];
                    if (Objects.equals(sensor, sensor2)) {
                        this.mPosture = i;
                        return false;
                    }
                    if (this.mRegistered) {
                        boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, sensor);
                        if (DozeSensors.DEBUG) {
                            Log.d("DozeSensors", "posture changed, cancelTriggerSensor[" + sensor + "] " + cancelTriggerSensor);
                        }
                        this.mRegistered = false;
                    }
                    this.mPosture = i;
                    updateListening();
                    DozeLog r7 = DozeSensors.this.mDozeLog;
                    int i3 = this.mPosture;
                    r7.tracePostureChanged(i3, "DozeSensors swap {" + sensor + "} => {" + sensor2 + "}, mRegistered=" + this.mRegistered);
                    return true;
                }
            }
            return false;
        }

        public void setListening(boolean z) {
            if (this.mRequested != z) {
                this.mRequested = z;
                updateListening();
            }
        }

        public void setConfigured(boolean z) {
            if (this.mConfigured != z) {
                this.mConfigured = z;
                updateListening();
            }
        }

        public void updateListening() {
            Sensor sensor = this.mSensors[this.mPosture];
            if (this.mConfigured && sensor != null) {
                if (!this.mRequested || this.mDisabled || (!enabledBySetting() && !this.mIgnoresSetting)) {
                    if (this.mRegistered) {
                        boolean cancelTriggerSensor = DozeSensors.this.mSensorManager.cancelTriggerSensor(this, sensor);
                        if (DozeSensors.DEBUG) {
                            Log.d("DozeSensors", "cancelTriggerSensor[" + sensor + "] " + cancelTriggerSensor);
                        }
                        this.mRegistered = false;
                    }
                } else if (!this.mRegistered) {
                    this.mRegistered = DozeSensors.this.mSensorManager.requestTriggerSensor(this, sensor);
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "requestTriggerSensor[" + sensor + "] " + this.mRegistered);
                    }
                } else if (DozeSensors.DEBUG) {
                    Log.d("DozeSensors", "requestTriggerSensor[" + sensor + "] already registered");
                }
            }
        }

        public boolean enabledBySetting() {
            if (!DozeSensors.this.mConfig.enabled(-2)) {
                return false;
            }
            if (TextUtils.isEmpty(this.mSetting)) {
                return true;
            }
            if (DozeSensors.this.mSecureSettings.getIntForUser(this.mSetting, this.mSettingDefault ? 1 : 0, -2) != 0) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("mRegistered=");
            sb.append(this.mRegistered);
            sb.append(", mRequested=");
            sb.append(this.mRequested);
            sb.append(", mDisabled=");
            sb.append(this.mDisabled);
            sb.append(", mConfigured=");
            sb.append(this.mConfigured);
            sb.append(", mIgnoresSetting=");
            sb.append(this.mIgnoresSetting);
            sb.append(", mSensors=");
            sb.append(Arrays.toString(this.mSensors));
            if (this.mSensors.length > 2) {
                sb.append(", mPosture=");
                sb.append(DevicePostureController.devicePostureToString(DozeSensors.this.mDevicePosture));
            }
            sb.append("}");
            return sb.toString();
        }

        public void onTrigger(TriggerEvent triggerEvent) {
            Sensor sensor = this.mSensors[this.mPosture];
            DozeSensors.this.mDozeLog.traceSensor(this.mPulseReason);
            DozeSensors.this.mHandler.post(DozeSensors.this.mWakeLock.wrap(new DozeSensors$TriggerSensor$$ExternalSyntheticLambda0(this, triggerEvent, sensor)));
        }

        /* access modifiers changed from: private */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x005a  */
        /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public /* synthetic */ void lambda$onTrigger$0(android.hardware.TriggerEvent r5, android.hardware.Sensor r6) {
            /*
                r4 = this;
                boolean r0 = com.android.systemui.doze.DozeSensors.DEBUG
                if (r0 == 0) goto L_0x0020
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "onTrigger: "
                r0.append(r1)
                java.lang.String r1 = r4.triggerEventToString(r5)
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "DozeSensors"
                android.util.Log.d(r1, r0)
            L_0x0020:
                if (r6 == 0) goto L_0x0033
                int r6 = r6.getType()
                r0 = 25
                if (r6 != r0) goto L_0x0033
                com.android.internal.logging.UiEventLogger r6 = com.android.systemui.doze.DozeSensors.UI_EVENT_LOGGER
                com.android.systemui.doze.DozeSensors$DozeSensorsUiEvent r0 = com.android.systemui.doze.DozeSensors.DozeSensorsUiEvent.ACTION_AMBIENT_GESTURE_PICKUP
                r6.log(r0)
            L_0x0033:
                r6 = 0
                r4.mRegistered = r6
                boolean r0 = r4.mReportsTouchCoordinates
                r1 = -1082130432(0xffffffffbf800000, float:-1.0)
                if (r0 == 0) goto L_0x0048
                float[] r0 = r5.values
                int r2 = r0.length
                r3 = 2
                if (r2 < r3) goto L_0x0048
                r1 = r0[r6]
                r6 = 1
                r6 = r0[r6]
                goto L_0x0049
            L_0x0048:
                r6 = r1
            L_0x0049:
                com.android.systemui.doze.DozeSensors r0 = com.android.systemui.doze.DozeSensors.this
                com.android.systemui.doze.DozeSensors$Callback r0 = r0.mSensorCallback
                int r2 = r4.mPulseReason
                float[] r5 = r5.values
                r0.onSensorPulse(r2, r1, r6, r5)
                boolean r5 = r4.mRegistered
                if (r5 != 0) goto L_0x005d
                r4.updateListening()
            L_0x005d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSensors.TriggerSensor.lambda$onTrigger$0(android.hardware.TriggerEvent, android.hardware.Sensor):void");
        }

        public void registerSettingsObserver(ContentObserver contentObserver) {
            if (this.mConfigured && !TextUtils.isEmpty(this.mSetting)) {
                DozeSensors.this.mSecureSettings.registerContentObserverForUser(this.mSetting, DozeSensors.this.mSettingsObserver, -1);
            }
        }

        public String triggerEventToString(TriggerEvent triggerEvent) {
            if (triggerEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("SensorEvent[");
            sb.append(triggerEvent.timestamp);
            sb.append(',');
            sb.append(triggerEvent.sensor.getName());
            if (triggerEvent.values != null) {
                for (float append : triggerEvent.values) {
                    sb.append(',');
                    sb.append(append);
                }
            }
            sb.append(']');
            return sb.toString();
        }
    }

    public class PluginSensor extends TriggerSensor implements SensorManagerPlugin.SensorEventListener {
        public long mDebounce;
        public final SensorManagerPlugin.Sensor mPluginSensor;
        public final /* synthetic */ DozeSensors this$0;

        public PluginSensor(DozeSensors dozeSensors, SensorManagerPlugin.Sensor sensor, String str, boolean z, int i, boolean z2, boolean z3) {
            this(dozeSensors, sensor, str, z, i, z2, z3, 0);
        }

        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public PluginSensor(com.android.systemui.doze.DozeSensors r10, com.android.systemui.plugins.SensorManagerPlugin.Sensor r11, java.lang.String r12, boolean r13, int r14, boolean r15, boolean r16, long r17) {
            /*
                r9 = this;
                r8 = r9
                r1 = r10
                r8.this$0 = r1
                r2 = 0
                r0 = r9
                r3 = r12
                r4 = r13
                r5 = r14
                r6 = r15
                r7 = r16
                r0.<init>(r1, r2, r3, r4, r5, r6, r7)
                r0 = r11
                r8.mPluginSensor = r0
                r0 = r17
                r8.mDebounce = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSensors.PluginSensor.<init>(com.android.systemui.doze.DozeSensors, com.android.systemui.plugins.SensorManagerPlugin$Sensor, java.lang.String, boolean, int, boolean, boolean, long):void");
        }

        public void updateListening() {
            if (this.mConfigured) {
                AsyncSensorManager r0 = this.this$0.mSensorManager;
                if (this.mRequested && !this.mDisabled && ((enabledBySetting() || this.mIgnoresSetting) && !this.mRegistered)) {
                    r0.registerPluginListener(this.mPluginSensor, this);
                    this.mRegistered = true;
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "registerPluginListener");
                    }
                } else if (this.mRegistered) {
                    r0.unregisterPluginListener(this.mPluginSensor, this);
                    this.mRegistered = false;
                    if (DozeSensors.DEBUG) {
                        Log.d("DozeSensors", "unregisterPluginListener");
                    }
                }
            }
        }

        public String toString() {
            return "{mRegistered=" + this.mRegistered + ", mRequested=" + this.mRequested + ", mDisabled=" + this.mDisabled + ", mConfigured=" + this.mConfigured + ", mIgnoresSetting=" + this.mIgnoresSetting + ", mSensor=" + this.mPluginSensor + "}";
        }

        public final String triggerEventToString(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (sensorEvent == null) {
                return null;
            }
            StringBuilder sb = new StringBuilder("PluginTriggerEvent[");
            sb.append(sensorEvent.getSensor());
            sb.append(',');
            sb.append(sensorEvent.getVendorType());
            if (sensorEvent.getValues() != null) {
                for (float append : sensorEvent.getValues()) {
                    sb.append(',');
                    sb.append(append);
                }
            }
            sb.append(']');
            return sb.toString();
        }

        public void onSensorChanged(SensorManagerPlugin.SensorEvent sensorEvent) {
            this.this$0.mDozeLog.traceSensor(this.mPulseReason);
            this.this$0.mHandler.post(this.this$0.mWakeLock.wrap(new DozeSensors$PluginSensor$$ExternalSyntheticLambda0(this, sensorEvent)));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSensorChanged$0(SensorManagerPlugin.SensorEvent sensorEvent) {
            if (SystemClock.uptimeMillis() < this.this$0.mDebounceFrom + this.mDebounce) {
                Log.d("DozeSensors", "onSensorEvent dropped: " + triggerEventToString(sensorEvent));
                return;
            }
            if (DozeSensors.DEBUG) {
                Log.d("DozeSensors", "onSensorEvent: " + triggerEventToString(sensorEvent));
            }
            this.this$0.mSensorCallback.onSensorPulse(this.mPulseReason, -1.0f, -1.0f, sensorEvent.getValues());
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i) {
        if (this.mDevicePosture != i) {
            this.mDevicePosture = i;
            for (TriggerSensor posture : this.mTriggerSensors) {
                posture.setPosture(this.mDevicePosture);
            }
        }
    }
}

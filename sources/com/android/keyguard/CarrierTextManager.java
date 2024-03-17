package com.android.keyguard;

import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.R$array;
import com.android.systemui.R$string;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.FiveGServiceClient;
import com.android.systemui.telephony.TelephonyListenerManager;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class CarrierTextManager {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final Executor mBgExecutor;
    public final KeyguardUpdateMonitorCallback mCallback;
    public CarrierTextCallback mCarrierTextCallback;
    public final Context mContext;
    public FiveGServiceClient mFiveGServiceClient;
    public final boolean mIsEmergencyCallCapable;
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final Executor mMainExecutor;
    public final AtomicBoolean mNetworkSupported;
    public final TelephonyCallback.ActiveDataSubscriptionIdListener mPhoneStateListener;
    public final CharSequence mSeparator;
    public final boolean mShowAirplaneMode;
    public final boolean mShowMissingSim;
    public final boolean[] mSimErrorState;
    public final int mSimSlotsNumber;
    public boolean mTelephonyCapable;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public final TelephonyManager mTelephonyManager;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WakefulnessLifecycle.Observer mWakefulnessObserver;
    public final WifiManager mWifiManager;

    public interface CarrierTextCallback {
        void finishedWakingUp() {
        }

        void startedGoingToSleep() {
        }

        void updateCarrierInfo(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        }
    }

    public enum StatusMode {
        Normal,
        NetworkLocked,
        SimMissing,
        SimMissingLocked,
        SimPukLocked,
        SimLocked,
        SimPermDisabled,
        SimNotReady,
        SimIoError,
        SimUnknown
    }

    public CarrierTextManager(Context context, CharSequence charSequence, boolean z, boolean z2, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mNetworkSupported = new AtomicBoolean();
        this.mWakefulnessObserver = new WakefulnessLifecycle.Observer() {
            public void onFinishedWakingUp() {
                CarrierTextCallback r0 = CarrierTextManager.this.mCarrierTextCallback;
                if (r0 != null) {
                    r0.finishedWakingUp();
                }
            }

            public void onStartedGoingToSleep() {
                CarrierTextCallback r0 = CarrierTextManager.this.mCarrierTextCallback;
                if (r0 != null) {
                    r0.startedGoingToSleep();
                }
            }
        };
        this.mCallback = new KeyguardUpdateMonitorCallback() {
            public void onRefreshCarrierInfo() {
                if (CarrierTextManager.DEBUG) {
                    Log.d("CarrierTextController", "onRefreshCarrierInfo(), mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                }
                CarrierTextManager.this.updateCarrierText();
            }

            public void onTelephonyCapable(boolean z) {
                if (CarrierTextManager.DEBUG) {
                    Log.d("CarrierTextController", "onTelephonyCapable() mTelephonyCapable: " + Boolean.toString(z));
                }
                CarrierTextManager.this.mTelephonyCapable = z;
                CarrierTextManager.this.updateCarrierText();
            }

            public void onSimStateChanged(int i, int i2, int i3) {
                if (i2 < 0 || i2 >= CarrierTextManager.this.mSimSlotsNumber) {
                    Log.d("CarrierTextController", "onSimStateChanged() - slotId invalid: " + i2 + " mTelephonyCapable: " + Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                    return;
                }
                if (CarrierTextManager.DEBUG) {
                    Log.d("CarrierTextController", "onSimStateChanged: " + CarrierTextManager.this.getStatusForIccState(i3));
                }
                if (CarrierTextManager.this.getStatusForIccState(i3) == StatusMode.SimIoError) {
                    CarrierTextManager.this.mSimErrorState[i2] = true;
                    CarrierTextManager.this.updateCarrierText();
                } else if (CarrierTextManager.this.mSimErrorState[i2]) {
                    CarrierTextManager.this.mSimErrorState[i2] = false;
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() {
            public void onActiveDataSubscriptionIdChanged(int i) {
                if (CarrierTextManager.this.mNetworkSupported.get() && CarrierTextManager.this.mCarrierTextCallback != null) {
                    CarrierTextManager.this.updateCarrierText();
                }
            }
        };
        this.mContext = context;
        this.mIsEmergencyCallCapable = telephonyManager.isVoiceCapable();
        this.mShowAirplaneMode = z;
        this.mShowMissingSim = z2;
        this.mWifiManager = wifiManager;
        this.mTelephonyManager = telephonyManager;
        this.mSeparator = charSequence;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        int supportedModemCount = getTelephonyManager().getSupportedModemCount();
        this.mSimSlotsNumber = supportedModemCount;
        this.mSimErrorState = new boolean[supportedModemCount];
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        executor2.execute(new CarrierTextManager$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        boolean hasSystemFeature = this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
        if (hasSystemFeature && this.mNetworkSupported.compareAndSet(false, hasSystemFeature)) {
            lambda$setListening$4(this.mCarrierTextCallback);
        }
    }

    public final TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    public final CharSequence updateCarrierTextWithSimIoError(CharSequence charSequence, CharSequence[] charSequenceArr, int[] iArr, boolean z) {
        CharSequence carrierTextForSimState = getCarrierTextForSimState(8, "");
        for (int i = 0; i < getTelephonyManager().getActiveModemCount(); i++) {
            if (this.mSimErrorState[i]) {
                if (z) {
                    return concatenate(carrierTextForSimState, getContext().getText(17040207), this.mSeparator);
                }
                int i2 = iArr[i];
                if (i2 != -1) {
                    charSequenceArr[i2] = concatenate(carrierTextForSimState, charSequenceArr[i2], this.mSeparator);
                } else {
                    charSequence = concatenate(charSequence, carrierTextForSimState, this.mSeparator);
                }
            }
        }
        return charSequence;
    }

    /* renamed from: handleSetListening */
    public final void lambda$setListening$4(CarrierTextCallback carrierTextCallback) {
        if (carrierTextCallback != null) {
            this.mCarrierTextCallback = carrierTextCallback;
            if (this.mNetworkSupported.get()) {
                this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda3(this));
                this.mTelephonyListenerManager.addActiveDataSubscriptionIdListener(this.mPhoneStateListener);
                return;
            }
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda4(carrierTextCallback));
            return;
        }
        this.mCarrierTextCallback = null;
        this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda5(this));
        this.mTelephonyListenerManager.removeActiveDataSubscriptionIdListener(this.mPhoneStateListener);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListening$1() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mCallback);
        this.mWakefulnessLifecycle.addObserver(this.mWakefulnessObserver);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListening$3() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mCallback);
        this.mWakefulnessLifecycle.removeObserver(this.mWakefulnessObserver);
    }

    public void setListening(CarrierTextCallback carrierTextCallback) {
        this.mBgExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, carrierTextCallback));
    }

    public List<SubscriptionInfo> getSubscriptionInfo() {
        return this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo(false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x01df  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01e6  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateCarrierText() {
        /*
            r18 = this;
            r0 = r18
            android.content.Context r1 = r18.getContext()
            android.content.res.Resources r1 = r1.getResources()
            int r2 = com.android.systemui.R$bool.config_show_customize_carrier_name
            boolean r1 = r1.getBoolean(r2)
            java.util.List r2 = r18.getSubscriptionInfo()
            int r3 = r2.size()
            int[] r8 = new int[r3]
            boolean r4 = DEBUG
            java.lang.String r5 = "updateCarrierText(): "
            java.lang.String r6 = "CarrierTextController"
            if (r4 == 0) goto L_0x0035
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            r4.append(r5)
            r4.append(r3)
            java.lang.String r4 = r4.toString()
            android.util.Log.d(r6, r4)
        L_0x0035:
            int r4 = r0.mSimSlotsNumber
            int[] r4 = new int[r4]
            r9 = 0
        L_0x003a:
            int r10 = r0.mSimSlotsNumber
            if (r9 >= r10) goto L_0x0044
            r10 = -1
            r4[r9] = r10
            int r9 = r9 + 1
            goto L_0x003a
        L_0x0044:
            java.lang.CharSequence[] r9 = new java.lang.CharSequence[r3]
            boolean r10 = DEBUG
            if (r10 == 0) goto L_0x005c
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            r10.append(r5)
            r10.append(r3)
            java.lang.String r5 = r10.toString()
            android.util.Log.d(r6, r5)
        L_0x005c:
            r10 = 0
            r11 = 1
            r12 = 0
        L_0x005f:
            java.lang.String r13 = ""
            if (r10 >= r3) goto L_0x0135
            java.lang.Object r14 = r2.get(r10)
            android.telephony.SubscriptionInfo r14 = (android.telephony.SubscriptionInfo) r14
            int r14 = r14.getSubscriptionId()
            r9[r10] = r13
            r8[r10] = r14
            java.lang.Object r13 = r2.get(r10)
            android.telephony.SubscriptionInfo r13 = (android.telephony.SubscriptionInfo) r13
            int r13 = r13.getSimSlotIndex()
            r4[r13] = r10
            com.android.keyguard.KeyguardUpdateMonitor r13 = r0.mKeyguardUpdateMonitor
            int r13 = r13.getSimState(r14)
            java.lang.Object r15 = r2.get(r10)
            android.telephony.SubscriptionInfo r15 = (android.telephony.SubscriptionInfo) r15
            java.lang.CharSequence r15 = r15.getCarrierName()
            if (r1 == 0) goto L_0x009b
            java.lang.Object r16 = r2.get(r10)
            r5 = r16
            android.telephony.SubscriptionInfo r5 = (android.telephony.SubscriptionInfo) r5
            java.lang.String r15 = r0.getCustomizeCarrierName(r15, r5)
        L_0x009b:
            java.lang.CharSequence r5 = r0.getCarrierTextForSimState(r13, r15)
            boolean r16 = DEBUG
            if (r16 == 0) goto L_0x00ca
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            r17 = r1
            java.lang.String r1 = "Handling (subId="
            r7.append(r1)
            r7.append(r14)
            java.lang.String r1 = "): "
            r7.append(r1)
            r7.append(r13)
            java.lang.String r1 = " "
            r7.append(r1)
            r7.append(r15)
            java.lang.String r1 = r7.toString()
            android.util.Log.d(r6, r1)
            goto L_0x00cc
        L_0x00ca:
            r17 = r1
        L_0x00cc:
            if (r5 == 0) goto L_0x00d1
            r9[r10] = r5
            r11 = 0
        L_0x00d1:
            r1 = 5
            if (r13 != r1) goto L_0x012f
            com.android.keyguard.KeyguardUpdateMonitor r1 = r0.mKeyguardUpdateMonitor
            java.util.HashMap<java.lang.Integer, android.telephony.ServiceState> r1 = r1.mServiceStates
            java.lang.Integer r5 = java.lang.Integer.valueOf(r14)
            java.lang.Object r1 = r1.get(r5)
            android.telephony.ServiceState r1 = (android.telephony.ServiceState) r1
            if (r1 == 0) goto L_0x012f
            int r5 = r1.getDataRegistrationState()
            if (r5 != 0) goto L_0x012f
            int r5 = r1.getRilDataRadioTechnology()
            r7 = 18
            if (r5 != r7) goto L_0x0110
            android.net.wifi.WifiManager r5 = r0.mWifiManager
            if (r5 == 0) goto L_0x012f
            boolean r5 = r5.isWifiEnabled()
            if (r5 == 0) goto L_0x012f
            android.net.wifi.WifiManager r5 = r0.mWifiManager
            android.net.wifi.WifiInfo r5 = r5.getConnectionInfo()
            if (r5 == 0) goto L_0x012f
            android.net.wifi.WifiManager r5 = r0.mWifiManager
            android.net.wifi.WifiInfo r5 = r5.getConnectionInfo()
            java.lang.String r5 = r5.getBSSID()
            if (r5 == 0) goto L_0x012f
        L_0x0110:
            if (r16 == 0) goto L_0x012e
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r7 = "SIM ready and in service: subId="
            r5.append(r7)
            r5.append(r14)
            java.lang.String r7 = ", ss="
            r5.append(r7)
            r5.append(r1)
            java.lang.String r1 = r5.toString()
            android.util.Log.d(r6, r1)
        L_0x012e:
            r12 = 1
        L_0x012f:
            int r10 = r10 + 1
            r1 = r17
            goto L_0x005f
        L_0x0135:
            r1 = 0
            if (r11 == 0) goto L_0x014f
            if (r12 != 0) goto L_0x014f
            if (r3 == 0) goto L_0x0152
            java.lang.String r1 = r18.getMissingSimMessage()
            r3 = 0
            java.lang.Object r2 = r2.get(r3)
            android.telephony.SubscriptionInfo r2 = (android.telephony.SubscriptionInfo) r2
            java.lang.CharSequence r2 = r2.getCarrierName()
            java.lang.CharSequence r1 = r0.makeCarrierStringOnEmergencyCapable(r1, r2)
        L_0x014f:
            r3 = 0
            goto L_0x01c5
        L_0x0152:
            android.content.Context r2 = r18.getContext()
            r3 = 17040207(0x104034f, float:2.4246945E-38)
            java.lang.CharSequence r2 = r2.getText(r3)
            android.content.Context r3 = r18.getContext()
            android.content.IntentFilter r5 = new android.content.IntentFilter
            java.lang.String r7 = "android.telephony.action.SERVICE_PROVIDERS_UPDATED"
            r5.<init>(r7)
            android.content.Intent r1 = r3.registerReceiver(r1, r5)
            if (r1 == 0) goto L_0x01bc
            java.lang.String r2 = "android.telephony.extra.SHOW_SPN"
            r3 = 0
            boolean r2 = r1.getBooleanExtra(r2, r3)
            if (r2 == 0) goto L_0x017e
            java.lang.String r2 = "android.telephony.extra.SPN"
            java.lang.String r2 = r1.getStringExtra(r2)
            goto L_0x017f
        L_0x017e:
            r2 = r13
        L_0x017f:
            java.lang.String r5 = "android.telephony.extra.SHOW_PLMN"
            boolean r5 = r1.getBooleanExtra(r5, r3)
            if (r5 == 0) goto L_0x018d
            java.lang.String r5 = "android.telephony.extra.PLMN"
            java.lang.String r13 = r1.getStringExtra(r5)
        L_0x018d:
            boolean r1 = DEBUG
            if (r1 == 0) goto L_0x01ad
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r5 = "Getting plmn/spn sticky brdcst "
            r1.append(r5)
            r1.append(r13)
            java.lang.String r5 = "/"
            r1.append(r5)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r6, r1)
        L_0x01ad:
            boolean r1 = java.util.Objects.equals(r13, r2)
            if (r1 == 0) goto L_0x01b5
            r2 = r13
            goto L_0x01bd
        L_0x01b5:
            java.lang.CharSequence r1 = r0.mSeparator
            java.lang.CharSequence r2 = concatenate(r13, r2, r1)
            goto L_0x01bd
        L_0x01bc:
            r3 = 0
        L_0x01bd:
            java.lang.String r1 = r18.getMissingSimMessage()
            java.lang.CharSequence r1 = r0.makeCarrierStringOnEmergencyCapable(r1, r2)
        L_0x01c5:
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 == 0) goto L_0x01d1
            java.lang.CharSequence r1 = r0.mSeparator
            java.lang.CharSequence r1 = joinNotEmpty(r1, r9)
        L_0x01d1:
            java.lang.CharSequence r1 = r0.updateCarrierTextWithSimIoError(r1, r9, r4, r11)
            if (r12 != 0) goto L_0x01e6
            android.content.Context r2 = r0.mContext
            boolean r2 = com.android.settingslib.WirelessUtils.isAirplaneModeOn(r2)
            if (r2 == 0) goto L_0x01e6
            java.lang.String r1 = r18.getAirplaneModeMessage()
            r5 = r1
            r3 = 1
            goto L_0x01e7
        L_0x01e6:
            r5 = r1
        L_0x01e7:
            com.android.keyguard.CarrierTextManager$CarrierTextCallbackInfo r1 = new com.android.keyguard.CarrierTextManager$CarrierTextCallbackInfo
            r2 = 1
            r7 = r11 ^ 1
            r4 = r1
            r6 = r9
            r9 = r3
            r4.<init>(r5, r6, r7, r8, r9)
            r0.postToCallback(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.CarrierTextManager.updateCarrierText():void");
    }

    public void postToCallback(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        CarrierTextCallback carrierTextCallback = this.mCarrierTextCallback;
        if (carrierTextCallback != null) {
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda1(carrierTextCallback, carrierTextCallbackInfo));
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final String getMissingSimMessage() {
        return (!this.mShowMissingSim || !this.mTelephonyCapable) ? "" : getContext().getString(R$string.keyguard_missing_sim_message_short);
    }

    public final String getAirplaneModeMessage() {
        return this.mShowAirplaneMode ? getContext().getString(R$string.airplane_mode) : "";
    }

    /* renamed from: com.android.keyguard.CarrierTextManager$4  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass4 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode;

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
                com.android.keyguard.CarrierTextManager$StatusMode[] r0 = com.android.keyguard.CarrierTextManager.StatusMode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode = r0
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.Normal     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimNotReady     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.NetworkLocked     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimMissing     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x003e }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimPermDisabled     // Catch:{ NoSuchFieldError -> 0x003e }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x003e }
                r2 = 5
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x003e }
            L_0x003e:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0049 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimMissingLocked     // Catch:{ NoSuchFieldError -> 0x0049 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0049 }
                r2 = 6
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0049 }
            L_0x0049:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0054 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimLocked     // Catch:{ NoSuchFieldError -> 0x0054 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0054 }
                r2 = 7
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0054 }
            L_0x0054:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0060 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimPukLocked     // Catch:{ NoSuchFieldError -> 0x0060 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0060 }
                r2 = 8
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0060 }
            L_0x0060:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x006c }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimIoError     // Catch:{ NoSuchFieldError -> 0x006c }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x006c }
                r2 = 9
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x006c }
            L_0x006c:
                int[] r0 = $SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode     // Catch:{ NoSuchFieldError -> 0x0078 }
                com.android.keyguard.CarrierTextManager$StatusMode r1 = com.android.keyguard.CarrierTextManager.StatusMode.SimUnknown     // Catch:{ NoSuchFieldError -> 0x0078 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0078 }
                r2 = 10
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0078 }
            L_0x0078:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.CarrierTextManager.AnonymousClass4.<clinit>():void");
        }
    }

    public final CharSequence getCarrierTextForSimState(int i, CharSequence charSequence) {
        switch (AnonymousClass4.$SwitchMap$com$android$keyguard$CarrierTextManager$StatusMode[getStatusForIccState(i).ordinal()]) {
            case 1:
                return charSequence;
            case 2:
                return "";
            case 3:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(R$string.keyguard_network_locked_message), charSequence);
            case 5:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(R$string.keyguard_permanent_disabled_sim_message_short), charSequence);
            case 7:
                return makeCarrierStringOnLocked(getContext().getText(R$string.keyguard_sim_locked_message), charSequence);
            case 8:
                return makeCarrierStringOnLocked(getContext().getText(R$string.keyguard_sim_puk_locked_message), charSequence);
            case 9:
                return makeCarrierStringOnEmergencyCapable(getContext().getText(R$string.keyguard_sim_error_message_short), charSequence);
            default:
                return null;
        }
    }

    public final CharSequence makeCarrierStringOnEmergencyCapable(CharSequence charSequence, CharSequence charSequence2) {
        return this.mIsEmergencyCallCapable ? concatenate(charSequence, charSequence2, this.mSeparator) : charSequence;
    }

    public final CharSequence makeCarrierStringOnLocked(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            return this.mContext.getString(R$string.keyguard_carrier_name_with_sim_locked_template, new Object[]{charSequence2, charSequence});
        } else if (z) {
            return charSequence;
        } else {
            return z2 ? charSequence2 : "";
        }
    }

    public final StatusMode getStatusForIccState(int i) {
        boolean z = true;
        if (this.mKeyguardUpdateMonitor.isDeviceProvisioned() || !(i == 1 || i == 7)) {
            z = false;
        }
        if (z) {
            i = 4;
        }
        switch (i) {
            case 0:
                return StatusMode.SimUnknown;
            case 1:
                return StatusMode.SimMissing;
            case 2:
                return StatusMode.SimLocked;
            case 3:
                return StatusMode.SimPukLocked;
            case 4:
                return StatusMode.SimMissingLocked;
            case 5:
                return StatusMode.Normal;
            case 6:
                return StatusMode.SimNotReady;
            case 7:
                return StatusMode.SimPermDisabled;
            case 8:
                return StatusMode.SimIoError;
            default:
                return StatusMode.SimUnknown;
        }
    }

    public static CharSequence concatenate(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            StringBuilder sb = new StringBuilder();
            sb.append(charSequence);
            sb.append(charSequence3);
            sb.append(charSequence2);
            return sb.toString();
        } else if (z) {
            return charSequence;
        } else {
            return z2 ? charSequence2 : "";
        }
    }

    public static CharSequence joinNotEmpty(CharSequence charSequence, CharSequence[] charSequenceArr) {
        int length = charSequenceArr.length;
        if (length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (!TextUtils.isEmpty(charSequenceArr[i])) {
                if (!TextUtils.isEmpty(sb)) {
                    sb.append(charSequence);
                }
                sb.append(charSequenceArr[i]);
            }
        }
        return sb.toString();
    }

    public static class Builder {
        public final Executor mBgExecutor;
        public final Context mContext;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final Executor mMainExecutor;
        public final String mSeparator;
        public boolean mShowAirplaneMode;
        public boolean mShowMissingSim;
        public final TelephonyListenerManager mTelephonyListenerManager;
        public final TelephonyManager mTelephonyManager;
        public final WakefulnessLifecycle mWakefulnessLifecycle;
        public final WifiManager mWifiManager;

        public Builder(Context context, Resources resources, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            this.mContext = context;
            this.mSeparator = resources.getString(17040573);
            this.mWifiManager = wifiManager;
            this.mTelephonyManager = telephonyManager;
            this.mTelephonyListenerManager = telephonyListenerManager;
            this.mWakefulnessLifecycle = wakefulnessLifecycle;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        }

        public Builder setShowAirplaneMode(boolean z) {
            this.mShowAirplaneMode = z;
            return this;
        }

        public Builder setShowMissingSim(boolean z) {
            this.mShowMissingSim = z;
            return this;
        }

        public CarrierTextManager build() {
            return new CarrierTextManager(this.mContext, this.mSeparator, this.mShowAirplaneMode, this.mShowMissingSim, this.mWifiManager, this.mTelephonyManager, this.mTelephonyListenerManager, this.mWakefulnessLifecycle, this.mMainExecutor, this.mBgExecutor, this.mKeyguardUpdateMonitor);
        }
    }

    public static final class CarrierTextCallbackInfo {
        public boolean airplaneMode;
        public final boolean anySimReady;
        public final CharSequence carrierText;
        public final CharSequence[] listOfCarriers;
        public final int[] subscriptionIds;

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr) {
            this(charSequence, charSequenceArr, z, iArr, false);
        }

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr, boolean z2) {
            this.carrierText = charSequence;
            this.listOfCarriers = charSequenceArr;
            this.anySimReady = z;
            this.subscriptionIds = iArr;
            this.airplaneMode = z2;
        }
    }

    public final String getCustomizeCarrierName(CharSequence charSequence, SubscriptionInfo subscriptionInfo) {
        StringBuilder sb = new StringBuilder();
        int networkType = getNetworkType(subscriptionInfo.getSubscriptionId());
        String networkTypeToString = networkTypeToString(networkType);
        String str = get5GNetworkClass(subscriptionInfo, networkType);
        if (str != null) {
            networkTypeToString = str;
        }
        if (!TextUtils.isEmpty(charSequence)) {
            String[] split = charSequence.toString().split(this.mSeparator.toString(), 2);
            for (int i = 0; i < split.length; i++) {
                String localString = getLocalString(split[i], R$array.origin_carrier_names, R$array.locale_carrier_names);
                split[i] = localString;
                if (!TextUtils.isEmpty(localString)) {
                    if (!TextUtils.isEmpty(networkTypeToString)) {
                        split[i] = split[i] + " " + networkTypeToString;
                    }
                    if (i <= 0 || !split[i].equals(split[i - 1])) {
                        if (i > 0) {
                            sb.append(this.mSeparator);
                        }
                        sb.append(split[i]);
                    }
                }
            }
        }
        return sb.toString();
    }

    public final String getLocalString(String str, int i, int i2) {
        String[] stringArray = getContext().getResources().getStringArray(i);
        String[] stringArray2 = getContext().getResources().getStringArray(i2);
        for (int i3 = 0; i3 < stringArray.length; i3++) {
            if (stringArray[i3].equalsIgnoreCase(str)) {
                return stringArray2[i3];
            }
        }
        return str;
    }

    public final int getNetworkType(int i) {
        ServiceState serviceState = this.mKeyguardUpdateMonitor.mServiceStates.get(Integer.valueOf(i));
        if (serviceState == null || (serviceState.getDataRegState() != 0 && serviceState.getVoiceRegState() != 0)) {
            return 0;
        }
        int dataNetworkType = serviceState.getDataNetworkType();
        return dataNetworkType == 0 ? serviceState.getVoiceNetworkType() : dataNetworkType;
    }

    public final String networkTypeToString(int i) {
        int i2 = R$string.config_rat_unknown;
        long bitMaskForNetworkType = TelephonyManager.getBitMaskForNetworkType(i);
        if ((32843 & bitMaskForNetworkType) != 0) {
            i2 = R$string.config_rat_2g;
        } else if ((93108 & bitMaskForNetworkType) != 0) {
            i2 = R$string.config_rat_3g;
        } else if ((bitMaskForNetworkType & 397312) != 0) {
            i2 = R$string.config_rat_4g;
        }
        return getContext().getResources().getString(i2);
    }

    public final String get5GNetworkClass(SubscriptionInfo subscriptionInfo, int i) {
        if (i == 20) {
            return this.mContext.getResources().getString(R$string.data_connection_5g);
        }
        int simSlotIndex = subscriptionInfo.getSimSlotIndex();
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        if (this.mFiveGServiceClient == null) {
            FiveGServiceClient instance = FiveGServiceClient.getInstance(this.mContext);
            this.mFiveGServiceClient = instance;
            instance.registerCallback(this.mCallback);
        }
        if (!this.mFiveGServiceClient.getCurrentServiceState(simSlotIndex).isNrIconTypeValid() || !isDataRegisteredOnLte(subscriptionId)) {
            return null;
        }
        return this.mContext.getResources().getString(R$string.data_connection_5g);
    }

    public final boolean isDataRegisteredOnLte(int i) {
        int dataNetworkType = ((TelephonyManager) this.mContext.getSystemService("phone")).getDataNetworkType(i);
        return dataNetworkType == 13 || dataNetworkType == 19;
    }
}

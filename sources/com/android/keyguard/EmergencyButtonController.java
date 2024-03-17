package com.android.keyguard;

import android.app.ActivityOptions;
import android.app.ActivityTaskManager;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telecom.TelecomManager;
import android.telephony.CellInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$bool;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import java.util.List;

public class EmergencyButtonController extends ViewController<EmergencyButton> {
    public final ActivityTaskManager mActivityTaskManager;
    public final ConfigurationController mConfigurationController;
    public final ConfigurationController.ConfigurationListener mConfigurationListener;
    public EmergencyButtonCallback mEmergencyButtonCallback;
    public final KeyguardUpdateMonitorCallback mInfoCallback;
    public boolean mIsCellAvailable;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final MetricsLogger mMetricsLogger;
    public final PowerManager mPowerManager;
    public ServiceState mServiceState;
    public ShadeController mShadeController;
    public final TelecomManager mTelecomManager;
    public final TelephonyManager mTelephonyManager;

    public interface EmergencyButtonCallback {
        void onEmergencyButtonClickedWhenInCall();
    }

    public EmergencyButtonController(EmergencyButton emergencyButton, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, TelephonyManager telephonyManager, PowerManager powerManager, ActivityTaskManager activityTaskManager, ShadeController shadeController, TelecomManager telecomManager, MetricsLogger metricsLogger) {
        super(emergencyButton);
        this.mInfoCallback = new KeyguardUpdateMonitorCallback() {
            public void onSimStateChanged(int i, int i2, int i3) {
                EmergencyButtonController.this.updateEmergencyCallButton();
                EmergencyButtonController.this.requestCellInfoUpdate();
            }

            public void onPhoneStateChanged(int i) {
                EmergencyButtonController.this.updateEmergencyCallButton();
                EmergencyButtonController.this.requestCellInfoUpdate();
            }

            public void onServiceStateChanged(int i, ServiceState serviceState) {
                EmergencyButtonController.this.mServiceState = serviceState;
                EmergencyButtonController.this.updateEmergencyCallButton();
                EmergencyButtonController.this.requestCellInfoUpdate();
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() {
            public void onConfigChanged(Configuration configuration) {
                EmergencyButtonController.this.updateEmergencyCallButton();
            }
        };
        this.mConfigurationController = configurationController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mTelephonyManager = telephonyManager;
        this.mPowerManager = powerManager;
        this.mActivityTaskManager = activityTaskManager;
        this.mShadeController = shadeController;
        this.mTelecomManager = telecomManager;
        this.mMetricsLogger = metricsLogger;
    }

    public void onInit() {
        DejankUtils.whitelistIpcs((Runnable) new EmergencyButtonController$$ExternalSyntheticLambda0(this));
    }

    public void onViewAttached() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        ((EmergencyButton) this.mView).setOnClickListener(new EmergencyButtonController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$0(View view) {
        takeEmergencyCallAction();
    }

    public void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public final void updateEmergencyCallButton() {
        T t = this.mView;
        if (t != null) {
            EmergencyButton emergencyButton = (EmergencyButton) t;
            TelecomManager telecomManager = this.mTelecomManager;
            emergencyButton.updateEmergencyCallButton(telecomManager != null && telecomManager.isInCall(), getContext().getPackageManager().hasSystemFeature("android.hardware.telephony"), this.mKeyguardUpdateMonitor.isSimPinVoiceSecure(), isEmergencyCapable());
        }
    }

    public void setEmergencyButtonCallback(EmergencyButtonCallback emergencyButtonCallback) {
        this.mEmergencyButtonCallback = emergencyButtonCallback;
    }

    public void takeEmergencyCallAction() {
        this.mMetricsLogger.action(200);
        PowerManager powerManager = this.mPowerManager;
        if (powerManager != null) {
            powerManager.userActivity(SystemClock.uptimeMillis(), true);
        }
        this.mActivityTaskManager.stopSystemLockTaskMode();
        this.mShadeController.collapsePanel(false);
        TelecomManager telecomManager = this.mTelecomManager;
        if (telecomManager == null || !telecomManager.isInCall()) {
            this.mKeyguardUpdateMonitor.reportEmergencyCallAction(true);
            TelecomManager telecomManager2 = this.mTelecomManager;
            if (telecomManager2 == null) {
                Log.wtf("EmergencyButton", "TelecomManager was null, cannot launch emergency dialer");
                return;
            }
            getContext().startActivityAsUser(telecomManager2.createLaunchEmergencyDialerIntent((String) null).setFlags(343932928).putExtra("com.android.phone.EmergencyDialer.extra.ENTRY_TYPE", 1), ActivityOptions.makeCustomAnimation(getContext(), 0, 0).toBundle(), new UserHandle(KeyguardUpdateMonitor.getCurrentUser()));
            return;
        }
        this.mTelecomManager.showInCallScreen(false);
        EmergencyButtonCallback emergencyButtonCallback = this.mEmergencyButtonCallback;
        if (emergencyButtonCallback != null) {
            emergencyButtonCallback.onEmergencyButtonClickedWhenInCall();
        }
    }

    public final void requestCellInfoUpdate() {
        if (getContext().getResources().getBoolean(R$bool.kg_hide_emgcy_btn_when_oos)) {
            try {
                this.mTelephonyManager.createForSubscriptionId(-1).requestCellInfoUpdate(getContext().getMainExecutor(), new TelephonyManager.CellInfoCallback() {
                    public void onCellInfo(List<CellInfo> list) {
                        int i;
                        StringBuilder sb = new StringBuilder();
                        sb.append("requestCellInfoUpdate.onCellInfo cellInfoList.size=");
                        if (list == null) {
                            i = 0;
                        } else {
                            i = list.size();
                        }
                        sb.append(i);
                        Log.d("EmergencyButton", sb.toString());
                        if (list == null || list.isEmpty()) {
                            EmergencyButtonController.this.mIsCellAvailable = false;
                        } else {
                            EmergencyButtonController.this.mIsCellAvailable = true;
                        }
                        EmergencyButtonController.this.updateEmergencyCallButton();
                    }
                });
            } catch (Exception e) {
                Log.e("EmergencyButton", "Fail to call TelephonyManager.requestCellInfoUpdate ", e);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
        r1 = r1.mServiceState;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean isEmergencyCapable() {
        /*
            r1 = this;
            com.android.keyguard.KeyguardUpdateMonitor r0 = r1.mKeyguardUpdateMonitor
            boolean r0 = r0.isOOS()
            if (r0 == 0) goto L_0x0019
            boolean r0 = r1.mIsCellAvailable
            if (r0 != 0) goto L_0x0019
            android.telephony.ServiceState r1 = r1.mServiceState
            if (r1 == 0) goto L_0x0017
            boolean r1 = r1.isEmergencyOnly()
            if (r1 == 0) goto L_0x0017
            goto L_0x0019
        L_0x0017:
            r1 = 0
            goto L_0x001a
        L_0x0019:
            r1 = 1
        L_0x001a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.EmergencyButtonController.isEmergencyCapable():boolean");
    }

    public static class Factory {
        public final ActivityTaskManager mActivityTaskManager;
        public final ConfigurationController mConfigurationController;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final MetricsLogger mMetricsLogger;
        public final PowerManager mPowerManager;
        public ShadeController mShadeController;
        public final TelecomManager mTelecomManager;
        public final TelephonyManager mTelephonyManager;

        public Factory(ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, TelephonyManager telephonyManager, PowerManager powerManager, ActivityTaskManager activityTaskManager, ShadeController shadeController, TelecomManager telecomManager, MetricsLogger metricsLogger) {
            this.mConfigurationController = configurationController;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mTelephonyManager = telephonyManager;
            this.mPowerManager = powerManager;
            this.mActivityTaskManager = activityTaskManager;
            this.mShadeController = shadeController;
            this.mTelecomManager = telecomManager;
            this.mMetricsLogger = metricsLogger;
        }

        public EmergencyButtonController create(EmergencyButton emergencyButton) {
            return new EmergencyButtonController(emergencyButton, this.mConfigurationController, this.mKeyguardUpdateMonitor, this.mTelephonyManager, this.mPowerManager, this.mActivityTaskManager, this.mShadeController, this.mTelecomManager, this.mMetricsLogger);
        }
    }
}

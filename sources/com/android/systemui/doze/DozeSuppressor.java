package com.android.systemui.doze;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.AmbientDisplayConfiguration;
import android.text.TextUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import dagger.Lazy;
import java.io.PrintWriter;

public class DozeSuppressor implements DozeMachine.Part {
    public final Lazy<BiometricUnlockController> mBiometricUnlockControllerLazy;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (UiModeManager.ACTION_ENTER_CAR_MODE.equals(intent.getAction())) {
                DozeSuppressor.this.mDozeLog.traceImmediatelyEndDoze("car_mode");
                DozeSuppressor.this.mMachine.requestState(DozeMachine.State.FINISH);
            }
        }
    };
    public boolean mBroadcastReceiverRegistered;
    public final AmbientDisplayConfiguration mConfig;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public DozeHost.Callback mHostCallback = new DozeHost.Callback() {
        public void onPowerSaveChanged(boolean z) {
            DozeMachine.State state;
            if (DozeSuppressor.this.mDozeHost.isPowerSaveActive()) {
                state = DozeMachine.State.DOZE;
            } else {
                state = (DozeSuppressor.this.mMachine.getState() != DozeMachine.State.DOZE || !DozeSuppressor.this.mConfig.alwaysOnEnabled(-2)) ? null : DozeMachine.State.DOZE_AOD;
            }
            if (state != null) {
                DozeSuppressor.this.mDozeLog.tracePowerSaveChanged(DozeSuppressor.this.mDozeHost.isPowerSaveActive(), state);
                DozeSuppressor.this.mMachine.requestState(state);
            }
        }

        public void onAlwaysOnSuppressedChanged(boolean z) {
            DozeMachine.State state;
            if (!DozeSuppressor.this.mConfig.alwaysOnEnabled(-2) || z) {
                state = DozeMachine.State.DOZE;
            } else {
                state = DozeMachine.State.DOZE_AOD;
            }
            DozeSuppressor.this.mDozeLog.traceAlwaysOnSuppressedChange(z, state);
            DozeSuppressor.this.mMachine.requestState(state);
        }
    };
    public DozeMachine mMachine;
    public final UiModeManager mUiModeManager;

    public DozeSuppressor(DozeHost dozeHost, AmbientDisplayConfiguration ambientDisplayConfiguration, DozeLog dozeLog, BroadcastDispatcher broadcastDispatcher, UiModeManager uiModeManager, Lazy<BiometricUnlockController> lazy) {
        this.mDozeHost = dozeHost;
        this.mConfig = ambientDisplayConfiguration;
        this.mDozeLog = dozeLog;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUiModeManager = uiModeManager;
        this.mBiometricUnlockControllerLazy = lazy;
    }

    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    /* renamed from: com.android.systemui.doze.DozeSuppressor$3  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
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
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.FINISH     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeSuppressor.AnonymousClass3.<clinit>():void");
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        int i = AnonymousClass3.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()];
        if (i == 1) {
            registerBroadcastReceiver();
            this.mDozeHost.addCallback(this.mHostCallback);
            checkShouldImmediatelyEndDoze();
        } else if (i == 2) {
            destroy();
        }
    }

    public void destroy() {
        unregisterBroadcastReceiver();
        this.mDozeHost.removeCallback(this.mHostCallback);
    }

    public final void checkShouldImmediatelyEndDoze() {
        String str;
        if (this.mUiModeManager.getCurrentModeType() == 3) {
            str = "car_mode";
        } else if (!this.mDozeHost.isProvisioned()) {
            str = "device_unprovisioned";
        } else {
            str = this.mBiometricUnlockControllerLazy.get().hasPendingAuthentication() ? "has_pending_auth" : null;
        }
        if (!TextUtils.isEmpty(str)) {
            this.mDozeLog.traceImmediatelyEndDoze(str);
            this.mMachine.requestState(DozeMachine.State.FINISH);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println(" uiMode=" + this.mUiModeManager.getCurrentModeType());
        printWriter.println(" hasPendingAuth=" + this.mBiometricUnlockControllerLazy.get().hasPendingAuthentication());
        printWriter.println(" isProvisioned=" + this.mDozeHost.isProvisioned());
        printWriter.println(" isAlwaysOnSuppressed=" + this.mDozeHost.isAlwaysOnSuppressed());
        printWriter.println(" aodPowerSaveActive=" + this.mDozeHost.isPowerSaveActive());
    }

    public final void registerBroadcastReceiver() {
        if (!this.mBroadcastReceiverRegistered) {
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, new IntentFilter(UiModeManager.ACTION_ENTER_CAR_MODE));
            this.mBroadcastReceiverRegistered = true;
        }
    }

    public final void unregisterBroadcastReceiver() {
        if (this.mBroadcastReceiverRegistered) {
            this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
            this.mBroadcastReceiverRegistered = false;
        }
    }
}

package com.android.systemui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.biometrics.BiometricSourceType;
import android.os.Build;
import android.provider.DeviceConfig;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.io.PrintWriter;

public class LatencyTester extends CoreStartable {
    public static final boolean DEFAULT_ENABLED = Build.IS_ENG;
    public final BiometricUnlockController mBiometricUnlockController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.android.systemui.latency.ACTION_FINGERPRINT_WAKE".equals(action)) {
                LatencyTester.this.fakeWakeAndUnlock(BiometricSourceType.FINGERPRINT);
            } else if ("com.android.systemui.latency.ACTION_FACE_WAKE".equals(action)) {
                LatencyTester.this.fakeWakeAndUnlock(BiometricSourceType.FACE);
            }
        }
    };
    public final DeviceConfigProxy mDeviceConfigProxy;
    public boolean mEnabled;

    public LatencyTester(Context context, BiometricUnlockController biometricUnlockController, BroadcastDispatcher broadcastDispatcher, DeviceConfigProxy deviceConfigProxy, DelayableExecutor delayableExecutor) {
        super(context);
        this.mBiometricUnlockController = biometricUnlockController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDeviceConfigProxy = deviceConfigProxy;
        updateEnabled();
        deviceConfigProxy.addOnPropertiesChangedListener("latency_tracker", delayableExecutor, new LatencyTester$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        updateEnabled();
    }

    public void start() {
        registerForBroadcasts(this.mEnabled);
    }

    public final void fakeWakeAndUnlock(BiometricSourceType biometricSourceType) {
        if (this.mEnabled) {
            this.mBiometricUnlockController.onBiometricAcquired(biometricSourceType, 0);
            this.mBiometricUnlockController.onBiometricAuthenticated(KeyguardUpdateMonitor.getCurrentUser(), biometricSourceType, true);
        }
    }

    public final void registerForBroadcasts(boolean z) {
        if (z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.android.systemui.latency.ACTION_FINGERPRINT_WAKE");
            intentFilter.addAction("com.android.systemui.latency.ACTION_FACE_WAKE");
            this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter);
            return;
        }
        this.mBroadcastDispatcher.unregisterReceiver(this.mBroadcastReceiver);
    }

    public final void updateEnabled() {
        boolean z = this.mEnabled;
        boolean z2 = Build.IS_DEBUGGABLE && this.mDeviceConfigProxy.getBoolean("latency_tracker", "enabled", DEFAULT_ENABLED);
        this.mEnabled = z2;
        if (z2 != z) {
            registerForBroadcasts(z2);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mEnabled=" + this.mEnabled);
    }
}

package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyConfig;
import kotlin.Unit;

/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$configCallback$1 implements PrivacyConfig.Callback {
    public final /* synthetic */ AppOpsPrivacyItemMonitor this$0;

    public AppOpsPrivacyItemMonitor$configCallback$1(AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor) {
        this.this$0 = appOpsPrivacyItemMonitor;
    }

    public void onFlagLocationChanged(boolean z) {
        onFlagChanged();
    }

    public void onFlagMicCameraChanged(boolean z) {
        onFlagChanged();
    }

    public final void onFlagChanged() {
        Object access$getLock$p = this.this$0.lock;
        AppOpsPrivacyItemMonitor appOpsPrivacyItemMonitor = this.this$0;
        synchronized (access$getLock$p) {
            appOpsPrivacyItemMonitor.micCameraAvailable = appOpsPrivacyItemMonitor.privacyConfig.getMicCameraAvailable();
            appOpsPrivacyItemMonitor.locationAvailable = appOpsPrivacyItemMonitor.privacyConfig.getLocationAvailable();
            appOpsPrivacyItemMonitor.setListeningStateLocked();
            Unit unit = Unit.INSTANCE;
        }
        this.this$0.dispatchOnPrivacyItemsChanged();
    }
}

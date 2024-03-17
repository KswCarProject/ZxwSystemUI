package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyItemMonitor;

/* compiled from: AppOpsPrivacyItemMonitor.kt */
public final class AppOpsPrivacyItemMonitor$dispatchOnPrivacyItemsChanged$1 implements Runnable {
    public final /* synthetic */ PrivacyItemMonitor.Callback $cb;

    public AppOpsPrivacyItemMonitor$dispatchOnPrivacyItemsChanged$1(PrivacyItemMonitor.Callback callback) {
        this.$cb = callback;
    }

    public final void run() {
        this.$cb.onPrivacyItemsChanged();
    }
}

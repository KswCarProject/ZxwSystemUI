package com.android.systemui.privacy;

import com.android.systemui.privacy.PrivacyItemMonitor;

/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$privacyItemMonitorCallback$1 implements PrivacyItemMonitor.Callback {
    public final /* synthetic */ PrivacyItemController this$0;

    public PrivacyItemController$privacyItemMonitorCallback$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    public void onPrivacyItemsChanged() {
        this.this$0.update();
    }
}

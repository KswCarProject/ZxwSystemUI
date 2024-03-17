package com.android.systemui.privacy;

/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$update$1 implements Runnable {
    public final /* synthetic */ PrivacyItemController this$0;

    public PrivacyItemController$update$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    public final void run() {
        this.this$0.updateListAndNotifyChanges.run();
    }
}

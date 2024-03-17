package com.android.systemui.privacy;

/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$MyExecutor$updateListeningState$1 implements Runnable {
    public final /* synthetic */ PrivacyItemController this$0;

    public PrivacyItemController$MyExecutor$updateListeningState$1(PrivacyItemController privacyItemController) {
        this.this$0 = privacyItemController;
    }

    public final void run() {
        this.this$0.setListeningState();
    }
}

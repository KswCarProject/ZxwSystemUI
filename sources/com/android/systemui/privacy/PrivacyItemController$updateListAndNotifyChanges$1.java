package com.android.systemui.privacy;

import com.android.systemui.util.concurrency.DelayableExecutor;

/* compiled from: PrivacyItemController.kt */
public final class PrivacyItemController$updateListAndNotifyChanges$1 implements Runnable {
    public final /* synthetic */ DelayableExecutor $uiExecutor;
    public final /* synthetic */ PrivacyItemController this$0;

    public PrivacyItemController$updateListAndNotifyChanges$1(PrivacyItemController privacyItemController, DelayableExecutor delayableExecutor) {
        this.this$0 = privacyItemController;
        this.$uiExecutor = delayableExecutor;
    }

    public final void run() {
        this.this$0.updatePrivacyList();
        this.$uiExecutor.execute(this.this$0.notifyChanges);
    }
}

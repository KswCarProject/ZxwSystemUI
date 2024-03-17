package com.android.systemui.controls.ui;

import com.android.systemui.plugins.ActivityStarter;

/* compiled from: DetailDialog.kt */
public final class DetailDialog$4$1$action$1 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ DetailDialog this$0;

    public DetailDialog$4$1$action$1(DetailDialog detailDialog) {
        this.this$0 = detailDialog;
    }

    public final boolean onDismiss() {
        this.this$0.getBroadcastSender().closeSystemDialogs();
        this.this$0.getPendingIntent().send();
        return false;
    }
}

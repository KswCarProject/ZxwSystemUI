package com.android.systemui.controls.ui;

import android.view.View;
import org.jetbrains.annotations.NotNull;

/* compiled from: DetailDialog.kt */
public final class DetailDialog$4$1 implements View.OnClickListener {
    public final /* synthetic */ DetailDialog this$0;

    public DetailDialog$4$1(DetailDialog detailDialog) {
        this.this$0 = detailDialog;
    }

    public final void onClick(@NotNull View view) {
        this.this$0.removeDetailTask();
        this.this$0.dismiss();
        DetailDialog$4$1$action$1 detailDialog$4$1$action$1 = new DetailDialog$4$1$action$1(this.this$0);
        if (this.this$0.getKeyguardStateController().isUnlocked()) {
            detailDialog$4$1$action$1.onDismiss();
        } else {
            this.this$0.getActivityStarter().dismissKeyguardThenExecute(detailDialog$4$1$action$1, (Runnable) null, true);
        }
    }
}

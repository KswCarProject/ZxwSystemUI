package com.android.systemui.qs;

import android.content.DialogInterface;
import com.android.systemui.qs.FgsManagerController;
import kotlin.Unit;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$showDialog$1$2 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$showDialog$1$2(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.this$0.changesSinceDialog = false;
        Object access$getLock$p = this.this$0.lock;
        FgsManagerController fgsManagerController = this.this$0;
        synchronized (access$getLock$p) {
            fgsManagerController.dialog = null;
            fgsManagerController.updateAppItemsLocked();
            Unit unit = Unit.INSTANCE;
        }
        FgsManagerController fgsManagerController2 = this.this$0;
        for (FgsManagerController.OnDialogDismissedListener fgsManagerController$showDialog$1$2$2$1 : this.this$0.getOnDialogDismissedListeners()) {
            fgsManagerController2.mainExecutor.execute(new FgsManagerController$showDialog$1$2$2$1(fgsManagerController$showDialog$1$2$2$1));
        }
    }
}

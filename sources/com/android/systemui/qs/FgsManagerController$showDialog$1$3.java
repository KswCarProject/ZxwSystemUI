package com.android.systemui.qs;

import android.view.View;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import kotlin.Unit;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$showDialog$1$3 implements Runnable {
    public final /* synthetic */ SystemUIDialog $dialog;
    public final /* synthetic */ View $viewLaunchedFrom;
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$showDialog$1$3(View view, SystemUIDialog systemUIDialog, FgsManagerController fgsManagerController) {
        this.$viewLaunchedFrom = view;
        this.$dialog = systemUIDialog;
        this.this$0 = fgsManagerController;
    }

    public final void run() {
        Unit unit;
        View view = this.$viewLaunchedFrom;
        if (view == null) {
            unit = null;
        } else {
            FgsManagerController fgsManagerController = this.this$0;
            DialogLaunchAnimator.showFromView$default(fgsManagerController.dialogLaunchAnimator, this.$dialog, view, false, 4, (Object) null);
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            this.$dialog.show();
        }
    }
}

package com.android.systemui.qs;

import android.view.View;
import com.android.systemui.globalactions.GlobalActionsDialogLite;

/* compiled from: FooterActionsController.kt */
public final class FooterActionsController$onClickListener$1 implements View.OnClickListener {
    public final /* synthetic */ FooterActionsController this$0;

    public FooterActionsController$onClickListener$1(FooterActionsController footerActionsController) {
        this.this$0 = footerActionsController;
    }

    public final void onClick(View view) {
        if (this.this$0.getVisible() && !this.this$0.falsingManager.isFalseTap(1)) {
            if (view == this.this$0.settingsButtonContainer) {
                if (!this.this$0.deviceProvisionedController.isCurrentUserSetup()) {
                    this.this$0.activityStarter.postQSRunnableDismissingKeyguard(AnonymousClass1.INSTANCE);
                    return;
                }
                this.this$0.metricsLogger.action(406);
                this.this$0.startSettingsActivity();
            } else if (view == this.this$0.powerMenuLite) {
                this.this$0.uiEventLogger.log(GlobalActionsDialogLite.GlobalActionsEvent.GA_OPEN_QS);
                GlobalActionsDialogLite access$getGlobalActionsDialog$p = this.this$0.globalActionsDialog;
                if (access$getGlobalActionsDialog$p != null) {
                    access$getGlobalActionsDialog$p.showOrHideDialog(false, true, view);
                }
            }
        }
    }
}

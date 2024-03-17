package com.android.systemui.qs;

import android.view.View;

/* compiled from: FooterActionsController.kt */
public final class FooterActionsController$onViewAttached$visibilityListener$1 implements VisibilityChangedDispatcher$OnVisibilityChangedListener {
    public final /* synthetic */ View $fgsFooter;
    public final /* synthetic */ View $securityFooter;
    public final /* synthetic */ FooterActionsController this$0;

    public FooterActionsController$onViewAttached$visibilityListener$1(View view, View view2, FooterActionsController footerActionsController) {
        this.$securityFooter = view;
        this.$fgsFooter = view2;
        this.this$0 = footerActionsController;
    }

    public final void onVisibilityChanged(int i) {
        boolean z = false;
        if (this.$securityFooter.getVisibility() == 0 && this.$fgsFooter.getVisibility() == 0) {
            this.this$0.getSecurityFootersSeparator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().setVisibility(0);
        } else {
            this.this$0.getSecurityFootersSeparator$frameworks__base__packages__SystemUI__android_common__SystemUI_core().setVisibility(8);
        }
        QSFgsManagerFooter access$getFgsManagerFooterController$p = this.this$0.fgsManagerFooterController;
        if (this.$securityFooter.getVisibility() == 0) {
            z = true;
        }
        access$getFgsManagerFooterController$p.setCollapsed(z);
    }
}

package com.android.systemui.qs;

/* compiled from: FooterActionsView.kt */
public final class FooterActionsView$updateEverything$1 implements Runnable {
    public final /* synthetic */ boolean $multiUserEnabled;
    public final /* synthetic */ FooterActionsView this$0;

    public FooterActionsView$updateEverything$1(FooterActionsView footerActionsView, boolean z) {
        this.this$0 = footerActionsView;
        this.$multiUserEnabled = z;
    }

    public final void run() {
        this.this$0.updateVisibilities(this.$multiUserEnabled);
        this.this$0.updateClickabilities();
        this.this$0.setClickable(false);
    }
}

package com.android.wm.shell.bubbles;

/* compiled from: ManageEducationView.kt */
public final class ManageEducationView$hide$2 implements Runnable {
    public final /* synthetic */ ManageEducationView this$0;

    public ManageEducationView$hide$2(ManageEducationView manageEducationView) {
        this.this$0 = manageEducationView;
    }

    public final void run() {
        this.this$0.isHiding = false;
        this.this$0.setVisibility(8);
    }
}

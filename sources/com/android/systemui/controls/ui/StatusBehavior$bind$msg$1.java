package com.android.systemui.controls.ui;

import android.view.View;

/* compiled from: StatusBehavior.kt */
public final class StatusBehavior$bind$msg$1 implements View.OnClickListener {
    public final /* synthetic */ ControlWithState $cws;
    public final /* synthetic */ StatusBehavior this$0;

    public StatusBehavior$bind$msg$1(StatusBehavior statusBehavior, ControlWithState controlWithState) {
        this.this$0 = statusBehavior;
        this.$cws = controlWithState;
    }

    public final void onClick(View view) {
        StatusBehavior statusBehavior = this.this$0;
        statusBehavior.showNotFoundDialog(statusBehavior.getCvh(), this.$cws);
    }
}

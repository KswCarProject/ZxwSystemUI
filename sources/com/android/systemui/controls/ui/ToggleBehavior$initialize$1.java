package com.android.systemui.controls.ui;

import android.view.View;

/* compiled from: ToggleBehavior.kt */
public final class ToggleBehavior$initialize$1 implements View.OnClickListener {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ ToggleBehavior this$0;

    public ToggleBehavior$initialize$1(ControlViewHolder controlViewHolder, ToggleBehavior toggleBehavior) {
        this.$cvh = controlViewHolder;
        this.this$0 = toggleBehavior;
    }

    public final void onClick(View view) {
        this.$cvh.getControlActionCoordinator().toggle(this.$cvh, this.this$0.getTemplate().getTemplateId(), this.this$0.getTemplate().isChecked());
    }
}

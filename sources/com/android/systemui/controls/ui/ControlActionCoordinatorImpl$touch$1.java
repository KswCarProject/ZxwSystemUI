package com.android.systemui.controls.ui;

import android.service.controls.Control;
import android.service.controls.actions.CommandAction;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$touch$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Control $control;
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ String $templateId;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$touch$1(ControlViewHolder controlViewHolder, ControlActionCoordinatorImpl controlActionCoordinatorImpl, Control control, String str) {
        super(0);
        this.$cvh = controlViewHolder;
        this.this$0 = controlActionCoordinatorImpl;
        this.$control = control;
        this.$templateId = str;
    }

    public final void invoke() {
        this.$cvh.getLayout().performHapticFeedback(6);
        if (this.$cvh.usePanel()) {
            this.this$0.showDetail(this.$cvh, this.$control.getAppIntent());
        } else {
            this.$cvh.action(new CommandAction(this.$templateId));
        }
    }
}

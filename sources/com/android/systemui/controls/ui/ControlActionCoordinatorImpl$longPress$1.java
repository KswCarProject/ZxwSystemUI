package com.android.systemui.controls.ui;

import android.service.controls.Control;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$longPress$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$longPress$1(ControlViewHolder controlViewHolder, ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        super(0);
        this.$cvh = controlViewHolder;
        this.this$0 = controlActionCoordinatorImpl;
    }

    public final void invoke() {
        Control control = this.$cvh.getCws().getControl();
        if (control != null) {
            ControlViewHolder controlViewHolder = this.$cvh;
            ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
            controlViewHolder.getLayout().performHapticFeedback(0);
            controlActionCoordinatorImpl.showDetail(controlViewHolder, control.getAppIntent());
        }
    }
}

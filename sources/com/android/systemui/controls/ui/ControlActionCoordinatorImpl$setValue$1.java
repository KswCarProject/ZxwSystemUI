package com.android.systemui.controls.ui;

import android.service.controls.actions.FloatAction;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$setValue$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ float $newValue;
    public final /* synthetic */ String $templateId;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$setValue$1(ControlViewHolder controlViewHolder, String str, float f) {
        super(0);
        this.$cvh = controlViewHolder;
        this.$templateId = str;
        this.$newValue = f;
    }

    public final void invoke() {
        this.$cvh.action(new FloatAction(this.$templateId, this.$newValue));
    }
}

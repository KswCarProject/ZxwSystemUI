package com.android.systemui.controls.management;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlAdapter.kt */
public /* synthetic */ class ControlHolder$accessibilityDelegate$2 extends FunctionReferenceImpl implements Function0<Integer> {
    public ControlHolder$accessibilityDelegate$2(Object obj) {
        super(0, obj, ControlHolder.class, "getLayoutPosition", "getLayoutPosition()I", 0);
    }

    @NotNull
    public final Integer invoke() {
        return Integer.valueOf(((ControlHolder) this.receiver).getLayoutPosition());
    }
}

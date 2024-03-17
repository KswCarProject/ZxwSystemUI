package com.android.systemui.controls.management;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
import org.jetbrains.annotations.Nullable;

/* compiled from: ControlAdapter.kt */
public /* synthetic */ class ControlHolder$accessibilityDelegate$1 extends FunctionReferenceImpl implements Function1<Boolean, CharSequence> {
    public ControlHolder$accessibilityDelegate$1(Object obj) {
        super(1, obj, ControlHolder.class, "stateDescription", "stateDescription(Z)Ljava/lang/CharSequence;", 0);
    }

    @Nullable
    public final CharSequence invoke(boolean z) {
        return ((ControlHolder) this.receiver).stateDescription(z);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Boolean) obj).booleanValue());
    }
}

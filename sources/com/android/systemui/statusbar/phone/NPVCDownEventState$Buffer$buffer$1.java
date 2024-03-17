package com.android.systemui.statusbar.phone;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NPVCDownEventState.kt */
public final class NPVCDownEventState$Buffer$buffer$1 extends Lambda implements Function0<NPVCDownEventState> {
    public static final NPVCDownEventState$Buffer$buffer$1 INSTANCE = new NPVCDownEventState$Buffer$buffer$1();

    public NPVCDownEventState$Buffer$buffer$1() {
        super(0);
    }

    @NotNull
    public final NPVCDownEventState invoke() {
        return new NPVCDownEventState(0, 0.0f, 0.0f, false, false, false, false, false, false, false, false, 2047, (DefaultConstructorMarker) null);
    }
}

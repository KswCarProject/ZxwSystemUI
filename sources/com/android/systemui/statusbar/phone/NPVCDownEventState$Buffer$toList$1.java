package com.android.systemui.statusbar.phone;

import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: NPVCDownEventState.kt */
public final class NPVCDownEventState$Buffer$toList$1 extends Lambda implements Function1<NPVCDownEventState, List<? extends String>> {
    public static final NPVCDownEventState$Buffer$toList$1 INSTANCE = new NPVCDownEventState$Buffer$toList$1();

    public NPVCDownEventState$Buffer$toList$1() {
        super(1);
    }

    @NotNull
    public final List<String> invoke(@NotNull NPVCDownEventState nPVCDownEventState) {
        return nPVCDownEventState.getAsStringList();
    }
}

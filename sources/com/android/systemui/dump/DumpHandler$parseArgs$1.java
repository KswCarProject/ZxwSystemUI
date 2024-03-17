package com.android.systemui.dump;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: DumpHandler.kt */
public final class DumpHandler$parseArgs$1 extends Lambda implements Function1<String, String> {
    public static final DumpHandler$parseArgs$1 INSTANCE = new DumpHandler$parseArgs$1();

    public DumpHandler$parseArgs$1() {
        super(1);
    }

    @NotNull
    public final String invoke(@NotNull String str) {
        if (ArraysKt___ArraysKt.contains((T[]) DumpHandlerKt.PRIORITY_OPTIONS, str)) {
            return str;
        }
        throw new IllegalArgumentException();
    }
}

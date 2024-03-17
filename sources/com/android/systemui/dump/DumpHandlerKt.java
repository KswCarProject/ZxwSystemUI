package com.android.systemui.dump;

import org.jetbrains.annotations.NotNull;

/* compiled from: DumpHandler.kt */
public final class DumpHandlerKt {
    @NotNull
    public static final String[] COMMANDS = {"bugreport-critical", "bugreport-normal", "buffers", "dumpables"};
    @NotNull
    public static final String[] PRIORITY_OPTIONS = {"CRITICAL", "HIGH", "NORMAL"};
}

package com.android.systemui.util;

import android.util.IndentingPrintWriter;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DumpUtils.kt */
public final class DumpUtilsKt {
    @NotNull
    public static final IndentingPrintWriter asIndenting(@NotNull PrintWriter printWriter) {
        IndentingPrintWriter indentingPrintWriter = printWriter instanceof IndentingPrintWriter ? (IndentingPrintWriter) printWriter : null;
        return indentingPrintWriter == null ? new IndentingPrintWriter(printWriter) : indentingPrintWriter;
    }

    public static final void withIncreasedIndent(@NotNull IndentingPrintWriter indentingPrintWriter, @NotNull Runnable runnable) {
        indentingPrintWriter.increaseIndent();
        try {
            runnable.run();
        } finally {
            indentingPrintWriter.decreaseIndent();
        }
    }

    @NotNull
    public static final String visibilityString(int i) {
        if (i == 0) {
            return "visible";
        }
        if (i != 4) {
            return i != 8 ? Intrinsics.stringPlus("unknown:", Integer.valueOf(i)) : "gone";
        }
        return "invisible";
    }
}

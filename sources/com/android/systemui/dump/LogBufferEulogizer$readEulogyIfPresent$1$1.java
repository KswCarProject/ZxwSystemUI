package com.android.systemui.dump;

import java.io.PrintWriter;
import java.util.function.Consumer;

/* compiled from: LogBufferEulogizer.kt */
public final class LogBufferEulogizer$readEulogyIfPresent$1$1<T> implements Consumer {
    public final /* synthetic */ PrintWriter $pw;

    public LogBufferEulogizer$readEulogyIfPresent$1$1(PrintWriter printWriter) {
        this.$pw = printWriter;
    }

    public final void accept(String str) {
        this.$pw.println(str);
    }
}

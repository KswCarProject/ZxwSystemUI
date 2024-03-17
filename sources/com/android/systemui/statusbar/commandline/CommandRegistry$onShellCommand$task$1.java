package com.android.systemui.statusbar.commandline;

import java.io.PrintWriter;
import java.util.concurrent.Callable;
import kotlin.collections.ArraysKt___ArraysKt;

/* compiled from: CommandRegistry.kt */
public final class CommandRegistry$onShellCommand$task$1<V> implements Callable {
    public final /* synthetic */ String[] $args;
    public final /* synthetic */ Command $command;
    public final /* synthetic */ PrintWriter $pw;

    public CommandRegistry$onShellCommand$task$1(Command command, PrintWriter printWriter, String[] strArr) {
        this.$command = command;
        this.$pw = printWriter;
        this.$args = strArr;
    }

    public final void call() {
        this.$command.execute(this.$pw, ArraysKt___ArraysKt.drop(this.$args, 1));
    }
}

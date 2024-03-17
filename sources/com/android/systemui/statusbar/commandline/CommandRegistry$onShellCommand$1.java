package com.android.systemui.statusbar.commandline;

import java.util.concurrent.FutureTask;
import kotlin.Unit;

/* compiled from: CommandRegistry.kt */
public final class CommandRegistry$onShellCommand$1 implements Runnable {
    public final /* synthetic */ FutureTask<Unit> $task;

    public CommandRegistry$onShellCommand$1(FutureTask<Unit> futureTask) {
        this.$task = futureTask;
    }

    public final void run() {
        this.$task.run();
    }
}

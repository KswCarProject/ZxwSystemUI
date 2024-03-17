package com.android.systemui.statusbar.commandline;

import android.content.Context;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: CommandRegistry.kt */
public final class CommandRegistry {
    @NotNull
    public final Map<String, CommandWrapper> commandMap = new LinkedHashMap();
    @NotNull
    public final Context context;
    public boolean initialized;
    @NotNull
    public final Executor mainExecutor;

    public CommandRegistry(@NotNull Context context2, @NotNull Executor executor) {
        this.context = context2;
        this.mainExecutor = executor;
    }

    @NotNull
    public final Context getContext() {
        return this.context;
    }

    public final synchronized void registerCommand(@NotNull String str, @NotNull Function0<? extends Command> function0, @NotNull Executor executor) {
        if (this.commandMap.get(str) == null) {
            this.commandMap.put(str, new CommandWrapper(function0, executor));
        } else {
            throw new IllegalStateException("A command is already registered for (" + str + ')');
        }
    }

    public final synchronized void registerCommand(@NotNull String str, @NotNull Function0<? extends Command> function0) {
        registerCommand(str, function0, this.mainExecutor);
    }

    public final synchronized void unregisterCommand(@NotNull String str) {
        this.commandMap.remove(str);
    }

    public final void initializeCommands() {
        this.initialized = true;
        registerCommand("prefs", new CommandRegistry$initializeCommands$1(this));
    }

    public final void onShellCommand(@NotNull PrintWriter printWriter, @NotNull String[] strArr) {
        if (!this.initialized) {
            initializeCommands();
        }
        if (strArr.length == 0) {
            help(printWriter);
            return;
        }
        CommandWrapper commandWrapper = this.commandMap.get(strArr[0]);
        if (commandWrapper == null) {
            help(printWriter);
            return;
        }
        FutureTask futureTask = new FutureTask(new CommandRegistry$onShellCommand$task$1(commandWrapper.getCommandFactory().invoke(), printWriter, strArr));
        commandWrapper.getExecutor().execute(new CommandRegistry$onShellCommand$1(futureTask));
        futureTask.get();
    }

    public final void help(PrintWriter printWriter) {
        printWriter.println("Usage: adb shell cmd statusbar <command>");
        printWriter.println("  known commands:");
        for (String stringPlus : this.commandMap.keySet()) {
            printWriter.println(Intrinsics.stringPlus("   ", stringPlus));
        }
    }
}

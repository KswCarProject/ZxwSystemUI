package com.android.systemui.statusbar.commandline;

import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CommandRegistry.kt */
public final class CommandWrapper {
    @NotNull
    public final Function0<Command> commandFactory;
    @NotNull
    public final Executor executor;

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CommandWrapper)) {
            return false;
        }
        CommandWrapper commandWrapper = (CommandWrapper) obj;
        return Intrinsics.areEqual((Object) this.commandFactory, (Object) commandWrapper.commandFactory) && Intrinsics.areEqual((Object) this.executor, (Object) commandWrapper.executor);
    }

    public int hashCode() {
        return (this.commandFactory.hashCode() * 31) + this.executor.hashCode();
    }

    @NotNull
    public String toString() {
        return "CommandWrapper(commandFactory=" + this.commandFactory + ", executor=" + this.executor + ')';
    }

    public CommandWrapper(@NotNull Function0<? extends Command> function0, @NotNull Executor executor2) {
        this.commandFactory = function0;
        this.executor = executor2;
    }

    @NotNull
    public final Function0<Command> getCommandFactory() {
        return this.commandFactory;
    }

    @NotNull
    public final Executor getExecutor() {
        return this.executor;
    }
}

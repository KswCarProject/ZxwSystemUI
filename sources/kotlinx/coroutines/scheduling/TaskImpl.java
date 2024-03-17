package kotlinx.coroutines.scheduling;

import kotlinx.coroutines.DebugStringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: Tasks.kt */
public final class TaskImpl extends Task {
    @NotNull
    public final Runnable block;

    public TaskImpl(@NotNull Runnable runnable, long j, @NotNull TaskContext taskContext) {
        super(j, taskContext);
        this.block = runnable;
    }

    public void run() {
        try {
            this.block.run();
        } finally {
            this.taskContext.afterTask();
        }
    }

    @NotNull
    public String toString() {
        return "Task[" + DebugStringsKt.getClassSimpleName(this.block) + '@' + DebugStringsKt.getHexAddress(this.block) + ", " + this.submissionTime + ", " + this.taskContext + ']';
    }
}

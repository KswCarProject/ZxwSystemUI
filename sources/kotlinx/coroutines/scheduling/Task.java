package kotlinx.coroutines.scheduling;

import org.jetbrains.annotations.NotNull;

/* compiled from: Tasks.kt */
public abstract class Task implements Runnable {
    public long submissionTime;
    @NotNull
    public TaskContext taskContext;

    public Task(long j, @NotNull TaskContext taskContext2) {
        this.submissionTime = j;
        this.taskContext = taskContext2;
    }

    public Task() {
        this(0, NonBlockingContext.INSTANCE);
    }
}

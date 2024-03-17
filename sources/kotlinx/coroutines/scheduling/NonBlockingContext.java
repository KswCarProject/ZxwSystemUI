package kotlinx.coroutines.scheduling;

import org.jetbrains.annotations.NotNull;

/* compiled from: Tasks.kt */
public final class NonBlockingContext implements TaskContext {
    @NotNull
    public static final NonBlockingContext INSTANCE = new NonBlockingContext();
    public static final int taskMode = 0;

    public void afterTask() {
    }

    public int getTaskMode() {
        return taskMode;
    }
}

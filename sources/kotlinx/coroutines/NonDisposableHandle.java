package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: Job.kt */
public final class NonDisposableHandle implements DisposableHandle, ChildHandle {
    @NotNull
    public static final NonDisposableHandle INSTANCE = new NonDisposableHandle();

    public boolean childCancelled(@NotNull Throwable th) {
        return false;
    }

    public void dispose() {
    }

    @NotNull
    public String toString() {
        return "NonDisposableHandle";
    }
}

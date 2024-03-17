package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: CancellableContinuationImpl.kt */
public final class Active implements NotCompleted {
    @NotNull
    public static final Active INSTANCE = new Active();

    @NotNull
    public String toString() {
        return "Active";
    }
}

package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: CancellableContinuation.kt */
public final class CancellableContinuationKt {
    public static final void disposeOnCancellation(@NotNull CancellableContinuation<?> cancellableContinuation, @NotNull DisposableHandle disposableHandle) {
        cancellableContinuation.invokeOnCancellation(new DisposeOnCancel(disposableHandle));
    }
}

package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: Exceptions.common.kt */
public final class CompletionHandlerException extends RuntimeException {
    public CompletionHandlerException(@NotNull String str, @NotNull Throwable th) {
        super(str, th);
    }
}

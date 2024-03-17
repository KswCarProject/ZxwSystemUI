package kotlinx.coroutines;

import org.jetbrains.annotations.NotNull;

/* compiled from: Exceptions.common.kt */
public final class CoroutinesInternalError extends Error {
    public CoroutinesInternalError(@NotNull String str, @NotNull Throwable th) {
        super(str, th);
    }
}

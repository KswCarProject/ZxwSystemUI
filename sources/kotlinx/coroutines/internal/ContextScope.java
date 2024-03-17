package kotlinx.coroutines.internal;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;

/* compiled from: Scopes.kt */
public final class ContextScope implements CoroutineScope {
    @NotNull
    public final CoroutineContext coroutineContext;

    public ContextScope(@NotNull CoroutineContext coroutineContext2) {
        this.coroutineContext = coroutineContext2;
    }

    @NotNull
    public CoroutineContext getCoroutineContext() {
        return this.coroutineContext;
    }

    @NotNull
    public String toString() {
        return "CoroutineScope(coroutineContext=" + getCoroutineContext() + ')';
    }
}

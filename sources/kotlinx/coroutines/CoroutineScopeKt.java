package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.ContextScope;
import org.jetbrains.annotations.NotNull;

/* compiled from: CoroutineScope.kt */
public final class CoroutineScopeKt {
    @NotNull
    public static final CoroutineScope CoroutineScope(@NotNull CoroutineContext coroutineContext) {
        if (coroutineContext.get(Job.Key) == null) {
            coroutineContext = coroutineContext.plus(JobKt__JobKt.Job$default((Job) null, 1, (Object) null));
        }
        return new ContextScope(coroutineContext);
    }
}

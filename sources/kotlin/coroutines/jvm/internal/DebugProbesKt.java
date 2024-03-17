package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;

/* compiled from: DebugProbes.kt */
public final class DebugProbesKt {
    @NotNull
    public static final <T> Continuation<T> probeCoroutineCreated(@NotNull Continuation<? super T> continuation) {
        return continuation;
    }

    public static final void probeCoroutineResumed(@NotNull Continuation<?> continuation) {
    }

    public static final void probeCoroutineSuspended(@NotNull Continuation<?> continuation) {
    }
}

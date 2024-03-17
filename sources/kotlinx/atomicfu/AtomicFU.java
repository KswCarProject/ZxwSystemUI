package kotlinx.atomicfu;

import kotlinx.atomicfu.TraceBase;
import org.jetbrains.annotations.NotNull;

/* compiled from: AtomicFU.kt */
public final class AtomicFU {
    @NotNull
    public static final <T> AtomicRef<T> atomic(T t, @NotNull TraceBase traceBase) {
        return new AtomicRef<>(t, traceBase);
    }

    @NotNull
    public static final <T> AtomicRef<T> atomic(T t) {
        return atomic(t, (TraceBase) TraceBase.None.INSTANCE);
    }

    @NotNull
    public static final AtomicInt atomic(int i, @NotNull TraceBase traceBase) {
        return new AtomicInt(i, traceBase);
    }

    @NotNull
    public static final AtomicInt atomic(int i) {
        return atomic(i, (TraceBase) TraceBase.None.INSTANCE);
    }

    @NotNull
    public static final AtomicLong atomic(long j, @NotNull TraceBase traceBase) {
        return new AtomicLong(j, traceBase);
    }

    @NotNull
    public static final AtomicLong atomic(long j) {
        return atomic(j, (TraceBase) TraceBase.None.INSTANCE);
    }

    @NotNull
    public static final AtomicBoolean atomic(boolean z, @NotNull TraceBase traceBase) {
        return new AtomicBoolean(z, traceBase);
    }

    @NotNull
    public static final AtomicBoolean atomic(boolean z) {
        return atomic(z, (TraceBase) TraceBase.None.INSTANCE);
    }
}

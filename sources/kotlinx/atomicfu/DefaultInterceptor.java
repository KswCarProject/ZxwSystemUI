package kotlinx.atomicfu;

import org.jetbrains.annotations.NotNull;

/* compiled from: Interceptor.kt */
public final class DefaultInterceptor extends AtomicOperationInterceptor {
    @NotNull
    public static final DefaultInterceptor INSTANCE = new DefaultInterceptor();

    @NotNull
    public String toString() {
        return "DefaultInterceptor";
    }
}

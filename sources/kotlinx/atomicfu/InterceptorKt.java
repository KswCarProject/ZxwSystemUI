package kotlinx.atomicfu;

import java.util.concurrent.locks.ReentrantLock;
import org.jetbrains.annotations.NotNull;

/* compiled from: Interceptor.kt */
public final class InterceptorKt {
    @NotNull
    public static AtomicOperationInterceptor interceptor = DefaultInterceptor.INSTANCE;
    @NotNull
    public static final ReentrantLock interceptorLock = new ReentrantLock();

    @NotNull
    public static final AtomicOperationInterceptor getInterceptor() {
        return interceptor;
    }
}

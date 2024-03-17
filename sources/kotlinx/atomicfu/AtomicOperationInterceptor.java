package kotlinx.atomicfu;

import org.jetbrains.annotations.NotNull;

/* compiled from: Interceptor.kt */
public class AtomicOperationInterceptor {
    public void afterRMW(@NotNull AtomicBoolean atomicBoolean, boolean z, boolean z2) {
    }

    public void afterRMW(@NotNull AtomicInt atomicInt, int i, int i2) {
    }

    public void afterRMW(@NotNull AtomicLong atomicLong, long j, long j2) {
    }

    public <T> void afterRMW(@NotNull AtomicRef<T> atomicRef, T t, T t2) {
    }

    public void afterSet(@NotNull AtomicBoolean atomicBoolean, boolean z) {
    }

    public void afterSet(@NotNull AtomicInt atomicInt, int i) {
    }

    public void afterSet(@NotNull AtomicLong atomicLong, long j) {
    }

    public <T> void afterSet(@NotNull AtomicRef<T> atomicRef, T t) {
    }

    public void beforeUpdate(@NotNull AtomicBoolean atomicBoolean) {
    }

    public void beforeUpdate(@NotNull AtomicInt atomicInt) {
    }

    public void beforeUpdate(@NotNull AtomicLong atomicLong) {
    }

    public <T> void beforeUpdate(@NotNull AtomicRef<T> atomicRef) {
    }
}

package kotlinx.atomicfu;

import org.jetbrains.annotations.NotNull;

/* compiled from: AtomicFU.common.kt */
public final class AtomicFU_commonKt {
    @NotNull
    public static final <T> AtomicArray<T> atomicArrayOfNulls(int i) {
        return new AtomicArray<>(i);
    }
}

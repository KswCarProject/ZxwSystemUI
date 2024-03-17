package kotlinx.coroutines.internal;

import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Atomic.kt */
public abstract class AtomicOp<T> extends OpDescriptor {
    @NotNull
    public final AtomicRef<Object> _consensus = AtomicFU.atomic(AtomicKt.NO_DECISION);

    public abstract void complete(T t, @Nullable Object obj);

    @NotNull
    public AtomicOp<?> getAtomicOp() {
        return this;
    }

    public long getOpSequence() {
        return 0;
    }

    @Nullable
    public abstract Object prepare(T t);

    @Nullable
    public final Object decide(@Nullable Object obj) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(obj != AtomicKt.NO_DECISION)) {
                throw new AssertionError();
            }
        }
        Object value = this._consensus.getValue();
        Object obj2 = AtomicKt.NO_DECISION;
        if (value != obj2) {
            return value;
        }
        if (this._consensus.compareAndSet(obj2, obj)) {
            return obj;
        }
        return this._consensus.getValue();
    }

    @Nullable
    public final Object perform(@Nullable Object obj) {
        Object value = this._consensus.getValue();
        if (value == AtomicKt.NO_DECISION) {
            value = decide(prepare(obj));
        }
        complete(obj, value);
        return value;
    }
}

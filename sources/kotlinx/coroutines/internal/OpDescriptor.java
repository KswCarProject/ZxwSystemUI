package kotlinx.coroutines.internal;

import kotlinx.coroutines.DebugStringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Atomic.kt */
public abstract class OpDescriptor {
    @Nullable
    public abstract AtomicOp<?> getAtomicOp();

    @Nullable
    public abstract Object perform(@Nullable Object obj);

    @NotNull
    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '@' + DebugStringsKt.getHexAddress(this);
    }

    public final boolean isEarlierThan(@NotNull OpDescriptor opDescriptor) {
        AtomicOp<?> atomicOp;
        AtomicOp<?> atomicOp2 = getAtomicOp();
        if (atomicOp2 == null || (atomicOp = opDescriptor.getAtomicOp()) == null || atomicOp2.getOpSequence() >= atomicOp.getOpSequence()) {
            return false;
        }
        return true;
    }
}

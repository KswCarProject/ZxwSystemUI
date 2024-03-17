package kotlinx.coroutines.internal;

import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockFreeTaskQueue.kt */
public class LockFreeTaskQueue<E> {
    @NotNull
    public final AtomicRef<LockFreeTaskQueueCore<E>> _cur;

    public LockFreeTaskQueue(boolean z) {
        this._cur = AtomicFU.atomic(new LockFreeTaskQueueCore(8, z));
    }

    public final int getSize() {
        return this._cur.getValue().getSize();
    }

    public final void close() {
        AtomicRef<LockFreeTaskQueueCore<E>> atomicRef = this._cur;
        while (true) {
            LockFreeTaskQueueCore value = atomicRef.getValue();
            if (!value.close()) {
                this._cur.compareAndSet(value, value.next());
            } else {
                return;
            }
        }
    }

    public final boolean addLast(@NotNull E e) {
        AtomicRef<LockFreeTaskQueueCore<E>> atomicRef = this._cur;
        while (true) {
            LockFreeTaskQueueCore value = atomicRef.getValue();
            int addLast = value.addLast(e);
            if (addLast == 0) {
                return true;
            }
            if (addLast == 1) {
                this._cur.compareAndSet(value, value.next());
            } else if (addLast == 2) {
                return false;
            }
        }
    }

    @Nullable
    public final E removeFirstOrNull() {
        AtomicRef<LockFreeTaskQueueCore<E>> atomicRef = this._cur;
        while (true) {
            LockFreeTaskQueueCore value = atomicRef.getValue();
            E removeFirstOrNull = value.removeFirstOrNull();
            if (removeFirstOrNull != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return removeFirstOrNull;
            }
            this._cur.compareAndSet(value, value.next());
        }
    }
}

package kotlinx.coroutines.internal;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.atomicfu.AtomicArray;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicFU_commonKt;
import kotlinx.atomicfu.AtomicLong;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockFreeTaskQueue.kt */
public final class LockFreeTaskQueueCore<E> {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    public static final Symbol REMOVE_FROZEN = new Symbol("REMOVE_FROZEN");
    @NotNull
    public final AtomicRef<LockFreeTaskQueueCore<E>> _next = AtomicFU.atomic(null);
    @NotNull
    public final AtomicLong _state = AtomicFU.atomic(0);
    @NotNull
    public final AtomicArray<Object> array;
    public final int capacity;
    public final int mask;
    public final boolean singleConsumer;

    public LockFreeTaskQueueCore(int i, boolean z) {
        this.capacity = i;
        this.singleConsumer = z;
        int i2 = i - 1;
        this.mask = i2;
        this.array = AtomicFU_commonKt.atomicArrayOfNulls(i);
        boolean z2 = false;
        if (i2 <= 1073741823) {
            if (!((i & i2) == 0 ? true : z2)) {
                throw new IllegalStateException("Check failed.".toString());
            }
            return;
        }
        throw new IllegalStateException("Check failed.".toString());
    }

    public final boolean isEmpty() {
        long value = this._state.getValue();
        return ((int) ((1073741823 & value) >> 0)) == ((int) ((value & 1152921503533105152L) >> 30));
    }

    public final int getSize() {
        long value = this._state.getValue();
        return 1073741823 & (((int) ((value & 1152921503533105152L) >> 30)) - ((int) ((1073741823 & value) >> 0)));
    }

    public final boolean close() {
        long value;
        AtomicLong atomicLong = this._state;
        do {
            value = atomicLong.getValue();
            if ((value & 2305843009213693952L) != 0) {
                return true;
            }
            if ((1152921504606846976L & value) != 0) {
                return false;
            }
        } while (!atomicLong.compareAndSet(value, 2305843009213693952L | value));
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0071 A[LOOP:1: B:20:0x0071->B:23:0x0087, LOOP_START, PHI: r14 
      PHI: (r14v3 'this' kotlinx.coroutines.internal.LockFreeTaskQueueCore) = (r14v0 'this' kotlinx.coroutines.internal.LockFreeTaskQueueCore A[THIS]), (r14v5 'this' kotlinx.coroutines.internal.LockFreeTaskQueueCore) binds: [B:19:0x0066, B:23:0x0087] A[DONT_GENERATE, DONT_INLINE]] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int addLast(@org.jetbrains.annotations.NotNull E r15) {
        /*
            r14 = this;
            kotlinx.atomicfu.AtomicLong r0 = r14._state
        L_0x0002:
            long r1 = r0.getValue()
            r3 = 3458764513820540928(0x3000000000000000, double:1.727233711018889E-77)
            long r3 = r3 & r1
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 == 0) goto L_0x0016
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r14 = Companion
            int r14 = r14.addFailReason(r1)
            return r14
        L_0x0016:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore$Companion r3 = Companion
            r7 = 1073741823(0x3fffffff, double:5.304989472E-315)
            long r7 = r7 & r1
            r4 = 0
            long r7 = r7 >> r4
            int r7 = (int) r7
            r8 = 1152921503533105152(0xfffffffc0000000, double:1.2882296003504729E-231)
            long r8 = r8 & r1
            r10 = 30
            long r8 = r8 >> r10
            int r8 = (int) r8
            int r9 = r14.mask
            int r10 = r8 + 2
            r10 = r10 & r9
            r11 = r7 & r9
            r12 = 1
            if (r10 != r11) goto L_0x0034
            return r12
        L_0x0034:
            boolean r10 = r14.singleConsumer
            r11 = 1073741823(0x3fffffff, float:1.9999999)
            if (r10 != 0) goto L_0x0057
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r10 = r14.array
            r13 = r8 & r9
            kotlinx.atomicfu.AtomicRef r10 = r10.get(r13)
            java.lang.Object r10 = r10.getValue()
            if (r10 == 0) goto L_0x0057
            int r1 = r14.capacity
            r2 = 1024(0x400, float:1.435E-42)
            if (r1 < r2) goto L_0x0056
            int r8 = r8 - r7
            r2 = r8 & r11
            int r1 = r1 >> 1
            if (r2 <= r1) goto L_0x0002
        L_0x0056:
            return r12
        L_0x0057:
            int r7 = r8 + 1
            r7 = r7 & r11
            kotlinx.atomicfu.AtomicLong r10 = r14._state
            long r11 = r3.updateTail(r1, r7)
            boolean r1 = r10.compareAndSet(r1, r11)
            if (r1 == 0) goto L_0x0002
            kotlinx.atomicfu.AtomicArray<java.lang.Object> r0 = r14.array
            r1 = r8 & r9
            kotlinx.atomicfu.AtomicRef r0 = r0.get(r1)
            r0.setValue(r15)
        L_0x0071:
            kotlinx.atomicfu.AtomicLong r0 = r14._state
            long r0 = r0.getValue()
            r2 = 1152921504606846976(0x1000000000000000, double:1.2882297539194267E-231)
            long r0 = r0 & r2
            int r0 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
            if (r0 != 0) goto L_0x007f
            goto L_0x0089
        L_0x007f:
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r14 = r14.next()
            kotlinx.coroutines.internal.LockFreeTaskQueueCore r14 = r14.fillPlaceholder(r8, r15)
            if (r14 != 0) goto L_0x0071
        L_0x0089:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeTaskQueueCore.addLast(java.lang.Object):int");
    }

    public final LockFreeTaskQueueCore<E> fillPlaceholder(int i, E e) {
        Object value = this.array.get(this.mask & i).getValue();
        if (!(value instanceof Placeholder) || ((Placeholder) value).index != i) {
            return null;
        }
        this.array.get(i & this.mask).setValue(e);
        return this;
    }

    @Nullable
    public final Object removeFirstOrNull() {
        AtomicLong atomicLong = this._state;
        while (true) {
            long value = atomicLong.getValue();
            if ((1152921504606846976L & value) != 0) {
                return REMOVE_FROZEN;
            }
            Companion companion = Companion;
            int i = (int) ((1073741823 & value) >> 0);
            int i2 = this.mask;
            if ((((int) ((1152921503533105152L & value) >> 30)) & i2) == (i & i2)) {
                return null;
            }
            Object value2 = this.array.get(i2 & i).getValue();
            if (value2 == null) {
                if (this.singleConsumer) {
                    return null;
                }
            } else if (value2 instanceof Placeholder) {
                return null;
            } else {
                int i3 = (i + 1) & 1073741823;
                if (this._state.compareAndSet(value, companion.updateHead(value, i3))) {
                    this.array.get(this.mask & i).setValue(null);
                    return value2;
                } else if (this.singleConsumer) {
                    do {
                        this = this.removeSlowPath(i, i3);
                    } while (this != null);
                    return value2;
                }
            }
        }
    }

    public final LockFreeTaskQueueCore<E> removeSlowPath(int i, int i2) {
        long value;
        Companion companion;
        int i3;
        AtomicLong atomicLong = this._state;
        do {
            value = atomicLong.getValue();
            companion = Companion;
            boolean z = false;
            i3 = (int) ((1073741823 & value) >> 0);
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (i3 == i) {
                    z = true;
                }
                if (!z) {
                    throw new AssertionError();
                }
            }
            if ((1152921504606846976L & value) != 0) {
                return next();
            }
        } while (!this._state.compareAndSet(value, companion.updateHead(value, i2)));
        this.array.get(this.mask & i3).setValue(null);
        return null;
    }

    @NotNull
    public final LockFreeTaskQueueCore<E> next() {
        return allocateOrGetNextCopy(markFrozen());
    }

    public final long markFrozen() {
        long value;
        long j;
        AtomicLong atomicLong = this._state;
        do {
            value = atomicLong.getValue();
            if ((value & 1152921504606846976L) != 0) {
                return value;
            }
            j = 1152921504606846976L | value;
        } while (!atomicLong.compareAndSet(value, j));
        return j;
    }

    public final LockFreeTaskQueueCore<E> allocateOrGetNextCopy(long j) {
        AtomicRef<LockFreeTaskQueueCore<E>> atomicRef = this._next;
        while (true) {
            LockFreeTaskQueueCore<E> value = atomicRef.getValue();
            if (value != null) {
                return value;
            }
            this._next.compareAndSet(null, allocateNextCopy(j));
        }
    }

    public final LockFreeTaskQueueCore<E> allocateNextCopy(long j) {
        LockFreeTaskQueueCore<E> lockFreeTaskQueueCore = new LockFreeTaskQueueCore<>(this.capacity * 2, this.singleConsumer);
        int i = (int) ((1073741823 & j) >> 0);
        int i2 = (int) ((1152921503533105152L & j) >> 30);
        while (true) {
            int i3 = this.mask;
            if ((i & i3) != (i2 & i3)) {
                Object value = this.array.get(i3 & i).getValue();
                if (value == null) {
                    value = new Placeholder(i);
                }
                lockFreeTaskQueueCore.array.get(lockFreeTaskQueueCore.mask & i).setValue(value);
                i++;
            } else {
                lockFreeTaskQueueCore._state.setValue(Companion.wo(j, 1152921504606846976L));
                return lockFreeTaskQueueCore;
            }
        }
    }

    /* compiled from: LockFreeTaskQueue.kt */
    public static final class Placeholder {
        public final int index;

        public Placeholder(int i) {
            this.index = i;
        }
    }

    /* compiled from: LockFreeTaskQueue.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int addFailReason(long j) {
            return (j & 2305843009213693952L) != 0 ? 2 : 1;
        }

        public final long wo(long j, long j2) {
            return j & (~j2);
        }

        public Companion() {
        }

        public final long updateHead(long j, int i) {
            return wo(j, 1073741823) | (((long) i) << 0);
        }

        public final long updateTail(long j, int i) {
            return wo(j, 1152921503533105152L) | (((long) i) << 30);
        }
    }
}

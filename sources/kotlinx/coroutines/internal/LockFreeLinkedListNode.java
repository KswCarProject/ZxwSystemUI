package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockFreeLinkedList.kt */
public class LockFreeLinkedListNode {
    @NotNull
    public final AtomicRef<Object> _next = AtomicFU.atomic(this);
    @NotNull
    public final AtomicRef<LockFreeLinkedListNode> _prev = AtomicFU.atomic(this);
    @NotNull
    public final AtomicRef<Removed> _removedRef = AtomicFU.atomic(null);

    public final Removed removed() {
        Removed value = this._removedRef.getValue();
        if (value != null) {
            return value;
        }
        Removed removed = new Removed(this);
        this._removedRef.lazySet(removed);
        return removed;
    }

    /* compiled from: LockFreeLinkedList.kt */
    public static abstract class CondAddOp extends AtomicOp<LockFreeLinkedListNode> {
        @NotNull
        public final LockFreeLinkedListNode newNode;
        @Nullable
        public LockFreeLinkedListNode oldNext;

        public CondAddOp(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode) {
            this.newNode = lockFreeLinkedListNode;
        }

        public void complete(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode, @Nullable Object obj) {
            boolean z = obj == null;
            LockFreeLinkedListNode lockFreeLinkedListNode2 = z ? this.newNode : this.oldNext;
            if (lockFreeLinkedListNode2 != null && lockFreeLinkedListNode._next.compareAndSet(this, lockFreeLinkedListNode2) && z) {
                LockFreeLinkedListNode lockFreeLinkedListNode3 = this.newNode;
                LockFreeLinkedListNode lockFreeLinkedListNode4 = this.oldNext;
                Intrinsics.checkNotNull(lockFreeLinkedListNode4);
                lockFreeLinkedListNode3.finishAdd(lockFreeLinkedListNode4);
            }
        }
    }

    public boolean isRemoved() {
        return getNext() instanceof Removed;
    }

    @NotNull
    public final Object getNext() {
        AtomicRef<Object> atomicRef = this._next;
        while (true) {
            Object value = atomicRef.getValue();
            if (!(value instanceof OpDescriptor)) {
                return value;
            }
            ((OpDescriptor) value).perform(this);
        }
    }

    @NotNull
    public final LockFreeLinkedListNode getNextNode() {
        return LockFreeLinkedListKt.unwrap(getNext());
    }

    @NotNull
    public final LockFreeLinkedListNode getPrevNode() {
        LockFreeLinkedListNode correctPrev = correctPrev((OpDescriptor) null);
        return correctPrev == null ? findPrevNonRemoved(this._prev.getValue()) : correctPrev;
    }

    public final LockFreeLinkedListNode findPrevNonRemoved(LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode._prev.getValue();
        }
        return lockFreeLinkedListNode;
    }

    public final boolean addOneIfEmpty(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode) {
        lockFreeLinkedListNode._prev.lazySet(this);
        lockFreeLinkedListNode._next.lazySet(this);
        while (getNext() == this) {
            if (this._next.compareAndSet(this, lockFreeLinkedListNode)) {
                lockFreeLinkedListNode.finishAdd(this);
                return true;
            }
        }
        return false;
    }

    public final int tryCondAddNext(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode, @NotNull LockFreeLinkedListNode lockFreeLinkedListNode2, @NotNull CondAddOp condAddOp) {
        lockFreeLinkedListNode._prev.lazySet(this);
        lockFreeLinkedListNode._next.lazySet(lockFreeLinkedListNode2);
        condAddOp.oldNext = lockFreeLinkedListNode2;
        if (!this._next.compareAndSet(lockFreeLinkedListNode2, condAddOp)) {
            return 0;
        }
        return condAddOp.perform(this) == null ? 1 : 2;
    }

    public boolean remove() {
        return removeOrNext() == null;
    }

    @Nullable
    public final LockFreeLinkedListNode removeOrNext() {
        Object next;
        LockFreeLinkedListNode lockFreeLinkedListNode;
        do {
            next = getNext();
            if (next instanceof Removed) {
                return ((Removed) next).ref;
            }
            if (next == this) {
                return (LockFreeLinkedListNode) next;
            }
            lockFreeLinkedListNode = (LockFreeLinkedListNode) next;
        } while (!this._next.compareAndSet(next, lockFreeLinkedListNode.removed()));
        lockFreeLinkedListNode.correctPrev((OpDescriptor) null);
        return null;
    }

    public final void finishAdd(LockFreeLinkedListNode lockFreeLinkedListNode) {
        LockFreeLinkedListNode value;
        AtomicRef<LockFreeLinkedListNode> atomicRef = lockFreeLinkedListNode._prev;
        do {
            value = atomicRef.getValue();
            if (getNext() != lockFreeLinkedListNode) {
                return;
            }
        } while (!lockFreeLinkedListNode._prev.compareAndSet(value, this));
        if (isRemoved()) {
            lockFreeLinkedListNode.correctPrev((OpDescriptor) null);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: kotlinx.coroutines.internal.OpDescriptor} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: kotlinx.coroutines.internal.LockFreeLinkedListNode} */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0050, code lost:
        if (r3._next.compareAndSet(r2, ((kotlinx.coroutines.internal.Removed) r4).ref) != false) goto L_0x0053;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final kotlinx.coroutines.internal.LockFreeLinkedListNode correctPrev(kotlinx.coroutines.internal.OpDescriptor r8) {
        /*
            r7 = this;
        L_0x0000:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r0 = r7._prev
            java.lang.Object r0 = r0.getValue()
            kotlinx.coroutines.internal.LockFreeLinkedListNode r0 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r0
            r1 = 0
            r2 = r0
        L_0x000a:
            r3 = r1
        L_0x000b:
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r4 = r2._next
            java.lang.Object r4 = r4.getValue()
            if (r4 != r7) goto L_0x0020
            if (r0 != r2) goto L_0x0016
            return r2
        L_0x0016:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r1 = r7._prev
            boolean r0 = r1.compareAndSet(r0, r2)
            if (r0 != 0) goto L_0x001f
            goto L_0x0000
        L_0x001f:
            return r2
        L_0x0020:
            boolean r5 = r7.isRemoved()
            if (r5 == 0) goto L_0x0027
            return r1
        L_0x0027:
            if (r4 != r8) goto L_0x002a
            return r2
        L_0x002a:
            boolean r5 = r4 instanceof kotlinx.coroutines.internal.OpDescriptor
            if (r5 == 0) goto L_0x0040
            if (r8 == 0) goto L_0x003a
            r0 = r4
            kotlinx.coroutines.internal.OpDescriptor r0 = (kotlinx.coroutines.internal.OpDescriptor) r0
            boolean r0 = r8.isEarlierThan(r0)
            if (r0 == 0) goto L_0x003a
            return r1
        L_0x003a:
            kotlinx.coroutines.internal.OpDescriptor r4 = (kotlinx.coroutines.internal.OpDescriptor) r4
            r4.perform(r2)
            goto L_0x0000
        L_0x0040:
            boolean r5 = r4 instanceof kotlinx.coroutines.internal.Removed
            if (r5 == 0) goto L_0x005e
            if (r3 == 0) goto L_0x0055
            kotlinx.atomicfu.AtomicRef<java.lang.Object> r5 = r3._next
            kotlinx.coroutines.internal.Removed r4 = (kotlinx.coroutines.internal.Removed) r4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r4 = r4.ref
            boolean r2 = r5.compareAndSet(r2, r4)
            if (r2 != 0) goto L_0x0053
            goto L_0x0000
        L_0x0053:
            r2 = r3
            goto L_0x000a
        L_0x0055:
            kotlinx.atomicfu.AtomicRef<kotlinx.coroutines.internal.LockFreeLinkedListNode> r2 = r2._prev
            java.lang.Object r2 = r2.getValue()
            kotlinx.coroutines.internal.LockFreeLinkedListNode r2 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r2
            goto L_0x000b
        L_0x005e:
            r3 = r4
            kotlinx.coroutines.internal.LockFreeLinkedListNode r3 = (kotlinx.coroutines.internal.LockFreeLinkedListNode) r3
            r6 = r3
            r3 = r2
            r2 = r6
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.LockFreeLinkedListNode.correctPrev(kotlinx.coroutines.internal.OpDescriptor):kotlinx.coroutines.internal.LockFreeLinkedListNode");
    }

    @NotNull
    public String toString() {
        return getClass().getSimpleName() + '@' + Integer.toHexString(System.identityHashCode(this));
    }
}

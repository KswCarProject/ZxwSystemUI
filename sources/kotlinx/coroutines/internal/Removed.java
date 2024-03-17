package kotlinx.coroutines.internal;

import org.jetbrains.annotations.NotNull;

/* compiled from: LockFreeLinkedList.kt */
public final class Removed {
    @NotNull
    public final LockFreeLinkedListNode ref;

    public Removed(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode) {
        this.ref = lockFreeLinkedListNode;
    }

    @NotNull
    public String toString() {
        return "Removed[" + this.ref + ']';
    }
}

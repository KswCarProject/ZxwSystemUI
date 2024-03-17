package kotlinx.coroutines.internal;

import org.jetbrains.annotations.NotNull;

/* compiled from: LockFreeLinkedList.kt */
public final class LockFreeLinkedListKt {
    @NotNull
    public static final Object CONDITION_FALSE = new Symbol("CONDITION_FALSE");
    @NotNull
    public static final Object LIST_EMPTY = new Symbol("LIST_EMPTY");

    @NotNull
    public static final Object getCONDITION_FALSE() {
        return CONDITION_FALSE;
    }

    @NotNull
    public static final LockFreeLinkedListNode unwrap(@NotNull Object obj) {
        LockFreeLinkedListNode lockFreeLinkedListNode = null;
        Removed removed = obj instanceof Removed ? (Removed) obj : null;
        if (removed != null) {
            lockFreeLinkedListNode = removed.ref;
        }
        return lockFreeLinkedListNode == null ? (LockFreeLinkedListNode) obj : lockFreeLinkedListNode;
    }
}

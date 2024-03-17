package kotlinx.coroutines;

import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: LockFreeLinkedList.kt */
public final class JobSupport$addLastAtomic$$inlined$addLastIf$1 extends LockFreeLinkedListNode.CondAddOp {
    public final /* synthetic */ Object $expect$inlined;
    public final /* synthetic */ LockFreeLinkedListNode $node;
    public final /* synthetic */ JobSupport this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public JobSupport$addLastAtomic$$inlined$addLastIf$1(LockFreeLinkedListNode lockFreeLinkedListNode, JobSupport jobSupport, Object obj) {
        super(lockFreeLinkedListNode);
        this.$node = lockFreeLinkedListNode;
        this.this$0 = jobSupport;
        this.$expect$inlined = obj;
    }

    @Nullable
    public Object prepare(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode) {
        if (this.this$0.getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() == this.$expect$inlined) {
            return null;
        }
        return LockFreeLinkedListKt.getCONDITION_FALSE();
    }
}

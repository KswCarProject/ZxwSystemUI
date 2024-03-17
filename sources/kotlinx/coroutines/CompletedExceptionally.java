package kotlinx.coroutines;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.atomicfu.AtomicBoolean;
import kotlinx.atomicfu.AtomicFU;
import org.jetbrains.annotations.NotNull;

/* compiled from: CompletionState.kt */
public class CompletedExceptionally {
    @NotNull
    public final AtomicBoolean _handled;
    @NotNull
    public final Throwable cause;

    public CompletedExceptionally(@NotNull Throwable th, boolean z) {
        this.cause = th;
        this._handled = AtomicFU.atomic(z);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ CompletedExceptionally(Throwable th, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(th, (i & 2) != 0 ? false : z);
    }

    public final boolean getHandled() {
        return this._handled.getValue();
    }

    public final boolean makeHandled() {
        return this._handled.compareAndSet(false, true);
    }

    @NotNull
    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '[' + this.cause + ']';
    }
}

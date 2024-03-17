package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public final class InvokeOnCancelling extends JobCancellingNode {
    @NotNull
    public final AtomicInt _invoked = AtomicFU.atomic(0);
    @NotNull
    public final Function1<Throwable, Unit> handler;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public InvokeOnCancelling(@NotNull Function1<? super Throwable, Unit> function1) {
        this.handler = function1;
    }

    public void invoke(@Nullable Throwable th) {
        if (this._invoked.compareAndSet(0, 1)) {
            this.handler.invoke(th);
        }
    }
}

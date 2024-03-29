package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public final class InvokeOnCompletion extends JobNode {
    @NotNull
    public final Function1<Throwable, Unit> handler;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public InvokeOnCompletion(@NotNull Function1<? super Throwable, Unit> function1) {
        this.handler = function1;
    }

    public void invoke(@Nullable Throwable th) {
        this.handler.invoke(th);
    }
}

package kotlinx.coroutines;

import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public final class ChildContinuation extends JobCancellingNode {
    @NotNull
    public final CancellableContinuationImpl<?> child;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public ChildContinuation(@NotNull CancellableContinuationImpl<?> cancellableContinuationImpl) {
        this.child = cancellableContinuationImpl;
    }

    public void invoke(@Nullable Throwable th) {
        CancellableContinuationImpl<?> cancellableContinuationImpl = this.child;
        cancellableContinuationImpl.parentCancelled$external__kotlinx_coroutines__android_common__kotlinx_coroutines(cancellableContinuationImpl.getContinuationCancellationCause(getJob()));
    }
}

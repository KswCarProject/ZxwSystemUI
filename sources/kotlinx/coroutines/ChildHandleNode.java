package kotlinx.coroutines;

import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public final class ChildHandleNode extends JobCancellingNode implements ChildHandle {
    @NotNull
    public final ChildJob childJob;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public ChildHandleNode(@NotNull ChildJob childJob2) {
        this.childJob = childJob2;
    }

    public void invoke(@Nullable Throwable th) {
        this.childJob.parentCancelled(getJob());
    }

    public boolean childCancelled(@NotNull Throwable th) {
        return getJob().childCancelled(th);
    }
}

package kotlinx.coroutines;

import kotlin.Result;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: JobSupport.kt */
public final class ResumeOnCompletion extends JobNode {
    @NotNull
    public final Continuation<Unit> continuation;

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((Throwable) obj);
        return Unit.INSTANCE;
    }

    public ResumeOnCompletion(@NotNull Continuation<? super Unit> continuation2) {
        this.continuation = continuation2;
    }

    public void invoke(@Nullable Throwable th) {
        Continuation<Unit> continuation2 = this.continuation;
        Result.Companion companion = Result.Companion;
        continuation2.resumeWith(Result.m4641constructorimpl(Unit.INSTANCE));
    }
}

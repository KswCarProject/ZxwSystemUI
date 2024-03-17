package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CompletionState.kt */
public final class CompletionStateKt {
    public static /* synthetic */ Object toState$default(Object obj, Function1 function1, int i, Object obj2) {
        if ((i & 1) != 0) {
            function1 = null;
        }
        return toState(obj, (Function1<? super Throwable, Unit>) function1);
    }

    @Nullable
    public static final <T> Object toState(@NotNull Object obj, @Nullable Function1<? super Throwable, Unit> function1) {
        Throwable r0 = Result.m4643exceptionOrNullimpl(obj);
        if (r0 != null) {
            return new CompletedExceptionally(r0, false, 2, (DefaultConstructorMarker) null);
        }
        if (function1 != null) {
            return new CompletedWithCancellation(obj, function1);
        }
        return obj;
    }

    @Nullable
    public static final <T> Object toState(@NotNull Object obj, @NotNull CancellableContinuation<?> cancellableContinuation) {
        Throwable r0 = Result.m4643exceptionOrNullimpl(obj);
        if (r0 != null) {
            if (DebugKt.getRECOVER_STACK_TRACES()) {
                Continuation continuation = cancellableContinuation;
                if (continuation instanceof CoroutineStackFrame) {
                    r0 = StackTraceRecoveryKt.recoverFromStackFrame(r0, (CoroutineStackFrame) continuation);
                }
            }
            obj = new CompletedExceptionally(r0, false, 2, (DefaultConstructorMarker) null);
        }
        return obj;
    }

    @NotNull
    public static final <T> Object recoverResult(@Nullable Object obj, @NotNull Continuation<? super T> continuation) {
        if (obj instanceof CompletedExceptionally) {
            Result.Companion companion = Result.Companion;
            Throwable th = ((CompletedExceptionally) obj).cause;
            if (DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof CoroutineStackFrame)) {
                th = StackTraceRecoveryKt.recoverFromStackFrame(th, (CoroutineStackFrame) continuation);
            }
            return Result.m4641constructorimpl(ResultKt.createFailure(th));
        }
        Result.Companion companion2 = Result.Companion;
        return Result.m4641constructorimpl(obj);
    }
}

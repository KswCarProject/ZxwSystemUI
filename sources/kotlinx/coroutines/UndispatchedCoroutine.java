package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.ScopeCoroutine;
import kotlinx.coroutines.internal.ThreadContextKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CoroutineContext.kt */
public final class UndispatchedCoroutine<T> extends ScopeCoroutine<T> {
    @Nullable
    public CoroutineContext savedContext;
    @Nullable
    public Object savedOldValue;

    public final void saveThreadContext(@NotNull CoroutineContext coroutineContext, @Nullable Object obj) {
        this.savedContext = coroutineContext;
        this.savedOldValue = obj;
    }

    public final boolean clearThreadContext() {
        if (this.savedContext == null) {
            return false;
        }
        this.savedContext = null;
        this.savedOldValue = null;
        return true;
    }

    public void afterResume(@Nullable Object obj) {
        CoroutineContext coroutineContext = this.savedContext;
        UndispatchedCoroutine<?> undispatchedCoroutine = null;
        if (coroutineContext != null) {
            ThreadContextKt.restoreThreadContext(coroutineContext, this.savedOldValue);
            this.savedContext = undispatchedCoroutine;
            this.savedOldValue = undispatchedCoroutine;
        }
        Object recoverResult = CompletionStateKt.recoverResult(obj, this.uCont);
        Continuation<T> continuation = this.uCont;
        CoroutineContext context = continuation.getContext();
        Object updateThreadContext = ThreadContextKt.updateThreadContext(context, undispatchedCoroutine);
        if (updateThreadContext != ThreadContextKt.NO_THREAD_ELEMENTS) {
            undispatchedCoroutine = CoroutineContextKt.updateUndispatchedCompletion(continuation, context, updateThreadContext);
        }
        try {
            this.uCont.resumeWith(recoverResult);
            Unit unit = Unit.INSTANCE;
        } finally {
            if (undispatchedCoroutine == null || undispatchedCoroutine.clearThreadContext()) {
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            }
        }
    }
}

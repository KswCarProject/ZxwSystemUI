package kotlinx.coroutines;

import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.ThreadContextKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: DispatchedTask.kt */
public final class DispatchedTaskKt {
    public static final boolean isCancellableMode(int i) {
        return i == 1 || i == 2;
    }

    public static final boolean isReusableMode(int i) {
        return i == 2;
    }

    public static final <T> void dispatch(@NotNull DispatchedTask<? super T> dispatchedTask, int i) {
        boolean z = true;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(i != -1)) {
                throw new AssertionError();
            }
        }
        Continuation<? super T> delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines = dispatchedTask.getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (i != 4) {
            z = false;
        }
        if (z || !(delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof DispatchedContinuation) || isCancellableMode(i) != isCancellableMode(dispatchedTask.resumeMode)) {
            resume(dispatchedTask, delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines, z);
            return;
        }
        CoroutineDispatcher coroutineDispatcher = ((DispatchedContinuation) delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines).dispatcher;
        CoroutineContext context = delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines.getContext();
        if (coroutineDispatcher.isDispatchNeeded(context)) {
            coroutineDispatcher.dispatch(context, dispatchedTask);
        } else {
            resumeUnconfined(dispatchedTask);
        }
    }

    public static final <T> void resume(@NotNull DispatchedTask<? super T> dispatchedTask, @NotNull Continuation<? super T> continuation, boolean z) {
        Object obj;
        UndispatchedCoroutine<?> undispatchedCoroutine;
        Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines = dispatchedTask.takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        Throwable exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines = dispatchedTask.getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        if (exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines != null) {
            Result.Companion companion = Result.Companion;
            obj = ResultKt.createFailure(exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        } else {
            Result.Companion companion2 = Result.Companion;
            obj = dispatchedTask.getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        }
        Object r3 = Result.m4641constructorimpl(obj);
        if (z) {
            DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
            Continuation<T> continuation2 = dispatchedContinuation.continuation;
            Object obj2 = dispatchedContinuation.countOrElement;
            CoroutineContext context = continuation2.getContext();
            Object updateThreadContext = ThreadContextKt.updateThreadContext(context, obj2);
            if (updateThreadContext != ThreadContextKt.NO_THREAD_ELEMENTS) {
                undispatchedCoroutine = CoroutineContextKt.updateUndispatchedCompletion(continuation2, context, updateThreadContext);
            } else {
                undispatchedCoroutine = null;
            }
            try {
                dispatchedContinuation.continuation.resumeWith(r3);
                Unit unit = Unit.INSTANCE;
            } finally {
                if (undispatchedCoroutine == null || undispatchedCoroutine.clearThreadContext()) {
                    ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                }
            }
        } else {
            continuation.resumeWith(r3);
        }
    }

    public static final void resumeUnconfined(DispatchedTask<?> dispatchedTask) {
        EventLoop eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines = ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.isUnconfinedLoopActive()) {
            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.dispatchUnconfined(dispatchedTask);
            return;
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.incrementUseCount(true);
        try {
            resume(dispatchedTask, dispatchedTask.getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines(), true);
            do {
            } while (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.processUnconfinedEvent());
        } catch (Throwable th) {
            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
            throw th;
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
    }
}

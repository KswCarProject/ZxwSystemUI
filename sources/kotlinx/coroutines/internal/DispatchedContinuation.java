package kotlinx.coroutines.internal;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CompletedWithCancellation;
import kotlinx.coroutines.CompletionStateKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.DebugStringsKt;
import kotlinx.coroutines.DispatchedTask;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.ThreadLocalEventLoop;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DispatchedContinuation.kt */
public final class DispatchedContinuation<T> extends DispatchedTask<T> implements CoroutineStackFrame, Continuation<T> {
    @NotNull
    public final AtomicRef<Object> _reusableCancellableContinuation = AtomicFU.atomic(null);
    @Nullable
    public Object _state = DispatchedContinuationKt.UNDEFINED;
    @NotNull
    public final Continuation<T> continuation;
    @NotNull
    public final Object countOrElement = ThreadContextKt.threadContextElements(getContext());
    @NotNull
    public final CoroutineDispatcher dispatcher;

    @NotNull
    public CoroutineContext getContext() {
        return this.continuation.getContext();
    }

    @NotNull
    public Continuation<T> getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this;
    }

    @Nullable
    public StackTraceElement getStackTraceElement() {
        return null;
    }

    public DispatchedContinuation(@NotNull CoroutineDispatcher coroutineDispatcher, @NotNull Continuation<? super T> continuation2) {
        super(-1);
        this.dispatcher = coroutineDispatcher;
        this.continuation = continuation2;
    }

    @Nullable
    public CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation2 = this.continuation;
        if (continuation2 instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation2;
        }
        return null;
    }

    public final CancellableContinuationImpl<?> getReusableCancellableContinuation() {
        Object value = this._reusableCancellableContinuation.getValue();
        if (value instanceof CancellableContinuationImpl) {
            return (CancellableContinuationImpl) value;
        }
        return null;
    }

    public final boolean isReusable() {
        return this._reusableCancellableContinuation.getValue() != null;
    }

    public final void awaitReusability() {
        do {
        } while (this._reusableCancellableContinuation.getValue() == DispatchedContinuationKt.REUSABLE_CLAIMED);
    }

    public final void release() {
        awaitReusability();
        CancellableContinuationImpl<?> reusableCancellableContinuation = getReusableCancellableContinuation();
        if (reusableCancellableContinuation != null) {
            reusableCancellableContinuation.detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
    }

    @Nullable
    public final Throwable tryReleaseClaimedContinuation(@NotNull CancellableContinuation<?> cancellableContinuation) {
        Symbol symbol;
        AtomicRef<Object> atomicRef = this._reusableCancellableContinuation;
        do {
            Object value = atomicRef.getValue();
            symbol = DispatchedContinuationKt.REUSABLE_CLAIMED;
            if (value != symbol) {
                if (!(value instanceof Throwable)) {
                    throw new IllegalStateException(Intrinsics.stringPlus("Inconsistent state ", value).toString());
                } else if (this._reusableCancellableContinuation.compareAndSet(value, null)) {
                    return (Throwable) value;
                } else {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
            }
        } while (!this._reusableCancellableContinuation.compareAndSet(symbol, cancellableContinuation));
        return null;
    }

    public final boolean postponeCancellation(@NotNull Throwable th) {
        AtomicRef<Object> atomicRef = this._reusableCancellableContinuation;
        while (true) {
            Object value = atomicRef.getValue();
            Symbol symbol = DispatchedContinuationKt.REUSABLE_CLAIMED;
            if (Intrinsics.areEqual(value, (Object) symbol)) {
                if (this._reusableCancellableContinuation.compareAndSet(symbol, th)) {
                    return true;
                }
            } else if (value instanceof Throwable) {
                return true;
            } else {
                if (this._reusableCancellableContinuation.compareAndSet(value, null)) {
                    return false;
                }
            }
        }
    }

    @Nullable
    public Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        Object obj = this._state;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(obj != DispatchedContinuationKt.UNDEFINED)) {
                throw new AssertionError();
            }
        }
        this._state = DispatchedContinuationKt.UNDEFINED;
        return obj;
    }

    public void resumeWith(@NotNull Object obj) {
        CoroutineContext context;
        Object updateThreadContext;
        CoroutineContext context2 = this.continuation.getContext();
        Object state$default = CompletionStateKt.toState$default(obj, (Function1) null, 1, (Object) null);
        if (this.dispatcher.isDispatchNeeded(context2)) {
            this._state = state$default;
            this.resumeMode = 0;
            this.dispatcher.dispatch(context2, this);
            return;
        }
        boolean assertions_enabled = DebugKt.getASSERTIONS_ENABLED();
        EventLoop eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines = ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.isUnconfinedLoopActive()) {
            this._state = state$default;
            this.resumeMode = 0;
            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.dispatchUnconfined(this);
            return;
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.incrementUseCount(true);
        try {
            context = getContext();
            updateThreadContext = ThreadContextKt.updateThreadContext(context, this.countOrElement);
            this.continuation.resumeWith(obj);
            Unit unit = Unit.INSTANCE;
            ThreadContextKt.restoreThreadContext(context, updateThreadContext);
            do {
            } while (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.processUnconfinedEvent());
        } catch (Throwable th) {
            try {
                handleFatalException(th, (Throwable) null);
            } catch (Throwable th2) {
                eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
                throw th2;
            }
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
    }

    public void cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj, @NotNull Throwable th) {
        if (obj instanceof CompletedWithCancellation) {
            ((CompletedWithCancellation) obj).onCancellation.invoke(th);
        }
    }

    public final void dispatchYield$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@NotNull CoroutineContext coroutineContext, T t) {
        this._state = t;
        this.resumeMode = 1;
        this.dispatcher.dispatchYield(coroutineContext, this);
    }

    @NotNull
    public String toString() {
        return "DispatchedContinuation[" + this.dispatcher + ", " + DebugStringsKt.toDebugString(this.continuation) + ']';
    }
}

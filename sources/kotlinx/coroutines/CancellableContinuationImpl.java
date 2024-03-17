package kotlinx.coroutines;

import kotlin.KotlinNothingValueException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.CoroutineStackFrame;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: CancellableContinuationImpl.kt */
public class CancellableContinuationImpl<T> extends DispatchedTask<T> implements CancellableContinuation<T>, CoroutineStackFrame {
    @NotNull
    public final AtomicInt _decision;
    @NotNull
    public final AtomicRef<Object> _state;
    @NotNull
    public final CoroutineContext context;
    @NotNull
    public final Continuation<T> delegate;
    @Nullable
    public DisposableHandle parentHandle;

    @Nullable
    public StackTraceElement getStackTraceElement() {
        return null;
    }

    @NotNull
    public String nameString() {
        return "CancellableContinuation";
    }

    public final void callCancelHandler(Function1<? super Throwable, Unit> function1, Throwable th) {
        try {
            function1.invoke(th);
        } catch (Throwable th2) {
            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException(Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this), th2));
        }
    }

    @NotNull
    public final Continuation<T> getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this.delegate;
    }

    public CancellableContinuationImpl(@NotNull Continuation<? super T> continuation, int i) {
        super(i);
        this.delegate = continuation;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(i != -1)) {
                throw new AssertionError();
            }
        }
        this.context = continuation.getContext();
        this._decision = AtomicFU.atomic(0);
        this._state = AtomicFU.atomic(Active.INSTANCE);
    }

    @NotNull
    public CoroutineContext getContext() {
        return this.context;
    }

    @Nullable
    public final Object getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return this._state.getValue();
    }

    public boolean isCompleted() {
        return !(getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() instanceof NotCompleted);
    }

    public final String getStateDebugRepresentation() {
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof NotCompleted) {
            return "Active";
        }
        return state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CancelledContinuation ? "Cancelled" : "Completed";
    }

    public void initCancellability() {
        DisposableHandle installParentHandle = installParentHandle();
        if (installParentHandle != null && isCompleted()) {
            installParentHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    public final boolean isReusable() {
        return DispatchedTaskKt.isReusableMode(this.resumeMode) && ((DispatchedContinuation) this.delegate).isReusable();
    }

    @Nullable
    public CoroutineStackFrame getCallerFrame() {
        Continuation<T> continuation = this.delegate;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Nullable
    public Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        return getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
    }

    public void cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj, @NotNull Throwable th) {
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Object value = atomicRef.getValue();
            if (value instanceof NotCompleted) {
                throw new IllegalStateException("Not completed".toString());
            } else if (!(value instanceof CompletedExceptionally)) {
                if (value instanceof CompletedContinuation) {
                    CompletedContinuation completedContinuation = (CompletedContinuation) value;
                    if (!completedContinuation.getCancelled()) {
                        Throwable th2 = th;
                        if (this._state.compareAndSet(value, CompletedContinuation.copy$default(completedContinuation, (Object) null, (CancelHandler) null, (Function1) null, (Object) null, th, 15, (Object) null))) {
                            completedContinuation.invokeHandlers(this, th2);
                            return;
                        }
                    } else {
                        throw new IllegalStateException("Must be called at most once".toString());
                    }
                } else {
                    Throwable th3 = th;
                    if (this._state.compareAndSet(value, new CompletedContinuation(value, (CancelHandler) null, (Function1) null, (Object) null, th, 14, (DefaultConstructorMarker) null))) {
                        return;
                    }
                }
            } else {
                return;
            }
        }
    }

    public final boolean cancelLater(Throwable th) {
        if (!isReusable()) {
            return false;
        }
        return ((DispatchedContinuation) this.delegate).postponeCancellation(th);
    }

    public boolean cancel(@Nullable Throwable th) {
        Object value;
        boolean z;
        AtomicRef<Object> atomicRef = this._state;
        do {
            value = atomicRef.getValue();
            if (!(value instanceof NotCompleted)) {
                return false;
            }
            z = value instanceof CancelHandler;
        } while (!this._state.compareAndSet(value, new CancelledContinuation(this, th, z)));
        CancelHandler cancelHandler = z ? (CancelHandler) value : null;
        if (cancelHandler != null) {
            callCancelHandler(cancelHandler, th);
        }
        detachChildIfNonResuable();
        dispatchResume(this.resumeMode);
        return true;
    }

    public final void parentCancelled$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@NotNull Throwable th) {
        if (!cancelLater(th)) {
            cancel(th);
            detachChildIfNonResuable();
        }
    }

    public final void callCancelHandler(@NotNull CancelHandler cancelHandler, @Nullable Throwable th) {
        try {
            cancelHandler.invoke(th);
        } catch (Throwable th2) {
            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException(Intrinsics.stringPlus("Exception in invokeOnCancellation handler for ", this), th2));
        }
    }

    public final void callOnCancellation(@NotNull Function1<? super Throwable, Unit> function1, @NotNull Throwable th) {
        try {
            function1.invoke(th);
        } catch (Throwable th2) {
            CoroutineExceptionHandlerKt.handleCoroutineException(getContext(), new CompletionHandlerException(Intrinsics.stringPlus("Exception in resume onCancellation handler for ", this), th2));
        }
    }

    @NotNull
    public Throwable getContinuationCancellationCause(@NotNull Job job) {
        return job.getCancellationException();
    }

    public final boolean trySuspend() {
        AtomicInt atomicInt = this._decision;
        do {
            int value = atomicInt.getValue();
            if (value != 0) {
                if (value == 2) {
                    return false;
                }
                throw new IllegalStateException("Already suspended".toString());
            }
        } while (!this._decision.compareAndSet(0, 1));
        return true;
    }

    public final boolean tryResume() {
        AtomicInt atomicInt = this._decision;
        do {
            int value = atomicInt.getValue();
            if (value != 0) {
                if (value == 1) {
                    return false;
                }
                throw new IllegalStateException("Already resumed".toString());
            }
        } while (!this._decision.compareAndSet(0, 2));
        return true;
    }

    @Nullable
    public final Object getResult() {
        Job job;
        boolean isReusable = isReusable();
        if (trySuspend()) {
            if (this.parentHandle == null) {
                installParentHandle();
            }
            if (isReusable) {
                releaseClaimedReusableContinuation();
            }
            return IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        }
        if (isReusable) {
            releaseClaimedReusableContinuation();
        }
        Object state$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (state$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CompletedExceptionally) {
            Throwable th = ((CompletedExceptionally) state$external__kotlinx_coroutines__android_common__kotlinx_coroutines).cause;
            if (DebugKt.getRECOVER_STACK_TRACES()) {
                th = StackTraceRecoveryKt.recoverFromStackFrame(th, this);
            }
            throw th;
        } else if (!DispatchedTaskKt.isCancellableMode(this.resumeMode) || (job = (Job) getContext().get(Job.Key)) == null || job.isActive()) {
            return getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        } else {
            Throwable cancellationException = job.getCancellationException();
            cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(state$external__kotlinx_coroutines__android_common__kotlinx_coroutines, cancellationException);
            if (DebugKt.getRECOVER_STACK_TRACES()) {
                cancellationException = StackTraceRecoveryKt.recoverFromStackFrame(cancellationException, this);
            }
            throw cancellationException;
        }
    }

    public final DisposableHandle installParentHandle() {
        Job job = (Job) getContext().get(Job.Key);
        if (job == null) {
            return null;
        }
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(job, true, false, new ChildContinuation(this), 2, (Object) null);
        this.parentHandle = invokeOnCompletion$default;
        return invokeOnCompletion$default;
    }

    public final void releaseClaimedReusableContinuation() {
        Continuation<T> continuation = this.delegate;
        Throwable th = null;
        DispatchedContinuation dispatchedContinuation = continuation instanceof DispatchedContinuation ? (DispatchedContinuation) continuation : null;
        if (dispatchedContinuation != null) {
            th = dispatchedContinuation.tryReleaseClaimedContinuation(this);
        }
        if (th != null) {
            detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
            cancel(th);
        }
    }

    public void resumeWith(@NotNull Object obj) {
        resumeImpl$default(this, CompletionStateKt.toState(obj, (CancellableContinuation<?>) this), this.resumeMode, (Function1) null, 4, (Object) null);
    }

    public void invokeOnCancellation(@NotNull Function1<? super Throwable, Unit> function1) {
        CancelHandler makeCancelHandler = makeCancelHandler(function1);
        AtomicRef<Object> atomicRef = this._state;
        while (true) {
            Object value = atomicRef.getValue();
            if (value instanceof Active) {
                if (this._state.compareAndSet(value, makeCancelHandler)) {
                    return;
                }
            } else if (value instanceof CancelHandler) {
                multipleHandlersError(function1, value);
            } else {
                boolean z = value instanceof CompletedExceptionally;
                if (z) {
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) value;
                    if (!completedExceptionally.makeHandled()) {
                        multipleHandlersError(function1, value);
                    }
                    if (value instanceof CancelledContinuation) {
                        Throwable th = null;
                        if (!z) {
                            completedExceptionally = null;
                        }
                        if (completedExceptionally != null) {
                            th = completedExceptionally.cause;
                        }
                        callCancelHandler(function1, th);
                        return;
                    }
                    return;
                } else if (value instanceof CompletedContinuation) {
                    CompletedContinuation completedContinuation = (CompletedContinuation) value;
                    if (completedContinuation.cancelHandler != null) {
                        multipleHandlersError(function1, value);
                    }
                    if (completedContinuation.getCancelled()) {
                        callCancelHandler(function1, completedContinuation.cancelCause);
                        return;
                    }
                    if (this._state.compareAndSet(value, CompletedContinuation.copy$default(completedContinuation, (Object) null, makeCancelHandler, (Function1) null, (Object) null, (Throwable) null, 29, (Object) null))) {
                        return;
                    }
                } else {
                    if (this._state.compareAndSet(value, new CompletedContinuation(value, makeCancelHandler, (Function1) null, (Object) null, (Throwable) null, 28, (DefaultConstructorMarker) null))) {
                        return;
                    }
                }
            }
        }
    }

    public final void multipleHandlersError(Function1<? super Throwable, Unit> function1, Object obj) {
        throw new IllegalStateException(("It's prohibited to register multiple handlers, tried to register " + function1 + ", already has " + obj).toString());
    }

    public final CancelHandler makeCancelHandler(Function1<? super Throwable, Unit> function1) {
        return function1 instanceof CancelHandler ? (CancelHandler) function1 : new InvokeOnCancel(function1);
    }

    public final void dispatchResume(int i) {
        if (!tryResume()) {
            DispatchedTaskKt.dispatch(this, i);
        }
    }

    public final Object resumedState(NotCompleted notCompleted, Object obj, int i, Function1<? super Throwable, Unit> function1, Object obj2) {
        if (obj instanceof CompletedExceptionally) {
            boolean z = true;
            if (DebugKt.getASSERTIONS_ENABLED()) {
                if (!(obj2 == null)) {
                    throw new AssertionError();
                }
            }
            if (!DebugKt.getASSERTIONS_ENABLED()) {
                return obj;
            }
            if (function1 != null) {
                z = false;
            }
            if (z) {
                return obj;
            }
            throw new AssertionError();
        } else if (!DispatchedTaskKt.isCancellableMode(i) && obj2 == null) {
            return obj;
        } else {
            if (function1 == null && ((!(notCompleted instanceof CancelHandler) || (notCompleted instanceof BeforeResumeCancelHandler)) && obj2 == null)) {
                return obj;
            }
            return new CompletedContinuation(obj, notCompleted instanceof CancelHandler ? (CancelHandler) notCompleted : null, function1, obj2, (Throwable) null, 16, (DefaultConstructorMarker) null);
        }
    }

    public static /* synthetic */ void resumeImpl$default(CancellableContinuationImpl cancellableContinuationImpl, Object obj, int i, Function1 function1, int i2, Object obj2) {
        if (obj2 == null) {
            if ((i2 & 4) != 0) {
                function1 = null;
            }
            cancellableContinuationImpl.resumeImpl(obj, i, function1);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: resumeImpl");
    }

    public final void resumeImpl(Object obj, int i, Function1<? super Throwable, Unit> function1) {
        Object value;
        AtomicRef<Object> atomicRef = this._state;
        do {
            value = atomicRef.getValue();
            if (value instanceof NotCompleted) {
            } else {
                if (value instanceof CancelledContinuation) {
                    CancelledContinuation cancelledContinuation = (CancelledContinuation) value;
                    if (cancelledContinuation.makeResumed()) {
                        if (function1 != null) {
                            callOnCancellation(function1, cancelledContinuation.cause);
                            return;
                        }
                        return;
                    }
                }
                alreadyResumedError(obj);
                throw new KotlinNothingValueException();
            }
        } while (!this._state.compareAndSet(value, resumedState((NotCompleted) value, obj, i, function1, (Object) null)));
        detachChildIfNonResuable();
        dispatchResume(i);
    }

    public final Void alreadyResumedError(Object obj) {
        throw new IllegalStateException(Intrinsics.stringPlus("Already resumed, but proposed with update ", obj).toString());
    }

    public final void detachChildIfNonResuable() {
        if (!isReusable()) {
            detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
    }

    public final void detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        DisposableHandle disposableHandle = this.parentHandle;
        if (disposableHandle != null) {
            disposableHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
    }

    public <T> T getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj) {
        return obj instanceof CompletedContinuation ? ((CompletedContinuation) obj).result : obj;
    }

    @Nullable
    public Throwable getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj) {
        Throwable exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines = super.getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(obj);
        if (exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines == null) {
            return null;
        }
        Continuation delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines = getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (DebugKt.getRECOVER_STACK_TRACES() && (delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines instanceof CoroutineStackFrame)) {
            exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines = StackTraceRecoveryKt.recoverFromStackFrame(exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines, (CoroutineStackFrame) delegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        }
        return exceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines;
    }

    @NotNull
    public String toString() {
        return nameString() + '(' + DebugStringsKt.toDebugString(this.delegate) + "){" + getStateDebugRepresentation() + "}@" + DebugStringsKt.getHexAddress(this);
    }
}

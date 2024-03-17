package kotlinx.coroutines;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: AbstractCoroutine.kt */
public abstract class AbstractCoroutine<T> extends JobSupport implements Continuation<T>, CoroutineScope {
    @NotNull
    public final CoroutineContext context;

    public void onCancelled(@NotNull Throwable th, boolean z) {
    }

    public void onCompleted(T t) {
    }

    public AbstractCoroutine(@NotNull CoroutineContext coroutineContext, boolean z, boolean z2) {
        super(z2);
        if (z) {
            initParentJob((Job) coroutineContext.get(Job.Key));
        }
        this.context = coroutineContext.plus(this);
    }

    @NotNull
    public final CoroutineContext getContext() {
        return this.context;
    }

    @NotNull
    public CoroutineContext getCoroutineContext() {
        return this.context;
    }

    public boolean isActive() {
        return super.isActive();
    }

    @NotNull
    public String cancellationExceptionMessage() {
        return Intrinsics.stringPlus(DebugStringsKt.getClassSimpleName(this), " was cancelled");
    }

    public final void onCompletionInternal(@Nullable Object obj) {
        if (obj instanceof CompletedExceptionally) {
            CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
            onCancelled(completedExceptionally.cause, completedExceptionally.getHandled());
            return;
        }
        onCompleted(obj);
    }

    public final void resumeWith(@NotNull Object obj) {
        Object makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines = makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines(CompletionStateKt.toState$default(obj, (Function1) null, 1, (Object) null));
        if (makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines != JobSupportKt.COMPLETING_WAITING_CHILDREN) {
            afterResume(makeCompletingOnce$external__kotlinx_coroutines__android_common__kotlinx_coroutines);
        }
    }

    public void afterResume(@Nullable Object obj) {
        afterCompletion(obj);
    }

    public final void handleOnCompletionException$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@NotNull Throwable th) {
        CoroutineExceptionHandlerKt.handleCoroutineException(this.context, th);
    }

    @NotNull
    public String nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines() {
        String coroutineName = CoroutineContextKt.getCoroutineName(this.context);
        if (coroutineName == null) {
            return super.nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
        return '\"' + coroutineName + "\":" + super.nameString$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
    }

    public final <R> void start(@NotNull CoroutineStart coroutineStart, R r, @NotNull Function2<? super R, ? super Continuation<? super T>, ? extends Object> function2) {
        coroutineStart.invoke(function2, r, this);
    }
}

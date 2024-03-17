package kotlinx.coroutines;

import kotlin.ExceptionsKt__ExceptionsKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.scheduling.Task;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: DispatchedTask.kt */
public abstract class DispatchedTask<T> extends Task {
    public int resumeMode;

    public void cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj, @NotNull Throwable th) {
    }

    @NotNull
    public abstract Continuation<T> getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines();

    public <T> T getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj) {
        return obj;
    }

    @Nullable
    public abstract Object takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines();

    public DispatchedTask(int i) {
        this.resumeMode = i;
    }

    public final void handleFatalException(@Nullable Throwable th, @Nullable Throwable th2) {
        if (th != null || th2 != null) {
            if (!(th == null || th2 == null)) {
                ExceptionsKt__ExceptionsKt.addSuppressed(th, th2);
            }
            if (th == null) {
                th = th2;
            }
            Intrinsics.checkNotNull(th);
            CoroutineExceptionHandlerKt.handleCoroutineException(getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines().getContext(), new CoroutinesInternalError("Fatal exception in coroutines machinery for " + this + ". Please read KDoc to 'handleFatalException' method and report this incident to maintainers", th));
        }
    }

    @Nullable
    public Throwable getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(@Nullable Object obj) {
        CompletedExceptionally completedExceptionally = obj instanceof CompletedExceptionally ? (CompletedExceptionally) obj : null;
        if (completedExceptionally == null) {
            return null;
        }
        return completedExceptionally.cause;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00aa, code lost:
        if (r4.clearThreadContext() != false) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00d3, code lost:
        if (r4.clearThreadContext() != false) goto L_0x00d5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void run() {
        /*
            r10 = this;
            boolean r0 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            if (r0 == 0) goto L_0x0017
            int r0 = r10.resumeMode
            r1 = -1
            if (r0 == r1) goto L_0x000d
            r0 = 1
            goto L_0x000e
        L_0x000d:
            r0 = 0
        L_0x000e:
            if (r0 == 0) goto L_0x0011
            goto L_0x0017
        L_0x0011:
            java.lang.AssertionError r10 = new java.lang.AssertionError
            r10.<init>()
            throw r10
        L_0x0017:
            kotlinx.coroutines.scheduling.TaskContext r0 = r10.taskContext
            kotlin.coroutines.Continuation r1 = r10.getDelegate$external__kotlinx_coroutines__android_common__kotlinx_coroutines()     // Catch:{ all -> 0x00d9 }
            kotlinx.coroutines.internal.DispatchedContinuation r1 = (kotlinx.coroutines.internal.DispatchedContinuation) r1     // Catch:{ all -> 0x00d9 }
            kotlin.coroutines.Continuation<T> r2 = r1.continuation     // Catch:{ all -> 0x00d9 }
            java.lang.Object r1 = r1.countOrElement     // Catch:{ all -> 0x00d9 }
            kotlin.coroutines.CoroutineContext r3 = r2.getContext()     // Catch:{ all -> 0x00d9 }
            java.lang.Object r1 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r3, r1)     // Catch:{ all -> 0x00d9 }
            kotlinx.coroutines.internal.Symbol r4 = kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS     // Catch:{ all -> 0x00d9 }
            r5 = 0
            if (r1 == r4) goto L_0x0035
            kotlinx.coroutines.UndispatchedCoroutine r4 = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(r2, r3, r1)     // Catch:{ all -> 0x00d9 }
            goto L_0x0036
        L_0x0035:
            r4 = r5
        L_0x0036:
            kotlin.coroutines.CoroutineContext r6 = r2.getContext()     // Catch:{ all -> 0x00cc }
            java.lang.Object r7 = r10.takeState$external__kotlinx_coroutines__android_common__kotlinx_coroutines()     // Catch:{ all -> 0x00cc }
            java.lang.Throwable r8 = r10.getExceptionalResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r7)     // Catch:{ all -> 0x00cc }
            if (r8 != 0) goto L_0x0055
            int r9 = r10.resumeMode     // Catch:{ all -> 0x00cc }
            boolean r9 = kotlinx.coroutines.DispatchedTaskKt.isCancellableMode(r9)     // Catch:{ all -> 0x00cc }
            if (r9 == 0) goto L_0x0055
            kotlinx.coroutines.Job$Key r9 = kotlinx.coroutines.Job.Key     // Catch:{ all -> 0x00cc }
            kotlin.coroutines.CoroutineContext$Element r6 = r6.get(r9)     // Catch:{ all -> 0x00cc }
            kotlinx.coroutines.Job r6 = (kotlinx.coroutines.Job) r6     // Catch:{ all -> 0x00cc }
            goto L_0x0056
        L_0x0055:
            r6 = r5
        L_0x0056:
            if (r6 == 0) goto L_0x0085
            boolean r9 = r6.isActive()     // Catch:{ all -> 0x00cc }
            if (r9 != 0) goto L_0x0085
            java.util.concurrent.CancellationException r6 = r6.getCancellationException()     // Catch:{ all -> 0x00cc }
            r10.cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r7, r6)     // Catch:{ all -> 0x00cc }
            kotlin.Result$Companion r7 = kotlin.Result.Companion     // Catch:{ all -> 0x00cc }
            boolean r7 = kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES()     // Catch:{ all -> 0x00cc }
            if (r7 == 0) goto L_0x0079
            boolean r7 = r2 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame     // Catch:{ all -> 0x00cc }
            if (r7 != 0) goto L_0x0072
            goto L_0x0079
        L_0x0072:
            r7 = r2
            kotlin.coroutines.jvm.internal.CoroutineStackFrame r7 = (kotlin.coroutines.jvm.internal.CoroutineStackFrame) r7     // Catch:{ all -> 0x00cc }
            java.lang.Throwable r6 = kotlinx.coroutines.internal.StackTraceRecoveryKt.recoverFromStackFrame(r6, r7)     // Catch:{ all -> 0x00cc }
        L_0x0079:
            java.lang.Object r6 = kotlin.ResultKt.createFailure(r6)     // Catch:{ all -> 0x00cc }
            java.lang.Object r6 = kotlin.Result.m4641constructorimpl(r6)     // Catch:{ all -> 0x00cc }
            r2.resumeWith(r6)     // Catch:{ all -> 0x00cc }
            goto L_0x00a2
        L_0x0085:
            if (r8 == 0) goto L_0x0095
            kotlin.Result$Companion r6 = kotlin.Result.Companion     // Catch:{ all -> 0x00cc }
            java.lang.Object r6 = kotlin.ResultKt.createFailure(r8)     // Catch:{ all -> 0x00cc }
            java.lang.Object r6 = kotlin.Result.m4641constructorimpl(r6)     // Catch:{ all -> 0x00cc }
            r2.resumeWith(r6)     // Catch:{ all -> 0x00cc }
            goto L_0x00a2
        L_0x0095:
            java.lang.Object r6 = r10.getSuccessfulResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r7)     // Catch:{ all -> 0x00cc }
            kotlin.Result$Companion r7 = kotlin.Result.Companion     // Catch:{ all -> 0x00cc }
            java.lang.Object r6 = kotlin.Result.m4641constructorimpl(r6)     // Catch:{ all -> 0x00cc }
            r2.resumeWith(r6)     // Catch:{ all -> 0x00cc }
        L_0x00a2:
            kotlin.Unit r2 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00cc }
            if (r4 == 0) goto L_0x00ac
            boolean r4 = r4.clearThreadContext()     // Catch:{ all -> 0x00d9 }
            if (r4 == 0) goto L_0x00af
        L_0x00ac:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r3, r1)     // Catch:{ all -> 0x00d9 }
        L_0x00af:
            kotlin.Result$Companion r1 = kotlin.Result.Companion     // Catch:{ all -> 0x00b9 }
            r0.afterTask()     // Catch:{ all -> 0x00b9 }
            java.lang.Object r0 = kotlin.Result.m4641constructorimpl(r2)     // Catch:{ all -> 0x00b9 }
            goto L_0x00c4
        L_0x00b9:
            r0 = move-exception
            kotlin.Result$Companion r1 = kotlin.Result.Companion
            java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
            java.lang.Object r0 = kotlin.Result.m4641constructorimpl(r0)
        L_0x00c4:
            java.lang.Throwable r0 = kotlin.Result.m4643exceptionOrNullimpl(r0)
            r10.handleFatalException(r5, r0)
            goto L_0x00f8
        L_0x00cc:
            r2 = move-exception
            if (r4 == 0) goto L_0x00d5
            boolean r4 = r4.clearThreadContext()     // Catch:{ all -> 0x00d9 }
            if (r4 == 0) goto L_0x00d8
        L_0x00d5:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r3, r1)     // Catch:{ all -> 0x00d9 }
        L_0x00d8:
            throw r2     // Catch:{ all -> 0x00d9 }
        L_0x00d9:
            r1 = move-exception
            kotlin.Result$Companion r2 = kotlin.Result.Companion     // Catch:{ all -> 0x00e6 }
            r0.afterTask()     // Catch:{ all -> 0x00e6 }
            kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x00e6 }
            java.lang.Object r0 = kotlin.Result.m4641constructorimpl(r0)     // Catch:{ all -> 0x00e6 }
            goto L_0x00f1
        L_0x00e6:
            r0 = move-exception
            kotlin.Result$Companion r2 = kotlin.Result.Companion
            java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
            java.lang.Object r0 = kotlin.Result.m4641constructorimpl(r0)
        L_0x00f1:
            java.lang.Throwable r0 = kotlin.Result.m4643exceptionOrNullimpl(r0)
            r10.handleFatalException(r1, r0)
        L_0x00f8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.DispatchedTask.run():void");
    }
}

package kotlinx.coroutines.internal;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.DebugKt;
import kotlinx.coroutines.EventLoop;
import kotlinx.coroutines.ThreadLocalEventLoop;
import org.jetbrains.annotations.NotNull;

/* compiled from: DispatchedContinuation.kt */
public final class DispatchedContinuationKt {
    @NotNull
    public static final Symbol REUSABLE_CLAIMED = new Symbol("REUSABLE_CLAIMED");
    @NotNull
    public static final Symbol UNDEFINED = new Symbol("UNDEFINED");

    public static /* synthetic */ void resumeCancellableWith$default(Continuation continuation, Object obj, Function1 function1, int i, Object obj2) {
        if ((i & 2) != 0) {
            function1 = null;
        }
        resumeCancellableWith(continuation, obj, function1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0093, code lost:
        if (r8.clearThreadContext() != false) goto L_0x0095;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final <T> void resumeCancellableWith(@org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<? super T> r6, @org.jetbrains.annotations.NotNull java.lang.Object r7, @org.jetbrains.annotations.Nullable kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> r8) {
        /*
            boolean r0 = r6 instanceof kotlinx.coroutines.internal.DispatchedContinuation
            if (r0 == 0) goto L_0x00ba
            kotlinx.coroutines.internal.DispatchedContinuation r6 = (kotlinx.coroutines.internal.DispatchedContinuation) r6
            java.lang.Object r8 = kotlinx.coroutines.CompletionStateKt.toState((java.lang.Object) r7, (kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>) r8)
            kotlinx.coroutines.CoroutineDispatcher r0 = r6.dispatcher
            kotlin.coroutines.CoroutineContext r1 = r6.getContext()
            boolean r0 = r0.isDispatchNeeded(r1)
            r1 = 1
            if (r0 == 0) goto L_0x0026
            r6._state = r8
            r6.resumeMode = r1
            kotlinx.coroutines.CoroutineDispatcher r7 = r6.dispatcher
            kotlin.coroutines.CoroutineContext r8 = r6.getContext()
            r7.dispatch(r8, r6)
            goto L_0x00bd
        L_0x0026:
            boolean r0 = kotlinx.coroutines.DebugKt.getASSERTIONS_ENABLED()
            kotlinx.coroutines.ThreadLocalEventLoop r0 = kotlinx.coroutines.ThreadLocalEventLoop.INSTANCE
            kotlinx.coroutines.EventLoop r0 = r0.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines()
            boolean r2 = r0.isUnconfinedLoopActive()
            if (r2 == 0) goto L_0x003f
            r6._state = r8
            r6.resumeMode = r1
            r0.dispatchUnconfined(r6)
            goto L_0x00bd
        L_0x003f:
            r0.incrementUseCount(r1)
            r2 = 0
            kotlin.coroutines.CoroutineContext r3 = r6.getContext()     // Catch:{ all -> 0x00ad }
            kotlinx.coroutines.Job$Key r4 = kotlinx.coroutines.Job.Key     // Catch:{ all -> 0x00ad }
            kotlin.coroutines.CoroutineContext$Element r3 = r3.get(r4)     // Catch:{ all -> 0x00ad }
            kotlinx.coroutines.Job r3 = (kotlinx.coroutines.Job) r3     // Catch:{ all -> 0x00ad }
            if (r3 == 0) goto L_0x006d
            boolean r4 = r3.isActive()     // Catch:{ all -> 0x00ad }
            if (r4 != 0) goto L_0x006d
            java.util.concurrent.CancellationException r3 = r3.getCancellationException()     // Catch:{ all -> 0x00ad }
            r6.cancelCompletedResult$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r8, r3)     // Catch:{ all -> 0x00ad }
            kotlin.Result$Companion r8 = kotlin.Result.Companion     // Catch:{ all -> 0x00ad }
            java.lang.Object r8 = kotlin.ResultKt.createFailure(r3)     // Catch:{ all -> 0x00ad }
            java.lang.Object r8 = kotlin.Result.m4641constructorimpl(r8)     // Catch:{ all -> 0x00ad }
            r6.resumeWith(r8)     // Catch:{ all -> 0x00ad }
            r8 = r1
            goto L_0x006e
        L_0x006d:
            r8 = 0
        L_0x006e:
            if (r8 != 0) goto L_0x00a6
            kotlin.coroutines.Continuation<T> r8 = r6.continuation     // Catch:{ all -> 0x00ad }
            java.lang.Object r3 = r6.countOrElement     // Catch:{ all -> 0x00ad }
            kotlin.coroutines.CoroutineContext r4 = r8.getContext()     // Catch:{ all -> 0x00ad }
            java.lang.Object r3 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r4, r3)     // Catch:{ all -> 0x00ad }
            kotlinx.coroutines.internal.Symbol r5 = kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS     // Catch:{ all -> 0x00ad }
            if (r3 == r5) goto L_0x0085
            kotlinx.coroutines.UndispatchedCoroutine r8 = kotlinx.coroutines.CoroutineContextKt.updateUndispatchedCompletion(r8, r4, r3)     // Catch:{ all -> 0x00ad }
            goto L_0x0086
        L_0x0085:
            r8 = r2
        L_0x0086:
            kotlin.coroutines.Continuation<T> r5 = r6.continuation     // Catch:{ all -> 0x0099 }
            r5.resumeWith(r7)     // Catch:{ all -> 0x0099 }
            kotlin.Unit r7 = kotlin.Unit.INSTANCE     // Catch:{ all -> 0x0099 }
            if (r8 == 0) goto L_0x0095
            boolean r7 = r8.clearThreadContext()     // Catch:{ all -> 0x00ad }
            if (r7 == 0) goto L_0x00a6
        L_0x0095:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r4, r3)     // Catch:{ all -> 0x00ad }
            goto L_0x00a6
        L_0x0099:
            r7 = move-exception
            if (r8 == 0) goto L_0x00a2
            boolean r8 = r8.clearThreadContext()     // Catch:{ all -> 0x00ad }
            if (r8 == 0) goto L_0x00a5
        L_0x00a2:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r4, r3)     // Catch:{ all -> 0x00ad }
        L_0x00a5:
            throw r7     // Catch:{ all -> 0x00ad }
        L_0x00a6:
            boolean r7 = r0.processUnconfinedEvent()     // Catch:{ all -> 0x00ad }
            if (r7 != 0) goto L_0x00a6
            goto L_0x00b1
        L_0x00ad:
            r7 = move-exception
            r6.handleFatalException(r7, r2)     // Catch:{ all -> 0x00b5 }
        L_0x00b1:
            r0.decrementUseCount(r1)
            goto L_0x00bd
        L_0x00b5:
            r6 = move-exception
            r0.decrementUseCount(r1)
            throw r6
        L_0x00ba:
            r6.resumeWith(r7)
        L_0x00bd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.DispatchedContinuationKt.resumeCancellableWith(kotlin.coroutines.Continuation, java.lang.Object, kotlin.jvm.functions.Function1):void");
    }

    public static final boolean yieldUndispatched(@NotNull DispatchedContinuation<? super Unit> dispatchedContinuation) {
        Unit unit = Unit.INSTANCE;
        boolean assertions_enabled = DebugKt.getASSERTIONS_ENABLED();
        EventLoop eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines = ThreadLocalEventLoop.INSTANCE.getEventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.isUnconfinedQueueEmpty()) {
            return false;
        }
        if (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.isUnconfinedLoopActive()) {
            dispatchedContinuation._state = unit;
            dispatchedContinuation.resumeMode = 1;
            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.dispatchUnconfined(dispatchedContinuation);
            return true;
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.incrementUseCount(true);
        try {
            dispatchedContinuation.run();
            do {
            } while (eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.processUnconfinedEvent());
        } catch (Throwable th) {
            eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
            throw th;
        }
        eventLoop$external__kotlinx_coroutines__android_common__kotlinx_coroutines.decrementUseCount(true);
        return false;
    }
}

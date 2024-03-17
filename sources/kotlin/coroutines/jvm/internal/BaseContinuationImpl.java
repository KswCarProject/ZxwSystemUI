package kotlin.coroutines.jvm.internal;

import java.io.Serializable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ContinuationImpl.kt */
public abstract class BaseContinuationImpl implements Continuation<Object>, CoroutineStackFrame, Serializable {
    @Nullable
    private final Continuation<Object> completion;

    @Nullable
    public abstract Object invokeSuspend(@NotNull Object obj);

    public void releaseIntercepted() {
    }

    public BaseContinuationImpl(@Nullable Continuation<Object> continuation) {
        this.completion = continuation;
    }

    @Nullable
    public final Continuation<Object> getCompletion() {
        return this.completion;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: kotlin.coroutines.jvm.internal.BaseContinuationImpl} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void resumeWith(@org.jetbrains.annotations.NotNull java.lang.Object r3) {
        /*
            r2 = this;
        L_0x0000:
            r0 = r2
            kotlin.coroutines.Continuation r0 = (kotlin.coroutines.Continuation) r0
            kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineResumed(r0)
            kotlin.coroutines.jvm.internal.BaseContinuationImpl r2 = (kotlin.coroutines.jvm.internal.BaseContinuationImpl) r2
            kotlin.coroutines.Continuation r0 = r2.getCompletion()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            java.lang.Object r3 = r2.invokeSuspend(r3)     // Catch:{ all -> 0x0021 }
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()     // Catch:{ all -> 0x0021 }
            if (r3 != r1) goto L_0x001a
            return
        L_0x001a:
            kotlin.Result$Companion r1 = kotlin.Result.Companion     // Catch:{ all -> 0x0021 }
            java.lang.Object r3 = kotlin.Result.m4641constructorimpl(r3)     // Catch:{ all -> 0x0021 }
            goto L_0x002c
        L_0x0021:
            r3 = move-exception
            kotlin.Result$Companion r1 = kotlin.Result.Companion
            java.lang.Object r3 = kotlin.ResultKt.createFailure(r3)
            java.lang.Object r3 = kotlin.Result.m4641constructorimpl(r3)
        L_0x002c:
            r2.releaseIntercepted()
            boolean r2 = r0 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl
            if (r2 == 0) goto L_0x0035
            r2 = r0
            goto L_0x0000
        L_0x0035:
            r0.resumeWith(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(java.lang.Object):void");
    }

    @NotNull
    public Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }

    @NotNull
    public String toString() {
        Object stackTraceElement = getStackTraceElement();
        if (stackTraceElement == null) {
            stackTraceElement = getClass().getName();
        }
        return Intrinsics.stringPlus("Continuation at ", stackTraceElement);
    }

    @Nullable
    public CoroutineStackFrame getCallerFrame() {
        Continuation<Object> continuation = this.completion;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    @Nullable
    public StackTraceElement getStackTraceElement() {
        return DebugMetadataKt.getStackTraceElement(this);
    }
}

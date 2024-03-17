package kotlinx.coroutines;

import kotlinx.atomicfu.AtomicBoolean;
import org.jetbrains.annotations.NotNull;

/* compiled from: CompletionState.kt */
public final class CancelledContinuation extends CompletedExceptionally {
    @NotNull
    public final AtomicBoolean _resumed;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CancelledContinuation(@org.jetbrains.annotations.NotNull kotlin.coroutines.Continuation<?> r3, @org.jetbrains.annotations.Nullable java.lang.Throwable r4, boolean r5) {
        /*
            r2 = this;
            if (r4 != 0) goto L_0x001d
            java.util.concurrent.CancellationException r4 = new java.util.concurrent.CancellationException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Continuation "
            r0.append(r1)
            r0.append(r3)
            java.lang.String r3 = " was cancelled normally"
            r0.append(r3)
            java.lang.String r3 = r0.toString()
            r4.<init>(r3)
        L_0x001d:
            r2.<init>(r4, r5)
            r3 = 0
            kotlinx.atomicfu.AtomicBoolean r3 = kotlinx.atomicfu.AtomicFU.atomic((boolean) r3)
            r2._resumed = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CancelledContinuation.<init>(kotlin.coroutines.Continuation, java.lang.Throwable, boolean):void");
    }

    public final boolean makeResumed() {
        return this._resumed.compareAndSet(false, true);
    }
}

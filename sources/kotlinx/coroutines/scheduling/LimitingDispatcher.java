package kotlinx.coroutines.scheduling;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import kotlin.coroutines.CoroutineContext;
import kotlinx.atomicfu.AtomicFU;
import kotlinx.atomicfu.AtomicInt;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: Dispatcher.kt */
public final class LimitingDispatcher extends ExecutorCoroutineDispatcher implements TaskContext, Executor {
    @NotNull
    public final ExperimentalCoroutineDispatcher dispatcher;
    @NotNull
    public final AtomicInt inFlightTasks = AtomicFU.atomic(0);
    @Nullable
    public final String name;
    public final int parallelism;
    @NotNull
    public final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
    public final int taskMode;

    public int getTaskMode() {
        return this.taskMode;
    }

    public LimitingDispatcher(@NotNull ExperimentalCoroutineDispatcher experimentalCoroutineDispatcher, int i, @Nullable String str, int i2) {
        this.dispatcher = experimentalCoroutineDispatcher;
        this.parallelism = i;
        this.name = str;
        this.taskMode = i2;
    }

    public void execute(@NotNull Runnable runnable) {
        dispatch(runnable, false);
    }

    public void close() {
        throw new IllegalStateException("Close cannot be invoked on LimitingBlockingDispatcher".toString());
    }

    public void dispatch(@NotNull CoroutineContext coroutineContext, @NotNull Runnable runnable) {
        dispatch(runnable, false);
    }

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void dispatch(java.lang.Runnable r3, boolean r4) {
        /*
            r2 = this;
        L_0x0000:
            kotlinx.atomicfu.AtomicInt r0 = r2.inFlightTasks
            int r0 = r0.incrementAndGet()
            int r1 = r2.parallelism
            if (r0 > r1) goto L_0x0010
            kotlinx.coroutines.scheduling.ExperimentalCoroutineDispatcher r0 = r2.dispatcher
            r0.dispatchWithContext$external__kotlinx_coroutines__android_common__kotlinx_coroutines(r3, r2, r4)
            return
        L_0x0010:
            java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> r0 = r2.queue
            r0.add(r3)
            kotlinx.atomicfu.AtomicInt r3 = r2.inFlightTasks
            int r3 = r3.decrementAndGet()
            int r0 = r2.parallelism
            if (r3 < r0) goto L_0x0020
            return
        L_0x0020:
            java.util.concurrent.ConcurrentLinkedQueue<java.lang.Runnable> r3 = r2.queue
            java.lang.Object r3 = r3.poll()
            java.lang.Runnable r3 = (java.lang.Runnable) r3
            if (r3 != 0) goto L_0x0000
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.scheduling.LimitingDispatcher.dispatch(java.lang.Runnable, boolean):void");
    }

    public void dispatchYield(@NotNull CoroutineContext coroutineContext, @NotNull Runnable runnable) {
        dispatch(runnable, true);
    }

    @NotNull
    public String toString() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        return super.toString() + "[dispatcher = " + this.dispatcher + ']';
    }

    public void afterTask() {
        Runnable poll = this.queue.poll();
        if (poll != null) {
            this.dispatcher.dispatchWithContext$external__kotlinx_coroutines__android_common__kotlinx_coroutines(poll, this, true);
            return;
        }
        this.inFlightTasks.decrementAndGet();
        Runnable poll2 = this.queue.poll();
        if (poll2 != null) {
            dispatch(poll2, true);
        }
    }
}

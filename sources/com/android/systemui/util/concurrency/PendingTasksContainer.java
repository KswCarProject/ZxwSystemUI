package com.android.systemui.util.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;

/* compiled from: PendingTasksContainer.kt */
public final class PendingTasksContainer {
    @NotNull
    public AtomicReference<Runnable> completionCallback = new AtomicReference<>();
    @NotNull
    public AtomicInteger pendingTasksCount = new AtomicInteger(0);

    @NotNull
    public final Runnable registerTask(@NotNull String str) {
        this.pendingTasksCount.incrementAndGet();
        return new PendingTasksContainer$registerTask$1(this, str);
    }

    public final void reset() {
        this.completionCallback = new AtomicReference<>();
        this.pendingTasksCount = new AtomicInteger(0);
    }

    public final void onTasksComplete(@NotNull Runnable runnable) {
        Runnable andSet;
        this.completionCallback.set(runnable);
        if (this.pendingTasksCount.get() == 0 && (andSet = this.completionCallback.getAndSet((Object) null)) != null) {
            andSet.run();
        }
    }
}

package com.android.systemui.util.concurrency;

/* compiled from: PendingTasksContainer.kt */
public final class PendingTasksContainer$registerTask$1 implements Runnable {
    public final /* synthetic */ String $name;
    public final /* synthetic */ PendingTasksContainer this$0;

    public PendingTasksContainer$registerTask$1(PendingTasksContainer pendingTasksContainer, String str) {
        this.this$0 = pendingTasksContainer;
        this.$name = str;
    }

    public final void run() {
        Runnable runnable;
        if (this.this$0.pendingTasksCount.decrementAndGet() == 0 && (runnable = (Runnable) this.this$0.completionCallback.getAndSet((Object) null)) != null) {
            runnable.run();
        }
    }
}

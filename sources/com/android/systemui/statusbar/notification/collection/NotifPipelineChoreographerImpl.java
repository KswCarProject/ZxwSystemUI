package com.android.systemui.statusbar.notification.collection;

import android.view.Choreographer;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.concurrency.DelayableExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: NotifPipelineChoreographer.kt */
public final class NotifPipelineChoreographerImpl implements NotifPipelineChoreographer {
    @NotNull
    public final DelayableExecutor executor;
    @NotNull
    public final Choreographer.FrameCallback frameCallback = new NotifPipelineChoreographerImpl$frameCallback$1(this);
    public boolean isScheduled;
    @NotNull
    public final ListenerSet<Runnable> listeners = new ListenerSet<>();
    @Nullable
    public Runnable timeoutSubscription;
    @NotNull
    public final Choreographer viewChoreographer;

    public NotifPipelineChoreographerImpl(@NotNull Choreographer choreographer, @NotNull DelayableExecutor delayableExecutor) {
        this.viewChoreographer = choreographer;
        this.executor = delayableExecutor;
    }

    public void schedule() {
        if (!this.isScheduled) {
            this.isScheduled = true;
            this.viewChoreographer.postFrameCallback(this.frameCallback);
            if (this.isScheduled) {
                this.timeoutSubscription = this.executor.executeDelayed(new NotifPipelineChoreographerImpl$schedule$1(this), 100);
            }
        }
    }

    public void addOnEvalListener(@NotNull Runnable runnable) {
        this.listeners.addIfAbsent(runnable);
    }

    public final void onTimeout() {
        if (this.isScheduled) {
            this.isScheduled = false;
            this.viewChoreographer.removeFrameCallback(this.frameCallback);
            for (Runnable run : this.listeners) {
                run.run();
            }
        }
    }
}

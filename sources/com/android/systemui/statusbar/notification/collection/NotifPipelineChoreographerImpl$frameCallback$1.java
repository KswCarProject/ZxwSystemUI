package com.android.systemui.statusbar.notification.collection;

import android.view.Choreographer;

/* compiled from: NotifPipelineChoreographer.kt */
public final class NotifPipelineChoreographerImpl$frameCallback$1 implements Choreographer.FrameCallback {
    public final /* synthetic */ NotifPipelineChoreographerImpl this$0;

    public NotifPipelineChoreographerImpl$frameCallback$1(NotifPipelineChoreographerImpl notifPipelineChoreographerImpl) {
        this.this$0 = notifPipelineChoreographerImpl;
    }

    public final void doFrame(long j) {
        if (this.this$0.isScheduled) {
            this.this$0.isScheduled = false;
            Runnable access$getTimeoutSubscription$p = this.this$0.timeoutSubscription;
            if (access$getTimeoutSubscription$p != null) {
                access$getTimeoutSubscription$p.run();
            }
            for (Runnable run : this.this$0.listeners) {
                run.run();
            }
        }
    }
}

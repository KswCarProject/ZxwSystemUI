package com.android.systemui.statusbar.notification.collection;

/* compiled from: NotifPipelineChoreographer.kt */
public /* synthetic */ class NotifPipelineChoreographerImpl$schedule$1 implements Runnable {
    public final /* synthetic */ NotifPipelineChoreographerImpl $tmp0;

    public NotifPipelineChoreographerImpl$schedule$1(NotifPipelineChoreographerImpl notifPipelineChoreographerImpl) {
        this.$tmp0 = notifPipelineChoreographerImpl;
    }

    public final void run() {
        this.$tmp0.onTimeout();
    }
}

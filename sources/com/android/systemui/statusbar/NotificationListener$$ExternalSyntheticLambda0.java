package com.android.systemui.statusbar;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationListener$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationListener f$0;

    public /* synthetic */ NotificationListener$$ExternalSyntheticLambda0(NotificationListener notificationListener) {
        this.f$0 = notificationListener;
    }

    public final void run() {
        this.f$0.dispatchRankingUpdate();
    }
}

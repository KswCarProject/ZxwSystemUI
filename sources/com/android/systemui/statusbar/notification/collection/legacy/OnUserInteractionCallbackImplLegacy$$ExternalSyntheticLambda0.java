package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OnUserInteractionCallbackImplLegacy$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ OnUserInteractionCallbackImplLegacy f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ NotificationEntry f$3;

    public /* synthetic */ OnUserInteractionCallbackImplLegacy$$ExternalSyntheticLambda0(OnUserInteractionCallbackImplLegacy onUserInteractionCallbackImplLegacy, NotificationEntry notificationEntry, int i, NotificationEntry notificationEntry2) {
        this.f$0 = onUserInteractionCallbackImplLegacy;
        this.f$1 = notificationEntry;
        this.f$2 = i;
        this.f$3 = notificationEntry2;
    }

    public final void run() {
        this.f$0.lambda$registerFutureDismissal$0(this.f$1, this.f$2, this.f$3);
    }
}

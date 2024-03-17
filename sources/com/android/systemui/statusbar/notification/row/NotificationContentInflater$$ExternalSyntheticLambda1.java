package com.android.systemui.statusbar.notification.row;

import android.os.CancellationSignal;
import java.util.HashMap;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationContentInflater$$ExternalSyntheticLambda1 implements CancellationSignal.OnCancelListener {
    public final /* synthetic */ HashMap f$0;

    public /* synthetic */ NotificationContentInflater$$ExternalSyntheticLambda1(HashMap hashMap) {
        this.f$0 = hashMap;
    }

    public final void onCancel() {
        this.f$0.values().forEach(new NotificationContentInflater$$ExternalSyntheticLambda0());
    }
}

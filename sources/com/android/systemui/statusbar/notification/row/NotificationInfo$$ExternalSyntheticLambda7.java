package com.android.systemui.statusbar.notification.row;

import android.content.Intent;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationInfo$$ExternalSyntheticLambda7 implements View.OnClickListener {
    public final /* synthetic */ NotificationInfo f$0;
    public final /* synthetic */ Intent f$1;

    public /* synthetic */ NotificationInfo$$ExternalSyntheticLambda7(NotificationInfo notificationInfo, Intent intent) {
        this.f$0 = notificationInfo;
        this.f$1 = intent;
    }

    public final void onClick(View view) {
        this.f$0.lambda$bindHeader$4(this.f$1, view);
    }
}

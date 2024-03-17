package com.android.systemui.statusbar.notification.stack;

import android.util.IndentingPrintWriter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class NotificationStackScrollLayout$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ NotificationStackScrollLayout f$0;
    public final /* synthetic */ IndentingPrintWriter f$1;
    public final /* synthetic */ String[] f$2;

    public /* synthetic */ NotificationStackScrollLayout$$ExternalSyntheticLambda7(NotificationStackScrollLayout notificationStackScrollLayout, IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        this.f$0 = notificationStackScrollLayout;
        this.f$1 = indentingPrintWriter;
        this.f$2 = strArr;
    }

    public final void run() {
        this.f$0.lambda$dump$6(this.f$1, this.f$2);
    }
}

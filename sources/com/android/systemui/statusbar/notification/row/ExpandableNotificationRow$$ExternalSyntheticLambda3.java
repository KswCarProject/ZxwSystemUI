package com.android.systemui.statusbar.notification.row;

import android.util.IndentingPrintWriter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ExpandableNotificationRow$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ ExpandableNotificationRow f$0;
    public final /* synthetic */ IndentingPrintWriter f$1;
    public final /* synthetic */ String[] f$2;

    public /* synthetic */ ExpandableNotificationRow$$ExternalSyntheticLambda3(ExpandableNotificationRow expandableNotificationRow, IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        this.f$0 = expandableNotificationRow;
        this.f$1 = indentingPrintWriter;
        this.f$2 = strArr;
    }

    public final void run() {
        this.f$0.lambda$dump$7(this.f$1, this.f$2);
    }
}

package com.android.systemui.statusbar.phone;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SystemUIDialog$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SystemUIDialog f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ DialogInterface.OnClickListener f$2;

    public /* synthetic */ SystemUIDialog$$ExternalSyntheticLambda3(SystemUIDialog systemUIDialog, int i, DialogInterface.OnClickListener onClickListener) {
        this.f$0 = systemUIDialog;
        this.f$1 = i;
        this.f$2 = onClickListener;
    }

    public final void run() {
        this.f$0.lambda$setButton$1(this.f$1, this.f$2);
    }
}

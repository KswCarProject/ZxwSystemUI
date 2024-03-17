package com.android.systemui.statusbar.phone;

import android.content.DialogInterface;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SystemUIDialog$$ExternalSyntheticLambda4 implements View.OnClickListener {
    public final /* synthetic */ SystemUIDialog f$0;
    public final /* synthetic */ DialogInterface.OnClickListener f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ SystemUIDialog$$ExternalSyntheticLambda4(SystemUIDialog systemUIDialog, DialogInterface.OnClickListener onClickListener, int i) {
        this.f$0 = systemUIDialog;
        this.f$1 = onClickListener;
        this.f$2 = i;
    }

    public final void onClick(View view) {
        this.f$0.lambda$setButton$0(this.f$1, this.f$2, view);
    }
}

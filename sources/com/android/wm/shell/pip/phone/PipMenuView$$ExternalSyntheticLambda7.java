package com.android.wm.shell.pip.phone;

import android.app.RemoteAction;
import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipMenuView$$ExternalSyntheticLambda7 implements View.OnClickListener {
    public final /* synthetic */ PipMenuView f$0;
    public final /* synthetic */ RemoteAction f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ PipMenuView$$ExternalSyntheticLambda7(PipMenuView pipMenuView, RemoteAction remoteAction, boolean z) {
        this.f$0 = pipMenuView;
        this.f$1 = remoteAction;
        this.f$2 = z;
    }

    public final void onClick(View view) {
        this.f$0.lambda$updateActionViews$7(this.f$1, this.f$2, view);
    }
}

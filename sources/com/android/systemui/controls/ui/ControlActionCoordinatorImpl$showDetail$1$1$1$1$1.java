package com.android.systemui.controls.ui;

import android.content.DialogInterface;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$showDetail$1$1$1$1$1 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    public ControlActionCoordinatorImpl$showDetail$1$1$1$1$1(ControlActionCoordinatorImpl controlActionCoordinatorImpl) {
        this.this$0 = controlActionCoordinatorImpl;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.this$0.dialog = null;
    }
}

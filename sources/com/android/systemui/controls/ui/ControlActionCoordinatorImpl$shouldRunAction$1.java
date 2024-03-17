package com.android.systemui.controls.ui;

/* compiled from: ControlActionCoordinatorImpl.kt */
public final class ControlActionCoordinatorImpl$shouldRunAction$1 implements Runnable {
    public final /* synthetic */ String $controlId;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    public ControlActionCoordinatorImpl$shouldRunAction$1(ControlActionCoordinatorImpl controlActionCoordinatorImpl, String str) {
        this.this$0 = controlActionCoordinatorImpl;
        this.$controlId = str;
    }

    public final void run() {
        this.this$0.actionsInProgress.remove(this.$controlId);
    }
}

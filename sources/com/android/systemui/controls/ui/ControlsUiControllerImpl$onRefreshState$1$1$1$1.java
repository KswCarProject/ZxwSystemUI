package com.android.systemui.controls.ui;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$onRefreshState$1$1$1$1 implements Runnable {
    public final /* synthetic */ ControlWithState $cws;
    public final /* synthetic */ boolean $isLocked;
    public final /* synthetic */ ControlViewHolder $it;

    public ControlsUiControllerImpl$onRefreshState$1$1$1$1(ControlViewHolder controlViewHolder, ControlWithState controlWithState, boolean z) {
        this.$it = controlViewHolder;
        this.$cws = controlWithState;
        this.$isLocked = z;
    }

    public final void run() {
        this.$it.bindData(this.$cws, this.$isLocked);
    }
}

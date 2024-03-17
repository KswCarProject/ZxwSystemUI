package com.android.systemui.controls.ui;

/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$onActionResponse$1 implements Runnable {
    public final /* synthetic */ ControlKey $key;
    public final /* synthetic */ int $response;
    public final /* synthetic */ ControlsUiControllerImpl this$0;

    public ControlsUiControllerImpl$onActionResponse$1(ControlsUiControllerImpl controlsUiControllerImpl, ControlKey controlKey, int i) {
        this.this$0 = controlsUiControllerImpl;
        this.$key = controlKey;
        this.$response = i;
    }

    public final void run() {
        ControlViewHolder controlViewHolder = (ControlViewHolder) this.this$0.controlViewsById.get(this.$key);
        if (controlViewHolder != null) {
            controlViewHolder.actionResponse(this.$response);
        }
    }
}

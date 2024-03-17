package com.android.systemui.controls.management;

import android.util.Log;
import com.android.systemui.controls.management.ControlsListingController;

/* compiled from: ControlsListingControllerImpl.kt */
public final class ControlsListingControllerImpl$removeCallback$1 implements Runnable {
    public final /* synthetic */ ControlsListingController.ControlsListingCallback $listener;
    public final /* synthetic */ ControlsListingControllerImpl this$0;

    public ControlsListingControllerImpl$removeCallback$1(ControlsListingControllerImpl controlsListingControllerImpl, ControlsListingController.ControlsListingCallback controlsListingCallback) {
        this.this$0 = controlsListingControllerImpl;
        this.$listener = controlsListingCallback;
    }

    public final void run() {
        Log.d("ControlsListingControllerImpl", "Unsubscribing callback");
        this.this$0.callbacks.remove(this.$listener);
    }
}

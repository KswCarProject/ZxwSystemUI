package com.android.systemui.controls.controller;

import android.util.Log;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1 implements Runnable {
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1(ControlsControllerImpl controlsControllerImpl) {
        this.this$0 = controlsControllerImpl;
    }

    public final void run() {
        Log.d("ControlsControllerImpl", "Restore finished, storing auxiliary favorites");
        this.this$0.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().initialize();
        this.this$0.persistenceWrapper.storeFavorites(this.this$0.getAuxiliaryPersistenceWrapper$frameworks__base__packages__SystemUI__android_common__SystemUI_core().getFavorites());
        this.this$0.resetFavorites();
    }
}

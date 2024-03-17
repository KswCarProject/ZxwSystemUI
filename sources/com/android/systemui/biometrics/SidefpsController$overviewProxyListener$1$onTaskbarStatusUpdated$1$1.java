package com.android.systemui.biometrics;

import android.view.View;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$overviewProxyListener$1$onTaskbarStatusUpdated$1$1 implements Runnable {
    public final /* synthetic */ View $view;
    public final /* synthetic */ SidefpsController this$0;

    public SidefpsController$overviewProxyListener$1$onTaskbarStatusUpdated$1$1(SidefpsController sidefpsController, View view) {
        this.this$0 = sidefpsController;
        this.$view = view;
    }

    public final void run() {
        this.this$0.updateOverlayVisibility(this.$view);
    }
}

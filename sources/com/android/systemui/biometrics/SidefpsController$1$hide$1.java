package com.android.systemui.biometrics;

import android.view.View;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$1$hide$1 implements Runnable {
    public final /* synthetic */ SidefpsController this$0;

    public SidefpsController$1$hide$1(SidefpsController sidefpsController) {
        this.this$0 = sidefpsController;
    }

    public final void run() {
        this.this$0.setOverlayView((View) null);
    }
}

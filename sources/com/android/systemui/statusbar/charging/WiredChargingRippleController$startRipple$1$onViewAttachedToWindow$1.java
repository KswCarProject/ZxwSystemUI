package com.android.systemui.statusbar.charging;

/* compiled from: WiredChargingRippleController.kt */
public final class WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1 implements Runnable {
    public final /* synthetic */ WiredChargingRippleController this$0;

    public WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1(WiredChargingRippleController wiredChargingRippleController) {
        this.this$0 = wiredChargingRippleController;
    }

    public final void run() {
        this.this$0.windowManager.removeView(this.this$0.getRippleView());
    }
}

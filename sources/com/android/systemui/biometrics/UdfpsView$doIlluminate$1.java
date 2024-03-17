package com.android.systemui.biometrics;

import android.util.Log;

/* compiled from: UdfpsView.kt */
public final class UdfpsView$doIlluminate$1 implements Runnable {
    public final /* synthetic */ Runnable $onIlluminatedRunnable;
    public final /* synthetic */ UdfpsView this$0;

    public UdfpsView$doIlluminate$1(Runnable runnable, UdfpsView udfpsView) {
        this.$onIlluminatedRunnable = runnable;
        this.this$0 = udfpsView;
    }

    public final void run() {
        if (this.$onIlluminatedRunnable == null) {
            Log.w("UdfpsView", "doIlluminate | onIlluminatedRunnable is null");
        } else if (this.this$0.getHalControlsIllumination()) {
            this.$onIlluminatedRunnable.run();
        } else {
            UdfpsView udfpsView = this.this$0;
            udfpsView.postDelayed(this.$onIlluminatedRunnable, udfpsView.onIlluminatedDelayMs);
        }
    }
}

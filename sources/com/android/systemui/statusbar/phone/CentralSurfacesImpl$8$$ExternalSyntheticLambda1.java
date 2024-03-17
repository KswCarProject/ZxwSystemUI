package com.android.systemui.statusbar.phone;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$8$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ StatusBarKeyguardViewManager f$0;

    public /* synthetic */ CentralSurfacesImpl$8$$ExternalSyntheticLambda1(StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        this.f$0 = statusBarKeyguardViewManager;
    }

    public final void run() {
        this.f$0.readyForKeyguardDone();
    }
}

package com.android.systemui.statusbar.phone;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$8$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ ShadeController f$0;

    public /* synthetic */ CentralSurfacesImpl$8$$ExternalSyntheticLambda0(ShadeController shadeController) {
        this.f$0 = shadeController;
    }

    public final void run() {
        this.f$0.runPostCollapseRunnables();
    }
}

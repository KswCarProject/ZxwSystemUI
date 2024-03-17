package com.android.systemui.dreams;

import android.view.WindowManager;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ DreamOverlayService f$0;
    public final /* synthetic */ WindowManager.LayoutParams f$1;

    public /* synthetic */ DreamOverlayService$$ExternalSyntheticLambda0(DreamOverlayService dreamOverlayService, WindowManager.LayoutParams layoutParams) {
        this.f$0 = dreamOverlayService;
        this.f$1 = layoutParams;
    }

    public final void run() {
        this.f$0.lambda$onStartDream$1(this.f$1);
    }
}

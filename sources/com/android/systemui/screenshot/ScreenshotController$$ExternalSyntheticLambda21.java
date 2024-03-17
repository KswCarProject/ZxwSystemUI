package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ ScreenshotController f$0;
    public final /* synthetic */ ScreenshotController.QuickShareData f$1;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda21(ScreenshotController screenshotController, ScreenshotController.QuickShareData quickShareData) {
        this.f$0 = screenshotController;
        this.f$1 = quickShareData;
    }

    public final void run() {
        this.f$0.lambda$showUiOnQuickShareActionReady$17(this.f$1);
    }
}

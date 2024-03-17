package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda10 implements ScreenshotController.QuickShareActionReadyListener {
    public final /* synthetic */ ScreenshotController f$0;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda10(ScreenshotController screenshotController) {
        this.f$0 = screenshotController;
    }

    public final void onActionsReady(ScreenshotController.QuickShareData quickShareData) {
        this.f$0.showUiOnQuickShareActionReady(quickShareData);
    }
}

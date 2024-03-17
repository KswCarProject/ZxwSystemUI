package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda13 implements ScreenshotController.ActionsReadyListener {
    public final /* synthetic */ ScreenshotController f$0;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda13(ScreenshotController screenshotController) {
        this.f$0 = screenshotController;
    }

    public final void onActionsReady(ScreenshotController.SavedImageData savedImageData) {
        this.f$0.logSuccessOnActionsReady(savedImageData);
    }
}

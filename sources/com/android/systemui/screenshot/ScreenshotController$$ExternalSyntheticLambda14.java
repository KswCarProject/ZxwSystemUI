package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda14 implements ScreenshotController.ActionsReadyListener {
    public final /* synthetic */ ScreenshotController f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda14(ScreenshotController screenshotController, Consumer consumer) {
        this.f$0 = screenshotController;
        this.f$1 = consumer;
    }

    public final void onActionsReady(ScreenshotController.SavedImageData savedImageData) {
        this.f$0.lambda$saveScreenshotAndToast$15(this.f$1, savedImageData);
    }
}

package com.android.systemui.screenshot;

import android.graphics.Rect;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda2 implements ScreenshotController.TransitionDestination {
    public final /* synthetic */ ScreenshotController f$0;
    public final /* synthetic */ ScrollCaptureController.LongScreenshot f$1;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda2(ScreenshotController screenshotController, ScrollCaptureController.LongScreenshot longScreenshot) {
        this.f$0 = screenshotController;
        this.f$1 = longScreenshot;
    }

    public final void setTransitionDestination(Rect rect, Runnable runnable) {
        this.f$0.lambda$runBatchScrollCapture$9(this.f$1, rect, runnable);
    }
}

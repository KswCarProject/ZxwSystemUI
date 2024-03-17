package com.android.systemui.screenshot;

import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda5 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ ScreenshotController f$0;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda5(ScreenshotController screenshotController) {
        this.f$0 = screenshotController;
    }

    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return this.f$0.lambda$loadCameraSound$12(completer);
    }
}

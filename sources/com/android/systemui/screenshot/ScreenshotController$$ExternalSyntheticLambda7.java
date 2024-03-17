package com.android.systemui.screenshot;

import androidx.concurrent.futures.CallbackToFutureAdapter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ ScreenshotController f$0;
    public final /* synthetic */ CallbackToFutureAdapter.Completer f$1;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda7(ScreenshotController screenshotController, CallbackToFutureAdapter.Completer completer) {
        this.f$0 = screenshotController;
        this.f$1 = completer;
    }

    public final void run() {
        this.f$0.lambda$loadCameraSound$11(this.f$1);
    }
}

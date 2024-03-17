package com.android.systemui.screenshot;

import com.android.systemui.screenshot.TakeScreenshotService;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TakeScreenshotService$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ TakeScreenshotService f$0;
    public final /* synthetic */ TakeScreenshotService.RequestCallback f$1;

    public /* synthetic */ TakeScreenshotService$$ExternalSyntheticLambda2(TakeScreenshotService takeScreenshotService, TakeScreenshotService.RequestCallback requestCallback) {
        this.f$0 = takeScreenshotService;
        this.f$1 = requestCallback;
    }

    public final void run() {
        this.f$0.lambda$handleMessage$3(this.f$1);
    }
}

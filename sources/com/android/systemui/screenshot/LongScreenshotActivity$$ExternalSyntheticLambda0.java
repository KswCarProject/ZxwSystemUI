package com.android.systemui.screenshot;

import com.google.common.util.concurrent.ListenableFuture;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class LongScreenshotActivity$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LongScreenshotActivity f$0;
    public final /* synthetic */ ListenableFuture f$1;

    public /* synthetic */ LongScreenshotActivity$$ExternalSyntheticLambda0(LongScreenshotActivity longScreenshotActivity, ListenableFuture listenableFuture) {
        this.f$0 = longScreenshotActivity;
        this.f$1 = listenableFuture;
    }

    public final void run() {
        this.f$0.lambda$onStart$1(this.f$1);
    }
}

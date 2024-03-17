package com.android.systemui.screenshot;

import android.content.ComponentName;
import android.graphics.Rect;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ ScreenshotController f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ Consumer f$2;

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda6(ScreenshotController screenshotController, ComponentName componentName, Consumer consumer) {
        this.f$0 = screenshotController;
        this.f$1 = componentName;
        this.f$2 = consumer;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$takeScreenshotPartial$1(this.f$1, this.f$2, (Rect) obj);
    }
}

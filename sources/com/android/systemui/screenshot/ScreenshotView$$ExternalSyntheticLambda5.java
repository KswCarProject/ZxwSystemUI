package com.android.systemui.screenshot;

import android.view.View;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenshotView$$ExternalSyntheticLambda5 implements View.OnClickListener {
    public final /* synthetic */ ScreenshotView f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Runnable f$2;

    public /* synthetic */ ScreenshotView$$ExternalSyntheticLambda5(ScreenshotView screenshotView, String str, Runnable runnable) {
        this.f$0 = screenshotView;
        this.f$1 = str;
        this.f$2 = runnable;
    }

    public final void onClick(View view) {
        this.f$0.lambda$showScrollChip$0(this.f$1, this.f$2, view);
    }
}

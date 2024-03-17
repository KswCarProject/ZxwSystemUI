package com.android.systemui.accessibility;

import android.view.View;
import android.view.WindowInsets;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class WindowMagnificationController$$ExternalSyntheticLambda0 implements View.OnApplyWindowInsetsListener {
    public final /* synthetic */ WindowMagnificationController f$0;

    public /* synthetic */ WindowMagnificationController$$ExternalSyntheticLambda0(WindowMagnificationController windowMagnificationController) {
        this.f$0 = windowMagnificationController;
    }

    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        return this.f$0.lambda$createMirrorWindow$5(view, windowInsets);
    }
}

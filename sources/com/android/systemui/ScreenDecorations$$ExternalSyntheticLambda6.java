package com.android.systemui;

import com.android.systemui.decor.DecorProvider;
import com.android.systemui.decor.OverlayWindow;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ ScreenDecorations f$0;
    public final /* synthetic */ OverlayWindow f$1;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda6(ScreenDecorations screenDecorations, OverlayWindow overlayWindow) {
        this.f$0 = screenDecorations;
        this.f$1 = overlayWindow;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$initOverlay$3(this.f$1, (DecorProvider) obj);
    }
}

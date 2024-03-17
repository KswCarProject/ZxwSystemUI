package com.android.systemui.biometrics;

import android.view.Display;
import android.view.View;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$createOverlayForDisplay$1 implements LottieOnCompositionLoadedListener {
    public final /* synthetic */ Display $display;
    public final /* synthetic */ View $view;
    public final /* synthetic */ SidefpsController this$0;

    public SidefpsController$createOverlayForDisplay$1(SidefpsController sidefpsController, View view, Display display) {
        this.this$0 = sidefpsController;
        this.$view = view;
        this.$display = display;
    }

    public final void onCompositionLoaded(LottieComposition lottieComposition) {
        if (this.this$0.overlayView != null && Intrinsics.areEqual((Object) this.this$0.overlayView, (Object) this.$view)) {
            this.this$0.updateOverlayParams$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.$display, lottieComposition.getBounds());
        }
    }
}

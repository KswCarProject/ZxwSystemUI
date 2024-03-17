package com.android.systemui.biometrics;

import android.content.Context;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;

/* compiled from: SidefpsController.kt */
public final class SidefpsControllerKt$addOverlayDynamicColor$1 implements LottieOnCompositionLoadedListener {
    public final /* synthetic */ Context $context;
    public final /* synthetic */ LottieAnimationView $this_addOverlayDynamicColor;

    public SidefpsControllerKt$addOverlayDynamicColor$1(Context context, LottieAnimationView lottieAnimationView) {
        this.$context = context;
        this.$this_addOverlayDynamicColor = lottieAnimationView;
    }

    public final void onCompositionLoaded(LottieComposition lottieComposition) {
        SidefpsControllerKt.addOverlayDynamicColor$update(this.$context, this.$this_addOverlayDynamicColor);
    }
}

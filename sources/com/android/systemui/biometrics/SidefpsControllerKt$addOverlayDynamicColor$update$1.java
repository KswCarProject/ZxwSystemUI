package com.android.systemui.biometrics;

import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;

/* compiled from: SidefpsController.kt */
public final class SidefpsControllerKt$addOverlayDynamicColor$update$1<T> implements SimpleLottieValueCallback {
    public final /* synthetic */ int $c;

    public SidefpsControllerKt$addOverlayDynamicColor$update$1(int i) {
        this.$c = i;
    }

    public final ColorFilter getValue(LottieFrameInfo<ColorFilter> lottieFrameInfo) {
        return new PorterDuffColorFilter(this.$c, PorterDuff.Mode.SRC_ATOP);
    }
}

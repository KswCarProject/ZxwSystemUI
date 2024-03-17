package com.android.systemui.unfold;

import android.view.DisplayInfo;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
public final class UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2 extends Lambda implements Function1<DisplayInfo, Boolean> {
    public static final UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2 INSTANCE = new UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2();

    public UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2() {
        super(1);
    }

    @NotNull
    public final Boolean invoke(@NotNull DisplayInfo displayInfo) {
        int i = displayInfo.type;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}

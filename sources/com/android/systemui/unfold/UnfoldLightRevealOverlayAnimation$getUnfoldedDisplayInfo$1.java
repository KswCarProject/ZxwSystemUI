package com.android.systemui.unfold;

import android.view.Display;
import android.view.DisplayInfo;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
public final class UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1 extends Lambda implements Function1<Display, DisplayInfo> {
    public static final UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1 INSTANCE = new UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1();

    public UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1() {
        super(1);
    }

    @NotNull
    public final DisplayInfo invoke(Display display) {
        DisplayInfo displayInfo = new DisplayInfo();
        display.getDisplayInfo(displayInfo);
        return displayInfo;
    }
}

package com.android.keyguard.mediator;

import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/* compiled from: ScreenOnCoordinator.kt */
public /* synthetic */ class ScreenOnCoordinator$unfoldLightRevealAnimation$1 implements Function {
    public static final ScreenOnCoordinator$unfoldLightRevealAnimation$1 INSTANCE = new ScreenOnCoordinator$unfoldLightRevealAnimation$1();

    @NotNull
    public final UnfoldLightRevealOverlayAnimation apply(@NotNull SysUIUnfoldComponent sysUIUnfoldComponent) {
        return sysUIUnfoldComponent.getUnfoldLightRevealOverlayAnimation();
    }
}

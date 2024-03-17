package com.android.keyguard.mediator;

import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

/* compiled from: ScreenOnCoordinator.kt */
public /* synthetic */ class ScreenOnCoordinator$foldAodAnimationController$1 implements Function {
    public static final ScreenOnCoordinator$foldAodAnimationController$1 INSTANCE = new ScreenOnCoordinator$foldAodAnimationController$1();

    @NotNull
    public final FoldAodAnimationController apply(@NotNull SysUIUnfoldComponent sysUIUnfoldComponent) {
        return sysUIUnfoldComponent.getFoldAodAnimationController();
    }
}

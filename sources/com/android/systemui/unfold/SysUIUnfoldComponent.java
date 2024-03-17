package com.android.systemui.unfold;

import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import org.jetbrains.annotations.NotNull;

/* compiled from: SysUIUnfoldModule.kt */
public interface SysUIUnfoldComponent {

    /* compiled from: SysUIUnfoldModule.kt */
    public interface Factory {
        @NotNull
        SysUIUnfoldComponent create(@NotNull UnfoldTransitionProgressProvider unfoldTransitionProgressProvider, @NotNull NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider, @NotNull ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider);
    }

    @NotNull
    FoldAodAnimationController getFoldAodAnimationController();

    @NotNull
    KeyguardUnfoldTransition getKeyguardUnfoldTransition();

    @NotNull
    NotificationPanelUnfoldAnimationController getNotificationPanelUnfoldAnimationController();

    @NotNull
    StatusBarMoveFromCenterAnimationController getStatusBarMoveFromCenterAnimationController();

    @NotNull
    UnfoldLightRevealOverlayAnimation getUnfoldLightRevealOverlayAnimation();

    @NotNull
    UnfoldTransitionWallpaperController getUnfoldTransitionWallpaperController();
}

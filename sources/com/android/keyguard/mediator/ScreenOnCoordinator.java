package com.android.keyguard.mediator;

import android.os.Trace;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.UnfoldLightRevealOverlayAnimation;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.concurrency.PendingTasksContainer;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: ScreenOnCoordinator.kt */
public final class ScreenOnCoordinator implements ScreenLifecycle.Observer {
    @NotNull
    public final Execution execution;
    @Nullable
    public final FoldAodAnimationController foldAodAnimationController;
    @NotNull
    public final PendingTasksContainer pendingTasks = new PendingTasksContainer();
    @Nullable
    public final UnfoldLightRevealOverlayAnimation unfoldLightRevealAnimation;

    public ScreenOnCoordinator(@NotNull ScreenLifecycle screenLifecycle, @NotNull Optional<SysUIUnfoldComponent> optional, @NotNull Execution execution2) {
        this.execution = execution2;
        this.unfoldLightRevealAnimation = (UnfoldLightRevealOverlayAnimation) optional.map(ScreenOnCoordinator$unfoldLightRevealAnimation$1.INSTANCE).orElse((Object) null);
        this.foldAodAnimationController = (FoldAodAnimationController) optional.map(ScreenOnCoordinator$foldAodAnimationController$1.INSTANCE).orElse((Object) null);
        screenLifecycle.addObserver(this);
    }

    public void onScreenTurningOn(@NotNull Runnable runnable) {
        this.execution.assertIsMainThread();
        Trace.beginSection("ScreenOnCoordinator#onScreenTurningOn");
        this.pendingTasks.reset();
        UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = this.unfoldLightRevealAnimation;
        if (unfoldLightRevealOverlayAnimation != null) {
            unfoldLightRevealOverlayAnimation.onScreenTurningOn(this.pendingTasks.registerTask("unfold-reveal"));
        }
        FoldAodAnimationController foldAodAnimationController2 = this.foldAodAnimationController;
        if (foldAodAnimationController2 != null) {
            foldAodAnimationController2.onScreenTurningOn(this.pendingTasks.registerTask("fold-to-aod"));
        }
        this.pendingTasks.onTasksComplete(new ScreenOnCoordinator$onScreenTurningOn$1(runnable));
        Trace.endSection();
    }

    public void onScreenTurnedOn() {
        this.execution.assertIsMainThread();
        FoldAodAnimationController foldAodAnimationController2 = this.foldAodAnimationController;
        if (foldAodAnimationController2 != null) {
            foldAodAnimationController2.onScreenTurnedOn();
        }
        this.pendingTasks.reset();
    }
}

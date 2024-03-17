package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import org.jetbrains.annotations.Nullable;

/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler$runChipAnimation$2 implements Runnable {
    public final /* synthetic */ SystemStatusAnimationScheduler this$0;

    public SystemStatusAnimationScheduler$runChipAnimation$2(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.this$0 = systemStatusAnimationScheduler;
    }

    public final void run() {
        AnimatorSet access$collectFinishAnimations = this.this$0.collectFinishAnimations();
        this.this$0.animationState = 4;
        final SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.this$0;
        access$collectFinishAnimations.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(@Nullable Animator animator) {
                SystemStatusAnimationScheduler systemStatusAnimationScheduler = systemStatusAnimationScheduler;
                systemStatusAnimationScheduler.animationState = systemStatusAnimationScheduler.getHasPersistentDot() ? 5 : 0;
                systemStatusAnimationScheduler.statusBarWindowController.setForceStatusBarVisible(false);
            }
        });
        access$collectFinishAnimations.start();
        this.this$0.scheduledEvent = null;
    }
}

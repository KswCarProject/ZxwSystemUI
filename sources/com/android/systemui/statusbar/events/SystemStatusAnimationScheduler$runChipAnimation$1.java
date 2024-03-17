package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import org.jetbrains.annotations.Nullable;

/* compiled from: SystemStatusAnimationScheduler.kt */
public final class SystemStatusAnimationScheduler$runChipAnimation$1 extends AnimatorListenerAdapter {
    public final /* synthetic */ SystemStatusAnimationScheduler this$0;

    public SystemStatusAnimationScheduler$runChipAnimation$1(SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.this$0 = systemStatusAnimationScheduler;
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.animationState = 3;
    }
}

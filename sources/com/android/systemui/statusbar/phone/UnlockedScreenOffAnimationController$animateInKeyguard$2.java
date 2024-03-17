package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$animateInKeyguard$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ View $keyguardView;
    public final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    public UnlockedScreenOffAnimationController$animateInKeyguard$2(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, View view) {
        this.this$0 = unlockedScreenOffAnimationController;
        this.$keyguardView = view;
    }

    public void onAnimationCancel(@Nullable Animator animator) {
        this.this$0.aodUiAnimationPlaying = false;
        this.this$0.decidedToAnimateGoingToSleep = null;
        this.$keyguardView.animate().setListener((Animator.AnimatorListener) null);
        this.this$0.interactionJankMonitor.cancel(41);
    }

    public void onAnimationStart(@Nullable Animator animator) {
        InteractionJankMonitor access$getInteractionJankMonitor$p = this.this$0.interactionJankMonitor;
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            access$getMCentralSurfaces$p = null;
        }
        access$getInteractionJankMonitor$p.begin(access$getMCentralSurfaces$p.getNotificationShadeWindowView(), 41);
    }
}

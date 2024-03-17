package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealScrim;
import org.jetbrains.annotations.Nullable;

/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$lightRevealAnimator$1$2 extends AnimatorListenerAdapter {
    public final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    public UnlockedScreenOffAnimationController$lightRevealAnimator$1$2(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        this.this$0 = unlockedScreenOffAnimationController;
    }

    public void onAnimationCancel(@Nullable Animator animator) {
        LightRevealScrim access$getLightRevealScrim$p = this.this$0.lightRevealScrim;
        LightRevealScrim lightRevealScrim = null;
        if (access$getLightRevealScrim$p == null) {
            access$getLightRevealScrim$p = null;
        }
        if (!(access$getLightRevealScrim$p.getRevealEffect() instanceof CircleReveal)) {
            LightRevealScrim access$getLightRevealScrim$p2 = this.this$0.lightRevealScrim;
            if (access$getLightRevealScrim$p2 != null) {
                lightRevealScrim = access$getLightRevealScrim$p2;
            }
            lightRevealScrim.setRevealAmount(1.0f);
        }
    }

    public void onAnimationEnd(@Nullable Animator animator) {
        this.this$0.lightRevealAnimationPlaying = false;
        this.this$0.interactionJankMonitor.end(40);
    }

    public void onAnimationStart(@Nullable Animator animator) {
        InteractionJankMonitor access$getInteractionJankMonitor$p = this.this$0.interactionJankMonitor;
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            access$getMCentralSurfaces$p = null;
        }
        access$getInteractionJankMonitor$p.begin(access$getMCentralSurfaces$p.getNotificationShadeWindowView(), 40);
    }
}

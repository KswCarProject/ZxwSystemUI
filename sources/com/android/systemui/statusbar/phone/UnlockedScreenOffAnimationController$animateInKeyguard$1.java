package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.view.View;
import com.android.systemui.keyguard.KeyguardViewMediator;

/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$animateInKeyguard$1 implements Runnable {
    public final /* synthetic */ Runnable $after;
    public final /* synthetic */ View $keyguardView;
    public final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    public UnlockedScreenOffAnimationController$animateInKeyguard$1(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Runnable runnable, View view) {
        this.this$0 = unlockedScreenOffAnimationController;
        this.$after = runnable;
        this.$keyguardView = view;
    }

    public final void run() {
        this.this$0.aodUiAnimationPlaying = false;
        ((KeyguardViewMediator) this.this$0.keyguardViewMediatorLazy.get()).maybeHandlePendingLock();
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            access$getMCentralSurfaces$p = null;
        }
        access$getMCentralSurfaces$p.updateIsKeyguard();
        this.$after.run();
        this.this$0.decidedToAnimateGoingToSleep = null;
        this.$keyguardView.animate().setListener((Animator.AnimatorListener) null);
        this.this$0.interactionJankMonitor.end(41);
    }
}

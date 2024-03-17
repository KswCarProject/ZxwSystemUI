package com.android.systemui.unfold;

import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;

/* compiled from: FoldAodAnimationController.kt */
public final class FoldAodAnimationController$startAnimationRunnable$1 implements Runnable {
    public final /* synthetic */ FoldAodAnimationController this$0;

    public FoldAodAnimationController$startAnimationRunnable$1(FoldAodAnimationController foldAodAnimationController) {
        this.this$0 = foldAodAnimationController;
    }

    public final void run() {
        CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
        if (access$getMCentralSurfaces$p == null) {
            access$getMCentralSurfaces$p = null;
        }
        NotificationPanelViewController notificationPanelViewController = access$getMCentralSurfaces$p.getNotificationPanelViewController();
        final FoldAodAnimationController foldAodAnimationController = this.this$0;
        notificationPanelViewController.startFoldToAodAnimation(new Runnable() {
            public final void run() {
                foldAodAnimationController.setAnimationState(false);
            }
        });
    }
}

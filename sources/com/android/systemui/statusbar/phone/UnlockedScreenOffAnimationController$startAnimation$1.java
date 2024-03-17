package com.android.systemui.statusbar.phone;

/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController$startAnimation$1 implements Runnable {
    public final /* synthetic */ UnlockedScreenOffAnimationController this$0;

    public UnlockedScreenOffAnimationController$startAnimation$1(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        this.this$0 = unlockedScreenOffAnimationController;
    }

    public final void run() {
        if (!this.this$0.powerManager.isInteractive()) {
            this.this$0.aodUiAnimationPlaying = true;
            CentralSurfaces access$getMCentralSurfaces$p = this.this$0.mCentralSurfaces;
            if (access$getMCentralSurfaces$p == null) {
                access$getMCentralSurfaces$p = null;
            }
            access$getMCentralSurfaces$p.getNotificationPanelViewController().showAodUi();
        }
    }
}

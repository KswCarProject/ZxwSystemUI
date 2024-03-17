package com.android.systemui.biometrics;

import android.view.View;
import com.android.systemui.recents.OverviewProxyService;

/* compiled from: SidefpsController.kt */
public final class SidefpsController$overviewProxyListener$1 implements OverviewProxyService.OverviewProxyListener {
    public final /* synthetic */ SidefpsController this$0;

    public SidefpsController$overviewProxyListener$1(SidefpsController sidefpsController) {
        this.this$0 = sidefpsController;
    }

    public void onTaskbarStatusUpdated(boolean z, boolean z2) {
        View access$getOverlayView$p = this.this$0.overlayView;
        if (access$getOverlayView$p != null) {
            SidefpsController sidefpsController = this.this$0;
            sidefpsController.handler.postDelayed(new SidefpsController$overviewProxyListener$1$onTaskbarStatusUpdated$1$1(sidefpsController, access$getOverlayView$p), 500);
        }
    }
}

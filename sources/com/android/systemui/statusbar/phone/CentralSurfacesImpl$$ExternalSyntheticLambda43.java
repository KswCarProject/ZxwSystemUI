package com.android.systemui.statusbar.phone;

import com.android.systemui.statusbar.BackDropView;
import com.android.systemui.statusbar.NotificationShadeDepthController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CentralSurfacesImpl$$ExternalSyntheticLambda43 implements NotificationShadeDepthController.DepthListener {
    public final /* synthetic */ float f$0;
    public final /* synthetic */ BackDropView f$1;

    public /* synthetic */ CentralSurfacesImpl$$ExternalSyntheticLambda43(float f, BackDropView backDropView) {
        this.f$0 = f;
        this.f$1 = backDropView;
    }

    public final void onWallpaperZoomOutChanged(float f) {
        CentralSurfacesImpl.lambda$makeStatusBarView$16(this.f$0, this.f$1, f);
    }
}

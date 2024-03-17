package com.android.systemui.statusbar.phone;

import com.android.systemui.qs.carrier.QSCarrierGroupController;

/* compiled from: LargeScreenShadeHeaderController.kt */
public final class LargeScreenShadeHeaderController$updateListeners$1 implements QSCarrierGroupController.OnSingleCarrierChangedListener {
    public final /* synthetic */ LargeScreenShadeHeaderController this$0;

    public LargeScreenShadeHeaderController$updateListeners$1(LargeScreenShadeHeaderController largeScreenShadeHeaderController) {
        this.this$0 = largeScreenShadeHeaderController;
    }

    public final void onSingleCarrierChanged(boolean z) {
        this.this$0.updateSingleCarrier(z);
    }
}

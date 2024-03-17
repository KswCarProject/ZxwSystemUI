package com.android.systemui.controls.management;

import android.view.View;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$listingCallback$1$onServicesUpdated$1 implements Runnable {
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    public ControlsFavoritingActivity$listingCallback$1$onServicesUpdated$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public final void run() {
        View access$getOtherAppsButton$p = this.this$0.otherAppsButton;
        if (access$getOtherAppsButton$p == null) {
            access$getOtherAppsButton$p = null;
        }
        access$getOtherAppsButton$p.setVisibility(0);
    }
}

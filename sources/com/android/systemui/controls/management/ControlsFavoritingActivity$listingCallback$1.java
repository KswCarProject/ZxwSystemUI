package com.android.systemui.controls.management;

import android.view.View;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$listingCallback$1 implements ControlsListingController.ControlsListingCallback {
    public final /* synthetic */ ControlsFavoritingActivity this$0;

    public ControlsFavoritingActivity$listingCallback$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onServicesUpdated(@NotNull List<ControlsServiceInfo> list) {
        if (list.size() > 1) {
            View access$getOtherAppsButton$p = this.this$0.otherAppsButton;
            if (access$getOtherAppsButton$p == null) {
                access$getOtherAppsButton$p = null;
            }
            access$getOtherAppsButton$p.post(new ControlsFavoritingActivity$listingCallback$1$onServicesUpdated$1(this.this$0));
        }
    }
}

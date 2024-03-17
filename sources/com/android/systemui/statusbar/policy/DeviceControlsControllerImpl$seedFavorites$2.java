package com.android.systemui.statusbar.policy;

import android.content.SharedPreferences;
import android.util.Log;
import com.android.systemui.controls.controller.SeedResponse;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.Optional;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: DeviceControlsControllerImpl.kt */
public final class DeviceControlsControllerImpl$seedFavorites$2<T> implements Consumer {
    public final /* synthetic */ SharedPreferences $prefs;
    public final /* synthetic */ DeviceControlsControllerImpl this$0;

    public DeviceControlsControllerImpl$seedFavorites$2(DeviceControlsControllerImpl deviceControlsControllerImpl, SharedPreferences sharedPreferences) {
        this.this$0 = deviceControlsControllerImpl;
        this.$prefs = sharedPreferences;
    }

    public final void accept(@NotNull SeedResponse seedResponse) {
        Log.d("DeviceControlsControllerImpl", Intrinsics.stringPlus("Controls seeded: ", seedResponse));
        if (seedResponse.getAccepted()) {
            this.this$0.addPackageToSeededSet(this.$prefs, seedResponse.getPackageName());
            if (this.this$0.getPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core() == null) {
                this.this$0.setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(7);
            }
            this.this$0.fireControlsUpdate();
            Optional<ControlsListingController> controlsListingController = this.this$0.controlsComponent.getControlsListingController();
            final DeviceControlsControllerImpl deviceControlsControllerImpl = this.this$0;
            controlsListingController.ifPresent(new Consumer() {
                public final void accept(@NotNull ControlsListingController controlsListingController) {
                    controlsListingController.removeCallback(deviceControlsControllerImpl.listingCallback);
                }
            });
        }
    }
}

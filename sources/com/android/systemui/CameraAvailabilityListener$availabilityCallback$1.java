package com.android.systemui;

import android.hardware.camera2.CameraManager;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: CameraAvailabilityListener.kt */
public final class CameraAvailabilityListener$availabilityCallback$1 extends CameraManager.AvailabilityCallback {
    public final /* synthetic */ CameraAvailabilityListener this$0;

    public CameraAvailabilityListener$availabilityCallback$1(CameraAvailabilityListener cameraAvailabilityListener) {
        this.this$0 = cameraAvailabilityListener;
    }

    public void onCameraClosed(@NotNull String str) {
        if (Intrinsics.areEqual((Object) this.this$0.targetCameraId, (Object) str)) {
            this.this$0.notifyCameraInactive();
        }
    }

    public void onCameraOpened(@NotNull String str, @NotNull String str2) {
        if (Intrinsics.areEqual((Object) this.this$0.targetCameraId, (Object) str) && !this.this$0.isExcluded(str2)) {
            this.this$0.notifyCameraActive();
        }
    }
}

package com.android.systemui.statusbar.policy;

public interface RotationLockController extends CallbackController<RotationLockControllerCallback> {

    public interface RotationLockControllerCallback {
        void onRotationLockStateChanged(boolean z, boolean z2);
    }

    int getRotationLockOrientation();

    boolean isCameraRotationEnabled();

    boolean isRotationLocked();

    void setRotationLocked(boolean z);
}

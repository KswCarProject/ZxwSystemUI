package com.android.systemui.util.wrapper;

import com.android.internal.view.RotationPolicy;
import org.jetbrains.annotations.NotNull;

/* compiled from: RotationPolicyWrapper.kt */
public interface RotationPolicyWrapper {
    int getRotationLockOrientation();

    boolean isCameraRotationEnabled();

    boolean isRotationLockToggleVisible();

    boolean isRotationLocked();

    void registerRotationPolicyListener(@NotNull RotationPolicy.RotationPolicyListener rotationPolicyListener, int i);

    void setRotationLock(boolean z);

    void unregisterRotationPolicyListener(@NotNull RotationPolicy.RotationPolicyListener rotationPolicyListener);
}

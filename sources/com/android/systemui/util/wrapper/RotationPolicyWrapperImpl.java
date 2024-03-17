package com.android.systemui.util.wrapper;

import android.content.Context;
import android.os.Trace;
import com.android.internal.view.RotationPolicy;
import com.android.systemui.util.settings.SecureSettings;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

/* compiled from: RotationPolicyWrapper.kt */
public final class RotationPolicyWrapperImpl implements RotationPolicyWrapper {
    @NotNull
    public final Context context;
    @NotNull
    public final SecureSettings secureSettings;

    public void setRotationLock(boolean z) {
        Trace.beginSection("RotationPolicyWrapperImpl#setRotationLock");
        try {
            RotationPolicy.setRotationLock(this.context, z);
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    public RotationPolicyWrapperImpl(@NotNull Context context2, @NotNull SecureSettings secureSettings2) {
        this.context = context2;
        this.secureSettings = secureSettings2;
    }

    public int getRotationLockOrientation() {
        return RotationPolicy.getRotationLockOrientation(this.context);
    }

    public boolean isRotationLockToggleVisible() {
        return RotationPolicy.isRotationLockToggleVisible(this.context);
    }

    public boolean isRotationLocked() {
        return RotationPolicy.isRotationLocked(this.context);
    }

    public boolean isCameraRotationEnabled() {
        return this.secureSettings.getInt("camera_autorotate", 0) == 1;
    }

    public void registerRotationPolicyListener(@NotNull RotationPolicy.RotationPolicyListener rotationPolicyListener, int i) {
        RotationPolicy.registerRotationPolicyListener(this.context, rotationPolicyListener, i);
    }

    public void unregisterRotationPolicyListener(@NotNull RotationPolicy.RotationPolicyListener rotationPolicyListener) {
        RotationPolicy.unregisterRotationPolicyListener(this.context, rotationPolicyListener);
    }
}

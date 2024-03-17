package com.android.systemui.biometrics;

public interface UdfpsHbmProvider {
    void disableHbm(Runnable runnable);

    void enableHbm(boolean z, Runnable runnable);
}

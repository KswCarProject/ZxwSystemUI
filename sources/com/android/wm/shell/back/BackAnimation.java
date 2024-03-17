package com.android.wm.shell.back;

public interface BackAnimation {
    IBackAnimation createExternalInterface() {
        return null;
    }

    void onBackMotion(float f, float f2, int i, int i2);

    void setSwipeThresholds(float f, float f2);

    void setTriggerBack(boolean z);
}

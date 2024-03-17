package com.android.systemui.lowlightclock;

import android.view.ViewGroup;

public interface LowLightClockController {
    void attachLowLightClockView(ViewGroup viewGroup);

    void dozeTimeTick();

    boolean isLowLightClockEnabled();

    boolean showLowLightClock(boolean z);
}

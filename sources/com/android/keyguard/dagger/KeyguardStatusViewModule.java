package com.android.keyguard.dagger;

import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardSliceView;
import com.android.keyguard.KeyguardStatusView;
import com.android.systemui.R$id;

public abstract class KeyguardStatusViewModule {
    public static KeyguardClockSwitch getKeyguardClockSwitch(KeyguardStatusView keyguardStatusView) {
        return (KeyguardClockSwitch) keyguardStatusView.findViewById(R$id.keyguard_clock_container);
    }

    public static KeyguardSliceView getKeyguardSliceView(KeyguardClockSwitch keyguardClockSwitch) {
        return (KeyguardSliceView) keyguardClockSwitch.findViewById(R$id.keyguard_slice_view);
    }
}

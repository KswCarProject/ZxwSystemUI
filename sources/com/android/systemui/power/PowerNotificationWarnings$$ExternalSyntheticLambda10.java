package com.android.systemui.power;

import com.android.systemui.plugins.ActivityStarter;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PowerNotificationWarnings$$ExternalSyntheticLambda10 implements ActivityStarter.Callback {
    public final /* synthetic */ PowerNotificationWarnings f$0;

    public /* synthetic */ PowerNotificationWarnings$$ExternalSyntheticLambda10(PowerNotificationWarnings powerNotificationWarnings) {
        this.f$0 = powerNotificationWarnings;
    }

    public final void onActivityStarted(int i) {
        this.f$0.lambda$showUsbHighTemperatureAlarmInternal$4(i);
    }
}

package com.android.systemui.power;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PowerNotificationWarnings$$ExternalSyntheticLambda2 implements DialogInterface.OnClickListener {
    public final /* synthetic */ PowerNotificationWarnings f$0;

    public /* synthetic */ PowerNotificationWarnings$$ExternalSyntheticLambda2(PowerNotificationWarnings powerNotificationWarnings) {
        this.f$0 = powerNotificationWarnings;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showUsbHighTemperatureAlarmInternal$5(dialogInterface, i);
    }
}

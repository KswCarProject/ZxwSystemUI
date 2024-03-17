package com.android.systemui.power;

import android.content.DialogInterface;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PowerNotificationWarnings$$ExternalSyntheticLambda3 implements DialogInterface.OnDismissListener {
    public final /* synthetic */ PowerNotificationWarnings f$0;

    public /* synthetic */ PowerNotificationWarnings$$ExternalSyntheticLambda3(PowerNotificationWarnings powerNotificationWarnings) {
        this.f$0 = powerNotificationWarnings;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showUsbHighTemperatureAlarmInternal$6(dialogInterface);
    }
}

package com.android.systemui.power;

import com.android.internal.logging.UiEventLogger;

public enum BatteryWarningEvents$LowBatteryWarningEvent implements UiEventLogger.UiEventEnum {
    LOW_BATTERY_NOTIFICATION(1048),
    LOW_BATTERY_NOTIFICATION_TURN_ON(1049),
    LOW_BATTERY_NOTIFICATION_CANCEL(1050),
    LOW_BATTERY_NOTIFICATION_SETTINGS(1051),
    SAVER_CONFIRM_DIALOG(1052),
    SAVER_CONFIRM_OK(1053),
    SAVER_CONFIRM_CANCEL(1054),
    SAVER_CONFIRM_DISMISS(1055);
    
    private final int mId;

    /* access modifiers changed from: public */
    BatteryWarningEvents$LowBatteryWarningEvent(int i) {
        this.mId = i;
    }

    public int getId() {
        return this.mId;
    }
}

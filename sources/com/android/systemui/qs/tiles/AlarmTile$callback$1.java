package com.android.systemui.qs.tiles;

import android.app.AlarmManager;
import com.android.systemui.statusbar.policy.NextAlarmController;

/* compiled from: AlarmTile.kt */
public final class AlarmTile$callback$1 implements NextAlarmController.NextAlarmChangeCallback {
    public final /* synthetic */ AlarmTile this$0;

    public AlarmTile$callback$1(AlarmTile alarmTile) {
        this.this$0 = alarmTile;
    }

    public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
        this.this$0.lastAlarmInfo = alarmClockInfo;
        this.this$0.refreshState();
    }
}

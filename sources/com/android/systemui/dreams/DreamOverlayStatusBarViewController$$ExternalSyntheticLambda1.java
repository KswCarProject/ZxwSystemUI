package com.android.systemui.dreams;

import android.app.AlarmManager;
import com.android.systemui.statusbar.policy.NextAlarmController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class DreamOverlayStatusBarViewController$$ExternalSyntheticLambda1 implements NextAlarmController.NextAlarmChangeCallback {
    public final /* synthetic */ DreamOverlayStatusBarViewController f$0;

    public /* synthetic */ DreamOverlayStatusBarViewController$$ExternalSyntheticLambda1(DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController) {
        this.f$0 = dreamOverlayStatusBarViewController;
    }

    public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
        this.f$0.lambda$new$1(alarmClockInfo);
    }
}

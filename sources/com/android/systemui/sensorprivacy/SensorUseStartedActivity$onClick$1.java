package com.android.systemui.sensorprivacy;

import android.os.Handler;
import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.plugins.ActivityStarter;

/* compiled from: SensorUseStartedActivity.kt */
public final class SensorUseStartedActivity$onClick$1 implements ActivityStarter.OnDismissAction {
    public final /* synthetic */ SensorUseStartedActivity this$0;

    public SensorUseStartedActivity$onClick$1(SensorUseStartedActivity sensorUseStartedActivity) {
        this.this$0 = sensorUseStartedActivity;
    }

    public final boolean onDismiss() {
        Handler access$getBgHandler$p = this.this$0.bgHandler;
        final SensorUseStartedActivity sensorUseStartedActivity = this.this$0;
        access$getBgHandler$p.postDelayed(new Runnable() {
            public final void run() {
                sensorUseStartedActivity.disableSensorPrivacy();
                String access$getSensorUsePackageName$p = sensorUseStartedActivity.sensorUsePackageName;
                if (access$getSensorUsePackageName$p == null) {
                    access$getSensorUsePackageName$p = null;
                }
                FrameworkStatsLog.write(382, 1, access$getSensorUsePackageName$p);
            }
        }, 200);
        return false;
    }
}

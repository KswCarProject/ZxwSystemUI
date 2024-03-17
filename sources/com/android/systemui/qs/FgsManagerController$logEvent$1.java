package com.android.systemui.qs;

import com.android.systemui.shared.system.SysUiStatsLog;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$logEvent$1 implements Runnable {
    public final /* synthetic */ int $event;
    public final /* synthetic */ String $packageName;
    public final /* synthetic */ long $timeLogged;
    public final /* synthetic */ long $timeStarted;
    public final /* synthetic */ int $userId;
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$logEvent$1(FgsManagerController fgsManagerController, String str, int i, int i2, long j, long j2) {
        this.this$0 = fgsManagerController;
        this.$packageName = str;
        this.$userId = i;
        this.$event = i2;
        this.$timeLogged = j;
        this.$timeStarted = j2;
    }

    public final void run() {
        SysUiStatsLog.write(450, this.this$0.packageManager.getPackageUidAsUser(this.$packageName, this.$userId), this.$event, this.$timeLogged - this.$timeStarted);
    }
}

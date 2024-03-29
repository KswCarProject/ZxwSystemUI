package com.android.systemui.controls.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$restoreFinishedReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ ControlsControllerImpl this$0;

    public ControlsControllerImpl$restoreFinishedReceiver$1(ControlsControllerImpl controlsControllerImpl) {
        this.this$0 = controlsControllerImpl;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if (intent.getIntExtra("android.intent.extra.USER_ID", -10000) == this.this$0.getCurrentUserId()) {
            this.this$0.executor.execute(new ControlsControllerImpl$restoreFinishedReceiver$1$onReceive$1(this.this$0));
        }
    }
}

package com.android.systemui.controls.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.jetbrains.annotations.NotNull;

/* compiled from: ControlsActivity.kt */
public final class ControlsActivity$initBroadcastReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ ControlsActivity this$0;

    public ControlsActivity$initBroadcastReceiver$1(ControlsActivity controlsActivity) {
        this.this$0 = controlsActivity;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
            this.this$0.finish();
        }
    }
}

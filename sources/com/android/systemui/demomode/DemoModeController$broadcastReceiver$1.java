package com.android.systemui.demomode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;

/* compiled from: DemoModeController.kt */
public final class DemoModeController$broadcastReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ DemoModeController this$0;

    public DemoModeController$broadcastReceiver$1(DemoModeController demoModeController) {
        this.this$0 = demoModeController;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        Bundle extras;
        if ("com.android.systemui.demo".equals(intent.getAction()) && (extras = intent.getExtras()) != null) {
            String lowerCase = StringsKt__StringsKt.trim(extras.getString("command", "")).toString().toLowerCase();
            if (lowerCase.length() != 0) {
                try {
                    this.this$0.dispatchDemoCommand(lowerCase, extras);
                } catch (Throwable th) {
                    Log.w("DemoModeController", "Error running demo command, intent=" + intent + ' ' + th);
                }
            }
        }
    }
}

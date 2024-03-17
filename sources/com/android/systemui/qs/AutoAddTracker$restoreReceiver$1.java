package com.android.systemui.qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: AutoAddTracker.kt */
public final class AutoAddTracker$restoreReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ AutoAddTracker this$0;

    public AutoAddTracker$restoreReceiver$1(AutoAddTracker autoAddTracker) {
        this.this$0 = autoAddTracker;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.os.action.SETTING_RESTORED")) {
            this.this$0.processRestoreIntent(intent);
        }
    }
}

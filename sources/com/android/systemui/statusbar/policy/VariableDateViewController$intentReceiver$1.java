package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewController$intentReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ VariableDateViewController this$0;

    public VariableDateViewController$intentReceiver$1(VariableDateViewController variableDateViewController) {
        this.this$0 = variableDateViewController;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        Handler handler = ((VariableDateView) this.this$0.mView).getHandler();
        if (handler != null) {
            String action = intent.getAction();
            if (Intrinsics.areEqual((Object) "android.intent.action.TIME_TICK", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.TIME_SET", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.TIMEZONE_CHANGED", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.LOCALE_CHANGED", (Object) action)) {
                if (Intrinsics.areEqual((Object) "android.intent.action.LOCALE_CHANGED", (Object) action) || Intrinsics.areEqual((Object) "android.intent.action.TIMEZONE_CHANGED", (Object) action)) {
                    handler.post(new VariableDateViewController$intentReceiver$1$onReceive$1(this.this$0));
                }
                handler.post(new VariableDateViewController$intentReceiver$1$onReceive$2(this.this$0));
            }
        }
    }
}

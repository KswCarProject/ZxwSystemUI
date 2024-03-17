package com.android.systemui.qs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: FgsManagerController.kt */
public final class FgsManagerController$init$1$3 extends BroadcastReceiver {
    public final /* synthetic */ FgsManagerController this$0;

    public FgsManagerController$init$1$3(FgsManagerController fgsManagerController) {
        this.this$0 = fgsManagerController;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.intent.action.SHOW_FOREGROUND_SERVICE_MANAGER")) {
            this.this$0.showDialog((View) null);
        }
    }
}

package com.android.systemui.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$initBroadcastReceiver$1 extends BroadcastReceiver {
    public final /* synthetic */ UserSwitcherActivity this$0;

    public UserSwitcherActivity$initBroadcastReceiver$1(UserSwitcherActivity userSwitcherActivity) {
        this.this$0 = userSwitcherActivity;
    }

    public void onReceive(@NotNull Context context, @NotNull Intent intent) {
        if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
            this.this$0.finish();
        }
    }
}

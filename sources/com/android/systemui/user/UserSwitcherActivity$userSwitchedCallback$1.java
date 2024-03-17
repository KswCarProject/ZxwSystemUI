package com.android.systemui.user;

import android.content.Context;
import com.android.systemui.settings.UserTracker;
import org.jetbrains.annotations.NotNull;

/* compiled from: UserSwitcherActivity.kt */
public final class UserSwitcherActivity$userSwitchedCallback$1 implements UserTracker.Callback {
    public final /* synthetic */ UserSwitcherActivity this$0;

    public UserSwitcherActivity$userSwitchedCallback$1(UserSwitcherActivity userSwitcherActivity) {
        this.this$0 = userSwitcherActivity;
    }

    public void onUserChanged(int i, @NotNull Context context) {
        this.this$0.finish();
    }
}

package com.android.systemui.statusbar.phone.userswitcher;

import java.util.concurrent.Executor;

/* compiled from: StatusBarUserInfoTracker.kt */
public final class StatusBarUserInfoTracker$checkEnabled$1 implements Runnable {
    public final /* synthetic */ StatusBarUserInfoTracker this$0;

    public StatusBarUserInfoTracker$checkEnabled$1(StatusBarUserInfoTracker statusBarUserInfoTracker) {
        this.this$0 = statusBarUserInfoTracker;
    }

    public final void run() {
        boolean userSwitcherEnabled = this.this$0.getUserSwitcherEnabled();
        StatusBarUserInfoTracker statusBarUserInfoTracker = this.this$0;
        statusBarUserInfoTracker.userSwitcherEnabled = statusBarUserInfoTracker.userManager.isUserSwitcherEnabled();
        if (userSwitcherEnabled != this.this$0.getUserSwitcherEnabled()) {
            Executor access$getMainExecutor$p = this.this$0.mainExecutor;
            final StatusBarUserInfoTracker statusBarUserInfoTracker2 = this.this$0;
            access$getMainExecutor$p.execute(new Runnable() {
                public final void run() {
                    statusBarUserInfoTracker2.notifyListenersSettingChanged();
                }
            });
        }
    }
}

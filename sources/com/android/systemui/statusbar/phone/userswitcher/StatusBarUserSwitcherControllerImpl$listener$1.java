package com.android.systemui.statusbar.phone.userswitcher;

/* compiled from: StatusBarUserSwitcherController.kt */
public final class StatusBarUserSwitcherControllerImpl$listener$1 implements CurrentUserChipInfoUpdatedListener {
    public final /* synthetic */ StatusBarUserSwitcherControllerImpl this$0;

    public StatusBarUserSwitcherControllerImpl$listener$1(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl) {
        this.this$0 = statusBarUserSwitcherControllerImpl;
    }

    public void onCurrentUserChipInfoUpdated() {
        this.this$0.updateChip();
    }

    public void onStatusBarUserSwitcherSettingChanged(boolean z) {
        this.this$0.updateEnabled();
    }
}

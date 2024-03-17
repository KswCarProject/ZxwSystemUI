package com.android.systemui.statusbar.phone.userswitcher;

/* compiled from: StatusBarUserSwitcherController.kt */
public final class StatusBarUserSwitcherControllerImpl$featureFlagListener$1 implements OnUserSwitcherPreferenceChangeListener {
    public final /* synthetic */ StatusBarUserSwitcherControllerImpl this$0;

    public StatusBarUserSwitcherControllerImpl$featureFlagListener$1(StatusBarUserSwitcherControllerImpl statusBarUserSwitcherControllerImpl) {
        this.this$0 = statusBarUserSwitcherControllerImpl;
    }

    public void onUserSwitcherPreferenceChange(boolean z) {
        this.this$0.updateEnabled();
    }
}

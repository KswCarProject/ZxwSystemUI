package com.android.keyguard;

import com.android.keyguard.KeyguardSecurityContainer;
import com.android.systemui.statusbar.policy.UserSwitcherController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda0 implements UserSwitcherController.UserSwitchCallback {
    public final /* synthetic */ KeyguardSecurityContainer.UserSwitcherViewMode f$0;

    public /* synthetic */ KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda0(KeyguardSecurityContainer.UserSwitcherViewMode userSwitcherViewMode) {
        this.f$0 = userSwitcherViewMode;
    }

    public final void onUserSwitched() {
        this.f$0.setupUserSwitcher();
    }
}

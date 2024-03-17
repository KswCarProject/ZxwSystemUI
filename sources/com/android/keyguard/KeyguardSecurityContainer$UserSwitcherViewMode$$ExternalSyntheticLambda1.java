package com.android.keyguard;

import android.view.View;
import android.view.ViewGroup;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.systemui.statusbar.policy.UserSwitcherController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ KeyguardSecurityContainer.UserSwitcherViewMode f$0;
    public final /* synthetic */ ViewGroup f$1;
    public final /* synthetic */ UserSwitcherController.BaseUserAdapter f$2;

    public /* synthetic */ KeyguardSecurityContainer$UserSwitcherViewMode$$ExternalSyntheticLambda1(KeyguardSecurityContainer.UserSwitcherViewMode userSwitcherViewMode, ViewGroup viewGroup, UserSwitcherController.BaseUserAdapter baseUserAdapter) {
        this.f$0 = userSwitcherViewMode;
        this.f$1 = viewGroup;
        this.f$2 = baseUserAdapter;
    }

    public final void onClick(View view) {
        this.f$0.lambda$setupUserSwitcher$0(this.f$1, this.f$2, view);
    }
}

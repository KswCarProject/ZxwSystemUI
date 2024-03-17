package com.android.systemui.statusbar.policy;

import android.content.pm.UserInfo;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherController$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ UserSwitcherController f$0;
    public final /* synthetic */ UserInfo f$1;

    public /* synthetic */ UserSwitcherController$$ExternalSyntheticLambda0(UserSwitcherController userSwitcherController, UserInfo userInfo) {
        this.f$0 = userSwitcherController;
        this.f$1 = userInfo;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$removeGuestUser$4(this.f$1, (Integer) obj);
    }
}

package com.android.systemui.statusbar.policy;

import android.app.Dialog;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherController$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ UserSwitcherController f$0;
    public final /* synthetic */ Dialog f$1;
    public final /* synthetic */ Consumer f$2;

    public /* synthetic */ UserSwitcherController$$ExternalSyntheticLambda2(UserSwitcherController userSwitcherController, Dialog dialog, Consumer consumer) {
        this.f$0 = userSwitcherController;
        this.f$1 = dialog;
        this.f$2 = consumer;
    }

    public final void run() {
        this.f$0.lambda$createGuestAsync$6(this.f$1, this.f$2);
    }
}

package com.android.systemui.statusbar.policy;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherController$5$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ UserSwitcherController f$0;

    public /* synthetic */ UserSwitcherController$5$$ExternalSyntheticLambda0(UserSwitcherController userSwitcherController) {
        this.f$0 = userSwitcherController;
    }

    public final void run() {
        this.f$0.notifyAdapters();
    }
}

package com.android.systemui.statusbar.policy;

import android.util.SparseArray;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class UserSwitcherController$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ UserSwitcherController f$0;
    public final /* synthetic */ SparseArray f$1;

    public /* synthetic */ UserSwitcherController$$ExternalSyntheticLambda3(UserSwitcherController userSwitcherController, SparseArray sparseArray) {
        this.f$0 = userSwitcherController;
        this.f$1 = sparseArray;
    }

    public final void run() {
        this.f$0.lambda$refreshUsers$1(this.f$1);
    }
}

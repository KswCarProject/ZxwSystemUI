package com.android.systemui.statusbar.policy;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardUserSwitcherListView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardUserSwitcherListView f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ KeyguardUserDetailItemView[] f$2;

    public /* synthetic */ KeyguardUserSwitcherListView$$ExternalSyntheticLambda0(KeyguardUserSwitcherListView keyguardUserSwitcherListView, boolean z, KeyguardUserDetailItemView[] keyguardUserDetailItemViewArr) {
        this.f$0 = keyguardUserSwitcherListView;
        this.f$1 = z;
        this.f$2 = keyguardUserDetailItemViewArr;
    }

    public final void run() {
        this.f$0.lambda$updateVisibilities$0(this.f$1, this.f$2);
    }
}

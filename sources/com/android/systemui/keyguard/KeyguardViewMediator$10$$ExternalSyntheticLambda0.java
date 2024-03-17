package com.android.systemui.keyguard;

import com.android.systemui.keyguard.KeyguardViewMediator;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardViewMediator$10$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardViewMediator.AnonymousClass10 f$0;
    public final /* synthetic */ KeyguardViewMediator.StartKeyguardExitAnimParams f$1;

    public /* synthetic */ KeyguardViewMediator$10$$ExternalSyntheticLambda0(KeyguardViewMediator.AnonymousClass10 r1, KeyguardViewMediator.StartKeyguardExitAnimParams startKeyguardExitAnimParams) {
        this.f$0 = r1;
        this.f$1 = startKeyguardExitAnimParams;
    }

    public final void run() {
        this.f$0.lambda$handleMessage$0(this.f$1);
    }
}

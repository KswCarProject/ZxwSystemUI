package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.SafetyController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SafetyController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ SafetyController f$0;
    public final /* synthetic */ SafetyController.Listener f$1;

    public /* synthetic */ SafetyController$$ExternalSyntheticLambda0(SafetyController safetyController, SafetyController.Listener listener) {
        this.f$0 = safetyController;
        this.f$1 = listener;
    }

    public final void run() {
        this.f$0.lambda$addCallback$0(this.f$1);
    }
}

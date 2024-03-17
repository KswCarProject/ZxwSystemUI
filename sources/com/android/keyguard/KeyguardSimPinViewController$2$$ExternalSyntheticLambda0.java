package com.android.keyguard;

import android.telephony.PinResult;
import com.android.keyguard.KeyguardSimPinViewController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class KeyguardSimPinViewController$2$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ KeyguardSimPinViewController.AnonymousClass2 f$0;
    public final /* synthetic */ PinResult f$1;

    public /* synthetic */ KeyguardSimPinViewController$2$$ExternalSyntheticLambda0(KeyguardSimPinViewController.AnonymousClass2 r1, PinResult pinResult) {
        this.f$0 = r1;
        this.f$1 = pinResult;
    }

    public final void run() {
        this.f$0.lambda$onSimCheckResponse$0(this.f$1);
    }
}

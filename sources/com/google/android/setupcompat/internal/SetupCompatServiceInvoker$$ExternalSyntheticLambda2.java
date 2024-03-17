package com.google.android.setupcompat.internal;

import android.os.Bundle;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class SetupCompatServiceInvoker$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SetupCompatServiceInvoker f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Bundle f$2;

    public /* synthetic */ SetupCompatServiceInvoker$$ExternalSyntheticLambda2(SetupCompatServiceInvoker setupCompatServiceInvoker, String str, Bundle bundle) {
        this.f$0 = setupCompatServiceInvoker;
        this.f$1 = str;
        this.f$2 = bundle;
    }

    public final void run() {
        this.f$0.lambda$onFocusStatusChanged$2(this.f$1, this.f$2);
    }
}

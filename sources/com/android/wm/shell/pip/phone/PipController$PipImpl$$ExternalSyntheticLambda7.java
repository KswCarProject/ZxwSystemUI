package com.android.wm.shell.pip.phone;

import android.content.res.Configuration;
import com.android.wm.shell.pip.phone.PipController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$PipImpl$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ PipController.PipImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ PipController$PipImpl$$ExternalSyntheticLambda7(PipController.PipImpl pipImpl, Configuration configuration) {
        this.f$0 = pipImpl;
        this.f$1 = configuration;
    }

    public final void run() {
        this.f$0.lambda$onConfigurationChanged$1(this.f$1);
    }
}

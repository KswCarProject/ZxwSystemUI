package com.android.wm.shell.onehanded;

import android.content.res.Configuration;
import com.android.wm.shell.onehanded.OneHandedController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class OneHandedController$OneHandedImpl$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ OneHandedController.OneHandedImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ OneHandedController$OneHandedImpl$$ExternalSyntheticLambda5(OneHandedController.OneHandedImpl oneHandedImpl, Configuration configuration) {
        this.f$0 = oneHandedImpl;
        this.f$1 = configuration;
    }

    public final void run() {
        this.f$0.lambda$onConfigChanged$6(this.f$1);
    }
}

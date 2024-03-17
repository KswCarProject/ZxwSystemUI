package com.android.wm.shell.hidedisplaycutout;

import android.content.res.Configuration;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class HideDisplayCutoutController$HideDisplayCutoutImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ HideDisplayCutoutController.HideDisplayCutoutImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ HideDisplayCutoutController$HideDisplayCutoutImpl$$ExternalSyntheticLambda0(HideDisplayCutoutController.HideDisplayCutoutImpl hideDisplayCutoutImpl, Configuration configuration) {
        this.f$0 = hideDisplayCutoutImpl;
        this.f$1 = configuration;
    }

    public final void run() {
        this.f$0.lambda$onConfigurationChanged$0(this.f$1);
    }
}

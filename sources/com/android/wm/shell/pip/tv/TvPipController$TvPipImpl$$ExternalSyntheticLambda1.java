package com.android.wm.shell.pip.tv;

import android.content.res.Configuration;
import com.android.wm.shell.pip.tv.TvPipController;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipController$TvPipImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ TvPipController.TvPipImpl f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ TvPipController$TvPipImpl$$ExternalSyntheticLambda1(TvPipController.TvPipImpl tvPipImpl, Configuration configuration) {
        this.f$0 = tvPipImpl;
        this.f$1 = configuration;
    }

    public final void run() {
        this.f$0.lambda$onConfigurationChanged$0(this.f$1);
    }
}

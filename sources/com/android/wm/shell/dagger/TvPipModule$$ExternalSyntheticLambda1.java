package com.android.wm.shell.dagger;

import com.android.wm.shell.pip.PipAppOpsListener;
import com.android.wm.shell.pip.PipTaskOrganizer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TvPipModule$$ExternalSyntheticLambda1 implements PipAppOpsListener.Callback {
    public final /* synthetic */ PipTaskOrganizer f$0;

    public /* synthetic */ TvPipModule$$ExternalSyntheticLambda1(PipTaskOrganizer pipTaskOrganizer) {
        this.f$0 = pipTaskOrganizer;
    }

    public final void dismissPip() {
        this.f$0.removePip();
    }
}

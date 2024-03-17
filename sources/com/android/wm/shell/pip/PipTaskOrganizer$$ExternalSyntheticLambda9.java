package com.android.wm.shell.pip;

import android.window.WindowContainerTransaction;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ PipTaskOrganizer f$0;
    public final /* synthetic */ boolean f$1;
    public final /* synthetic */ WindowContainerTransaction f$2;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda9(PipTaskOrganizer pipTaskOrganizer, boolean z, WindowContainerTransaction windowContainerTransaction) {
        this.f$0 = pipTaskOrganizer;
        this.f$1 = z;
        this.f$2 = windowContainerTransaction;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$applyFinishBoundsResize$7(this.f$1, this.f$2, (SplitScreenController) obj);
    }
}

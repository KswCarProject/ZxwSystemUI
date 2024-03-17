package com.android.wm.shell;

import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda11 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda11(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).moveToSideStage(this.f$0, this.f$1);
    }
}

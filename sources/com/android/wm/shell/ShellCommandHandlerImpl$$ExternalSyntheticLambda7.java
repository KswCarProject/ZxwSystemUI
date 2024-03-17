package com.android.wm.shell;

import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda7(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((SplitScreenController) obj).setSideStagePosition(this.f$0);
    }
}

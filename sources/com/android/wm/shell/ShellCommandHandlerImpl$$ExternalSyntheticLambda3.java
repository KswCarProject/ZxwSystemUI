package com.android.wm.shell;

import com.android.wm.shell.hidedisplaycutout.HideDisplayCutoutController;
import java.io.PrintWriter;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ PrintWriter f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda3(PrintWriter printWriter) {
        this.f$0 = printWriter;
    }

    public final void accept(Object obj) {
        ((HideDisplayCutoutController) obj).dump(this.f$0);
    }
}

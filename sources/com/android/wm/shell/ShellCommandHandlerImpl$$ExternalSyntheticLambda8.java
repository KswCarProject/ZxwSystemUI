package com.android.wm.shell;

import com.android.wm.shell.apppairs.AppPairsController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda8 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda8(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((AppPairsController) obj).unpair(this.f$0);
    }
}

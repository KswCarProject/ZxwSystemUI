package com.android.wm.shell;

import com.android.wm.shell.apppairs.AppPairsController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda9 implements Consumer {
    public final /* synthetic */ int f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda9(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void accept(Object obj) {
        ((AppPairsController) obj).pair(this.f$0, this.f$1);
    }
}

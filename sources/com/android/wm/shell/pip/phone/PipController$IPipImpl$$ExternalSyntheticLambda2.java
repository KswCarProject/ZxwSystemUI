package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.IPipAnimationListener;
import com.android.wm.shell.pip.phone.PipController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PipController$IPipImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ PipController.IPipImpl f$0;
    public final /* synthetic */ IPipAnimationListener f$1;

    public /* synthetic */ PipController$IPipImpl$$ExternalSyntheticLambda2(PipController.IPipImpl iPipImpl, IPipAnimationListener iPipAnimationListener) {
        this.f$0 = iPipImpl;
        this.f$1 = iPipAnimationListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$setPinnedStackAnimationListener$5(this.f$1, (PipController) obj);
    }
}

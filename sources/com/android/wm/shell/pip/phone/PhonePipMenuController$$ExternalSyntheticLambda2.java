package com.android.wm.shell.pip.phone;

import com.android.wm.shell.pip.phone.PhonePipMenuController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class PhonePipMenuController$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int f$0;

    public /* synthetic */ PhonePipMenuController$$ExternalSyntheticLambda2(int i) {
        this.f$0 = i;
    }

    public final void accept(Object obj) {
        ((PhonePipMenuController.Listener) obj).onPipMenuStateChangeFinish(this.f$0);
    }
}

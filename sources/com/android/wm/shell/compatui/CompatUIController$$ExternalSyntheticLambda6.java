package com.android.wm.shell.compatui;

import com.android.wm.shell.common.DisplayLayout;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CompatUIController$$ExternalSyntheticLambda6 implements Consumer {
    public final /* synthetic */ DisplayLayout f$0;

    public /* synthetic */ CompatUIController$$ExternalSyntheticLambda6(DisplayLayout displayLayout) {
        this.f$0 = displayLayout;
    }

    public final void accept(Object obj) {
        ((CompatUIWindowManagerAbstract) obj).updateDisplayLayout(this.f$0);
    }
}

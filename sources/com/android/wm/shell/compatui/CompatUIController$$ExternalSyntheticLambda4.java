package com.android.wm.shell.compatui;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CompatUIController$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ CompatUIController f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ CompatUIController$$ExternalSyntheticLambda4(CompatUIController compatUIController, int i) {
        this.f$0 = compatUIController;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$onImeVisibilityChanged$2(this.f$1, (CompatUIWindowManagerAbstract) obj);
    }
}

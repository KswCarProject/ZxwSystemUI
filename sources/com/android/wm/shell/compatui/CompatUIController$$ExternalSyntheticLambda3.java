package com.android.wm.shell.compatui;

import java.util.List;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class CompatUIController$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ List f$0;

    public /* synthetic */ CompatUIController$$ExternalSyntheticLambda3(List list) {
        this.f$0 = list;
    }

    public final void accept(Object obj) {
        this.f$0.add(Integer.valueOf(((CompatUIWindowManagerAbstract) obj).getTaskId()));
    }
}

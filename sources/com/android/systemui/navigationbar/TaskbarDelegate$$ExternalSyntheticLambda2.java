package com.android.systemui.navigationbar;

import com.android.wm.shell.pip.Pip;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TaskbarDelegate$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ TaskbarDelegate f$0;

    public /* synthetic */ TaskbarDelegate$$ExternalSyntheticLambda2(TaskbarDelegate taskbarDelegate) {
        this.f$0 = taskbarDelegate;
    }

    public final void accept(Object obj) {
        this.f$0.addPipExclusionBoundsChangeListener((Pip) obj);
    }
}

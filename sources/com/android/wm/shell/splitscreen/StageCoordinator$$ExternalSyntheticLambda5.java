package com.android.wm.shell.splitscreen;

import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda5 implements Consumer {
    public final /* synthetic */ StageCoordinator f$0;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda5(StageCoordinator stageCoordinator) {
        this.f$0 = stageCoordinator;
    }

    public final void accept(Object obj) {
        this.f$0.onFoldedStateChanged(((Boolean) obj).booleanValue());
    }
}

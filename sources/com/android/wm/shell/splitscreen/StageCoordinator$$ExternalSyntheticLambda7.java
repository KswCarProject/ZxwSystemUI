package com.android.wm.shell.splitscreen;

import com.android.wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda7 implements Consumer {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda7(StageCoordinator stageCoordinator, int i) {
        this.f$0 = stageCoordinator;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$applyExitSplitScreen$2(this.f$1, (RecentTasksController) obj);
    }
}

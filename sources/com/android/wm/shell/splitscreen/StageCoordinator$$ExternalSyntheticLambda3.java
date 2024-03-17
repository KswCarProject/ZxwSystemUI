package com.android.wm.shell.splitscreen;

import android.window.TransitionInfo;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.splitscreen.SplitScreenTransitions;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class StageCoordinator$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ StageCoordinator f$0;
    public final /* synthetic */ SplitScreenTransitions.DismissTransition f$1;
    public final /* synthetic */ TransitionInfo f$2;

    public /* synthetic */ StageCoordinator$$ExternalSyntheticLambda3(StageCoordinator stageCoordinator, SplitScreenTransitions.DismissTransition dismissTransition, TransitionInfo transitionInfo) {
        this.f$0 = stageCoordinator;
        this.f$1 = dismissTransition;
        this.f$2 = transitionInfo;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$startPendingDismissAnimation$11(this.f$1, this.f$2, (RecentTasksController) obj);
    }
}

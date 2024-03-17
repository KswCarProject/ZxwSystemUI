package com.android.wm.shell.recents;

import com.android.wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda4 implements Consumer {
    public final /* synthetic */ RecentTasksController.IRecentTasksImpl f$0;

    public /* synthetic */ RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda4(RecentTasksController.IRecentTasksImpl iRecentTasksImpl) {
        this.f$0 = iRecentTasksImpl;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$unregisterRecentTasksListener$3((RecentTasksController) obj);
    }
}

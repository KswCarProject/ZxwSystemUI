package com.android.wm.shell.recents;

import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.util.GroupedRecentTaskInfo;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ GroupedRecentTaskInfo[][] f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;

    public /* synthetic */ RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda2(GroupedRecentTaskInfo[][] groupedRecentTaskInfoArr, int i, int i2, int i3) {
        this.f$0 = groupedRecentTaskInfoArr;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
    }

    public final void accept(Object obj) {
        RecentTasksController.IRecentTasksImpl.lambda$getRecentTasks$4(this.f$0, this.f$1, this.f$2, this.f$3, (RecentTasksController) obj);
    }
}

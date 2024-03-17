package com.android.wm.shell.fullscreen;

import android.app.ActivityManager;
import com.android.wm.shell.recents.RecentTasksController;
import java.util.function.Consumer;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class FullscreenTaskListener$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ ActivityManager.RunningTaskInfo f$0;

    public /* synthetic */ FullscreenTaskListener$$ExternalSyntheticLambda2(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.f$0 = runningTaskInfo;
    }

    public final void accept(Object obj) {
        FullscreenTaskListener.lambda$updateRecentsForVisibleFullscreenTask$2(this.f$0, (RecentTasksController) obj);
    }
}

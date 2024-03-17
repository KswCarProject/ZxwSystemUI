package com.android.wm.shell;

import android.app.ActivityOptions;
import android.content.pm.ShortcutInfo;

/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda12 implements Runnable {
    public final /* synthetic */ TaskView f$0;
    public final /* synthetic */ ShortcutInfo f$1;
    public final /* synthetic */ ActivityOptions f$2;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda12(TaskView taskView, ShortcutInfo shortcutInfo, ActivityOptions activityOptions) {
        this.f$0 = taskView;
        this.f$1 = shortcutInfo;
        this.f$2 = activityOptions;
    }

    public final void run() {
        this.f$0.lambda$startShortcutActivity$0(this.f$1, this.f$2);
    }
}
